package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
/**
 * Represents a response from the server containing the list of players in the lobby.
 * Inherits from the {@link MessageToClient} class.
 */
public class PlayerListResponse extends MessageToClient {
    private final ArrayList<String> clients;
    /**
     * Constructs a new PlayerListResponse message.
     *
     * @param clients The list of players in the lobby.
     */
    public PlayerListResponse(ArrayList<String> clients) {
        super(MessageType.LOBBY_MSG);
        this.clients = clients;
    }
    /**
     * Gets the list of players in the lobby.
     *
     * @return The list of players in the lobby.
     */
    public ArrayList<String> getClients() {
        return clients;
    }

    /**
     * Executes the message on the provided view, triggering the onPlayerListResponse callback.
     *
     * @param view The view on which to execute the message.
     */
    @Override
    public void execute(View view) {
        view.onPlayerListResponse(this);
    }
}
