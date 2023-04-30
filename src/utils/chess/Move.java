package utils.chess;

/**
 * @author professorik
 * @created 11/03/2023 - 20:52
 * @project socket-chess
 */
public class Move {
    private final Player player;
    private final Spot start;
    private final Spot end;
    private Piece pieceMoved;
    private Piece pieceKilled;
    private Pieces pieceChosen;
    private boolean castlingMove = false;

    public Move(Player player, Spot start, Spot end, Pieces pieceChosen) {
        this.player = player;
        this.start = start;
        this.end = end;
        this.pieceChosen = pieceChosen;
        this.pieceMoved = start.getPiece();
    }

    public boolean isCastlingMove() {
        return this.castlingMove;
    }

    public void setCastlingMove(boolean castlingMove) {
        this.castlingMove = castlingMove;
    }

    public void setPieceMoved(Piece pieceMoved) {
        this.pieceMoved = pieceMoved;
    }

    public void setPieceKilled(Piece pieceKilled) {
        this.pieceKilled = pieceKilled;
    }

    public void setPieceChosen(Pieces pieceChosen) {
        this.pieceChosen = pieceChosen;
    }

    public Pieces getPieceChosen() {
        return pieceChosen;
    }

    public Spot getStart() {
        return start;
    }

    public Spot getEnd() {
        return end;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Move{");
        sb.append("player=").append(player);
        sb.append(", start=").append(start);
        sb.append(", end=").append(end);
        sb.append(String.format(", start_c=(%d,%d)", getStart().getX(), getStart().getY()));
        sb.append(String.format(", end_c=(%d,%d)", getEnd().getX(), getEnd().getY()));
        sb.append(", pieceMoved=").append(pieceMoved);
        sb.append(", pieceKilled=").append(pieceKilled);
        sb.append(", castlingMove=").append(castlingMove);
        sb.append('}');
        return sb.toString();
    }
}
