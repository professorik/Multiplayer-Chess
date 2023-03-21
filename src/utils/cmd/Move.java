package utils.cmd;

import java.util.UUID;

/**
 * @author professorik
 * @created 21/03/2023 - 16:26
 * @project socket-chess
 */
public class Move extends Message {

    private final boolean isWhite;
    private final int from;
    private final int to;

    public Move(UUID ID, boolean isWhite, int from, int to) {
        super(ID, "M");
        this.isWhite = isWhite;
        this.from = from;
        this.to = to;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }
}
