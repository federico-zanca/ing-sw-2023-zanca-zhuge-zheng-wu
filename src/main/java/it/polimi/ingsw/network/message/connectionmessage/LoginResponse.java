package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a response message sent to the client indicating the outcome of a login request.
 * Inherits from the {@link MessageToClient} class.
 */
public class LoginResponse extends MessageToClient {
    private final boolean successful;
    private final String username;

    /**
     * Constructs a new LoginResponse message.
     *
     * @param successful Indicates whether the login was successful.
     * @param username The username associated with the login response.
     */
    public LoginResponse(boolean successful, String username) {
        super(MessageType.CONNECTION_MSG);
        this.successful = successful;
        this.username = username;
    }

    /**
     * Checks if the login was successful.
     *
     * @return true if the login was successful, false otherwise.
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * Retrieves the username of the client trying to log in.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Executes the response on the provided view, triggering the onLoginResponse callback.
     *
     * @param view The view on which to execute the response.
     */
    @Override
    public void execute(View view) {
        view.onLoginResponse(this);
    }
}
