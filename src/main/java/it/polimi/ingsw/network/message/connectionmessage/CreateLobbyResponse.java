package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

public class CreateLobbyResponse extends MessageToClient {
    private final boolean success;
    public CreateLobbyResponse(boolean success) {
        super(MessageType.CONNECTION_MSG);
        this.success = success;
    }

    public boolean isSuccessful() {
        return success;
    }

    public void execute(View view){
        view.onCreateLobbyResponse(this);
    }

}
