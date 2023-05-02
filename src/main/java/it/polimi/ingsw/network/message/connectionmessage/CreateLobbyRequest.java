package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;

public class CreateLobbyRequest extends ConnectionMessage{
    private final String lobbyName;

    public CreateLobbyRequest(String lobbyName) {
        super(ConnectionMessageType.CREATE_LOBBY_REQUEST);
        this.lobbyName = lobbyName;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    /**
     * @param client
     * @param preGameController
     */
    @Override
    public void execute(Client client, PreGameController preGameController) {
        preGameController.onCreateLobbyRequest(client, this);
    }
}
