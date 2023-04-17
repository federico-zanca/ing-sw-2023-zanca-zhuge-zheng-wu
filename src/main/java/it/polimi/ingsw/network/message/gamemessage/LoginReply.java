package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.gamemessage.GameMessageType;

public class LoginReply extends GameMessage {

    protected LoginReply(String username, GameMessageType type) {
        super(username, GameMessageType.LOGIN_REPLY);
    }
}
