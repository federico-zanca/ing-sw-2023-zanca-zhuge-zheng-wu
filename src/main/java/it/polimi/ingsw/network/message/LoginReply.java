package it.polimi.ingsw.network.message;

public class LoginReply extends Message{

    protected LoginReply(String username, MessageType type) {
        super(username, MessageType.LOGIN_REPLY);
    }
}
