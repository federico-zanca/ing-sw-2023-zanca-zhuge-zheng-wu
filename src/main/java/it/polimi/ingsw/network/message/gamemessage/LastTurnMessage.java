package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.gamemessage.GameMessageType;

public class LastTurnMessage extends GameMessage {
    private final String currentPlayer;

    public LastTurnMessage(String username, String currentPlayer) {
        super(username, GameMessageType.LAST_TURN);
        this.currentPlayer = currentPlayer;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }
}
