package it.polimi.ingsw.distributed;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.network.message.gamemessage.GameMessage;
import it.polimi.ingsw.network.message.lobbymessage.GameNotReadyMessage;
import it.polimi.ingsw.network.message.lobbymessage.InvalidComandMessage;
import it.polimi.ingsw.network.message.lobbymessage.NewAdminMessage;
import it.polimi.ingsw.network.message.lobbymessage.PlayersInLobbyUpdate;

import java.rmi.RemoteException;
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
        this.controller = new GameController(model);
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
        if(inLobbyClients.contains(client))
            throw new ClientAlreadyInLobbyException();
        else if(inLobbyClients.size() == model.getChosenPlayersNumber())
            throw new FullLobbyException();
        else {
            inLobbyClients.add(client);
            try {
                controller.addPlayer(server.getConnectedClientInfo(client).getClientID());
            } catch (InvalidUsernameException e) {
                //TODO migliora
                System.err.println("Invalid username: " + e.getMessage() + ". Skipping the update...");
            }
            this.model.addObserver((arg) -> {
                try {
                    if(arg instanceof GameMessage) {
                        if (controller.getCurrentPlayerUsername().equals(server.getConnectedClientInfo(client).getClientID()) && ((GameMessage) arg).getUsername().equals(server.getConnectedClientInfo(client).getClientID()))
                            client.update(arg);
                        else if(((GameMessage) arg).getUsername().equals(""))
                            client.update(arg);
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

    public String getClientUsername(Client client){
        return server.getConnectedClientInfo(client).getClientID();
    }

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
        inLobbyClients.remove(client);
        server.getConnectedClientInfo(client).setClientState(ClientState.IN_SERVER);
        controller.removePlayer(server.getConnectedClientInfo(client).getClientID());
        if(client.equals(admin) && inLobbyClients.size() > 0) {
            admin = inLobbyClients.get(0);
            for (Client c : inLobbyClients) {
                try {
                    c.update(new NewAdminMessage(server.getConnectedClientInfo(client).getClientID(), server.getConnectedClientInfo(admin).getClientID()));
                } catch (RemoteException e) {
                    System.err.println("Unable to send preGameMessage " + e);
                }
            }
        }else if(inLobbyClients.size() == 0){
            server.getLobbies().remove(this);
        }
        //TODO remove observer
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
}
