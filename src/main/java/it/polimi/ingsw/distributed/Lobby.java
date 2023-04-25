package it.polimi.ingsw.distributed;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.GamePhase;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.network.message.gamemessage.ExitGameResponse;
import it.polimi.ingsw.network.message.gamemessage.GameMessage;
import it.polimi.ingsw.network.message.gamemessage.PlayerLeftMessage;
import it.polimi.ingsw.network.message.lobbymessage.*;

import java.rmi.RemoteException;
import java.rmi.ServerError;
import java.util.ArrayList;

public class Lobby {
    private final ServerImpl server;
    private String lobbyName;
    private ArrayList<Client> inLobbyClients;
    private Client admin;
    private final Game model;
    private final GameController controller;

    public Lobby(ServerImpl server, Client admin, String lobbyName) throws FullLobbyException, ClientAlreadyInLobbyException {
        this.server = server;
        this.model = new Game();
        this.controller = new GameController(model, this);
        this.lobbyName = lobbyName;
        this.admin = admin;
        this.inLobbyClients = new ArrayList<>();
        addClient(admin);
    }

    /**
     * @param client the client that wants to join the lobby
     * @throws ClientAlreadyInLobbyException if the client is already in the lobby
     * @throws FullLobbyException if the lobby is full
     */
    public void addClient(Client client) throws ClientAlreadyInLobbyException, FullLobbyException{
        if(inLobbyClients.contains(client)) {
            throw new ClientAlreadyInLobbyException();
        }
        else if(inLobbyClients.size() == model.getChosenPlayersNumber())
            throw new FullLobbyException();
        else {
            inLobbyClients.add(client);
            if(model.getPlayersUsernames().contains(server.getUsernameOfClient(client)) && controller.getTurnController().getPlayersToSkipUsernames().contains(server.getUsernameOfClient(client))) {
                controller.reconnectExitedPlayer(server.getConnectedClientInfo(client).getClientID());
                server.getConnectedClientInfo(client).setClientState(ClientState.IN_GAME);
            } else {
                try {
                    controller.addPlayer(server.getConnectedClientInfo(client).getClientID());
                } catch (InvalidUsernameException e) {
                    System.err.println("Can't add player to the model: " + e.getMessage());
                }
            }
            this.model.addObserver((arg) -> {
                try {
                    if(arg instanceof GameMessage) {
                        if(((GameMessage) arg).getUsername().equals(""))
                            client.update(arg);
                        else if (model.isGameStarted() && controller.getCurrentPlayerUsername().equals(server.getConnectedClientInfo(client).getClientID()) && ((GameMessage) arg).getUsername().equals(server.getConnectedClientInfo(client).getClientID())) {
                            client.update(arg);
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
            try {
                c.update(new NewAdminMessage(server.getConnectedClientInfo(client).getClientID(), server.getConnectedClientInfo(admin).getClientID()));
            } catch (RemoteException e) {
                System.err.println("Unable to send preGameMessage " + e);
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
            controller.startGame();
            for(Client client : inLobbyClients)
                server.getConnectedClientInfo(client).setClientState(ClientState.IN_GAME);
        } catch (GameNotReadyException e) {
            try {
                admin.update(new GameNotReadyMessage("Game not ready to start"));
            } catch (RemoteException ex) {
                System.err.println("Unable to send preGameMessage "+ ex);
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
                admin.update(new InvalidComandMessage());
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
        for(int i=0; i < inLobbyClients.size(); i++){
            if(!player.equals(getClientUsername(inLobbyClients.get(i)))){
                try {
                    inLobbyClients.get(i).update(new PlayersInLobbyUpdate(getClientsUsernames(), content));
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
    public void sendToAll(LobbyMessage message) {
        for(int i=0; i < inLobbyClients.size(); i++){
            try {
                inLobbyClients.get(i).update(message);
            } catch (RemoteException e) {
                System.err.println("Unable to send LobbyMessage : " + e.getMessage());
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
        //TODO handle bug when last client exits
        this.model.removeObserver(inLobbyClients.indexOf(client));
        inLobbyClients.remove(client);
        server.getConnectedClientInfo(client).setClientState(ClientState.IN_SERVER);
        for(Client c : inLobbyClients){
            try {
                String content = getClientUsername(client) + " left the game. ";
                if(model.isGameStarted()){
                    content += "His turn will be skipped until he rejoins the game.";
                }
                c.update(new PlayerLeftMessage("", content));
            } catch (RemoteException e) {
                System.err.println("Unable to send PlayersInLobbyUpdate " + e);
            }
        }
        try {
            client.update(new ExitGameResponse(getClientUsername(client), "You exited the game"));
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
        server.getLobbies().remove(this);
    }
}
