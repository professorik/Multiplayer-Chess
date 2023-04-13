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
        int dy = end.getY() - start.getY(), dx = end.getX() - start.getX();
        int pdy = Math.abs(dy), pdx = Math.abs(dx);

        if (pdy != pdx || pdy == 0) return false;

        dy /= pdy;
        dx /= pdx;
        for (int y = start.getY() + dy, x = start.getX() + dx; y != end.getY(); y += dy, x += dx) {
            try {
                if (board.getBox(x, y).getPiece() != null) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "B";
    }
}
