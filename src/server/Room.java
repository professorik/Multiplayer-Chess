package server;

import utils.chess.Game;
import utils.chess.Player;
import utils.cmd.*;

import java.util.*;

/**
 * @author professorik
 * @created 08/03/2023 - 14:10
 * @project socket-chess
 */
public class Room {

    private final UUID ID;
    private final LinkedList<ServerHandler> users = new LinkedList<>();
    private final Map<UUID, Player> userToPlayer = new HashMap<>();
    private final HashSet<UUID> drawPool = new HashSet<>();
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
        int startC = move.getF().c, startR = move.getF().r;
        int endC = move.getT().c, endR = move.getT().r;
        Player player = userToPlayer.get(move.getID());
        if (player.whiteSide) {
            startR = 7 - startR;
            endR = 7 - endR;
        } else {
            startC = 7 - startC;
            endC = 7 - endC;
        }

        boolean success;
        if (move instanceof PromotionMove pm) {
            success = game.playerMove(player, startC, startR, endC, endR, pm.getPiece());
        } else {
            success = game.playerMove(player, startC, startR, endC, endR);
        }
        if (success)
            broadcast(new State(move, true, game.getLabel(startC, startR, endC, endR), game.getBoard().convert()));
        else
            broadcastMirror(new State(move, false, "", game.getBoard().convert()));

        game.processEnding(player, move.getTime());
        if (game.isEnd()) {
            broadcast(new Finish(ID, game.getStatus()));
            destruct();
        }
    }

    protected void draw(SuggestDraw cmd) {
        drawPool.add(cmd.getID());
        if (drawPool.size() == 2) {
            broadcast(new Finish(ID, true, true, Finish.Reason.AGREEMENT));
            destruct();
            return;
        }
        broadcastExclusive(cmd);
    }

    protected void decline(DeclineDraw cmd) {
        drawPool.clear();
        broadcastExclusive(cmd);
    }

    protected void forfeit(Forfeit cmd) {
        var user = userToPlayer.get(cmd.getID());
        broadcast(new Finish(ID, !user.whiteSide, user.whiteSide, Finish.Reason.FORFEIT));
        destruct();
    }

    protected void resign(Resign cmd) {
        var user = userToPlayer.get(cmd.getID());
        broadcast(new Finish(ID, !user.whiteSide, user.whiteSide, Finish.Reason.RESIGNATION));
        destruct();
    }

    protected void broadcast(Message msg) {
        msg.setID(ID);
        for (ServerHandler vr: users) {
            vr.sendObj(msg);
        }
    }

    protected void broadcastMirror(Message msg) {
        for (ServerHandler vr: users) {
            if (vr.getID() == msg.getID()) {
                vr.sendObj(msg);
                return;
            }
        }
    }

    protected void broadcastExclusive(Message msg) {
        var id = msg.getID();
        msg.setID(ID);
        for (ServerHandler vr: users) {
            if (vr.getID() != id)
                vr.sendObj(msg);
        }
    }

    private void destruct() {
        for (UUID id: userToPlayer.keySet()) {
            Server.rooms.remove(id);
        }
    }
}
