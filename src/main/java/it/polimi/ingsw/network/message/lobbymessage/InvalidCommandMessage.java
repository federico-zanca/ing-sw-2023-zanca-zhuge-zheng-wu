package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.view.View;

public class InvalidCommandMessage extends LobbyMessage implements MessageToClient {
    public InvalidCommandMessage(){
        super(LobbyMessageType.INVALID_COMMAND);
    }

    @Override
    public void execute(View view) {
        view.onInvalidCommandMessage(this);
    }
}
