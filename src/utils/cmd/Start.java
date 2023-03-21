package utils.cmd;

import java.util.UUID;

/**
 * @author professorik
 * @created 21/03/2023 - 16:26
 * @project socket-chess
 */
public class Start extends Message {

    private final boolean isWhite;

    public Start(UUID ID, boolean isWhite) {
        super(ID, "S");
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }
}
