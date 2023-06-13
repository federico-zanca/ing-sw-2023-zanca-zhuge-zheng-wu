package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a response message sent to the client indicating the outcome of a lobby creation request.
 * Inherits from the {@link MessageToClient} class.
 */
public class CreateLobbyResponse extends MessageToClient {
    private final boolean success;
    /**
     * Constructs a new CreateLobbyResponse message.
     * @param success Indicates whether the lobby creation was successful.
     */
    public CreateLobbyResponse(boolean success) {
        super(MessageType.CONNECTION_MSG);
        this.success = success;
    }

    /**
     * Checks if the lobby creation was successful.
     * @return true if the lobby creation was successful, false otherwise.
     */
    public boolean isSuccessful() {
        return success;
    }

    /**
     * Executes the response on the provided view, triggering the onCreateLobbyResponse callback.
     * @param view The view on which to execute the response.
     */
    public void execute(View view){
        view.onCreateLobbyResponse(this);
    }

}
