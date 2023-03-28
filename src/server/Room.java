package server;

import utils.chess.Game;
import utils.chess.Player;
import utils.cmd.Message;
import utils.cmd.Move;
import utils.cmd.Start;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

/**
 * @author professorik
 * @created 08/03/2023 - 14:10
 * @project socket-chess
 */
public class Room {

    private final UUID ID;
    private final LinkedList<ServerHandler> users = new LinkedList<>();
    private final Map<UUID, Player> userToPlayer = new HashMap<>();
    private final Game game;

    public Room() {
        ID = UUID.randomUUID();
        game = new Game();
    }

    protected void addMember(ServerHandler member){
        users.add(member);
    }

    protected void handshake(){
        var white = new Player(true);
        var black = new Player(false);
        userToPlayer.put(users.getFirst().getID(), white);
        userToPlayer.put(users.getLast().getID(), black);
        users.getFirst().sendObj(new Start(ID, true));
        users.getLast().sendObj(new Start(ID, false));
        game.initialize(white, black);
    }

    protected void move(Move move) throws Exception {
        int startX = move.getFrom() / 8, startY = move.getFrom() % 8;
        int endX = move.getTo() / 8, endY = move.getTo() % 8;

        System.out.println(startX + " " + startY + " " + endX + " " + endY);

        System.out.println(userToPlayer.containsKey(move.getID()));

        if (!userToPlayer.get(move.getID()).whiteSide) {
            startY = 7 - startY;
            endY = 7 - endY;
        } else {
            startX = 7 - startX;
            endX = 7 - endX;
        }

        System.out.println(startX + " " + startY + " " + endX + " " + endY);

        boolean fl = game.playerMove(userToPlayer.get(move.getID()), startX, startY, endX, endY);
        if (!fl) {
            System.out.println("error");
        }
        broadcast(move);
        if (game.isEnd()) {
            broadcast(new Message(ID, game.getStatus().name()));
        }
    }

    protected void broadcast(Message msg) {
        msg.setID(ID);
        for (ServerHandler vr: users) {
            vr.sendObj(msg);
        }
    }
}
