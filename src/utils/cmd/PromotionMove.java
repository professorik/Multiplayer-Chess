package utils.cmd;

import utils.Coord;
import utils.chess.Pieces;

import java.util.UUID;

/**
 * @author professorik
 * @created 21/03/2023 - 16:26
 * @project socket-chess
 */
public class PromotionMove extends Move {

    private final Pieces piece;

    public PromotionMove(UUID ID, boolean isWhite, int from, int to, Coord f, Coord t, Pieces piece, long time) {
        super(ID, isWhite, from, to, f, t, time);
        if (piece == Pieces.KING || piece == Pieces.PAWN) piece = Pieces.QUEEN;
        this.piece = piece;
    }

    public Pieces getPiece() {
        return piece;
    }
}
