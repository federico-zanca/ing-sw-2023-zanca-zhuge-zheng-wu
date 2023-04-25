package it.polimi.ingsw.distributed;

import it.polimi.ingsw.model.enumerations.JoinType;
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
                JoinType joinSuccess;
                ArrayList<String> usernames = new ArrayList<>();
                String lobbyName = ((JoinLobbyRequest) message).getLobbyName();
                String name = server.getUsernameOfClient(client);
                try {
                    if(server.getLobbyByName(lobbyName).getController().getTurnController().getPlayersToSkipUsernames().contains(name) && server.getLobbyByName(lobbyName).isGameStarted()) {
                        server.addClientToLobby(client, lobbyName);
                        joinSuccess = JoinType.REJOINED;
                        content = "Rejoined lobby";
                        usernames = server.getLobbyByName(lobbyName).getClientsUsernames();
                        server.getConnectedClientInfo(client).setClientState(ClientState.IN_GAME);
                    } else{
                        if(!server.getLobbyByName(lobbyName).isGameStarted()){
                            server.addClientToLobby(client, lobbyName);
                            joinSuccess = JoinType.JOINED;
                            content = "Joined lobby";
                            usernames = server.getLobbyByName(lobbyName).getClientsUsernames();
                        } else{
                            joinSuccess = JoinType.REFUSED;
                            content = "Can't join! Game already started";
                            usernames = new ArrayList<>();
                        }
                    }
                } catch (ClientAlreadyInLobbyException e) {
                    content = "You are already in a lobby";
                    joinSuccess = JoinType.REFUSED;
                    usernames = server.getLobbyOfClient(client).getClientsUsernames();
                } catch (FullLobbyException e) {
                    content = "Lobby is full";
                    joinSuccess = JoinType.REFUSED;
                    usernames = new ArrayList<>();
                } catch (LobbyNotFoundException e) {
                    content = "Lobby not found";
                    joinSuccess = JoinType.REFUSED;
                    usernames = new ArrayList<>();
                }
                try {
                    client.update(new JoinLobbyResponse(joinSuccess, content, usernames));
                } catch (RemoteException e) {
                    System.err.println("Unable to send lobby list response: " + e);
                }
                if(joinSuccess == JoinType.JOINED || joinSuccess == JoinType.REJOINED) {
                    String username = server.getConnectedClientInfo(client).getClientID();
                    Lobby lobbyOfClient = server.getLobbyOfClient(client);
                    //sends to everyone but the client that joined the lobby the new list of players
                    if(!lobbyOfClient.isGameStarted())
                        lobbyOfClient.sendPlayersListToEveryoneBut(username, Color.CYANTEXT + username + Color.NO_COLOR + " si è unito alla partita");
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
                String playername = ((LoginRequest) message).getUsername();
                if(server.isUsernameAvailable(playername)) {
                    server.setUsername(client, playername);
                    try {
                        client.update(new LoginResponse(true, playername));
                    } catch (RemoteException e) {
                        System.err.println("Unable to send username response: " + e);
                    }
                } else {
                    try {
                        client.update(new LoginResponse(false, playername));
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

                    try {
                        server.getLobbyByName(lobbyName).sendPlayersListToEveryoneBut(server.getUsernameOfClient(client), content);
                    } catch (LobbyNotFoundException e) {
                        System.err.println("Lobby not found: " + e); //not an error, the lobby was deleted. This is the expected behaviour
                        System.err.println("Probably the lobby was deleted because the last player in it left");
                    }
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
