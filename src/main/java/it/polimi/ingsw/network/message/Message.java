package it.polimi.ingsw.network.message;

import java.io.Serializable;

public abstract class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String username;
    private final MessageType type;

    protected Message(String username, MessageType type) {
        this.username = username;
        this.type = type;
    }

    /**
     *
     * @return the type of the message
     */
    public MessageType getType() {
        return type;
    }

    public String toString(){
        return "Message {" +
                "messageType = " + type +
                " }";
    }

    public String getUsername() {
        return username;
    }
}
