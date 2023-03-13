package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    public static final int PORT = 8080;
    public static LinkedList<ServerSomething> serverList = new LinkedList<>();
    public static Story story;
    public static Queue<ServerSomething> pull = new LinkedList<>();
    public static Map<UUID, Room> rooms = new HashMap<>();

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(PORT)) {
            story = new Story();
            System.out.println("Server Started");
            while (true) {
                Socket socket = server.accept();
                try {
                    var smth = new ServerSomething(socket);
                    //serverList.add(smth);
                } catch (IOException e) {
                    socket.close();
                }
            }
        }
    }

    public static void match(ServerSomething member) {
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
