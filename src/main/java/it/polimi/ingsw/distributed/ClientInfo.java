package it.polimi.ingsw.distributed;

import it.polimi.ingsw.network.message.ChatMessage;

import java.util.ArrayList;
import java.util.Timer;

public class ClientInfo {
    private boolean connected;
    private String clientID;
    private ClientState clientState;
    private Lobby lobby;

    private ArrayList<ChatMessage> chat;
    private Timer heartbeatTimer;
    public ClientInfo(Client client) {
        this.clientID = client.toString();
        this.clientState = ClientState.IN_SERVER;
        this.connected = true;
        this.chat = new ArrayList<>();
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

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public void setClientID(String username) {
        this.clientID = username;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setHeartbeatTimer(Timer heartbeatTimer) {
        this.heartbeatTimer = heartbeatTimer;
    }

    public Timer getHeartbeatTimer() {
        return heartbeatTimer;
    }

    public void addChatMessage(ChatMessage message) {
        chat.add(message);
    }

    public ArrayList<ChatMessage> getChat() {
        return chat;
    }

    public void setChat(ArrayList<ChatMessage> chat) {
        this.chat = chat;
    }
}
