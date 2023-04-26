package it.polimi.ingsw.network.message.lobbymessage;

public class InvalidCommandMessage extends LobbyMessage {
    public InvalidCommandMessage(){
        super(LobbyMessageType.INVALID_COMMAND);
    }
}
