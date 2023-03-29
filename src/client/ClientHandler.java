package client;

import client.ui.ButtonPanel;
import utils.cmd.Message;
import utils.cmd.Move;
import utils.cmd.Start;
import utils.cmd.SuggestDraw;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

/**
 * @author professorik
 * @created 08/03/2023 - 13:42
 * @project socket-chess
 */
public class ClientHandler {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final UUID ID;
    private UUID roomID;
    private boolean white;
    private boolean turn;

    public ClientHandler(String addr, int port) {
        ID = UUID.randomUUID();
        try {
            socket = new Socket(addr, port);
        } catch (IOException e) {
            System.err.println("Socket failed");
        }
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            new ReadMsg().start();
        } catch (IOException e) {
            ClientHandler.this.downService();
        }
    }

    public void start() {
        sendObj(new Message(ID, "s"));
    }

    public void move(int from, int to) {
        if (turn) sendObj(new Move(ID, white, from, to));
    }

    public void resign() {
        sendObj(new Message(ID, "r"));
    }

    public void suggestDraw() {
        sendObj(new SuggestDraw(ID, white));
    }

    public void declineDraw() {
        sendObj(new Message(ID, "d"));
    }

    private void sendObj(Message msg) {
        try {
            oos.writeObject(msg);
        } catch (IOException ignored) {
        }
    }

    private void downService() {
        if (socket.isClosed()) return;
        try {
            socket.close();
            ois.close();
            oos.close();
        } catch (IOException ignored) {
        }
    }

    private class ReadMsg extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    var tmp = ois.readObject();
                    switch (tmp) {
                        case Move cmd -> {
                            turn = !turn;
                            Client.gui.toggleClocks(turn);
                            if (cmd.isWhite() == white) {
                                continue;
                            }
                            Client.gui.getChessBoard().movePiece(cmd.getFrom(), cmd.getTo());
                        }
                        case Start cmd -> {
                            roomID = cmd.getID();
                            white = cmd.isWhite();
                            turn = white;
                            Client.gui.setupNewGame(white);
                            Client.gui.toggleClocks(turn);
                            Client.gui.setState(ButtonPanel.State.Standard);
                        }
                        case SuggestDraw cmd -> {
                            roomID = cmd.getID();
                            Client.gui.setState(ButtonPanel.State.Offered);
                        }
                        case Message cmd -> {
                            if (cmd.getMessage().equals("stop")) {
                                ClientHandler.this.downService();
                                return;
                            }
                            System.out.println(cmd.getID() + " " + cmd.getMessage());
                        }
                        default -> {}
                    }
                }
            } catch (IOException e) {
                ClientHandler.this.downService();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
