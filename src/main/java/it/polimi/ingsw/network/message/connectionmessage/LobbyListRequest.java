package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.gamemessage.GameMessageType;

public class LobbyListRequest extends ConnectionMessage {
    public LobbyListRequest() {
        super(ConnectionMessageType.LOBBY_LIST_REQUEST);
    }
}
