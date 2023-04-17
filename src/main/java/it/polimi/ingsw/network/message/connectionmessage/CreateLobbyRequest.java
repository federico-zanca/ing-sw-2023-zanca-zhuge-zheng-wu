package it.polimi.ingsw.network.message.connectionmessage;

public class CreateLobbyRequest extends ConnectionMessage{
    private final String lobbyName;

    public CreateLobbyRequest(String lobbyName) {
        super(ConnectionMessageType.CREATE_LOBBY_REQUEST);
        this.lobbyName = lobbyName;
    }

    public String getLobbyName() {
        return lobbyName;
    }
}
