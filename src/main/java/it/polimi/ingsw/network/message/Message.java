package it.polimi.ingsw.network.message;

import java.io.Serializable;

/**
 * The abstract base class for messages between client and server.
 */
public abstract class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private final MessageType type;
    /**
     * Constructs a new Message object with the specified type.
     * @param type the MessageType of the message
     */
    public Message(MessageType type) {
        this.type = type;
    }

    /**
     * Returns the MessageType of the message.
     * @return the MessageType of the message
     */
    public MessageType getType() {
        return type;
    }
}
