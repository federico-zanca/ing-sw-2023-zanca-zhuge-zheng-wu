package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.MessageType;

public class ExitLobbyRequest extends MessageToServer {
    public ExitLobbyRequest() {
        super(MessageType.LOBBY_MSG);
    }

    public void execute(Client client, PreGameController preGameController){
        preGameController.onExitLobbyRequest(client, this);
    }
}
