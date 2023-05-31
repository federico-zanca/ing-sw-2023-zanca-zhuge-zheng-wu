package it.polimi.ingsw.network.message;

public abstract class MessageToServer extends Message implements MsgToServer{
    public MessageToServer(MessageType type){
        super(type);
    }

}
