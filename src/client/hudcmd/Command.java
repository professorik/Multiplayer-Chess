package client.hudcmd;

import client.ClientHandler;

/**
 * @author professorik
 * @created 18/03/2023 - 12:16
 * @project socket-chess
 */
public abstract class Command {
    public ClientHandler client;

    public Command(ClientHandler client) {
        this.client = client;
    }

    public abstract boolean execute();
}
