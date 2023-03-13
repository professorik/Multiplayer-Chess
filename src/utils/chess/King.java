package utils.chess;

/**
 * @author professorik
 * @created 11/03/2023 - 20:51
 * @project socket-chess
 */
public class King extends Piece {

    private boolean moved = false;

    public King(boolean white) {
        super(white);
    }

    public boolean isMoved() {
        return this.moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        if (end.getPiece().isWhite() == this.isWhite()) return false;

        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());
        if (x + y == 1) return true;

        return this.isValidCastling(board, start, end);
    }

    private boolean isValidCastling(Board board, Spot start, Spot end) {
        if (this.isMoved() || (end.getPiece() instanceof Rook rook && rook.isMoved())) return false;

        if (Math.abs(start.getX() - end.getX()) < 2) return false;


        // Logic for returning true or false
        return start == end;
    }

    public boolean isCastlingMove(Spot start, Spot end) {
        // check if the starting and
        // ending position are correct
        // TODO: implement
        return true;
    }

    private boolean isChecked(Board board, int x, int y) {
        // TODO: implement
        return false;
    }
}
