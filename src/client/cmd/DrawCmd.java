package client.cmd;

import client.Client;

/**
 * @author professorik
 * @created 18/03/2023 - 12:18
 * @project socket-chess
 */
public class DrawCmd extends Command {

    public DrawCmd(Client client) {
        super(client);
    }

    @Override
    public boolean execute() {
        System.out.println("draw");
        return false;
    }
}
