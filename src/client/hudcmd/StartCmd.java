package client.hudcmd;

import client.ClientHandler;

/**
 * @author professorik
 * @created 18/03/2023 - 12:18
 * @project socket-chess
 */
public class StartCmd extends Command {

    public StartCmd(ClientHandler client) {
        super(client);
    }

    @Override
    public boolean execute() {
        client.start();
        return false;
    }
}
