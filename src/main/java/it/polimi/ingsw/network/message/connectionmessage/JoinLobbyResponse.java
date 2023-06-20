package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.model.enumerations.JoinType;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
/**
 * Represents a response message sent to the client indicating the outcome of a lobby join request.
 * Inherits from the {@link MessageToClient} class.
 */
public class JoinLobbyResponse extends MessageToClient {
    private final String content;
    private final ArrayList<String> usernames;
    private final JoinType joinType;
    //private final int numClients;
    //private final int maxNumClients;

    /**
     * Constructs a new JoinLobbyResponse message with the specified join type, content, and usernames.
     * @param joinType  the type of join operation (e.g., success, failure, already joined).
     * @param content  the additional content of the response message.
     * @param usernames  the list of usernames in the lobby.
     */
    public JoinLobbyResponse(JoinType joinType, String content, ArrayList<String> usernames) {
        super(MessageType.CONNECTION_MSG);
        this.joinType = joinType;
        this.content = content;
        this.usernames = usernames;
        //this.numClients = numClients;
        //this.maxNumClients = maxNumClients;
    }

    /**
     * Retrieves the content of the response message.
     * @return The content of the response.
     */
    public String getContent() {
        return content;
    }

    /**
     * Retrieves the list of usernames in the lobby.
     * @return The list of usernames.
     */
    public ArrayList<String> getUsernames() {
        return usernames;
    }

    /**
     * Retrieves the type of join operation.
     * @return The join type.
     */
    public JoinType getJoinType() {
        return joinType;
    }

    /**
     * Executes the response on the provided view, triggering the onJoinLobbyResponse callback.
     * @param view The view on which to execute the response.
     */
    @Override
    public void execute(View view) {
        view.onJoinLobbyResponse(this);
    }
/*
    public int getNumClients() {
        return numClients;
    }

    public int getMaxNumClients() {
        return maxNumClients;
    }

 */

}
