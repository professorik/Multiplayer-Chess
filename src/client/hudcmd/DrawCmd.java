package client.hudcmd;

import client.ClientHandler;

/**
 * @author professorik
 * @created 18/03/2023 - 12:18
 * @project socket-chess
 */
public class DrawCmd extends Command {

    public DrawCmd(ClientHandler client) {
        super(client);
    }

    @Override
    public boolean execute() {
        //client.client.move(from, to);
        return false;
    }
}
