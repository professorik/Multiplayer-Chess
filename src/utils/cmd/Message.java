package utils.cmd;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author professorik
 * @created 13/03/2023 - 17:54
 * @project socket-chess
 */
public class Message implements Serializable {
    private UUID ID;
    private final String message;

    public Message(UUID ID, String message) {
        this.ID = ID;
        this.message = message;
    }

    public static Message parse(String str) {
        var tokens = str.split(": ");
        return new Message(UUID.fromString(tokens[1]), tokens[0]);
    }

    public UUID getID() {
        return ID;
    }

    public String getMessage() {
        return message;
    }

    public void setID(UUID ID) {
        this.ID = ID;
    }
}
