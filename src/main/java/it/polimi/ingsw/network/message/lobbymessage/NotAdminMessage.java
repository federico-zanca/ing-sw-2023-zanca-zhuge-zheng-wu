package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

public class NotAdminMessage extends MessageToClient {
    public NotAdminMessage() {
        super(MessageType.LOBBY_MSG);
    }

    @Override
    public void execute(View view) {
        view.onNotAdminMessage(this);
    }
}
