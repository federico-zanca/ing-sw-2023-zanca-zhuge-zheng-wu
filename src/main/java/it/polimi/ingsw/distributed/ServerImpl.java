package it.polimi.ingsw.distributed;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.exceptions.ClientAlreadyInLobbyException;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.LobbyNotFoundException;
import it.polimi.ingsw.network.message.connectionmessage.ConnectedToServerMessage;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.connectionmessage.ConnectionMessage;
import it.polimi.ingsw.network.message.gamemessage.GameMessage;
import it.polimi.ingsw.network.message.lobbymessage.ChangeNumOfPlayerResponse;
import it.polimi.ingsw.network.message.lobbymessage.LobbyMessage;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerImpl extends UnicastRemoteObject implements Server {
    private HashMap<Client, ClientInfo> connectedClients;

    private ArrayList<Lobby> lobbies;

    private ClientHandler clientHandler;

    public ServerImpl() throws RemoteException {
        super();
        this.connectedClients = new HashMap<>();
        this.clientHandler = new ClientHandler(this);
        this.lobbies = new ArrayList<>();
    }

    public ServerImpl(int port) throws RemoteException {
        super(port);
    }

    public ServerImpl(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    /**
     * @param client adds a new client to the list of connected clients
     */
    @Override
    public void register(Client client) {
        connectedClients.put(client, new ClientInfo(client));

        try {
            client.update(new ConnectedToServerMessage(client));
        } catch (RemoteException e) {
            System.err.println("Unable to send preGameMessage "+ e);
        }

        /*
        //TODO move in lobby
        this.model.addObserver((arg) -> {
            try {
                client.update(arg);
            } catch (RemoteException e) {
                System.err.println("Unable to update the client: " + e.getMessage() + ". Skipping the update...");
            }
        });
        */

    }

    /**
     * @return the list of lobbies in this server
     */
    public ArrayList<Lobby> getLobbies() {
        return lobbies;
    }

    /**
     * Adds a new lobby to the list of lobbies in this server
     * @param lobby the lobby to add
     * @return true if the lobby was added, false otherwise
     */
    public boolean addLobby(Lobby lobby) {
        for(Lobby l : lobbies)
            if(l.getName().equals(lobby.getName()))
                return false;
        lobbies.add(lobby);
        connectedClients.get(lobby.getAdmin()).setLobby(lobby);
        connectedClients.get(lobby.getAdmin()).setClientState(ClientState.IN_A_LOBBY);
        return true;
    }

    /**
     * @return an HashMap containing the name of the lobbies as key and the number of players in the lobby as value, associated with the number of players needed to start the game
     */
    public HashMap<String, Map.Entry<Integer, Integer>> getLobbiesInfo() {
        HashMap <String, Map.Entry<Integer, Integer>> availableLobbies = new HashMap<>();
        for(Lobby l : lobbies)
            availableLobbies.put(l.getName(), new AbstractMap.SimpleEntry<>(l.getInLobbyClients().size(), l.getChosenNumOfPlayers()));
        return availableLobbies;
    }

    /**
     * This method adds a client to the lobby having the given name
     * @param client the client to add
     * @param lobbyName the name of the lobby to add the client to
     * @throws ClientAlreadyInLobbyException if the client is already in a lobby
     * @throws FullLobbyException if the lobby is full
     * @throws LobbyNotFoundException if the lobby is not found
     */
    public void addClientToLobby(Client client, String lobbyName) throws ClientAlreadyInLobbyException, FullLobbyException, LobbyNotFoundException {
        for(Lobby l : lobbies) {
            if (l.getInLobbyClients().contains(client)){
                throw new ClientAlreadyInLobbyException();
            }
            if (l.getName().equals(lobbyName)) {
                l.addClient(client);
                connectedClients.get(client).setLobby(l);
                connectedClients.get(client).setClientState(ClientState.IN_A_LOBBY);
                return;
            }
        }
        throw new LobbyNotFoundException();
    }

    /**
     * Checks if a given username is available
     * @param username the username to check
     * @return true if the username is available, false otherwise
     */
    public boolean isUsernameAvailable(String username) {
        for(ClientInfo c : connectedClients.values())
            if(c.getClientID().equals(username))
                return false;
        return true;
    }

    /**
     * Sets the username of a connected client
     * @param client the client whose username we want to set
     * @param username the username to set
     */
    public void setUsername(Client client, String username) {
        connectedClients.get(client).setClientID(username);
    }

    /**
     * This method returns the lobby having the given name
     * @param lobbyName the name of the lobby to get
     * @return the lobby having the given name
     */
    public Lobby getLobbyByName(String lobbyName) {
        for(Lobby l : lobbies)
            if(l.getName().equals(lobbyName))
                return l;
        return null;
    }

    /**
     * This method removes a client from the lobby he is in
     * @param client the client to remove from its lobby
     */
    public void removeClientFromLobby(Client client) {
        connectedClients.get(client).getLobby().removeClient(client);
        connectedClients.get(client).setLobby(null);
        connectedClients.get(client).setClientState(ClientState.IN_SERVER);
    }

    /**
     * This method returns the name of the lobby of a given client
     * @param client the client whose lobby name we want to get
     * @return the name of the lobby of the client
     */
    public String getLobbyNameOfClient(Client client) {
        return getLobbyOfClient(client).getName();
    }

    /**
     * This method returns the lobby of a given client
     * @param client the client whose lobby we want to get
     * @return the lobby of the client
     */
    public Lobby getLobbyOfClient(Client client) {
        return connectedClients.get(client).getLobby();
    }

    /**
     * This method is called when a client wants to start the game
     * @param client the client that wants to start the game
     */
    public void startGame(Client client) {
        if(connectedClients.get(client).getLobby().getAdmin().equals(client)
                && connectedClients.get(client).getLobby().getInLobbyClients().size() == connectedClients.get(client).getLobby().getChosenNumOfPlayers()) {
            connectedClients.get(client).getLobby().startGame();
        }
    }

    /**
     * This method is called when a client wants to change the number of players in the lobby
     * @param client the client that wants to change the number of players
     * @param chosenNum the number of players chosen by the client
     */
    public void changeLobbyNumOfPlayers(Client client, int chosenNum){
        if( chosenNum > GameController.MAX_PLAYERS){
            try {
                client.update(new ChangeNumOfPlayerResponse(false, "Il numero massimo di giocatori è " + GameController.MAX_PLAYERS));
            } catch (RemoteException e) {
                System.err.println("Unable to send ChangeNumOfPlayerResponse message");
            }
        } else if (chosenNum < GameController.MIN_PLAYERS){
            try {
                client.update(new ChangeNumOfPlayerResponse(false, "Il numero minimo di giocatori è " + GameController.MIN_PLAYERS));
            } catch (RemoteException e) {
                System.err.println("Unable to send ChangeNumOfPlayerResponse message");
            }
        } else if (chosenNum < connectedClients.get(client).getLobby().getInLobbyClients().size()){
            try {
                client.update(new ChangeNumOfPlayerResponse(false, "Non puoi diminuire il numero di giocatori se ci sono giocatori in attesa"));
            } catch (RemoteException e) {
                System.err.println("Unable to send ChangeNumOfPlayerResponse message");
            }
        } else {
            connectedClients.get(client).getLobby().changeChosenNumOfPlayers(chosenNum);
            getLobbyOfClient(client).sendToAll(new ChangeNumOfPlayerResponse(true, "\nNumero di giocatori della partita cambiato a " + chosenNum));
        }
    }

    /**
     * @param client the client to get the ClientInfo
     * @return the ClientInfo of the client connected to the server
     */
    public ClientInfo getConnectedClientInfo(Client client) {
        return connectedClients.get(client);
    }


    @Override
    public void update(Client client, Message message){
        System.out.println("Received message: " + message);
        if(message instanceof GameMessage) {
            Lobby lobby = getLobbyOfClient(client);
            lobby.getController().update(client, (GameMessage) message);
        }
        else if(message instanceof ConnectionMessage)
            this.clientHandler.onConnectionMessage(client, (ConnectionMessage) message);
        else if(message instanceof LobbyMessage)
            this.clientHandler.onLobbyMessage(client, (LobbyMessage) message);
        else
            System.err.println("Message not recognized: " + message);
    }

    public String getUsernameOfClient(Client client) {
        return getConnectedClientInfo(client).getClientID();
    }
}
