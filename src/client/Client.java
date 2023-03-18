package client;

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
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader inputUser; //console
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
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            handshake();
            new ReadMsg().start();
            new WriteMsg().start();
        } catch (IOException e) {
            Client.this.downService();
        }
    }

    private void handshake() {
        send("s: " + ID.toString());
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {
        }
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {
        }
    }

    private class ReadMsg extends Thread {
        @Override
        public void run() {

            String cmd;
            try {
                while (true) {
                    cmd = in.readLine();
                    System.out.println(cmd);
                    if (cmd.equals("stop")) {
                        Client.this.downService();
                        break;
                    }
                    var msg = Message.parse(cmd);
                    if (msg.getMessage().equals("S")) {
                        roomID = UUID.fromString(msg.getID());
                    }
                }
            } catch (IOException e) {
                Client.this.downService();
            }
        }
    }

    public class WriteMsg extends Thread {

        @Override
        public void run() {
            while (true) {
                String cmd;
                try {
                    cmd = inputUser.readLine();
                    if (cmd.equals("stop")) {
                        send("stop");
                        Client.this.downService();
                        break;
                    }

                    send(cmd + ": " + ID.toString());
                } catch (IOException e) {
                    Client.this.downService();
                }
            }
        }
    }
}
