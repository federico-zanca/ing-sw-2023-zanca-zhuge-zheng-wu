package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class LoginResponse extends MessageToClient {
    private final boolean successful;
    private final String username;

    public LoginResponse(boolean successful, String username) {
        super(MessageType.CONNECTION_MSG);
        this.successful = successful;
        this.username = username;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getUsername() {
        return username;
    }


    @Override
    public void execute(View view) {
        view.onLoginResponse(this);
    }
}
