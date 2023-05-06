package utils.cmd;

import utils.chess.GameStatus;

import java.util.UUID;

import static utils.chess.GameStatus.*;

/**
 * @author professorik
 * @created 21/03/2023 - 16:26
 * @project socket-chess
 */
public class Finish extends Message {

    public enum Reason {
        MATE,
        STALEMATE,
        RESIGNATION,
        AGREEMENT,
        FORFEIT,
    }

    private final boolean isWhite;
    private final boolean isBlack;
    private String reason;

    public Finish(UUID ID, boolean isWhite, boolean isBlack) {
        super(ID, "F");
        this.isWhite = isWhite;
        this.isBlack = isBlack;
        this.reason = "";
    }

    public Finish(UUID ID, boolean isWhite, boolean isBlack, Reason reason) {
        this(ID, isWhite, isBlack);
        String tmp = isWhite? "White": "Black";
        switch (reason) {
            case MATE -> this.reason = tmp + " won by mate";
            case STALEMATE -> this.reason = "Stalemate";
            case RESIGNATION -> this.reason = tmp + " won by resignation";
            case AGREEMENT -> this.reason = "By agreement";
        }
    }

    public Finish(UUID ID, GameStatus status) {
        super(ID, "F");
        switch (status) {
            case NOT_ENOUGH_FIGURES, STALEMATE -> {
                this.isBlack = true;
                this.isWhite = true;
                this.reason = status == STALEMATE? "Stalemate": "Not enough figures";
            }
            case FORFEIT_WHITE, FORFEIT_BLACK -> {
                this.isBlack = status == FORFEIT_WHITE;
                this.isWhite = !this.isBlack;
                this.reason = "By forfeit";
            }
            case MATE_BLACK_WIN, MATE_WHITE_WIN -> {
                this.isBlack = status == MATE_BLACK_WIN;
                this.isWhite = !this.isBlack;
                this.reason = "Checkmate";
            }
            default -> {
                this.isBlack = false;
                this.isWhite = false;
                this.reason = "";
            }
        }
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean isBlack() {
        return isBlack;
    }

    public String getReason() {
        return reason;
    }
}
