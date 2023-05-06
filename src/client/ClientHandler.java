package client;

import client.ui.ButtonPanel;
import client.ui.GUI;
import client.ui.Popup;
import utils.Coord;
import utils.chess.Pieces;
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

    public void forfeit() {
        sendObj(new Forfeit(ID));
    }

    public void move(Coord f, Coord t) {
        if (turn) sendObj(new Move(ID, white, -1, -1, f, t, Client.gui.getTime()));
    }

    public void move(Coord f, Coord t, Pieces piece) {
        if (turn) sendObj(new PromotionMove(ID, white, -1, -1, f, t, piece, Client.gui.getTime()));
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
                        case utils.cmd.State cmd -> {
                            Client.gui.getChessBoard().mergeBoards(cmd.getBoard());
                            if (cmd.isSuccess()) {
                                turn = !turn;
                                Client.gui.addLabel(cmd.getLabel());
                                Client.gui.toggleClocks(turn);
                            }
                        }
                        case Start cmd -> {
                            white = cmd.isWhite();
                            turn = white;
                            Client.gui.setupNewGame(white);
                            Client.gui.toggleClocks(turn);
                            Client.gui.setState(ButtonPanel.State.Standard);
                        }
                        case SuggestDraw ignored -> Client.gui.setState(ButtonPanel.State.Offered);
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
