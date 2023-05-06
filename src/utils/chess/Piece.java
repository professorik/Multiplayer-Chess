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

    public boolean allowedToMove(Board board, Spot start, Spot end) {
        return canMove(board, start, end) && !moveUnderCheck(board, start, end);
    }

    public boolean canMove(Board board, Spot start) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (start.getX() == col && start.getY() == row) continue;
                if (allowedToMove(board, start, board.getBox(col, row))) return true;
            }
        }
        return false;
    }

    private boolean moveUnderCheck(Board board, Spot start, Spot end) {
        if (start.getPiece() instanceof King) {
            System.out.println(start.getY() + " " + start.getX() + " -> " + end.getY() + " " + end.getX());
        }
        end.setPiece(start.getPiece());
        start.setPiece(null);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getBox(col, row).getPiece() instanceof King king && king.isWhite() == isWhite()) {
                    boolean checked = king.isChecked(board, col, row);
                    if (end.getPiece() instanceof King) {
                        System.out.println(checked + " " + row + " " + col);
                    }
                    start.setPiece(end.getPiece());
                    end.setPiece(null);
                    return checked;
                }
            }
        }
        return false;
    }

    public abstract int getIndex();

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
