package server;

import utils.cmd.DeclineDraw;
import utils.cmd.Message;
import utils.cmd.Move;
import utils.cmd.SuggestDraw;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

/**
 * @author professorik
 * @created 08/03/2023 - 13:23
 * @project socket-chess
 */
class ServerHandler extends Thread {

    private UUID ID;
    private final Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public ServerHandler(Socket socket) throws IOException {
        this.socket = socket;
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                var tmp = ois.readObject();
                if (tmp instanceof Move cmd) {
                    Server.rooms.get(cmd.getID()).move(cmd);
                    continue;
                }
                if (tmp instanceof SuggestDraw cmd) {
                    Server.rooms.get(cmd.getID()).draw(cmd);
                    continue;
                }
                if (tmp instanceof DeclineDraw cmd) {
                    Server.rooms.get(cmd.getID()).decline(cmd);
                    continue;
                }
                if (tmp instanceof Message msg) {
                    if (msg.getMessage().equals("stop")) {
                        this.downService();
                        return;
                    }
                    if (msg.getMessage().equals("s")) {
                        ID = msg.getID();
                        Server.match(this);
                        continue;
                    }
                    System.out.println(msg.getID() + " " + msg.getMessage());
                    Server.rooms.get(msg.getID()).broadcast(msg);
                }
            }
        } catch (NullPointerException | IOException e) {
            this.downService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void sendObj(Message msg) {
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException ignored) {
        }
    }

    private void downService() {
        if (socket.isClosed()) return;
        try {
            socket.close();
            ois.close();
            oos.close();
            for (ServerHandler vr : Server.serverList) {
                if (vr.equals(this)) vr.interrupt();
                Server.serverList.remove(this);
            }
        } catch (IOException ignored) {
        }
    }

    public UUID getID() {
        return ID;
    }
}
