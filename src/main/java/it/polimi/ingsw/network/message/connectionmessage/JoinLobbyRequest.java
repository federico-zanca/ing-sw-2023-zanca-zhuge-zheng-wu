package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.MessageType;

public class JoinLobbyRequest extends MessageToServer {
    private final String lobbyName;

    public JoinLobbyRequest(String lobbyName) {
        super(MessageType.CONNECTION_MSG);
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
