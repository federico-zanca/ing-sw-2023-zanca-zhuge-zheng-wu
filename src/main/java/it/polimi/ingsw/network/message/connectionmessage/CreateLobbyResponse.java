package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class CreateLobbyResponse extends ConnectionMessage implements MsgToClient {
    private boolean success;
    public CreateLobbyResponse(boolean success) {
        super(ConnectionMessageType.CREATE_LOBBY_RESPONSE);
        this.success = success;
    }

    public boolean isSuccessful() {
        return success;
    }

    public void execute(View view){
        view.onCreateLobbyResponse(this);
    }

}
