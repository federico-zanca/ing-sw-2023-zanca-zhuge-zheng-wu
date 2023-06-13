package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.tui.LobbyDisplayInfo;

import java.util.ArrayList;
/**
 * Represents a response message sent to the client containing the list of available lobbies.
 * Inherits from the {@link MessageToClient} class.
 */
public class LobbyListResponse extends MessageToClient {


    private final ArrayList<LobbyDisplayInfo> lobbies;

    /**
     * Constructs a new LobbyListResponse message.
     * @param lobbies The list of available lobbies.
     */
    public LobbyListResponse(ArrayList<LobbyDisplayInfo> lobbies) {
        super(MessageType.CONNECTION_MSG);
        this.lobbies = lobbies;
    }

    /**
     * Retrieves the list of available lobbies.
     * @return The list of lobbies.
     */
    public ArrayList<LobbyDisplayInfo> getLobbies() {
        return lobbies;
    }

    /**
     * Executes the response on the provided view, triggering the onLobbyListResponse callback.
     * @param view The view on which to execute the response.
     */
    @Override
    public void execute(View view) {
        view.onLobbyListResponse(this);
    }
}
