package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.distributed.Client;

import java.util.ArrayList;

public class PlayerListResponse extends LobbyMessage{
    private final ArrayList<String> clients;

    public PlayerListResponse(ArrayList<String> clients) {
        super(LobbyMessageType.PLAYER_LIST_RESPONSE);
        this.clients = clients;
    }

    public ArrayList<String> getClients() {
        return clients;
    }
}
