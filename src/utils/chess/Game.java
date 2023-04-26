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
        currentTurn = p1.isWhiteSide() ? p1 : p2;
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
            System.out.println(player.whiteSide + " " + move);
            if (player != currentTurn) return false;

            Piece sourcePiece = move.getStart().getPiece();
            if (sourcePiece == null || sourcePiece.isWhite() != player.isWhiteSide()) return false;
            if (!sourcePiece.canMove(board, move.getStart(), move.getEnd())) return false;

            Piece destPiece = move.getEnd().getPiece();
            if (destPiece != null) {
                destPiece.setKilled(true);
                move.setPieceKilled(destPiece);
            }

            boolean isNotCastling = true;
            if (sourcePiece instanceof King king) {
                king.setMoved(true);
                if (king.isCastlingMove(move.getStart(), move.getEnd())) {
                    int dc = move.getEnd().getX() - move.getStart().getX();
                    dc /= Math.abs(dc);
                    Spot rookSpot = board.getBox(dc > 0? 7: 0, move.getStart().getY());
                    Rook rook = (Rook) rookSpot.getPiece();
                    rook.setMoved(true);

                    rookSpot.setPiece(null);
                    board.getBox(move.getStart().getX() + dc, move.getStart().getY()).setPiece(rook);
                    board.getBox(move.getStart().getX() + 2*dc, move.getStart().getY()).setPiece(king);
                    isNotCastling = false;
                }
            } else if (sourcePiece instanceof Rook rook) {
                rook.setMoved(true);
            } else if (sourcePiece instanceof Pawn pawn) {
                if (pawn.isCheckEnPassant()) {
                    var prev = movesPlayed.get(movesPlayed.size() - 1);
                    if (pawn.isEnPassant(move.getStart(), move.getEnd(), prev)) {
                        move.setPieceKilled(prev.getEnd().getPiece());
                        prev.getEnd().setPiece(null);
                    } else {
                        return false;
                    }
                }
                if (pawn.isPromotion(move.getEnd())) {
                    System.out.println("Promotion");
                }
                pawn.setMoved(true);
            }

            movesPlayed.add(move);
            if (isNotCastling)
                move.getEnd().setPiece(move.getStart().getPiece());
            move.getStart().setPiece(null);

            if (destPiece instanceof King) {
                setStatus(player.isWhiteSide() ? GameStatus.WHITE_WIN : GameStatus.BLACK_WIN);
            }

            currentTurn = currentTurn == players[0] ? players[1] : players[0];
            System.out.println(board);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public Board getBoard() {
        return board;
    }
}

