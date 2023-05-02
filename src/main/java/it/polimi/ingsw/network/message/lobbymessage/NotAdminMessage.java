package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.view.View;

public class NotAdminMessage extends LobbyMessage implements MessageToClient {
    public NotAdminMessage() {
        super(LobbyMessageType.NOT_ADMIN);
    }

    @Override
    public void execute(View view) {
        view.onNotAdminMessage(this);
    }
}
