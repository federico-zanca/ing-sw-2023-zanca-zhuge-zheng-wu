package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class PlayersInLobbyUpdate extends MessageToClient {
    private final ArrayList<String> allPlayersUsernames;
    private final String content;

    public PlayersInLobbyUpdate(ArrayList<String> clientsUsernames, String content) {
        super(MessageType.LOBBY_MSG);
        this.allPlayersUsernames = clientsUsernames;
        this.content = content ;
    }

    public ArrayList<String> getAllPlayersUsernames() {
        return allPlayersUsernames;
    }

    public String getContent() {
        return content;
    }

    @Override
    public void execute(View view) {
        view.onPlayersInLobbyUpdate(this);
    }
}
