package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    public static final int PORT = 8080;
    public static LinkedList<ServerHandler> serverList = new LinkedList<>();
    public static Queue<ServerHandler> pull = new LinkedList<>();
    public static Map<UUID, Room> rooms = new HashMap<>();

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server Started");
            while (true) {
                Socket socket = server.accept();
                try {
                   new ServerHandler(socket);
                } catch (IOException e) {
                    socket.close();
                }
            }
        }
    }

    public static void match(ServerHandler member) {
        if (pull.isEmpty()) {
            pull.add(member);
            return;
        }

        var opponent = pull.poll();
        var room = new Room();
        room.addMember(member);
        room.addMember(opponent);
        room.handshake();

        rooms.put(member.getID(), room);
        rooms.put(opponent.getID(), room);
    }
}
