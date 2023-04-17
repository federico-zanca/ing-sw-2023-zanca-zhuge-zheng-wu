package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.gamemessage.GameMessage;
import it.polimi.ingsw.network.message.gamemessage.GameMessageType;

public class LoginRequest extends GameMessage {

    public LoginRequest(String username){
        super(username, GameMessageType.LOGINREQUEST);
    }

}
