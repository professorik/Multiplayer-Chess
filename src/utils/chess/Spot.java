package utils.chess;

/**
 * @author professorik
 * @created 11/03/2023 - 20:49
 * @project socket-chess
 */
public class Spot {
    private Piece piece;
    private int x;
    private int y;

    public Spot(int y, int x, Piece piece) {
        this.setPiece(piece);
        this.setX(x);
        this.setY(y);
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

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return this.x;
    }

    public void setY(int y) {
        this.y = y;
    }
}

