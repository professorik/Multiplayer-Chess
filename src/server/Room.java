package server;

import utils.cmd.Message;
import utils.cmd.Start;

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
    // here insert the chess board

    public Room() {
        ID = UUID.randomUUID();
    }

    protected void addMember(ServerSomething member){
        users.add(member);
    }

    protected void handshake(){
        users.getFirst().sendObj(new Start(ID, true));
        users.getLast().sendObj(new Start(ID, false));
    }

    protected void broadcast(Message msg) {
        msg.setID(ID);
        for (ServerSomething vr: users) {
            vr.sendObj(msg);
        }
    }
}
