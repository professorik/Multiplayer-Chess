package utils.chess;

import utils.Coord;

/**
 * @author professorik
 * @created 11/03/2023 - 20:49
 * @project socket-chess
 */
public class Spot {

    private Piece piece;
    private final Coord coord;
    public static final Spot DEFAULT = new Spot(-1, -1, null);

    public Spot(int r, int c, Piece piece) {
        this.setPiece(piece);
        this.coord = new Coord(r, c);
    }

    @Override
    public String toString() {
        if (piece == null) return "#";
        return piece.toString();
    }

    public Piece getPiece() {
        return this.piece;
    }

    public void setPiece(Piece p) {
        this.piece = p;
    }

    public Coord getCoord() {
        return coord;
    }

    public int getY() {
        return this.coord.r;
    }

    public int getX() {
        return this.coord.c;
    }
}

