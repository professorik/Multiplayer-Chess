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
        if (isTaken(end)) return false;

        int r = Math.abs(start.getY() - end.getY());
        int c = Math.abs(start.getX() - end.getX());
        if (r < 2 && c < 2) return r + c != 0;
        if (r > 0) return false;

        return isValidCastling(board, start, end);
    }

    private boolean isValidCastling(Board board, Spot start, Spot end) {
        if (isMoved()) return false;

        int dc = end.getX() - start.getX();
        int adc = Math.abs(dc);
        if (adc < 2) return false;
        dc /= adc;

        if (isMoved() || isChecked(board, start.getX(), start.getY())) return false;

        int col = start.getX() + dc;
        for (; 0 < col && col < 7; col += dc) {
            Spot spot = board.getBox(col, start.getY());
            if (spot.getPiece() != null || isChecked(board, col, start.getY()))
                return false;
        }
        return board.getBox(col, start.getY()).getPiece() instanceof Rook rook && !rook.isMoved();
    }

    public boolean isCastlingMove(Spot start, Spot end) {
        return Math.abs(start.getX() - end.getX()) > 1;
    }

    protected boolean isChecked(Board board, int c, int r) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Spot tmp = board.getBox(col, row);
                Piece piece = tmp.getPiece();
                if (piece == null || piece.isWhite() == isWhite()) continue;
                if (!(piece instanceof Pawn && col == c) && piece.canMove(board, tmp, board.getBox(c, r)))
                    return true;
            }
        }
        return false;
    }

    @Override
    public int getIndex() {
        return 5;
    }

    @Override
    public String toString() {
        return "K";
    }
}
