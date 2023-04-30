package utils.chess;

/**
 * @author professorik
 * @created 11/03/2023 - 21:04
 * @project socket-chess
 */
public enum Pieces {
    PAWN,
    ROOK,
    BISHOP,
    KNIGHT,
    KING,
    QUEEN;

    public Piece getPiece(boolean white) {
        return switch (this) {
            case QUEEN -> new Queen(white);
            case BISHOP -> new Bishop(white);
            case ROOK -> new Rook(white);
            case KNIGHT -> new Knight(white);
            case PAWN -> new Pawn(white);
            default -> new King(white);
        };
    }
}
