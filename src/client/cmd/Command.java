package client.cmd;

import client.Client;

/**
 * @author professorik
 * @created 18/03/2023 - 12:16
 * @project socket-chess
 */
public abstract class Command {
    public Client client;

    public Command(Client client) {
        this.client = client;
    }

    public abstract boolean execute();
}
