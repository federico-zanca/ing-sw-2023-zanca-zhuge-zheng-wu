package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a message indicating a new turn in the game.
 * Inherits from the {@link MessageToClient} class.
 */
public class NewTurnMessage extends MessageToClient {
    private final String currentPlayer;
    private final String username;
    /**
     * Constructs a new NewTurnMessage.
     * @param username The username of the player receiving the message.
     * @param currentPlayer The username of the player who is the current turn player.
     */
    public NewTurnMessage(String username, String currentPlayer) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.currentPlayer = currentPlayer;
    }
    /**
     * Gets the content of the message.
     * @return The content of the message.
     */
    public String getContent(){
        return "Turno di " + getCurrentPlayer();
    }
    /**
     * Gets the username of the player who is the current turn player.
     * @return The username of the current turn player.
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }
    /**
     * Gets the username of the player receiving the message.
     * @return The username of the player receiving the message.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Executes the message on the client's view.
     * @param view The client's view.
     */
    @Override
    public void execute(View view) {
        view.onNewTurnMessage(this);
    }
}
