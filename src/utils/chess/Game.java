package utils.chess;

import java.util.ArrayList;

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

    private boolean capture, castling, check, checkmate;
    private String promotion;

    public Game() {
        players = new Player[2];
        board = new Board();
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

    public void processEnding(Player player, long time) {
        if (time <= 0) {
            setStatus(player.isWhiteSide() ? GameStatus.FORFEIT_WHITE : GameStatus.FORFEIT_BLACK);
            return;
        }
        if (checkmate) {
            setStatus(player.isWhiteSide() ? GameStatus.MATE_WHITE_WIN : GameStatus.MATE_BLACK_WIN);
            return;
        }
        if (noMoves(currentTurn)) {
            setStatus(GameStatus.STALEMATE);
            return;
        }
        if (notEnoughFigures(player) && notEnoughFigures(currentTurn)) {
            setStatus(GameStatus.NOT_ENOUGH_FIGURES);
        }
    }

    public boolean playerMove(Player player, int startX, int startY, int endX, int endY) {
        return playerMove(player, startX, startY, endX, endY, null);
    }

    public boolean playerMove(Player player, int startX, int startY, int endX, int endY, Pieces piece) {
        Spot startBox = board.getBox(startX, startY);
        Spot endBox = board.getBox(endX, endY);
        Move move = new Move(player, startBox, endBox, piece);
        return makeMove(move, player);
    }

    private boolean makeMove(Move move, Player player) {
        capture = castling = check = checkmate = false;
        promotion = "";

        if (player != currentTurn) return false;
        Piece sourcePiece = move.getStart().getPiece();
        // check if the player is allowed to move
        if (sourcePiece == null || sourcePiece.isWhite() != player.isWhiteSide()) return false;
        if (!sourcePiece.canMove(board, move.getStart(), move.getEnd())) return false;
        if (moveUnderCheck(move, player)) return false;

        if (sourcePiece instanceof King king) {
            king.setMoved(true);
            if (king.isCastlingMove(move.getStart(), move.getEnd())) {
                int dc = move.getEnd().getX() - move.getStart().getX();
                dc /= Math.abs(dc);
                Spot rookSpot = board.getBox(dc > 0 ? 7 : 0, move.getStart().getY());
                Rook rook = (Rook) rookSpot.getPiece();
                rook.setMoved(true);

                rookSpot.setPiece(null);
                board.getBox(move.getStart().getX() + dc, move.getStart().getY()).setPiece(rook);
                board.getBox(move.getStart().getX() + 2 * dc, move.getStart().getY()).setPiece(king);
                castling = true;
            }
        } else if (sourcePiece instanceof Rook rook) {
            rook.setMoved(true);
        } else if (sourcePiece instanceof Pawn pawn) {
            if (pawn.isEnPassant()) {
                var prev = board.getPrevMove();
                move.setPieceKilled(prev.getEnd().getPiece());
                prev.getEnd().setPiece(null);
                capture = true;
            }
            if (pawn.isPromotion(move.getEnd())) {
                Pieces piece = move.getPieceChosen();
                if (piece == null) piece = Pieces.QUEEN;
                move.getStart().setPiece(piece.getPiece(player.isWhiteSide()));

                promotion = move.getStart().getPiece().toString();
            }
            pawn.setMoved(true);
        }

        Piece destPiece = move.getEnd().getPiece();
        if (destPiece != null) {
            destPiece.setKilled(true);
            move.setPieceKilled(destPiece);
            capture = true;
        }

        board.setPrevMove(move);
        if (!castling)
            move.getEnd().setPiece(move.getStart().getPiece());
        move.getStart().setPiece(null);

        currentTurn = currentTurn == players[0] ? players[1] : players[0];

        if (isUnderCheck(currentTurn)) {
            check = true;
            if (noMoves(currentTurn)) {
                checkmate = true;
            }
        }
        System.out.println(board);
        return true;
    }

    private boolean moveUnderCheck(Move move, Player player) {
        Piece end = move.getEnd().getPiece();
        move.getEnd().setPiece(move.getStart().getPiece());
        move.getStart().setPiece(null);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getBox(col, row).getPiece() instanceof King king && king.isWhite() == player.isWhiteSide()) {
                    boolean checked = king.isChecked(board, col, row);
                    move.getStart().setPiece(move.getEnd().getPiece());
                    move.getEnd().setPiece(end);
                    return checked;
                }
            }
        }
        return false;
    }

    private boolean isUnderCheck(Player player) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getBox(col, row).getPiece() instanceof King king && king.isWhite() == player.isWhiteSide())
                    return king.isChecked(board, col, row);
            }
        }
        return false;
    }

    private boolean noMoves(Player player) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                var spot = board.getBox(col, row);
                var piece = spot.getPiece();
                if (piece == null || piece.isWhite() != player.isWhiteSide()) continue;
                if (piece.canMove(board, spot)) return false;
            }
        }
        return true;
    }

    private boolean notEnoughFigures(Player player) {
        record Figure(Integer index, int row, int col) {
            int getParity() {
                return (row() + col()) % 2;
            }
        }

        var list = new ArrayList<Figure>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                var piece = board.getBox(col, row).getPiece();
                if (piece != null && piece.isWhite() == player.isWhiteSide() && !(piece instanceof King))
                    list.add(new Figure(piece.getIndex(), row, col));
            }
        }
        if (list.size() == 0) return true;
        if (list.size() == 1)
            return list.get(0).index == Pieces.BISHOP.ordinal() + 1 || list.get(0).index == Pieces.KNIGHT.ordinal() + 1;

        int parity = list.get(0).getParity();
        for (Figure f : list) {
            if (f.index != Pieces.BISHOP.ordinal() + 1) return false;
            if (f.getParity() != parity) return false;
        }
        return true;
    }

    public String getLabel(int startFile, int startRank, int endFile, int endRank) {
        System.out.println(startRank + " " + startFile + " " + endRank + " " + endFile);
        System.out.println(castling + " " + checkmate + " " + check + " " + capture);
        System.out.println(promotion);

        if (castling) {
            return startFile < endFile ? "0-0" : "0-0-0";
        }

        Piece piece = board.getBox(endFile, endRank).getPiece();
        String p = piece.toString();
        if (piece instanceof Pawn) p = "";

        String f = String.valueOf((char) ('a' + endFile));
        String r = String.valueOf(endRank + 1);

        String m = capture ? "x" : "";

        String dFile = "", dRank = "";
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                if ((rank == endRank && file == endFile) || (rank == startRank && file == startFile)) continue;
                var spot = board.getBox(rank, file);
                var tmp = spot.getPiece();
                if (tmp == null || piece.isWhite() != tmp.isWhite() || piece.getIndex() != tmp.getIndex()) continue;

                board.getBox(startFile, startRank).setPiece(piece);
                board.getBox(endFile, endRank).setPiece(null);
                boolean canMove = tmp.canMove(board, spot, board.getBox(endFile, endRank));
                board.getBox(endFile, endRank).setPiece(piece);
                board.getBox(startFile, startRank).setPiece(null);
                if (canMove) {
                    if (startFile != file) {
                        dFile = String.valueOf((char) ('a' + startFile));
                    } else {
                        dRank = String.valueOf(startRank + 1);
                    }
                }
            }
        }
        String d = dFile + dRank;
        if (d.isEmpty() && capture && piece instanceof Pawn) {
            d = String.valueOf((char) ('a' + startFile));
        }

        String n = "";
        if (!promotion.isEmpty()) {
            n = "=" + promotion;
        }
        if (check) {
            n += checkmate ? "#" : "+";
        }
        return p + d + m + f + r + n;
    }

    public Board getBoard() {
        return board;
    }
}

