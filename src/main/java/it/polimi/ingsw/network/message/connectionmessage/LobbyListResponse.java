package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.distributed.Lobby;
import it.polimi.ingsw.network.message.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LobbyListResponse extends ConnectionMessage {


    private final HashMap<String, Map.Entry<Integer, Integer>> lobbies;

    public LobbyListResponse(HashMap<String, Map.Entry<Integer, Integer>> lobbies) {
        super(ConnectionMessageType.LOBBY_LIST_RESPONSE);
        this.lobbies = lobbies;
    }


    public HashMap<String, Map.Entry<Integer, Integer>> getLobbies() {
        return lobbies;
    }
}
