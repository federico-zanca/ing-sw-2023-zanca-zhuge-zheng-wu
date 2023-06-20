package it.polimi.ingsw.network.message;

/**
 * A message representing a chat message from client, used for exchanging text-based communication between users.
 */
public class ChatMessage extends Message{
    private final String content;

    private final String receiver;

    private String sender;

    /**
     * Constructs a new ChatMessage object with the specified content and recipient.
     * The message type is set to CHAT_MSG.
     * @param content the content of the chat message
     * @param recipient the username of the message recipient
     */
    public ChatMessage(String content, String recipient){
        super(MessageType.CHAT_MSG);
        this.content = content;
        this.receiver = recipient;
    }

    /**
     * Returns the content of the chat message.
     * @return the content of the chat message
     */
    public String getContent(){
        return content;
    }

    /**
     * Returns the username of the message receiver.
     * @return the username of the message receiver
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Returns the username of the message sender.
     * @return the username of the message sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets the username of the message sender.
     * @param sender the username of the message sender
     */
    public void setSender(String sender) {
        this.sender = sender;
    }
}
