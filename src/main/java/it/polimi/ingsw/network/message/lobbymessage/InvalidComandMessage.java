package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.Message;

public class InvalidComandMessage extends LobbyMessage {
    public InvalidComandMessage(){
        super(LobbyMessageType.INVALID_COMAND);
    }
}
