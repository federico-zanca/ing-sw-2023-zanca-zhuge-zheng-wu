package it.polimi.ingsw.distributed;

import it.polimi.ingsw.model.exceptions.ClientAlreadyInLobbyException;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.LobbyNotFoundException;
import it.polimi.ingsw.network.message.connectionmessage.*;
import it.polimi.ingsw.network.message.lobbymessage.*;
import it.polimi.ingsw.view.Color;

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
                } catch (FullLobbyException | ClientAlreadyInLobbyException e) {
                    //TODO: handle exception
                    System.err.println("Unable to create lobby (should never happen): " + e);
                }
                break;
            case JOIN_LOBBY_REQUEST:
                String content;
                boolean success;
                String lobbyName = ((JoinLobbyRequest) message).getLobbyName();
                //int numClients = 0;
                //int maxNumClients = 0;
                try {
                    server.addClientToLobby(client, lobbyName);
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
                    client.update(new JoinLobbyResponse(success, content, server.getLobbyByName(lobbyName).getClientsUsernames()));
                } catch (RemoteException e) {
                    System.err.println("Unable to send lobby list response: " + e);
                }
                if(success){
                    String username = server.getConnectedClientInfo(client).getClientID();
                    server.getLobbyOfClient(client).sendPlayersListToEveryoneBut(username, Color.CYANTEXT + username + Color.NO_COLOR + " si è unito alla partita");
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
            case LOGIN_REQUEST:
                String name = ((LoginRequest) message).getUsername();
                if(server.isUsernameAvailable(name)) {
                    server.setUsername(client, name);
                    try {
                        client.update(new LoginResponse(true, name));
                    } catch (RemoteException e) {
                        System.err.println("Unable to send username response: " + e);
                    }
                } else {
                    try {
                        client.update(new LoginResponse(false, name));
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
                    String content = Color.CYANTEXT + server.getUsernameOfClient(client) + Color.NO_COLOR + " ha abbandonato la lobby";
                    server.getLobbyByName(lobbyName).sendPlayersListToEveryoneBut(server.getUsernameOfClient(client), content);
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
                ArrayList<String> inLobbyClientsUsernames = server.getLobbyOfClient(client).getClientsUsernames();
                try {
                    client.update(new PlayerListResponse(inLobbyClientsUsernames));
                } catch (RemoteException e) {
                    System.err.println("Unable to send player list response: " + e);
                }
                break;
            case CHANGE_NUM_OF_PLAYER_REQUEST:
                int chosenNum = ((ChangeNumOfPlayerRequest) message).getChosenNum();
                if(client.equals(admin))
                    server.changeLobbyNumOfPlayers(client, chosenNum);
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
