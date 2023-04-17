package it.polimi.ingsw.distributed;

public class ClientInfo {
    private String clientID;
    private ClientState clientState;
    private ClientConnectionState clientConnectionState;

    private Lobby lobby;
    public ClientInfo(Client client) {
        this.clientID = client.toString();
        this.clientState = ClientState.IN_SERVER;
        this.clientConnectionState = ClientConnectionState.CONNECTED;
    }

    public String getClientID() {
        return clientID;
    }

    public ClientState getClientState() {
        return clientState;
    }

    public void setClientState(ClientState clientState) {
        this.clientState = clientState;
    }

    public ClientConnectionState getClientConnectionState() {
        return clientConnectionState;
    }

    public void setClientConnectionState(ClientConnectionState clientConnectionState) {
        this.clientConnectionState = clientConnectionState;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public void setClientID(String username) {
        this.clientID = username;
    }
}
