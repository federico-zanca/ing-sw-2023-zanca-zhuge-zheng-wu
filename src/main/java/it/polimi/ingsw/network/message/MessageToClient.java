package it.polimi.ingsw.network.message;

public abstract class MessageToClient extends Message implements MsgToClient{
    MessageType type;

    public MessageToClient(MessageType type){
        super();
        this.type = type;
    }
    public MessageType getType(){
        return type;
    }
}
