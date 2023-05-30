package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.MessageType;

public class LoginRequest extends MessageToServer {
    private final String username;

    public LoginRequest(String username) {
        super(MessageType.CONNECTION_MSG);
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
        preGameController.onLoginRequest(client, this);
    }
}
