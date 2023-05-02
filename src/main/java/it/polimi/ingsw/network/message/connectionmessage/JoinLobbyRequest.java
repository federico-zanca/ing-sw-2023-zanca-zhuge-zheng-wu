package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;

public class JoinLobbyRequest extends ConnectionMessage{
    private final String lobbyName;

    public JoinLobbyRequest(String lobbyName) {
        super(ConnectionMessageType.JOIN_LOBBY_REQUEST);
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
        preGameController.onJoinLobbyRequest(client, this);
    }
}
