package it.polimi.ingsw.distributed;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.enumerations.GamePhase;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import it.polimi.ingsw.network.message.ChatMessage;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.connectionmessage.ReconnectionMessage;
import it.polimi.ingsw.network.message.gamemessage.ExitGameResponse;
import it.polimi.ingsw.network.message.gamemessage.PlayerLeftMessage;
import it.polimi.ingsw.network.message.lobbymessage.GameNotReadyMessage;
import it.polimi.ingsw.network.message.lobbymessage.InvalidCommandMessage;
import it.polimi.ingsw.network.message.lobbymessage.NewAdminMessage;
import it.polimi.ingsw.network.message.lobbymessage.PlayersInLobbyUpdate;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 Represents a lobby in the server.
 */
public class Lobby {
    private final ServerImpl server;
    private final String lobbyName;
    private ArrayList<Client> inLobbyClients;
    private Client admin;
    private final Game model;
    private final GameController controller;

    private ArrayList<ChatMessage> chat;

    /**
     Constructs a new Lobby object with the given server, admin client, and lobby name.
     @param server the server implementation
     @param admin the admin client
     @param lobbyName the name of the lobby
     @throws FullLobbyException if the lobby is already full
     @throws ClientAlreadyInLobbyException if the admin client is already in a lobby
     */
    public Lobby(ServerImpl server, Client admin, String lobbyName) throws FullLobbyException, ClientAlreadyInLobbyException {
        this.server = server;
        this.model = new Game();
        this.controller = new GameController(model, this);
        this.lobbyName = lobbyName;
        this.admin = admin;
        this.inLobbyClients = new ArrayList<>();
        this.chat = new ArrayList<>();
        addClient(admin);
    }

    /**
     * @param client the client that wants to join the lobby
     * @throws ClientAlreadyInLobbyException if the client is already in the lobby
     * @throws FullLobbyException if the lobby is full
     */
    public void addClient(Client client) throws ClientAlreadyInLobbyException, FullLobbyException{
        if (inLobbyClients.contains(client)) {
            throw new ClientAlreadyInLobbyException();
        } else if (numOfActuallyConnectedClientsInThisLobby() == model.getChosenPlayersNumber()) {
            throw new FullLobbyException();
        } else {
            inLobbyClients.add(client);
            ClientInfo connectedClientInfo = server.getConnectedClientInfo(client);
            String clientID = connectedClientInfo.getClientID();

            if (model.getPlayersUsernames().contains(server.getUsernameOfClient(client)) &&
                controller.getTurnController().getPlayersToSkipUsernames().contains(clientID)) {
                connectedClientInfo.setClientState(ClientState.IN_GAME);
                controller.reconnectPlayer(clientID);
            }
            /*
            else {

                try {
                    controller.addPlayer(clientID);
                } catch (InvalidUsernameException e) {
                    System.err.println("Can't add player to the model: " + e.getMessage());
                }
            }
            */

            this.model.addObserver((arg) -> {
                try {
                    if(arg == null) {
                        System.err.println("arg is null --- Lobby line 77");
                        return;
                    }
                    //System.err.println("Questo è il client --> " + client);
                    if (arg.getType()== MessageType.GAME_MSG) {
                        MessageToClient gameMessage = (MessageToClient) arg; // cast once
                        String username = gameMessage.getUsername();
                        if (username.equals("")) {
                            server.sendMessage(client, arg);
                            //client.update(arg);
                        } else if (model.isGameStarted() &&
                                   username.equals(clientID)) {
                            server.sendMessage(client, arg);
                            //client.update(arg);
                        }
                    } else {
                        System.err.println("Unable to update the client: " + arg + " is not a GameMessage. Skipping the update...");
                    }
                } catch (RemoteException e) {
                    System.err.println("Unable to update the client: " + e.getMessage() + ". Skipping the update...");
                }
            });
    }
}

    /**
     Returns the number of actually connected clients in this lobby.
     @return the number of connected clients in the lobby
     */
    private int numOfActuallyConnectedClientsInThisLobby() {
        int count = 0;
        for(Client client : inLobbyClients) {
            if(server.getConnectedClientInfo(client).isConnected()) {
                count++;
            }
        }
        return count;
    }


    /**
     * @return the admin of the lobby
     */
    public Client getAdmin() {
        return admin;
    }

    /**
     * @return the name of the lobby
     */
    public String getName() {
        return lobbyName;
    }

    /**
     * @return the list of clients in the lobby
     */
    public ArrayList<Client> getInLobbyClients() {
        return inLobbyClients;
    }

    /**
     * @param client the client whose username is requested
     * @return the username of the client
     */
    public String getClientUsername(Client client){
        return server.getConnectedClientInfo(client).getClientID();
    }

    /**
     * @return the list of clients' usernames in the lobby
     */
    public ArrayList<String> getClientsUsernames(){
        ArrayList<String> clientsUsername = new ArrayList<>();
        for(Client client : inLobbyClients)
            clientsUsername.add(getClientUsername(client));
        return clientsUsername;
    }

    /**
     * @return the number of clients in the lobby
     */
    public int getNumClients() {
        return inLobbyClients.size();
    }

    /**
     * @return the number of players chosen for the game
     */
    public int getChosenNumOfPlayers() {
        return model.getChosenPlayersNumber();
    }

    /**
     * Removes a client from the lobby
     * @param client the client to remove
     */
    public void removeClient(Client client) {
        this.model.removeObserver(inLobbyClients.indexOf(client));
        inLobbyClients.remove(client);
        server.getConnectedClientInfo(client).setClientState(ClientState.IN_SERVER);
        server.getConnectedClientInfo(client).setLobby(null);
        if(server.getConnectedClientInfo(client).isConnected())
            controller.removePlayer(server.getConnectedClientInfo(client).getClientID());

        if(client.equals(admin) && inLobbyClients.size() > 0 && !isGameEnded()) {
            updateAdmin(client);
        }else if(inLobbyClients.size() == 0){
            destroyLobby();
        }

    }

    /**
     * Updates the admin of the current lobby.
     *
     * @param client The client that triggered the update.
     */
    private void updateAdmin(Client client) {
        admin = inLobbyClients.get(0);
        for (Client c : inLobbyClients) {
            if (server.getConnectedClientInfo(c).isConnected()) {
                try {
                    server.sendMessage(c, new NewAdminMessage(server.getConnectedClientInfo(client).getClientID(), server.getConnectedClientInfo(admin).getClientID()));
                    //c.update(new NewAdminMessage(server.getConnectedClientInfo(client).getClientID(), server.getConnectedClientInfo(admin).getClientID()));
                } catch (RemoteException e) {
                    System.err.println("Unable to send preGameMessage " + e);
                }
            }
        }
    }

    /**
     * Checks if the game has ended.
     *
     * @return true if the game has ended, false otherwise.
     */
    private boolean isGameEnded() {
        return model.getGamePhase()== GamePhase.ENDED;
    }

    /**
     * Starts the game
     */
    public void startGame() {
        try {
            loadPlayersIntoGame();
            controller.startGame();
            for(Client client : inLobbyClients)
                server.getConnectedClientInfo(client).setClientState(ClientState.IN_GAME);
        } catch (GameNotReadyException e) {
            unloadPlayersFromGame();
            try {
                server.sendMessage(admin, new GameNotReadyMessage("Game not ready to start"));
                //admin.update(new GameNotReadyMessage("Game not ready to start"));
            } catch (RemoteException ex) {
                System.err.println("Unable to send preGameMessage "+ ex);
            }
        }
    }

    /**
     Unloads the players from the game by resetting the player state in the game controller.
     */
    private void unloadPlayersFromGame() {
        controller.resetPlayers();
    }

    /**
     Loads the players into the game by adding them to the game controller.
     @throws RuntimeException if an invalid username is encountered while adding a player
     */
    private void loadPlayersIntoGame() {
        for(Client c : inLobbyClients){
            try {
                controller.addPlayer(server.getConnectedClientInfo(c).getClientID());
            } catch (InvalidUsernameException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Changes the number of players chosen for the game
     * @param chosenNum the new number of players
     */
    public void changeChosenNumOfPlayers(int chosenNum){
        try {
            controller.changeChosenNumOfPlayers(chosenNum);
        } catch (InvalidCommandException e){
            try {
                server.sendMessage(admin, new InvalidCommandMessage());
                //admin.update(new InvalidCommandMessage());
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * @return the game controller
     */
    public GameController getController() {
        return controller;
    }

    /**
     * Sends a list of players in the lobby to all clients except for one specified player.
     * @param player the player to exclude from the client list
     * @param content the content to send to the clients along with the player list
     */
    public void sendPlayersListToEveryoneBut(String player, String content) {
        for (Client inLobbyClient : inLobbyClients) {
            if (!player.equals(getClientUsername(inLobbyClient)) && server.getConnectedClientInfo(inLobbyClient).isConnected()) {
                try {
                    server.sendMessage(inLobbyClient, new PlayersInLobbyUpdate(getClientsUsernames(), content));
                    //inLobbyClient.update(new PlayersInLobbyUpdate(getClientsUsernames(), content));
                } catch (RemoteException e) {
                    System.err.println("Unable to send PlayersInLobbyUpdate : " + e.getMessage());
                }
            }
        }
    }

    /**
     * Sends a message to all clients in the lobby
     * @param message the message to send
     */
    public void sendToAll(Message message) {
        for (Client c : inLobbyClients) {
            if (server.getConnectedClientInfo(c).isConnected()) {
                try {
                    server.sendMessage(c, message);
                    //c.update(message);
                } catch (RemoteException e) {
                    System.err.println("Unable to send LobbyMessage : " + e.getMessage());
                }
            }
        }
    }

    /**
     * Removes the given client from the lobby and notifies the other clients.
     * If the client was the admin of the lobby and there are still other clients
     * in the lobby, assigns a new admin to the lobby.
     * If the client was the last one in the lobby, removes the lobby from the server.
     * @param client the client to remove from the lobby
     */
    public void exitClient(Client client) {
        this.model.removeObserver(inLobbyClients.indexOf(client));
        inLobbyClients.remove(client);
        server.getConnectedClientInfo(client).setClientState(ClientState.IN_SERVER);
        for(Client c : inLobbyClients){
            if(server.getConnectedClientInfo(c).isConnected()) {
                try {
                    String content = getClientUsername(client) + " left the game. ";
                    if (model.isGameStarted()) {
                        content += "His turn will be skipped until he rejoins the game.";
                    }
                    server.sendMessage(c, new PlayerLeftMessage("", content));
                    //c.update(new PlayerLeftMessage("", content));
                } catch (RemoteException e) {
                    System.err.println("Unable to send PlayersInLobbyUpdate " + e);
                }
            }
        }
        try {
            server.sendMessage(client, new ExitGameResponse(getClientUsername(client), "You exited the game"));
            //client.update(new ExitGameResponse(getClientUsername(client), "You exited the game"));
        } catch (RemoteException e) {
            System.err.println("Unable to send ExitGameResponse " + e);
        }
        if(client.equals(admin) && inLobbyClients.size() > 0) {
            updateAdmin(client);
        }else if(inLobbyClients.size() == 0){
            server.getLobbies().remove(this);
        }
    }

    /**
     * @return true if the game has started, false otherwise
     */
    public boolean isGameStarted() {
        return model.isGameStarted();
    }

    /**
     * Ends the game and removes the lobby from the server.
     */
    public void endGame() {
        for(Client client : inLobbyClients){
            removeClient(client);
        }
        destroyLobby();
    }

    /**
     * Destroys the lobby by removing it from the server's list of lobbies.
     */
    private void destroyLobby() {
        inLobbyClients.clear();
        server.getLobbies().remove(this);
    }

    /**
     Disconnects a client from the lobby.
     @param client the client to disconnect
     */
    public void disconnectClient(Client client) {
        controller.disconnectPlayer(server.getConnectedClientInfo(client).getClientID());
        for(Client c : inLobbyClients) {
            if (server.getConnectedClientInfo(c).isConnected()) {
                try {
                    String content = getClientUsername(client) + " left the game. ";
                    if (model.isGameStarted()) {
                        content += "His turn will be skipped until he rejoins the game.";
                    }
                    server.sendMessage(c, new PlayerLeftMessage("", content));
                    //c.update(new PlayerLeftMessage("", content));
                } catch (RemoteException e) {
                    System.err.println("Unable to send PlayersInLobbyUpdate " + e);
                }
            }
        }
        //rimuovi observer
    }

    /**
     Reconnects a client to the lobby.
     @param oldClient the old client instance to be replaced
     @param client the new client instance
     @param username the new username for the client
     @throws RuntimeException if an error occurs during the reconnection process
     */
    public void reconnectClient(Client oldClient, Client client, String username) {
        removeClient(oldClient);


        System.err.println("Rimosso " + oldClient + " da " + inLobbyClients);


        server.getConnectedClientInfo(client).setClientID(username);
        try {
            addClient(client);
        } catch (ClientAlreadyInLobbyException | FullLobbyException e) {
            throw new RuntimeException(e);
        }
        try {
            PersonalGoalCard personalGoal = model.getPersonalGoalOfPlayer(username);
            ArrayList<ChatMessage> chat = server.getConnectedClientInfo(client).getChat();
            server.sendMessage(client, new ReconnectionMessage(new GameView(model), "You reconnected to the game you left: " + lobbyName + "\n", personalGoal, chat));
            //client.update(new ReconnectionMessage(new GameView(model), "You reconnected to the game you left: " + lobbyName + "\n", personalGoal));
        } catch (RemoteException e) {
            System.err.println("Can't send Reconnection Message :" + e.getMessage());
        }
    }

    /**
     Checks if the lobby contains a player with the given username.
     @param username the username to check
     @return true if the lobby contains a player with the given username, false otherwise
     */
    public boolean containsAPlayerWithThisUsername(String username) {
        return model.getPlayersUsernames().contains(username);
    }

    /**
     Handles a chat message sent by a client.
     @param client the client sending the chat message
     @param message the chat message to handle
     */
    public void onChatMessage(Client client, ChatMessage message) {
        message.setSender(getClientUsername(client));
        if(message.getReceiver()==null){
            for(Client c : inLobbyClients){
                server.getConnectedClientInfo(c).addChatMessage(message);
            }
            chat.add(message); //aggiungo il messaggio alla chat lato server solo se è un messaggio per tutti
            sendToAll(message);
        }else{
            Client receiver = getClientByUsername(message.getReceiver());
            if(receiver!=null){
                server.getConnectedClientInfo(client).addChatMessage(message);
                try {
                    server.sendMessage(getClientByUsername(message.getSender()), message);
                    server.sendMessage(receiver, message);
                } catch (RemoteException e) {
                    System.err.println("Unable to send ChatMessage " + e);
                }
            }
        }
}


    /**
     Retrieves a client from the lobby based on their username.
     @param username the username of the client to retrieve
     @return the client with the specified username, or null if not found
     */
    private Client getClientByUsername(String username) {
        for(Client c : inLobbyClients){
            if(getClientUsername(c).equals(username)){
                return c;
            }
        }
        return null;
    }
}
