package utils.cmd;

/**
 * @author professorik
 * @created 13/03/2023 - 17:54
 * @project socket-chess
 */
public class Message {
    private final String ID;
    private final String message;

    public Message(String ID, String message) {
        this.ID = ID;
        this.message = message;
    }

    public static Message parse(String str) {
        var tokens = str.split(": ");
        return new Message(tokens[1], tokens[0]);
    }

    public void process() {

    }

    public String getID() {
        return ID;
    }

    public String getMessage() {
        return message;
    }
}
