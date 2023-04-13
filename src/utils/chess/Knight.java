package utils.chess;

/**
 * @author professorik
 * @created 11/03/2023 - 20:51
 * @project socket-chess
 */
public class Knight extends Piece {
    public Knight(boolean white) {
        super(white);
    }

    @Override
    public boolean canMove(Board board, Spot start,
                           Spot end) {
        // we can't move the piece to a spot that has
        // a piece of the same colour
        if (end.getPiece() != null && end.getPiece().isWhite() == this.isWhite()) {
            return false;
        }

        int x = Math.abs(start.getY() - end.getY());
        int y = Math.abs(start.getX() - end.getX());
        return x * y == 2;
    }

    @Override
    public String toString() {
        return "N";
    }
}

