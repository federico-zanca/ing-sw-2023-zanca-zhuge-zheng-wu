package it.polimi.ingsw.controller;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.ClientState;
import it.polimi.ingsw.distributed.Lobby;
import it.polimi.ingsw.distributed.ServerImpl;
import it.polimi.ingsw.model.enumerations.JoinType;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.network.message.ChatMessage;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.connectionmessage.*;
import it.polimi.ingsw.network.message.lobbymessage.*;
import it.polimi.ingsw.view.tui.TextColor;

import java.rmi.RemoteException;
import java.util.ArrayList;
/**
 * Represents a controller for the pre-game phase (connection and in lobby phase).
 * It provides methods to interact with a server.
 */
public class PreGameController {
    private final ServerImpl server;
    /**
     * Constructs a new instance of PreGameController.
     * @param server the server implementation to be used
     */
    public PreGameController(ServerImpl server) {
        this.server = server;
    }

    /**
     * Handles a message received from a client not in a lobby
     * @param client the client that sent the message
     * @param message the message received
     */
    public void onConnectionMessage(Client client, MessageToServer message) throws RemoteException {
            message.execute(client, this);

    }

    /**
     * Replaces an old client with a new client in the server and updates associated objects.
     * This method migrates the chat, updates the client's username and lobby, and removes the old client from the server.
     * @param oldClient the old client to be replaced
     * @param client the new client to replace the old client with
     */
    private void replaceClient(Client oldClient, Client client) {
        migrateChat(oldClient, client);
        String username = server.getUsernameOfClient(oldClient);
        Lobby lobby = server.getLobbyOfClient(oldClient);
        lobby.reconnectClient(oldClient, client, username);
        server.getConnectedClientInfo(client).setLobby(lobby);
        server.removeClient(oldClient);
    }

    /**
     * Migrates the chat messages from an old client to a new client.
     * @param oldClient the old client whose chat messages will be migrated
     * @param client the new client to migrate the chat messages to
     */
    private void migrateChat(Client oldClient, Client client) {
        ArrayList<ChatMessage> chat = server.getConnectedClientInfo(oldClient).getChat();
        server.getConnectedClientInfo(client).setChat(chat);
    }

    /**
     * Handles lobby messages
     * @param client the client that sent the message
     * @param message the message
     */
    public void onLobbyMessage(Client client, MessageToServer message) throws RemoteException{

            message.execute(client, this);

    }

    /**
     * Handles a request from a client to create a new lobby.
     * This method creates a new lobby with the specified name and associates it with the client.
     * It sends a response to the client indicating whether the lobby creation was successful.
     * @param client the client making the lobby creation request
     * @param createLobbyRequest the request object containing the lobby name
     */
    public void onCreateLobbyRequest(Client client, CreateLobbyRequest createLobbyRequest) throws RemoteException {
        try{
            if(createLobbyRequest.getLobbyName().trim().equals("") || createLobbyRequest.getLobbyName()==null){
                throw new InvalidLobbyNameException();
            }
            Lobby newLobby = new Lobby(server, client,createLobbyRequest.getLobbyName());
            boolean success = server.addLobby(newLobby);
            server.sendMessage(client, new CreateLobbyResponse(success));
            //client.update(new CreateLobbyResponse(server.addLobby(new Lobby(server, client,((CreateLobbyRequest) message).getLobbyName()))));
        }catch (InvalidLobbyNameException | NullPointerException e) {
            server.sendMessage(client, new CreateLobbyResponse(false));
        }
         catch (FullLobbyException | ClientAlreadyInLobbyException e) {
            System.err.println("Unable to create lobby (should never happen): " + e);
        }
    }

    /**
     * Handles a request from a client to join a lobby.
     * This method processes the join request and updates the lobby and client state accordingly.
     * It sends a response to the client indicating whether the join request was successful.
     * @param client  the client making the join request
     * @param joinLobbyRequest  the request object containing the lobby name
     */
    public void onJoinLobbyRequest(Client client, JoinLobbyRequest joinLobbyRequest) throws RemoteException {
        String content;
        JoinType joinSuccess;
        ArrayList<String> usernames = new ArrayList<>();
        String lobbyName = (joinLobbyRequest.getLobbyName());
        String name = server.getUsernameOfClient(client);
        try {
            if(server.getLobbyByName(lobbyName).getController().getTurnController().getPlayersToSkipUsernames().contains(name) && server.getLobbyByName(lobbyName).isGameStarted()) {
                server.addClientToLobby(client, lobbyName);
                joinSuccess = JoinType.REJOINED;
                content = "Rejoined lobby";
                usernames = server.getLobbyByName(lobbyName).getClientsUsernames();
                server.getConnectedClientInfo(client).setClientState(ClientState.IN_GAME);
            }
            else{
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
            server.sendMessage(client, new JoinLobbyResponse(joinSuccess, content, usernames));
            //client.update(new JoinLobbyResponse(joinSuccess, content, usernames));
        if(joinSuccess == JoinType.JOINED || joinSuccess == JoinType.REJOINED) {
            String username = server.getConnectedClientInfo(client).getClientID();
            Lobby lobbyOfClient = server.getLobbyOfClient(client);
            //sends to everyone but the client that joined the lobby the new list of players
            if(!lobbyOfClient.isGameStarted())
                lobbyOfClient.sendPlayersListToEveryoneBut(username, TextColor.CYANTEXT + username + TextColor.NO_COLOR + " si è unito alla partita");
        }
    }

    /**
     * Handles a request from a client to get the list of available lobbies.
     * This method retrieves the information about the lobbies from the server and sends a response to the client
     * containing the lobby list information.
     * @param client the client making the lobby list request
     * @param lobbyListRequest  the request object
     */
    public void onLobbyListRequest(Client client, LobbyListRequest lobbyListRequest) throws RemoteException{

            server.sendMessage(client, new LobbyListResponse(server.getLobbiesInfo()));
            //client.update(new LobbyListResponse(server.getLobbiesInfo()));

    }

    /**
     * Handles a request from a client to log in with a username.
     * This method checks if the requested username is available.
     * If available, it assigns the username to the client and sends a successful login response.
     * If the username is not available, it checks if the previous client with the same username
     * is disconnected from an ongoing game. If so, it replaces the old client with the new client
     * and sends a successful login response. Otherwise, it sends a failed login response.
     * @param client  the client making the login request
     * @param loginRequest   the request object containing the requested username
     */
    public void onLoginRequest(Client client, LoginRequest loginRequest) throws RemoteException{
        String playername = (loginRequest.getUsername());

            if (server.isUsernameAvailable(playername)) {
                try {
                    server.setUsername(client, playername);
                } catch (InvalidUsernameException e) {
                    server.sendMessage(client, new LoginResponse(false, playername));
                    return;
                }
                server.sendMessage(client, new LoginResponse(true, playername));
                //client.update(new LoginResponse(true, playername));

            } else {
                Client oldClient = server.getClientByUsername(playername);
                if (oldClient != null && server.getConnectedClientInfo(oldClient).getClientState() == ClientState.IN_GAME &&
                        !server.getConnectedClientInfo(oldClient).isConnected()) {
                    server.sendMessage(client, new LoginResponse(true, playername));
                    //client.update(new LoginResponse(true, playername));
                    replaceClient(oldClient, client);
                } else {
                    server.sendMessage(client, new LoginResponse(false, playername));
                    //client.update(new LoginResponse(false, playername));
                }
            }
    }

    /**
     * Handles a request from a client to change username.
     * This method checks if the new username is available.
     * If available, it assigns the username to the client and sends a successful username response.
     * If the username is not available, it sends a failed username response with the client's current username.
     * @param client  the client making the username request
     * @param usernameRequest the request object containing the requested username
     */
    public void onUsernameRequest(Client client, UsernameRequest usernameRequest) throws RemoteException{
        String username = usernameRequest.getUsername();
        UsernameResponse usernameResponse;

            if (server.isUsernameAvailable(username)) {
                try {
                    server.setUsername(client, username);
                    usernameResponse = new UsernameResponse(true, username);
                } catch (InvalidUsernameException e) {
                    usernameResponse = new UsernameResponse(false, server.getConnectedClientInfo(client).getClientID());
                }
                server.sendMessage(client, usernameResponse);
                //client.update(new UsernameResponse(true, username));
            } else {
                server.sendMessage(client, new UsernameResponse(false, server.getConnectedClientInfo(client).getClientID()));
                //client.update(new UsernameResponse(false, server.getConnectedClientInfo(client).getClientID()));
            }

    }

    /**
     * Handles a request from a client to change the number of players in the lobby.
     * If the client making the request is the lobby's admin, the method changes the number of players
     * in the lobby to the chosen number specified in the request.
     * If the client is not the admin, it sends a "NotAdminMessage" response indicating that only the admin
     * can change the number of players.
     * @param client  the client making the request
     * @param changeNumOfPlayersRequest the request object containing the chosen number of players
     */
    public void onChangeNumOfPlayersRequest(Client client, ChangeNumOfPlayersRequest changeNumOfPlayersRequest) throws RemoteException {
        Client admin = server.getLobbyOfClient(client).getAdmin();
        int chosenNum = changeNumOfPlayersRequest.getChosenNum();
        if(client.equals(admin))
            server.changeLobbyNumOfPlayers(client, chosenNum);
        else{
            server.sendMessage(client, new NotAdminMessage());
            //client.update(new NotAdminMessage());
        }
    }

    /**
     * Handles a request from a client to exit the lobby.
     * This method removes the client from the lobby, sends a successful exit lobby response to the client,
     * and notifies the remaining players in the lobby about the client's departure.
     * @param client   the client making the exit lobby request
     * @param exitLobbyRequest    the request object
     */
    public void onExitLobbyRequest(Client client, ExitLobbyRequest exitLobbyRequest) throws RemoteException{
        String lobbyName = server.getLobbyNameOfClient(client);
        server.removeClientFromLobby(client);
        server.sendMessage(client, new ExitLobbyResponse(true, lobbyName));
        //client.update(new ExitLobbyResponse(true, lobbyName));
        String content = TextColor.CYANTEXT + server.getUsernameOfClient(client) + TextColor.NO_COLOR + " ha abbandonato la lobby";

        try {
            server.getLobbyByName(lobbyName).sendPlayersListToEveryoneBut(server.getUsernameOfClient(client), content);
        } catch (LobbyNotFoundException e) {
            System.err.println("Lobby not found: " + e); //not an error, the lobby was deleted. This is the expected behaviour
            System.err.println("Probably the lobby was deleted because the last player in it left");
        }
    }

    /**
     * Handles a request from a client to get the list of players in the lobby.
     * This method retrieves the usernames of all clients in the lobby of the requesting client
     * and sends a player list response containing the usernames back to the client.
     * @param client   the client making the player list request
     * @param playerListRequest   the request object
     */
    public void onPlayerListRequest(Client client, PlayerListRequest playerListRequest) throws RemoteException{
        ArrayList<String> inLobbyClientsUsernames = server.getLobbyOfClient(client).getClientsUsernames();
        server.sendMessage(client, new PlayerListResponse(inLobbyClientsUsernames));
        //client.update(new PlayerListResponse(inLobbyClientsUsernames));
    }

    /**
     * Handles a request from the lobby admin to start the game.
     * If the client making the request is not the admin, it sends a "NotAdminMessage" response
     * indicating that only the admin can start the game.
     * If the number of players in the lobby is not equal to the chosen number of players,
     * it sends a "GameNotReadyMessage" response indicating that the game is not ready to start.
     * If both conditions are satisfied, the method starts the game.
     * @param client  the client making the start game request
     * @param startGameRequest   the request object
     */
    public void onStartGameRequest(Client client, StartGameRequest startGameRequest) throws RemoteException{
        Client admin = server.getLobbyOfClient(client).getAdmin();
        int numPlayers = server.getLobbyOfClient(client).getNumClients();
        if (!client.equals(admin)) {
            server.sendMessage(client, new NotAdminMessage());
            //client.update(new NotAdminMessage());
        } else if (numPlayers != server.getLobbyOfClient(client).getChosenNumOfPlayers()) {
            server.sendMessage(client, new GameNotReadyMessage("game not ready to start"));
            //client.update(new GameNotReadyMessage("game not ready to start"));
        } else {
            server.startGame(client);
        }
    }

}
