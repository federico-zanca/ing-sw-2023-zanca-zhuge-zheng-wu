package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.tui.LobbyDisplayInfo;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class LobbyListResponse extends MessageToClient {


    private final ArrayList<LobbyDisplayInfo> lobbies;

    public LobbyListResponse(ArrayList<LobbyDisplayInfo> lobbies) {
        super(MessageType.CONNECTION_MSG);
        this.lobbies = lobbies;
    }


    public ArrayList<LobbyDisplayInfo> getLobbies() {
        return lobbies;
    }

    @Override
    public void execute(View view) {
        view.onLobbyListResponse(this);
    }
}
