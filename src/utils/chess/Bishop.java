package utils.chess;

/**
 * @author professorik
 * @created 11/03/2023 - 20:57
 * @project socket-chess
 */
public class Bishop extends Piece {
    public Bishop(boolean white) {
        super(white);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        if (isTaken(end)) return false;

        int dy = end.getY() - start.getY(), dx = end.getX() - start.getX();
        int pdy = Math.abs(dy), pdx = Math.abs(dx);

        if (pdy != pdx || pdy == 0) return false;

        dy /= pdy;
        dx /= pdx;
        for (int y = start.getY() + dy, x = start.getX() + dx; y != end.getY(); y += dy, x += dx) {
            if (board.getBox(x, y).getPiece() != null) return false;
        }
        return true;
    }

    @Override
    public int getIndex() {
        return 3;
    }

    @Override
    public String toString() {
        return "B";
    }
}
