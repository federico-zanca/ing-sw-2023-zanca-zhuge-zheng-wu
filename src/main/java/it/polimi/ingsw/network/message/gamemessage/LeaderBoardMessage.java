package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

import java.util.LinkedHashMap;
/**
 * Represents a message containing the leaderboard information.
 * Inherits from the {@link MessageToClient} class.
 */
public class LeaderBoardMessage extends MessageToClient {
    private final LinkedHashMap<String, Integer> leaderboard;
    private final String username;
    /**
     * Constructs a new LeaderBoardMessage.
     * @param username The username of the player receiving the message.
     * @param leaderboard The leaderboard data.
     */
    public LeaderBoardMessage(String username, LinkedHashMap<String, Integer> leaderboard) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.leaderboard = leaderboard;
    }
    /**
     * Gets the leaderboard.
     * @return The leaderboard data as a LinkedHashMap with usernames as keys and scores as values.
     */
    public LinkedHashMap<String, Integer> getLeaderboard() {
        return leaderboard;
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
        view.onLeaderBoardMessage(this);
    }
}

