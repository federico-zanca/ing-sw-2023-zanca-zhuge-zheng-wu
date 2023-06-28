package it.polimi.ingsw.distributed;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.model.exceptions.ClientAlreadyInLobbyException;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.InvalidUsernameException;
import it.polimi.ingsw.model.exceptions.LobbyNotFoundException;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.network.message.connectionmessage.ConnectedToServerMessage;
import it.polimi.ingsw.network.message.gamemessage.ExitGameRequest;
import it.polimi.ingsw.network.message.gamemessage.PlayerLeftMessage;
import it.polimi.ingsw.network.message.lobbymessage.ChangeNumOfPlayerResponse;
import it.polimi.ingsw.view.tui.LobbyDisplayInfo;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
/**
 * Represents a server implementation for a game server.
 */
public class ServerImpl extends UnicastRemoteObject implements Server {
    private static final int HEARTBEAT_TIMEOUT = 10000;
    private HashMap<Client, ClientInfo> connectedClients;

    private final Object clientsLock = new Object();
    private ArrayList<Lobby> lobbies;

    private PreGameController preGameController;

    /**
     * Constructs a new ServerImpl instance.
     * @throws RemoteException if a remote error occurs during object initialization.
     */
    public ServerImpl() throws RemoteException {
        super();
        this.connectedClients = new HashMap<>();
        this.preGameController = new PreGameController(this);
        this.lobbies = new ArrayList<>();
    }

    /**
     * Constructs a new ServerImpl instance with a specific port number.
     * @param port the port number to use for the server.
     * @throws RemoteException if a remote error occurs during object initialization.
     */
    public ServerImpl(int port) throws RemoteException {
        super(port);
    }

    /**
     * Constructs a new ServerImpl instance with a specific port number and custom socket factories.
     * @param port the port number to use for the server.
     * @param csf  the client socket factory to use for creating client sockets.
     * @param ssf  the server socket factory to use for creating server sockets.
     * @throws RemoteException if a remote error occurs during object initialization.
     */
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
            sendMessage(client, new ConnectedToServerMessage(client));
            //client.update(new ConnectedToServerMessage(client));
        } catch (RemoteException e) {
            System.err.println("Unable to send preGameMessage "+ e);
        }
        startHeartBeat(client);
    }

    /**
     * Gets the list of lobbies in this server
     * @return the list of lobbies
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
    public ArrayList<LobbyDisplayInfo> getLobbiesInfo() {
        String status;
        ArrayList<LobbyDisplayInfo> availableLobbies = new ArrayList<>();

        if (lobbies != null) {
            for (Lobby l : lobbies) {
                status = l.isGameStarted() ? "STARTED" : "WAITING";
                availableLobbies.add(new LobbyDisplayInfo(l.getName(), l.getInLobbyClients().size(), l.getChosenNumOfPlayers(), status));
            }
        }

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
                connectedClients.get(client).setClientState(ClientState.IN_A_LOBBY);
                connectedClients.get(client).setLobby(l);
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
        Client client = getClientByUsername(username);
        return client == null;
        /*
        for(Lobby l : lobbies){
            if(l.containsAPlayerWithThisUsername(username))
                return false;
        }

         */
/*
        for(ClientInfo c : connectedClients.values())
            if(c.getClientID().equals(username))
                return false;
        return true;
        */

    }

    /**
     * Sets the username of a connected client
     * @param client the client whose username we want to set
     * @param username the username to set
     */
    public void setUsername(Client client, String username) throws InvalidUsernameException {
        if(isValidUsername(username))
            getConnectedClientInfo(client).setClientID(username);
        else throw new InvalidUsernameException();
    }

    public boolean isValidUsername(String username) {
        if(username == null)
            return false;
        // Check for spaces in username
        if (username.equals("") || username.contains(" "))
            return false;


        // Check if username starts or ends with a special character or if it is solely composed of numbers
        char firstChar = username.charAt(0);
        if (firstChar == '-' || firstChar == '_' || firstChar == '.' || username.endsWith("-") || username.endsWith(".") || username.matches("[0-9]+")) {
            return false;
        }

        // Check for non-literal or non-numeric characters other than '-', '_' and '.'
        String pattern = "[^a-zA-Z0-9\\-_\\.]";
        return !username.matches(".*" + pattern + ".*");

        // All checks passed, username is valid
    }


    /**
     * This method returns the lobby having the given name
     * @param lobbyName the name of the lobby to get
     * @return the lobby having the given name
     */
    public Lobby getLobbyByName(String lobbyName) throws LobbyNotFoundException {
        for(Lobby l : lobbies)
            if(l.getName().equals(lobbyName))
                return l;
        throw new LobbyNotFoundException();
    }

    /**
     * This method removes a client from the lobby he is in
     * @param client the client to remove from its lobby
     */
    public void removeClientFromLobby(Client client) {
        connectedClients.get(client).getLobby().removeClient(client);
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
        try{
        if( chosenNum > GameController.MAX_PLAYERS){
            sendMessage(client, new ChangeNumOfPlayerResponse(false, "Il numero massimo di giocatori è " + GameController.MAX_PLAYERS, chosenNum));
            //client.update(new ChangeNumOfPlayerResponse(false, "Il numero massimo di giocatori è " + GameController.MAX_PLAYERS));
        } else if (chosenNum < GameController.MIN_PLAYERS){
            sendMessage(client, new ChangeNumOfPlayerResponse(false, "Il numero minimo di giocatori è " + GameController.MIN_PLAYERS, chosenNum));
            //client.update(new ChangeNumOfPlayerResponse(false, "Il numero minimo di giocatori è " + GameController.MIN_PLAYERS));
        } else if (chosenNum < connectedClients.get(client).getLobby().getInLobbyClients().size()) {
            sendMessage(client, new ChangeNumOfPlayerResponse(false, "Non puoi diminuire il numero di giocatori se ci sono giocatori in attesa", chosenNum));
            //client.update(new ChangeNumOfPlayerResponse(false, "Non puoi diminuire il numero di giocatori se ci sono giocatori in attesa"));
        } else{
            connectedClients.get(client).getLobby().changeChosenNumOfPlayers(chosenNum);
            getLobbyOfClient(client).sendToAll(new ChangeNumOfPlayerResponse(true, "\nNumero di giocatori della partita cambiato a " + chosenNum, chosenNum));
        }
        } catch (RemoteException e) {
            System.err.println("Unable to send ChangeNumOfPlayerResponse message");
        }
    }

    /**
     * @param client the client to get the ClientInfo
     * @return the ClientInfo of the client connected to the server
     */
    public ClientInfo getConnectedClientInfo(Client client) {
        return connectedClients.get(client);
    }

    /**
     * Handles the event when a client exits from its current lobby.
     * @param client the client that is exiting the lobby.
     */
    private void clientExitsFromItsLobby(Client client) {
        connectedClients.get(client).getLobby().exitClient(client);
        connectedClients.get(client).setLobby(null);
        connectedClients.get(client).setClientState(ClientState.IN_SERVER);
    }

    /**
     * Gets the username of a specific client.
     * @param client the client whose username is to be retrieved.
     * @return the username of the client.
     */
    public String getUsernameOfClient(Client client) {
        return getConnectedClientInfo(client).getClientID();
    }

    @Override
    public void update(Client client, Message message){
        new Thread(()->{
            if(message.getType()==MessageType.GAME_MSG)
                {
                    System.out.println("Received message: " + message);
                    Lobby lobby = getLobbyOfClient(client);
                    if (message instanceof ExitGameRequest) {
                        clientExitsFromItsLobby(client);
                    }
                    lobby.getController().update(getUsernameOfClient(client), (MessageToServer) message);
                }
            else if(message.getType()==MessageType.CONNECTION_MSG)

                {
                    System.out.println("Received message: " + message);
                    try {
                        this.preGameController.onConnectionMessage(client, (MessageToServer) message);
                    } catch (RemoteException e) {
                        System.err.println("Unable to send ConnectionResponse message");
                    }
                }
            else if(message.getType()==MessageType.LOBBY_MSG)

                {
                    System.out.println("Received message: " + message);
                    try {
                        this.preGameController.onLobbyMessage(client, (MessageToServer) message);
                    } catch (RemoteException e) {
                        System.err.println("Unable to send LobbyResponse message");
                    }
                }
            else if(message instanceof HeartBeatMessage)

                {
                    receiveHeartBeat(client);
                }
            else if(message instanceof ChatMessage)

                {
                    System.out.println("Received message: " + message);
                    Lobby lobby = getLobbyOfClient(client);
                    if (lobby != null)
                        lobby.onChatMessage(client, (ChatMessage) message);
                }
            else
                    System.err.println("Message not recognized: "+message);
        }
        ).start();
    }

    /*
    public void run(){
        while(!Thread.currentThread().isInterrupted()){
            synchronized (clientsLock){
                for(Client client : connectedClients.keySet()){
                    if(getConnectedClientInfo(client).isConnected()) {
                        try{
                            client.ping();
                        } catch (RemoteException e){
                            System.err.println("Client " + getConnectedClientInfo(client).getClientID() + " disconnected");
                            if(getConnectedClientInfo(client).getClientState() == ClientState.IN_A_LOBBY)
                                clientExitsFromItsLobby(client);
                            else if(getConnectedClientInfo(client).getClientState() == ClientState.IN_GAME){
                                getConnectedClientInfo(client).setConnected(false);
                                getLobbyOfClient(client).disconnectClient(client);
                            }
                            else{
                                connectedClients.remove(client);
                            }
                        }
                    }
                }
            }
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }
    */

    /**
     * Starts the heartbeat mechanism for a specific client.
     * A timer is scheduled to disconnect the client if a heartbeat is not received within a specified timeout.
     * @param client the client for which to start the heartbeat mechanism.
     */
    public void startHeartBeat(Client client){
        Timer timer = new Timer();
        getConnectedClientInfo(client).setHeartbeatTimer(timer);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                disconnect(client);
            }
        }, HEARTBEAT_TIMEOUT);
    }

    /**
     * Receives a heartbeat from a specific client.
     * If a heartbeat timer exists for the client, it cancels the timer and starts a new heartbeat timer.
     * @param client the client from which a heartbeat is received.
     */
    public void receiveHeartBeat(Client client){
        Timer timer = getConnectedClientInfo(client).getHeartbeatTimer();
        if(timer != null){
            timer.cancel();
            startHeartBeat(client);
        }
    }

    /**
     * Stops the heartbeat mechanism for a specific client.
     * If a heartbeat timer exists for the client, it cancels and purges the timer.
     * @param client the client for which to stop the heartbeat mechanism.
     */
    public void stopHeartBeat(Client client){
        Timer timer = getConnectedClientInfo(client).getHeartbeatTimer();
        if(timer != null){
            timer.cancel();
            timer.purge();
        }
    }

    /**
     * Disconnects a client from the server.
     * @param client the client to disconnect.
     */
    public void disconnect(Client client){
        if(getConnectedClientInfo(client).isConnected()) {
            stopHeartBeat(client);
            System.err.println("Client " + getConnectedClientInfo(client).getClientID() + " disconnected");
            System.err.println(connectedClients);
            if (getConnectedClientInfo(client).getClientState() == ClientState.IN_A_LOBBY) {
                Lobby lobby = getLobbyOfClient(client);
                String username = getConnectedClientInfo(client).getClientID();
                getConnectedClientInfo(client).setConnected(false);
                removeClientFromLobby(client);
                connectedClients.remove(client);
                lobby.sendToAll(new PlayerLeftMessage("", username + " left the game. His turn will be skipped until he reconnects."));
            }
            else if (getConnectedClientInfo(client).getClientState() == ClientState.IN_GAME) {
                getConnectedClientInfo(client).setConnected(false);
                getLobbyOfClient(client).disconnectClient(client);
            } else {
                connectedClients.remove(client);
            }
        }
    }

    /**
     * Gets the client object associated with a given player username.
     * @param playername the username of the player.
     * @return the client object associated with the player username, or null if not found.
     */
    public Client getClientByUsername(String playername) {
        for(Client client : connectedClients.keySet()){
            if(getUsernameOfClient(client).equals(playername))
                return client;
        }
        return null;
    }

    /**
     * Removes a client from the server's connected clients.
     * @param client the client to remove.
     */
    public void removeClient(Client client) {
        //WARNING : removes the client from the hashmap, but doesn't touch the lobby: use carefully
        connectedClients.remove(client);
    }

    /**
     * Gets the hashmap containing the connected clients and the associated {@link ClientInfo} objects.
     * @return the hashmap containing the connected clients and the associated {@link ClientInfo} objects.
     */
    public HashMap<Client, ClientInfo> getConnectedClients() {
        return connectedClients;
    }

    /**
     * Sends a message to a specific client.
     * @param client the client to send the message to.
     * @param message the message to send.
     * @throws RemoteException if a remote error occurs during the method call.
     */
    public void sendMessage(Client client, Message message) throws RemoteException {
        System.err.println("Sending message to " + getUsernameOfClient(client) + " : " + message);
        client.update(message);
    }
}
