package it.polimi.ingsw.network.message;

/**
 * The abstract base class for messages sent to clients in the application.
 * This class extends the Message class and implements the MsgToClient interface.
 */
public abstract class MessageToClient extends Message implements MsgToClient{
    /**
     * Constructs a new MessageToClient object with the specified type.
     * @param type the MessageType of the message
     */
    public MessageToClient(MessageType type){
        super(type);
    }

    /**
     * Returns the username of player associated with the message.
     * @return the username of player
     */
    public String getUsername(){
        return ""; //TODO dangerous practice
    }
}
