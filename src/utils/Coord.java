package utils;

import java.io.Serializable;

/**
 * @author professorik
 * @created 23/04/2023 - 15:38
 * @project socket-chess
 */
public class Coord implements Serializable {

    public static final Coord EMPTY = new Coord(-1, -1);

    public int r;
    public int c;

    public Coord(int r, int c) {
        this.r = r;
        this.c = c;
    }

    public void orient(boolean white) {
        if (white) return;
        r = 7 - r;
        c = 7 - c;
    }

    @Override
    public String toString() {
        return "(" + r + ", " + c + ")";
    }
}
