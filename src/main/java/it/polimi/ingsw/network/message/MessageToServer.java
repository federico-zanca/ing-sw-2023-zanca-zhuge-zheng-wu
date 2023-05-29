package it.polimi.ingsw.network.message;

public abstract class MessageToServer implements MsgToServer{
    MessageType type;

    public MessageToServer(MessageType type){
        super();
        this.type = type;
    }
    public MessageType getType(){
        return type;
    }
}
