package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.Message;

public class ExitLobbyRequest extends LobbyMessage {
    public ExitLobbyRequest() {
        super(LobbyMessageType.EXIT_LOBBY_REQUEST);
    }
}
