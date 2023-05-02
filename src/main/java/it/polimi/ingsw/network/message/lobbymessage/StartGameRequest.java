package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;

public class StartGameRequest extends LobbyMessage {
    public StartGameRequest() {
        super(LobbyMessageType.START_GAME_REQUEST);
    }

    /**
     * @param client
     * @param preGameController
     */
    @Override
    public void execute(Client client, PreGameController preGameController) {
        preGameController.onStartGameRequest(client, this);
    }
}
