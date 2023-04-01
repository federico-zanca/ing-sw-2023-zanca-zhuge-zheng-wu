package it.polimi.ingsw.network.message;

public abstract class Message {
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
