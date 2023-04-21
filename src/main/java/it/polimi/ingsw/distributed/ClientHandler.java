package it.polimi.ingsw.distributed;

import it.polimi.ingsw.model.exceptions.ClientAlreadyInLobbyException;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.LobbyNotFoundException;
import it.polimi.ingsw.network.message.connectionmessage.*;
import it.polimi.ingsw.network.message.lobbymessage.*;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class ClientHandler {
    private final ServerImpl server;
    public ClientHandler(ServerImpl server) {
        this.server = server;
    }

    /**
     * Handles a message received from a client not in a lobby
     * @param client the client that sent the message
     * @param message the message received
     */
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

    /**
     * Handles lobby messages
     * @param client the client that sent the message
     * @param message the message
     */
    public void onLobbyMessage(Client client, LobbyMessage message) {
        Client admin = server.getLobbyOfClient(client).getAdmin();
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
                int numPlayers = server.getLobbyOfClient(client).getNumClients();
                if(!client.equals(admin) ){
                    try {
                        client.update(new NotAdminMessage());
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }else if(numPlayers != server.getLobbyOfClient(client).getChosenNumOfPlayers()){
                    try {
                        client.update(new GameNotReadyMessage("game not ready to start"));
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    server.startGame(client);
                }
                break;
            case PLAYER_LIST_REQUEST:
                ArrayList<Client> inLobbyClients = server.getLobbyOfClient(client).getInLobbyClients();
                ArrayList<String> inLobbyClientsUsername = server.getLobbyOfClient(client).getClientsUsername(inLobbyClients);
                try {
                    client.update(new PlayerListResponse(inLobbyClientsUsername));
                } catch (RemoteException e) {
                    System.err.println("Unable to send player list response: " + e);
                }
                break;
            case CHANGE_NUM_OF_PLAYER_REQUEST:
                int chosenNum = ((ChangeNumOfPlayerRequest) message).getChosenNum();
                if(client.equals(admin))
                    server.changeNumOfPlayers(client, chosenNum);
                else{
                    try {
                        client.update(new NotAdminMessage());
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
        }
    }
}
