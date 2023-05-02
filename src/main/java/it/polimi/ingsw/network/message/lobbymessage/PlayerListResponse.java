package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class PlayerListResponse extends LobbyMessage implements MessageToClient {
    private final ArrayList<String> clients;

    public PlayerListResponse(ArrayList<String> clients) {
        super(LobbyMessageType.PLAYER_LIST_RESPONSE);
        this.clients = clients;
    }

    public ArrayList<String> getClients() {
        return clients;
    }

    @Override
    public void execute(View view) {
        view.onPlayerListResponse(this);
    }
}
