package it.polimi.ingsw.controller;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.ClientState;
import it.polimi.ingsw.distributed.Lobby;
import it.polimi.ingsw.distributed.ServerImpl;
import it.polimi.ingsw.model.enumerations.JoinType;
import it.polimi.ingsw.model.exceptions.ClientAlreadyInLobbyException;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.LobbyNotFoundException;
import it.polimi.ingsw.network.message.ChatMessage;
import it.polimi.ingsw.network.message.connectionmessage.*;
import it.polimi.ingsw.network.message.lobbymessage.*;
import it.polimi.ingsw.view.tui.Color;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class PreGameController {
    private final ServerImpl server;
    public PreGameController(ServerImpl server) {
        this.server = server;
    }

    /**
     * Handles a message received from a client not in a lobby
     * @param client the client that sent the message
     * @param message the message received
     */
    public void onConnectionMessage(Client client, ConnectionMessage message) {
        message.execute(client, this);
        /*
        switch(message.getType()) {
            case LOBBY_LIST_REQUEST:

                break;
            case CREATE_LOBBY_REQUEST:

                break;
            case JOIN_LOBBY_REQUEST:
                break;
            case USERNAME_REQUEST:

                break;
            case LOGIN_REQUEST:

                break;
            default:
                System.err.println("Invalid message type: not implemented yet");
        }

         */
    }

    private void replaceClient(Client oldClient, Client client) {
        migrateChat(oldClient, client);
        String username = server.getUsernameOfClient(oldClient);
        Lobby lobby = server.getLobbyOfClient(oldClient);
        lobby.reconnectClient(oldClient, client, username);
        server.getConnectedClientInfo(client).setLobby(lobby);
        server.removeClient(oldClient);
    }

    private void migrateChat(Client oldClient, Client client) {
        ArrayList<ChatMessage> chat = server.getConnectedClientInfo(oldClient).getChat();
        server.getConnectedClientInfo(client).setChat(chat);
    }

    /**
     * Handles lobby messages
     * @param client the client that sent the message
     * @param message the message
     */
    public void onLobbyMessage(Client client, LobbyMessage message) {
        message.execute(client, this);
        /*
        switch (message.getType()){
            case EXIT_LOBBY_REQUEST:

                break;
            case START_GAME_REQUEST:

                break;
            case PLAYER_LIST_REQUEST:

                break;
            case CHANGE_NUM_OF_PLAYERS_REQUEST:

                break;
        }
        */
    }

    public void onCreateLobbyRequest(Client client, CreateLobbyRequest createLobbyRequest) {
        try {
            Lobby newLobby = new Lobby(server, client,createLobbyRequest.getLobbyName());
            boolean success = server.addLobby(newLobby);
            server.sendMessage(client, new CreateLobbyResponse(success));
            //client.update(new CreateLobbyResponse(server.addLobby(new Lobby(server, client,((CreateLobbyRequest) message).getLobbyName()))));
        } catch (RemoteException e) {
            System.err.println("Unable to send lobby list response: " + e);
        } catch (FullLobbyException | ClientAlreadyInLobbyException e) {
            //TODO: handle exception
            System.err.println("Unable to create lobby (should never happen): " + e);
        }
    }

    public void onJoinLobbyRequest(Client client, JoinLobbyRequest joinLobbyRequest) {
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
            server.sendMessage(client, new JoinLobbyResponse(joinSuccess, content, usernames));
            //client.update(new JoinLobbyResponse(joinSuccess, content, usernames));
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
    }

    public void onLobbyListRequest(Client client, LobbyListRequest lobbyListRequest) {
        try {
            server.sendMessage(client, new LobbyListResponse(server.getLobbiesInfo()));
            //client.update(new LobbyListResponse(server.getLobbiesInfo()));
        } catch (RemoteException e) {
            System.err.println("Unable to send lobby list response: " + e);
        }
    }

    public void onLoginRequest(Client client, LoginRequest loginRequest) {
        String playername = (loginRequest.getUsername());
        if(server.isUsernameAvailable(playername)) {
            server.setUsername(client, playername);
            try {
                server.sendMessage(client, new LoginResponse(true, playername));
                //client.update(new LoginResponse(true, playername));
            } catch (RemoteException e) {
                System.err.println("Unable to send username response: " + e);
            }
        } else {
            Client oldClient = server.getClientByUsername(playername);
            if (oldClient!= null && server.getConnectedClientInfo(oldClient).getClientState() == ClientState.IN_GAME &&
                    !server.getConnectedClientInfo(oldClient).isConnected()){
                try {
                    server.sendMessage(client, new LoginResponse(true, playername));
                    //client.update(new LoginResponse(true, playername));
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                replaceClient(oldClient, client);
            } else {
                try {
                    server.sendMessage(client, new LoginResponse(false, playername));
                    //client.update(new LoginResponse(false, playername));
                } catch (RemoteException e) {
                    System.err.println("Unable to send username response: " + e);
                }
            }
        }
    }

    public void onUsernameRequest(Client client, UsernameRequest usernameRequest) {
        String username = usernameRequest.getUsername();
        if(server.isUsernameAvailable(username)) {
            server.setUsername(client, username);
            try {
                server.sendMessage(client, new UsernameResponse(true, username));
                //client.update(new UsernameResponse(true, username));
            } catch (RemoteException e) {
                System.err.println("Unable to send username response: " + e);
            }
        } else {
            try {
                server.sendMessage(client, new UsernameResponse(false, server.getConnectedClientInfo(client).getClientID()));
                //client.update(new UsernameResponse(false, server.getConnectedClientInfo(client).getClientID()));
            } catch (RemoteException e) {
                System.err.println("Unable to send username response: " + e);
            }
        }
    }

    public void onChangeNumOfPlayersRequest(Client client, ChangeNumOfPlayersRequest changeNumOfPlayersRequest) {
        Client admin = server.getLobbyOfClient(client).getAdmin();
        int chosenNum = changeNumOfPlayersRequest.getChosenNum();
        if(client.equals(admin))
            server.changeLobbyNumOfPlayers(client, chosenNum);
        else{
            try {
                server.sendMessage(client, new NotAdminMessage());
                //client.update(new NotAdminMessage());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void onExitLobbyRequest(Client client, ExitLobbyRequest exitLobbyRequest) {
        try {
            String lobbyName = server.getLobbyNameOfClient(client);
            server.removeClientFromLobby(client);
            server.sendMessage(client, new ExitLobbyResponse(true, lobbyName));
            //client.update(new ExitLobbyResponse(true, lobbyName));
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
    }

    public void onPlayerListRequest(Client client, PlayerListRequest playerListRequest) {
        ArrayList<String> inLobbyClientsUsernames = server.getLobbyOfClient(client).getClientsUsernames();
        try {
            server.sendMessage(client, new PlayerListResponse(inLobbyClientsUsernames));
            //client.update(new PlayerListResponse(inLobbyClientsUsernames));
        } catch (RemoteException e) {
            System.err.println("Unable to send player list response: " + e);
        }
    }

    public void onStartGameRequest(Client client, StartGameRequest startGameRequest) {
        Client admin = server.getLobbyOfClient(client).getAdmin();
        int numPlayers = server.getLobbyOfClient(client).getNumClients();
        if(!client.equals(admin) ){
            try {
                server.sendMessage(client, new NotAdminMessage());
                //client.update(new NotAdminMessage());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }else if(numPlayers != server.getLobbyOfClient(client).getChosenNumOfPlayers()){
            try {
                server.sendMessage(client, new GameNotReadyMessage("game not ready to start"));
                //client.update(new GameNotReadyMessage("game not ready to start"));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }else{
            server.startGame(client);
        }
    }
}
