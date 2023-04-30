package utils.cmd;

import utils.Coord;

import java.util.UUID;

/**
 * @author professorik
 * @created 21/03/2023 - 16:26
 * @project socket-chess
 */
public class State extends Move {

    private final boolean success;
    private final String label;
    private int[][] board;

    public State(UUID ID, boolean isWhite, int from, int to, boolean success, String label, Coord f, Coord t) {
        super(ID, isWhite, from, to, f, t);
        this.success = success;
        this.label = label;
    }

    public State(Move move, boolean success, String label, int[][] board) {
        this(move.getID(), move.isWhite(), move.getFrom(), move.getTo(), success, label, move.getF(), move.getT());
        this.board = board;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getLabel() {
        return label;
    }

    public int[][] getBoard() {
        return board;
    }
}
