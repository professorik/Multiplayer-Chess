package utils.cmd;

import java.util.UUID;

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
