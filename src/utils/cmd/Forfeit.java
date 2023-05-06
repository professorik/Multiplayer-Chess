package utils.cmd;

import java.util.UUID;

/**
 * @author professorik
 * @created 21/03/2023 - 16:26
 * @project socket-chess
 */
public class Forfeit extends Message {

    public Forfeit(UUID ID) {
        super(ID, "FT");
    }
}
