package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;
/**
 * Represents a response message sent to the client indicating the outcome of a request to change the number of players in the lobby.
 * Inherits from the {@link MessageToClient} class.
 */
public class ChangeNumOfPlayerResponse extends MessageToClient {

    private final boolean successful;
    private final String content;
    private final Integer choseNum;
    /**
     * Constructs a new ChangeNumOfPlayerResponse message.
     *
     * @param successful  Indicates whether the request to change the number of players was successful.
     * @param content  Additional content or information regarding the response.
     * @param chosenNum   The chosen number of players in the lobby (if successful).
     */
    public ChangeNumOfPlayerResponse(boolean successful, String content, int chosenNum) {
        super(MessageType.LOBBY_MSG);
        this.successful = successful;
        this.content = content;
        this.choseNum = chosenNum;
    }

    /**
     * Checks if the request to change the number of players was successful.
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
     * Retrieves the chosen number of players in the lobby.
     *
     * @return The chosen number of players.
     */
    public Integer getChosenNum(){return choseNum;}

    /**
     * Executes the response on the provided view, triggering the onChangeNumOfPlayerResponse callback.
     *
     * @param view The view on which to execute the response.
     */
    @Override
    public void execute(View view) {
        view.onChangeNumOfPlayerResponse(this);
    }
}
