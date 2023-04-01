package it.polimi.ingsw.network.message;

public class LoginRequest extends Message{

    public LoginRequest(String username){
        super(username, MessageType.LOGINREQUEST);
    }

}
