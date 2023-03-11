package server;

import java.util.LinkedList;

/**
 * @author professorik
 * @created 08/03/2023 - 14:10
 * @project socket-chess
 */
public class Room {

    private final String ID;
    private final LinkedList<ServerSomething> users = new LinkedList<>();

    public Room() {
        ID = "hello";
    }

    protected void addMember(ServerSomething member){
        users.add(member);
    }

    public String getID() {
        return ID;
    }
}
