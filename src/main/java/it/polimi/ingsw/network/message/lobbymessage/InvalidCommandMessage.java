package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class InvalidCommandMessage extends LobbyMessage implements MsgToClient {
    public InvalidCommandMessage(){
        super(LobbyMessageType.INVALID_COMMAND);
    }

    @Override
    public void execute(View view) {
        view.onInvalidCommandMessage(this);
    }
}
