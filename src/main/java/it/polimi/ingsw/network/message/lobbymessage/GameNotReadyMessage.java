package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.Message;

public class GameNotReadyMessage extends LobbyMessage {
    public GameNotReadyMessage(String game_not_ready_to_start) {
        super(LobbyMessageType.GAME_NOT_READY);
    }
}
