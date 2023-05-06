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
    private final Coord f;
    private final Coord t;
    private final long time;

    public Move(UUID ID, boolean isWhite, Coord f, Coord t, long time) {
        super(ID, "M");
        this.isWhite = isWhite;
        this.f = f;
        this.t = t;
        this.time = time;
    }

    public Move(UUID ID, boolean isWhite, Coord f, Coord t) {
        this(ID, isWhite, f, t, 1);
    }

    public boolean isWhite() {
        return isWhite;
    }

    public Coord getF() {
        return f;
    }

    public Coord getT() {
        return t;
    }

    public long getTime() {
        return time;
    }
}
