package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a response message sent to the client indicating the outcome of an exit lobby request.
 * Inherits from the {@link MessageToClient} class.
 */
public class ExitLobbyResponse extends MessageToClient {
    private final boolean successful;
    private final String content;
    /**
     * Constructs a new ExitLobbyResponse message.
     * @param successful Indicates whether the exit lobby request was successful.
     * @param content    Additional content or information regarding the response.
     */
    public ExitLobbyResponse(boolean successful, String content) {
        super(MessageType.LOBBY_MSG);
        this.successful = successful;
        this.content = content;
    }
    /**
     * Checks if the exit lobby request was successful.
     *
     * @return true if the request was successful, false otherwise.
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * Retrieves the content or additional information regarding the response.
     *
     * @return The content of the response.
     */
    public String getContent() {
        return content;
    }

    /**
     * Executes the response on the provided view, triggering the onExitLobbyResponse callback.
     *
     * @param view The view on which to execute the response.
     */
    @Override
    public void execute(View view) {
        view.onExitLobbyResponse(this);
    }
}
