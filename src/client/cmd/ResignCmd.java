package client.cmd;

import client.Client;

/**
 * @author professorik
 * @created 18/03/2023 - 12:18
 * @project socket-chess
 */
public class ResignCmd extends Command {

    public ResignCmd(Client client) {
        super(client);
    }

    @Override
    public boolean execute() {
        System.out.println("resign");
        return false;
    }
}
