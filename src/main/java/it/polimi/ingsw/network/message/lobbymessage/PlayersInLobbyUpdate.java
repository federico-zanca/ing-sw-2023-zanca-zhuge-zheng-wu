package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
/**
 * Represents an update from the server containing the list of players in the lobby and additional content.
 * Inherits from the {@link MessageToClient} class.
 */
public class PlayersInLobbyUpdate extends MessageToClient {
    private final ArrayList<String> allPlayersUsernames;
    private final String content;
    /**
     * Constructs a new PlayersInLobbyUpdate message.
     *
     * @param clientsUsernames The list of players in the lobby.
     * @param content Additional content associated with the update.
     */
    public PlayersInLobbyUpdate(ArrayList<String> clientsUsernames, String content) {
        super(MessageType.LOBBY_MSG);
        this.allPlayersUsernames = clientsUsernames;
        this.content = content ;
    }
    /**
     * Gets the list of players in the lobby.
     *
     * @return The list of players in the lobby.
     */
    public ArrayList<String> getAllPlayersUsernames() {
        return allPlayersUsernames;
    }

    /**
     * Gets the additional content associated with the update.
     *
     * @return The additional content associated with the update.
     */
    public String getContent() {
        return content;
    }

    /**
     * Executes the message on the provided view, triggering the onPlayersInLobbyUpdate callback.
     *
     * @param view The view on which to execute the message.
     */
    @Override
    public void execute(View view) {
        view.onPlayersInLobbyUpdate(this);
    }
}
