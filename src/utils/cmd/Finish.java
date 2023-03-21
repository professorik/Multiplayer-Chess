package utils.cmd;

import java.util.UUID;

/**
 * @author professorik
 * @created 21/03/2023 - 16:26
 * @project socket-chess
 */
public class Finish extends Message {

    private final boolean isWhite;
    private final boolean isBlack;

    public Finish(UUID ID, boolean isWhite, boolean isBlack) {
        super(ID, "F");
        this.isWhite = isWhite;
        this.isBlack = isBlack;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean isBlack() {
        return isBlack;
    }
}
