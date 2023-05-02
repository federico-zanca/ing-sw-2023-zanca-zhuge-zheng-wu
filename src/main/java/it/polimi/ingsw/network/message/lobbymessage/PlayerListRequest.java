package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;

public class PlayerListRequest extends LobbyMessage{
    public PlayerListRequest() {super(LobbyMessageType.PLAYER_LIST_REQUEST);}

    /**
     * @param client
     * @param preGameController
     */
    @Override
    public void execute(Client client, PreGameController preGameController) {
        preGameController.onPlayerListRequest(client, this);
    }
}
