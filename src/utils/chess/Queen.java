package utils.chess;

/**
 * @author professorik
 * @created 11/03/2023 - 20:57
 * @project socket-chess
 */
public class Queen extends Piece {
    public Queen(boolean white) {
        super(white);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        return false;
    }
}
