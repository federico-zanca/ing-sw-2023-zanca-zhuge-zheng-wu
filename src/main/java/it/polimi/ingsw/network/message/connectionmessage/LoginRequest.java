package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.network.message.Message;

public class LoginRequest extends ConnectionMessage {
    private final String username;

    public LoginRequest(String username) {
        super(ConnectionMessageType.LOGIN_REQUEST);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
