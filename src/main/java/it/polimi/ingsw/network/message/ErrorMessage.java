package it.polimi.ingsw.network.message;

public class ErrorMessage extends Message{

    private final String content;
    public ErrorMessage(String username, String content) {
        super(username, MessageType.ERROR);
        this.content=content;
    }

    public String getContent() {
        return content;
    }
}
