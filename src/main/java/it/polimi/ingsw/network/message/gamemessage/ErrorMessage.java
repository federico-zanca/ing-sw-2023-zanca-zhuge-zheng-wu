package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents an error message sent to the client.
 * Inherits from the {@link MessageToClient} class.
 */
public class ErrorMessage extends MessageToClient {

    private final String content;
    /**
     * Constructs a new ErrorMessage.
     * @param content The content of the error message.
     */
    public ErrorMessage(String username, String content) {
        super(MessageType.GAME_MSG);
        this.content=content;
    }
    /**
     * Gets the content of the error message.
     * @return The content of the error message.
     */
    public String getContent() {
        return content;
    }
    /**
     * Executes the message on the client.
     * @param view The view handling the message.
     */
    @Override
    public void execute(View view) {
        System.out.println(content);
    }
}
