package it.polimi.ingsw.network.message;

/**
 * The abstract base class for messages sent to the server in the application.
 * This class extends the Message class and implements the MsgToServer interface.
 */
public abstract class MessageToServer extends Message implements MsgToServer{
    /**
     * Constructs a new MessageToServer object with the specified type.
     * @param type the MessageType of the message
     */
    public MessageToServer(MessageType type){
        super(type);
    }

}
