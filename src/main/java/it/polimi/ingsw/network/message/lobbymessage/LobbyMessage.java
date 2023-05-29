package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.network.message.MsgToServer;
import it.polimi.ingsw.view.View;

public abstract class LobbyMessage extends Message implements MsgToClient, MsgToServer {
    private final LobbyMessageType type;

    public LobbyMessage(LobbyMessageType type) {
        this.type = type;
    }

    public LobbyMessageType getType() {
        return type;
    }

    public void execute(View view){
        System.err.println("Operation on client not allowed!");
    }

    @Override
    public void execute(Client client, PreGameController preGameController) {
        System.err.println("Operation on server not allowed!");
    }
}
