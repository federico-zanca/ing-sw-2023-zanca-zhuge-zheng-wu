package it.polimi.ingsw.network.message.connectionmessage;

public class JoinLobbyRequest extends ConnectionMessage{
    private final String lobbyName;

    public JoinLobbyRequest(String lobbyName) {
        super(ConnectionMessageType.JOIN_LOBBY_REQUEST);
        this.lobbyName = lobbyName;
    }

    public String getLobbyName() {
        return lobbyName;
    }
}
