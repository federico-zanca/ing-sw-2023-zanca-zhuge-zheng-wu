package it.polimi.ingsw.network.message.lobbymessage;

public class InvalidComandMessage extends LobbyMessage {
    public InvalidComandMessage(){
        super(LobbyMessageType.INVALID_COMMAND);
    }
}
