package it.polimi.ingsw.distributed;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.ClientAlreadyInLobbyException;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.GameNotReadyException;
import it.polimi.ingsw.model.exceptions.InvalidUsernameException;
import it.polimi.ingsw.network.message.lobbymessage.GameNotReadyMessage;
import it.polimi.ingsw.network.message.lobbymessage.NewAdminMessage;

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
                    client.update(arg);
                } catch (RemoteException e) {
                    System.err.println("Unable to update the client: " + e.getMessage() + ". Skipping the update...");
                }
            });
        }
    }

    public Client getAdmin() {
        return admin;
    }

    public String getName() {
        return lobbyName;
    }

    public ArrayList<Client> getInLobbyClients() {
        return inLobbyClients;
    }

    public int getNumClients() {
        return inLobbyClients.size();
    }

    public int getMaxNumClients() {
        return model.getChosenPlayersNumber();
    }

    public void removeClient(Client client) {
        inLobbyClients.remove(client);
        controller.removePlayer(server.getConnectedClientInfo(client).getClientID());
        if(client.equals(admin) && inLobbyClients.size() > 0){
            admin = inLobbyClients.get(0);
            for (Client c : inLobbyClients) {
                try {
                    c.update(new NewAdminMessage(server.getConnectedClientInfo(client).getClientID(), server.getConnectedClientInfo(admin).getClientID()));
                } catch (RemoteException e) {
                    System.err.println("Unable to send preGameMessage "+ e);
                }
            }
        }
    }

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
}
