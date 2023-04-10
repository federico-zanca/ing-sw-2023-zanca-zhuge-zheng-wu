package it.polimi.ingsw.network.message;

public class LastTurnMessage extends Message {
    public LastTurnMessage(String username) {
        super(username, MessageType.LAST_TURN);
    }
}
