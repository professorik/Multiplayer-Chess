package server;

import java.util.LinkedList;
import java.util.UUID;

/**
 * @author professorik
 * @created 08/03/2023 - 14:10
 * @project socket-chess
 */
public class Room {

    private final UUID ID;
    private final LinkedList<ServerSomething> users = new LinkedList<>();

    public Room() {
        ID = UUID.randomUUID();
    }

    protected void addMember(ServerSomething member){
        users.add(member);
    }

    protected void handshake(){
        broadcast("S: " + ID);
    }

    protected void broadcast(String cmd) {
        for (ServerSomething vr: users) {
            vr.send(cmd);
        }
    }

    protected UUID getID() {
        return ID;
    }
}
