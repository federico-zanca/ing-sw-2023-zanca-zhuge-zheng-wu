package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.gamemessage.GameMessageType;

public class NewTurnMessage extends GameMessage {
    public NewTurnMessage(String username) {
        super(username, GameMessageType.NEW_TURN);
    }

    public String getContent(){
        return "Turno di " + getUsername();
    }
}
