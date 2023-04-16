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
        if (isTaken(end)) return false;

        int dy = end.getY() - start.getY(), dx = end.getX() - start.getX();
        int pdy = Math.abs(dy), pdx = Math.abs(dx);

        if (pdy * pdx != 0 || pdy + pdx == 0) return false;

        dy /= pdy;
        dx /= pdx;
        for (int y = start.getY() + dy, x = start.getX() + dx; y != end.getY() && x != end.getX(); y += dy, x += dx) {
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

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    @Override
    public String toString() {
        return "R";
    }
}
