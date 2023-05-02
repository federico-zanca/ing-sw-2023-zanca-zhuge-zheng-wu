package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.view.View;

public class LoginResponse extends ConnectionMessage implements MessageToClient {
    private final boolean successful;
    private final String username;

    public LoginResponse(boolean successful, String username) {
        super(ConnectionMessageType.LOGIN_RESPONSE);
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
