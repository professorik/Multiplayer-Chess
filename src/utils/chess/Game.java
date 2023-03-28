package utils.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * @author professorik
 * @created 11/03/2023 - 20:53
 * @project socket-chess
 */
public class Game {
    private final Player[] players;
    private final Board board;
    private Player currentTurn;
    private GameStatus status;
    private final List<Move> movesPlayed;

    public Game() {
        players = new Player[2];
        board = new Board();
        movesPlayed = new ArrayList<>();
    }

    public void initialize(Player p1, Player p2) {
        players[0] = p1;
        players[1] = p2;
        currentTurn = p1.isWhiteSide()? p1: p2;
        status = GameStatus.ACTIVE;
        System.out.println(board);
    }

    public boolean isEnd() {
        return getStatus() != GameStatus.ACTIVE;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public boolean playerMove(Player player, int startX, int startY, int endX, int endY) throws Exception {
        Spot startBox = board.getBox(startX, startY);
        Spot endBox = board.getBox(endX, endY);
        Move move = new Move(player, startBox, endBox);
        return makeMove(move, player);
    }

    private boolean makeMove(Move move, Player player) {
        try {
            System.out.println(player.whiteSide + " " + move.getStart().getX() + " " + move.getStart().getY() + " " + move.getEnd().getX() + " " + move.getEnd().getY());
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
                setStatus(player.isWhiteSide() ? GameStatus.WHITE_WIN : GameStatus.BLACK_WIN);
            }

            currentTurn = currentTurn == players[0] ? players[1] : players[0];
            System.out.println(board);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}

