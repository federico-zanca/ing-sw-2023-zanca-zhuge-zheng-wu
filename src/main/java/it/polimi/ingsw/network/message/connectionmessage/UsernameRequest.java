package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;

public class UsernameRequest extends ConnectionMessage {
    private final String username;

    public UsernameRequest(String username) {
        super(ConnectionMessageType.USERNAME_REQUEST);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    /**
     * @param client
     * @param preGameController
     */
    @Override
    public void execute(Client client, PreGameController preGameController) {
        preGameController.onUsernameRequest(client, this);
    }
}
