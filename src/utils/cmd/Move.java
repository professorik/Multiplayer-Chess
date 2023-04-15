package utils.cmd;

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
        this(ID, isWhite, from, to, -1, -1, -1, -1);
    }

    public Move(UUID ID, boolean isWhite, int from, int to, int fx, int fy, int tx, int ty) {
        super(ID, "M");
        this.isWhite = isWhite;
        this.from = from;
        this.to = to;
        this.f = new Coord(fx, fy);
        this.t = new Coord(tx, ty);
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

    static class Coord implements Serializable {
        int x;
        int y;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}
