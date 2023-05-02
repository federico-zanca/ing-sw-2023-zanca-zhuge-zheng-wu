package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.view.View;

public class UsernameResponse extends ConnectionMessage implements MessageToClient {
    private final String username;
    private final boolean success;

    public UsernameResponse(boolean success, String username) {
        super(ConnectionMessageType.USERNAME_RESPONSE);

        this.success = success;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public boolean isSuccessful() {
        return success;
    }


    @Override
    public void execute(View view) {
        view.onUsernameResponse(this);
    }
}
