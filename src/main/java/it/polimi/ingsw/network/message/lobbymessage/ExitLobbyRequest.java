package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;

public class ExitLobbyRequest extends LobbyMessage {
    public ExitLobbyRequest() {
        super(LobbyMessageType.EXIT_LOBBY_REQUEST);
    }

    public void execute(Client client, PreGameController preGameController){
        preGameController.onExitLobbyRequest(client, this);
    }
}
