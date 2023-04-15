package client;

import client.ui.ButtonPanel;
import client.ui.Popup;
import utils.cmd.*;

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

    public void move(int from, int to, int fx, int fy, int tx, int ty) {
        if (turn) sendObj(new Move(ID, white, from, to, fx, fy, tx, ty));
    }

    public void resign() {
        sendObj(new Resign(ID));
    }

    public void suggestDraw() {
        sendObj(new SuggestDraw(ID, white));
    }

    public void declineDraw() {
        sendObj(new DeclineDraw(ID));
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
                        case ServerMove cmd -> {
                            if (cmd.isSuccess()) {
                                turn = !turn;
                                Client.gui.toggleClocks(turn);
                                if (cmd.isWhite() == white) continue;
                                Client.gui.getChessBoard().movePiece(cmd.getFrom(), cmd.getTo());
                                continue;
                            }
                            System.out.println("FAILED " + (63 - cmd.getTo()) + " " + (63 - cmd.getFrom()) + " " + cmd.getF() + " " + cmd.getT());
                            Client.gui.getChessBoard().movePiece(63 - cmd.getTo(), 63 - cmd.getFrom());
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
                        case DeclineDraw ignored -> Client.gui.setState(ButtonPanel.State.Standard);
                        case Finish cmd -> {
                            Client.gui.stopClocks();
                            Client.gui.setState(ButtonPanel.State.Initial);
                            Popup.finish(cmd.getReason(), cmd.isWhite(), cmd.isBlack(), Client.client.white);
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
