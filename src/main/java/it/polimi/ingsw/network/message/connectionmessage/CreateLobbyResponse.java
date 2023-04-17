package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.network.message.Message;

public class CreateLobbyResponse extends ConnectionMessage {
    private boolean success;
    public CreateLobbyResponse(boolean success) {
        super(ConnectionMessageType.CREATE_LOBBY_RESPONSE);
        this.success = success;
    }

    public boolean isSuccessful() {
        return success;
    }
}
