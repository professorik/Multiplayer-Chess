package client.cmd;

import client.Client;

/**
 * @author professorik
 * @created 18/03/2023 - 12:18
 * @project socket-chess
 */
public class MoveCmd extends Command {

    private final int from;
    private final int to;

    public MoveCmd(Client client, int from, int to) {
        super(client);
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean execute() {
        client.move(from, to);
        return true;
    }
}
