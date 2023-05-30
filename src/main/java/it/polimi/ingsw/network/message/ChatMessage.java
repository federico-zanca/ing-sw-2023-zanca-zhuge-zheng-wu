package it.polimi.ingsw.network.message;

public class ChatMessage extends Message{
    private final String content;

    private final String receiver;

    private String sender;

    public ChatMessage(String content, String recipient){
        super(MessageType.CHAT_MSG);
        this.content = content;
        this.receiver = recipient;
    }

    public String getContent(){
        return content;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
