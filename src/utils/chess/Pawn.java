package utils.chess;

/**
 * @author professorik
 * @created 11/03/2023 - 20:57
 * @project socket-chess
 */
public class Pawn extends Piece {

    private boolean moved;
    private boolean checkEnPassant;

    public Pawn(boolean white) {
        super(white);
        moved = false;
        checkEnPassant = false;
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        if (isTaken(end)) return false;

        int adx = Math.abs(end.getX() - start.getX());
        int dy = end.getY() - start.getY();
        int k = isWhite()? 1: -1;

        if (dy == k && adx == 0) return true;
        if (isDoubleMove(start, end) && !moved) return true;

        if (dy * adx == k){
            if (end.getPiece() != null && end.getPiece().isWhite() != isWhite()) return true;

            checkEnPassant = true;
            return true;
        }

        return false;
    }

    public boolean isDoubleMove(Spot start, Spot end) {
        int k = isWhite()? 1: -1;
        return start.getX() == end.getX() && end.getY() - start.getY() == 2*k;
    }

    public boolean isEnPassant(Spot start, Spot end, Move prev) {
        int k = isWhite()? 1: -1;
        int adx = Math.abs(end.getX() - start.getX());
        int dy = end.getY() - start.getY();
        if (prev.getStart().getPiece() instanceof Pawn pawn && pawn.isDoubleMove(prev.getStart(), prev.getEnd())) {
            return start.getY() == prev.getEnd().getY() && end.getX() == prev.getEnd().getX() && dy * adx == k;
        }
        return false;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    /**
     * One-shot flag indicating whether to check en passant
     * @return boolean
     */
    public boolean isCheckEnPassant() {
        checkEnPassant = !checkEnPassant;
        return !checkEnPassant;
    }

    public boolean isPromotion(Spot end) {
        return end.getY() == (isWhite()? 7: 0);
    }

    @Override
    public int getIndex() {
        return 1;
    }

    @Override
    public String toString() {
        return "p";
    }
}
