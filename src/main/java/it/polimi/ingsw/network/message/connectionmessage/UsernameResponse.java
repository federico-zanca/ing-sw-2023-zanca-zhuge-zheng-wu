package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a response message sent to the client indicating the outcome of a username changing request.
 * Inherits from the {@link MessageToClient} class.
 */
public class UsernameResponse extends MessageToClient {
    private final String username;
    private final boolean success;
    /**
     * Constructs a new UsernameResponse message.
     *
     * @param success  Indicates whether the username request was successful.
     * @param username The assigned username or the requested username (depending on the success status).
     */
    public UsernameResponse(boolean success, String username) {
        super(MessageType.CONNECTION_MSG);

        this.success = success;
        this.username = username;
    }
    /**
     * Retrieves the username associated with the username response.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Checks if the username request was successful.
     *
     * @return true if the username request was successful, false otherwise.
     */
    public boolean isSuccessful() {
        return success;
    }

    /**
     * Executes the response on the provided view, triggering the onUsernameResponse callback.
     *
     * @param view The view on which to execute the response.
     */
    @Override
    public void execute(View view) {
        view.onUsernameResponse(this);
    }
}
