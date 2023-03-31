package it.polimi.ingsw.network.message;

public abstract class Message {

    private final MessageType type;

    protected Message(MessageType type) {
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
}
