package it.polimi.ingsw.network.message;

public class NewTurnMessage extends Message {
    public NewTurnMessage(String username) {
        super(username, MessageType.NEW_TURN);
    }

    public String getContent(){
        return "Turno di " + getUsername();
    }
}
