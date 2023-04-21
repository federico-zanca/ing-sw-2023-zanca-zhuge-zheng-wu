package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.network.message.Message;

public class LoginResponse extends ConnectionMessage {
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
}
