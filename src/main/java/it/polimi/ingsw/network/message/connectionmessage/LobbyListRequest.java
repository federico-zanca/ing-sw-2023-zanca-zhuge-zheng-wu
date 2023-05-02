package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;

public class LobbyListRequest extends ConnectionMessage {
    public LobbyListRequest() {
        super(ConnectionMessageType.LOBBY_LIST_REQUEST);
    }

    /**
     * @param client
     * @param preGameController
     */
    @Override
    public void execute(Client client, PreGameController preGameController) {
        preGameController.onLobbyListRequest(client, this);
    }
}
