package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Server {

    public static final int PORT = 8080;
    public static LinkedList<ServerSomething> serverList = new LinkedList<>();
    public static Story story;
    public static Queue<ServerSomething> pull = new LinkedList<>();
    public static Map<ServerSomething, Room> rooms = new HashMap<>();

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(PORT)) {
            story = new Story();
            System.out.println("Server Started");
            while (true) {
                Socket socket = server.accept();
                try {
                    var smth = new ServerSomething(socket);
                    serverList.add(smth);
                    match(smth);
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

        rooms.put(member, room);
        rooms.put(opponent, room);
    }
}
