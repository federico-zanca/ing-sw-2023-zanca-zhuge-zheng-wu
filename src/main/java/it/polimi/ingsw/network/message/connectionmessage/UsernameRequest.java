package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.network.message.Message;

public class UsernameRequest extends ConnectionMessage {
    private final String username;

    public UsernameRequest(String username) {
        super(ConnectionMessageType.USERNAME_REQUEST);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
