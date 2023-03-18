package client.cmd;

import client.Client;

/**
 * @author professorik
 * @created 18/03/2023 - 12:18
 * @project socket-chess
 */
public class MoveCmd extends Command {

    public MoveCmd(Client client) {
        super(client);
    }

    @Override
    public boolean execute() {
        System.out.println("move");
        return false;
    }
}
