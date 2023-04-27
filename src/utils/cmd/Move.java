package utils.cmd;

import utils.Coord;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author professorik
 * @created 21/03/2023 - 16:26
 * @project socket-chess
 */
public class Move extends Message {

    private final boolean isWhite;
    private final int from;
    private final int to;
    private final Coord f;
    private final Coord t;

    public Move(UUID ID, boolean isWhite, int from, int to) {
        this(ID, isWhite, from, to, Coord.EMPTY, Coord.EMPTY);
    }

    public Move(UUID ID, boolean isWhite, int from, int to, Coord f, Coord t) {
        super(ID, "M");
        this.isWhite = isWhite;
        this.from = from;
        this.to = to;
        this.f = f;
        this.t = t;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public Coord getF() {
        return f;
    }

    public Coord getT() {
        return t;
    }
}
