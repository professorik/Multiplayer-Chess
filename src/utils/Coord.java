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

    public Coord getOrientation(Coord dest) {
        int dr = dest.r - r, dc = dest.c - c;
        int pdr = Math.abs(dr), pdc = Math.abs(dc);

        if (pdr != 0) dr /= pdr;
        if (pdc != 0) dc /= pdc;

        return new Coord(dr, dc);
    }

    @Override
    public String toString() {
        return "(" + r + ", " + c + ")";
    }
}
