package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class NotAdminMessage extends LobbyMessage implements MsgToClient {
    public NotAdminMessage() {
        super(LobbyMessageType.NOT_ADMIN);
    }

    @Override
    public void execute(View view) {
        view.onNotAdminMessage(this);
    }
}
