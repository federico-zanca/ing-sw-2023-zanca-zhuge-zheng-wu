package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.model.enumerations.JoinType;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class JoinLobbyResponse extends ConnectionMessage implements MsgToClient {
    private final String content;
    private final ArrayList<String> usernames;
    private final JoinType joinType;
    //private final int numClients;
    //private final int maxNumClients;

    public JoinLobbyResponse(JoinType joinType, String content, ArrayList<String> usernames) {
        super(ConnectionMessageType.JOIN_LOBBY_RESPONSE);
        this.joinType = joinType;
        this.content = content;
        this.usernames = usernames;
        //this.numClients = numClients;
        //this.maxNumClients = maxNumClients;
    }


    public String getContent() {
        return content;
    }

    public ArrayList<String> getUsernames() {
        return usernames;
    }

    public JoinType getJoinType() {
        return joinType;
    }

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
