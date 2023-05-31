package it.polimi.ingsw.network.message;

public abstract class MessageToClient extends Message implements MsgToClient{
    public MessageToClient(MessageType type){
        super(type);
    }

    public String getUsername(){
        return ""; //TODO dangerous practice
    }
}
