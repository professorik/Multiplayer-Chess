package utils.cmd;

import java.util.UUID;

/**
 * @author professorik
 * @created 21/03/2023 - 16:26
 * @project socket-chess
 */
public class Resign extends Message {

    public Resign(UUID ID) {
        super(ID, "R");
    }
}
