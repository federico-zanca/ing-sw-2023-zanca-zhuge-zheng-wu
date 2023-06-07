package it.polimi.ingsw.distributed;

import it.polimi.ingsw.network.message.ChatMessage;

import java.util.ArrayList;
import java.util.Timer;

/**
 Represents information about a client.
 */
public class ClientInfo {
    private boolean connected;
    private String clientID;
    private ClientState clientState;
    private Lobby lobby;

    private ArrayList<ChatMessage> chat;
    private Timer heartbeatTimer;

    /**
     Constructs a new ClientInfo object with the specified client.
     @param client the client for which the information is being created
     */
    public ClientInfo(Client client) {
        this.clientID = client.toString();
        this.clientState = ClientState.IN_SERVER;
        this.connected = true;
        this.chat = new ArrayList<>();
    }

    /**
     Gets the client ID.
     @return the client ID
     */
    public String getClientID() {
        return clientID;
    }

    /**
     Gets the client ID.
     @return the client ID
     */
    public ClientState getClientState() {
        return clientState;
    }

    /**
     Sets the client state.
     @param clientState the new client state
     */
    public void setClientState(ClientState clientState) {
        this.clientState = clientState;
    }

    /**
     Gets the lobby the client is in.
     @return the lobby
     */
    public Lobby getLobby() {
        return lobby;
    }

    /**
     Sets the lobby the client is in.
     @param lobby the new lobby
     */
    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }


    /**
     Sets the client ID.
     @param username the new client ID
     */
    public void setClientID(String username) {
        this.clientID = username;
    }

    /**
     Checks if the client is connected.
     @return true if the client is connected, false otherwise
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     Sets the connected status of the client.
     @param connected the connected status
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     Sets the heartbeat timer for the client.
     @param heartbeatTimer the heartbeat timer
     */
    public void setHeartbeatTimer(Timer heartbeatTimer) {
        this.heartbeatTimer = heartbeatTimer;
    }

    /**
     Gets the heartbeat timer for the client.
     @return the heartbeat timer
     */
    public Timer getHeartbeatTimer() {
        return heartbeatTimer;
    }

    /**
     Adds a chat message to the client's chat history.
     @param message the chat message to add
     */
    public void addChatMessage(ChatMessage message) {
        chat.add(message);
    }

    /**
     Gets the chat history of the client.
     @return the chat history
     */
    public ArrayList<ChatMessage> getChat() {
        return chat;
    }

    /**
     Sets the chat history of the client.
     @param chat the new chat history
     */
    public void setChat(ArrayList<ChatMessage> chat) {
        this.chat = chat;
    }
}
