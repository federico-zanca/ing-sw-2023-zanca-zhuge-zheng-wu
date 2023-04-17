package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.Message;

public class StartGameRequest extends LobbyMessage {
    public StartGameRequest() {
        super(LobbyMessageType.START_GAME_REQUEST);
    }
}
