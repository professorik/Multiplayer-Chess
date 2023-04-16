package utils.chess;

/**
 * @author professorik
 * @created 11/03/2023 - 20:50
 * @project socket-chess
 */
public abstract class Piece {

    private boolean killed = false;
    private boolean white = false;

    public Piece(boolean white) {
        this.setWhite(white);
    }

    public boolean isWhite() {
        return this.white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }

    public boolean isKilled() {
        return this.killed;
    }

    public void setKilled(boolean killed) {
        this.killed = killed;
    }

    public abstract boolean canMove(Board board, Spot start, Spot end);

    protected boolean isTaken(Spot spot) {
        return spot.getPiece() != null && spot.getPiece().isWhite() == this.isWhite();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Piece{");
        sb.append('}');
        return sb.toString();
    }
}
