package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.network.message.Message;

public class JoinLobbyResponse extends ConnectionMessage {
    private final boolean success;
    private final String content;
    //private final int numClients;
    //private final int maxNumClients;

    public JoinLobbyResponse(boolean success, String content) {
        super(ConnectionMessageType.JOIN_LOBBY_RESPONSE);
        this.success = success;
        this.content = content;
        //this.numClients = numClients;
        //this.maxNumClients = maxNumClients;
    }


    public boolean isSuccessful() {
        return success;
    }

    public String getContent() {
        return content;
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
