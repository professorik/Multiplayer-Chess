package utils.chess;

/**
 * @author professorik
 * @created 11/03/2023 - 20:57
 * @project socket-chess
 */
public class Queen extends Piece {

    private final Bishop bishop;
    private final Rook rook;

    public Queen(boolean white) {
        super(white);
        bishop = new Bishop(white);
        rook = new Rook(white);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        return bishop.canMove(board, start, end) || rook.canMove(board, start, end);
    }

    @Override
    public String toString() {
        return "Q";
    }
}
