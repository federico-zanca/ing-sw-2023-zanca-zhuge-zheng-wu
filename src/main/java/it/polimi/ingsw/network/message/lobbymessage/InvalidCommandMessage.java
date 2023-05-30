package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class InvalidCommandMessage extends MessageToClient {
    public InvalidCommandMessage(){
        super(MessageType.LOBBY_MSG);
    }

    @Override
    public void execute(View view) {
        view.onInvalidCommandMessage(this);
    }
}
