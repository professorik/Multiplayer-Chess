package utils.chess;

/**
 * @author professorik
 * @created 11/03/2023 - 20:57
 * @project socket-chess
 */
public class Rook extends Piece {

    private boolean moved = false;

    public Rook(boolean white) {
        super(white);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        return false;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }
}
