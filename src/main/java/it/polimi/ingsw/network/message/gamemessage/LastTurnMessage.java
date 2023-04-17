package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.gamemessage.GameMessageType;

public class LastTurnMessage extends GameMessage {
    public LastTurnMessage(String username) {
        super(username, GameMessageType.LAST_TURN);
    }
}
