package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.network.message.Message;

import java.util.ArrayList;

public class JoinLobbyResponse extends ConnectionMessage {
    private final boolean success;
    private final String content;
    private final ArrayList<String> usernames;
    //private final int numClients;
    //private final int maxNumClients;

    public JoinLobbyResponse(boolean success, String content, ArrayList<String> usernames) {
        super(ConnectionMessageType.JOIN_LOBBY_RESPONSE);
        this.success = success;
        this.content = content;
        this.usernames = usernames;
        //this.numClients = numClients;
        //this.maxNumClients = maxNumClients;
    }


    public boolean isSuccessful() {
        return success;
    }

    public String getContent() {
        return content;
    }

    public ArrayList<String> getUsernames() {
        return usernames;
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
