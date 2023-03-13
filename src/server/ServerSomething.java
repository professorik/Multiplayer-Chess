package server;

import utils.cmd.Message;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

/**
 * @author professorik
 * @created 08/03/2023 - 13:23
 * @project socket-chess
 */
class ServerSomething extends Thread {

    private UUID ID;
    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;

    public ServerSomething(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        Server.story.printStory(out);
        start();
    }

    @Override
    public void run() {
        String word;
        try {
            while (true) {
                word = in.readLine();
                if (word.equals("stop")) {
                    this.downService();
                    break;
                }
                var msg = Message.parse(word);
                if (msg.getMessage().equals("s")) {
                    ID = UUID.fromString(msg.getID());
                    Server.match(this);
                    continue;
                }
                Server.rooms.get(UUID.fromString(msg.getID())).broadcast(msg.getMessage() + ": " + msg.getID());
            }
        } catch (NullPointerException | IOException e) {
            this.downService();
        }
    }

    protected void send(String msg) {
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
                for (ServerSomething vr : Server.serverList) {
                    if (vr.equals(this)) vr.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {
        }
    }

    public UUID getID() {
        return ID;
    }
}
