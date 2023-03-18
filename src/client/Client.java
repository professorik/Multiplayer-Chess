package client;

import client.ui.ButtonPanel;
import utils.cmd.Message;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

/**
 * @author professorik
 * @created 08/03/2023 - 13:42
 * @project socket-chess
 */
public class Client {

    private Socket socket;
    private BufferedReader inputUser;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final UUID ID;
    private UUID roomID;
    public static int count = 0;

    public Client(String addr, int port) {
        ID = UUID.randomUUID();
        try {
            socket = new Socket(addr, port);
        } catch (IOException e) {
            System.err.println("Socket failed");
        }
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            new ReadMsg().start();
            new WriteMsg().start();
        } catch (IOException e) {
            Client.this.downService();
        }
    }

    public void start() {
        sendObj(new Message(ID.toString(), "s"));
    }

    public void move(int from, int to) {
        String fromStr = from / 8 + "," + from % 8;
        String toStr = to / 8 + "," + to % 8;
        sendObj(new Message(ID.toString(), String.format("m: %s : %s", fromStr, toStr)));
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
                    if (tmp instanceof Message msg) {
                        if (msg.getMessage().equals("stop")) {
                            Client.this.downService();
                            return;
                        }
                        if (msg.getMessage().equals("S")) {
                            roomID = UUID.fromString(msg.getID());
                            Main.cg.setState(ButtonPanel.State.Standard);
                        }
                        System.out.println(msg.getID() + " " + msg.getMessage());
                    }
                }
            } catch (IOException e) {
                Client.this.downService();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public class WriteMsg extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    String cmd = inputUser.readLine();
                    sendObj(new Message(ID.toString(), cmd));

                    if (cmd.equals("stop")) {
                        Client.this.downService();
                        break;
                    }
                } catch (IOException e) {
                    Client.this.downService();
                }
            }
        }
    }
}
