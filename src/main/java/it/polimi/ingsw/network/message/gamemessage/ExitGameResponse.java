package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a response to an exit game request.
 * Inherits from the {@link MessageToClient} class.
 */
public class ExitGameResponse extends MessageToClient {

    private final String content;
    private final String username;
    /**
     * Constructs a new ExitGameResponse message.
     * @param username The username of the player.
     * @param content The content of the response.
     */
    public ExitGameResponse(String username, String content) {
        super(MessageType.GAME_MSG );
        this.username = username;
        this.content = content;
    }
    /**
     * Gets the content of the response.
     *
     * @return The content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets the username of the player.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Executes the message on the client's view.
     * @param view The view that handles the message.
     */
    @Override
    public void execute(View view) {
        view.onExitGameResponse(this);
    }
}
