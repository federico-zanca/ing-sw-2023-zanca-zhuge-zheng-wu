package it.polimi.ingsw.distributed;

import it.polimi.ingsw.model.exceptions.ClientAlreadyInLobbyException;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.LobbyNotFoundException;
import it.polimi.ingsw.network.message.connectionmessage.*;
import it.polimi.ingsw.network.message.lobbymessage.ExitLobbyResponse;
import it.polimi.ingsw.network.message.lobbymessage.LobbyMessage;

import java.rmi.RemoteException;

public class ClientHandler {
    private final ServerImpl server;
    public ClientHandler(ServerImpl server) {
        this.server = server;
    }

    public void onConnectionMessage(Client client, ConnectionMessage message) {
        switch(message.getType()) {
            case LOBBY_LIST_REQUEST:
                try {
                    client.update(new LobbyListResponse(server.getLobbiesInfo()));
                } catch (RemoteException e) {
                    System.err.println("Unable to send lobby list response: " + e);
                }
                break;
            case CREATE_LOBBY_REQUEST:
                try {
                    client.update(new CreateLobbyResponse(server.addLobby(new Lobby(server, client,((CreateLobbyRequest) message).getLobbyName()))));
                } catch (RemoteException e) {
                    System.err.println("Unable to send lobby list response: " + e);
                } catch (FullLobbyException e) {
                    //TODO: handle exception
                    throw new RuntimeException(e);
                } catch (ClientAlreadyInLobbyException e) {
                    //TODO: handle exception
                    throw new RuntimeException(e);
                }
                break;
            case JOIN_LOBBY_REQUEST:
                    String content;
                    boolean success;
                    //int numClients = 0;
                    //int maxNumClients = 0;
                    try {
                        server.addClientToLobby(client, ((JoinLobbyRequest) message).getLobbyName());
                        content = "Joined lobby";
                        success = true;
                        //numClients = server.getLobbyByName(((JoinLobbyRequest) message).getLobbyName()).getNumClients();
                        //maxNumClients = server.getLobbyByName(((JoinLobbyRequest) message).getLobbyName()).getMaxNumClients();
                    } catch (ClientAlreadyInLobbyException e) {
                        content = "You are already in a lobby";
                        success = false;
                    } catch (FullLobbyException e) {
                        content = "Lobby is full";
                        success = false;
                    } catch (LobbyNotFoundException e) {
                        content = "Lobby not found";
                        success = false;
                    }
                    try {
                        client.update(new JoinLobbyResponse(success, content));
                    } catch (RemoteException e) {
                        System.err.println("Unable to send lobby list response: " + e);
                    }
                    break;
                case USERNAME_REQUEST:
                    String username = ((UsernameRequest) message).getUsername();
                    if(server.isUsernameAvailable(username)) {
                        server.setUsername(client, username);
                        try {
                            client.update(new UsernameResponse(true, username));
                        } catch (RemoteException e) {
                            System.err.println("Unable to send username response: " + e);
                        }
                    } else {
                        try {
                            client.update(new UsernameResponse(false, server.getConnectedClientInfo(client).getClientID()));
                        } catch (RemoteException e) {
                            System.err.println("Unable to send username response: " + e);
                        }
                    }
                    break;
            default:
                System.err.println("Invalid message type: not implemented yet");
        }
    }

    public void onLobbyMessage(Client client, LobbyMessage message) {
        switch (message.getType()){
            case EXIT_LOBBY_REQUEST:
                try {
                    String lobbyName = server.getLobbyNameOfClient(client);
                    server.removeClientFromLobby(client);
                    client.update(new ExitLobbyResponse(true, lobbyName));
                } catch (RemoteException e) {
                    System.err.println("Unable to send exit lobby response: " + e);
                }
                break;
            case START_GAME_REQUEST:
                server.startGame(client);
                break;
        }
    }
}
