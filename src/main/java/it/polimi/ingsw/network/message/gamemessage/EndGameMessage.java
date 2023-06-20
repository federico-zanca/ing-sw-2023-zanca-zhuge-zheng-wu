package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

import java.util.LinkedHashMap;

/**
 * Represents a message sent to the client indicating the end of the game and the final ranking.
 * Inherits from the {@link MessageToClient} class.
 */
public class EndGameMessage extends MessageToClient {
    private final LinkedHashMap<String, Integer> ranking;
    private final String username;
    /**
     * Constructs a new EndGameMessage.
     * @param username  The username of the player receiving the end game message.
     * @param ranking The final ranking of the players.
     */
    public EndGameMessage(String username, LinkedHashMap<String, Integer> ranking) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.ranking = ranking;
    }
    /**
     * Gets the final ranking of the players.
     *
     * @return The final ranking as a LinkedHashMap, where the keys are player usernames and the values are their scores.
     */
    public LinkedHashMap<String, Integer> getRanking() {
        return ranking;
    }
    /**
     * Gets the username of the player receiving the end game message.
     *
     * @return The receiving player's username.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Executes the message on the client.
     *
     * @param view  The view handling the message.
     */
    @Override
    public void execute(View view) {
        view.onEndGameMessage(this);
    }
}
