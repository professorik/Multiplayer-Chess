package utils.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * @author professorik
 * @created 11/03/2023 - 20:53
 * @project socket-chess
 */
public class Game {
    private Player[] players;
    private Board board;
    private Player currentTurn;
    private GameStatus status;
    private List<Move> movesPlayed;

    private void initialize(Player p1, Player p2) {
        players[0] = p1;
        players[1] = p2;
        board.resetBoard();
        currentTurn = p1.isWhiteSide()? p1: p2;
        movesPlayed = new ArrayList<>();
    }

    public boolean isEnd() {
        return this.getStatus() != GameStatus.ACTIVE;
    }

    public GameStatus getStatus() {
        return this.status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public boolean playerMove(Player player, int startX, int startY, int endX, int endY) throws Exception {
        Spot startBox = board.getBox(startX, startY);
        Spot endBox = board.getBox(endX, endY);
        Move move = new Move(player, startBox, endBox);
        return this.makeMove(move, player);
    }

    private boolean makeMove(Move move, Player player) {
        if (player != currentTurn) return false;

        Piece sourcePiece = move.getStart().getPiece();
        if (sourcePiece == null || sourcePiece.isWhite() != player.isWhiteSide()) return false;
        if (!sourcePiece.canMove(board, move.getStart(), move.getEnd())) return false;

        Piece destPiece = move.getStart().getPiece();
        if (destPiece != null) {
            destPiece.setKilled(true);
            move.setPieceKilled(destPiece);
        }

        if (sourcePiece instanceof King king && king.isCastlingMove(move.getStart(), move.getEnd())) {
            move.setCastlingMove(true);
        }

        movesPlayed.add(move);

        move.getEnd().setPiece(move.getStart().getPiece());
        move.getStart().setPiece(null);

        if (destPiece instanceof King) {
            this.setStatus(player.isWhiteSide()? GameStatus.WHITE_WIN: GameStatus.BLACK_WIN);
        }

        this.currentTurn = this.currentTurn == players[0]? players[1]: players[0];
        return true;
    }
}

