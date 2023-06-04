package it.polimi.ingsw.distributed;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.model.exceptions.ClientAlreadyInLobbyException;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
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

public class ServerImpl extends UnicastRemoteObject implements Server {
    private static final int HEARTBEAT_TIMEOUT = 10000;
    private HashMap<Client, ClientInfo> connectedClients;

    private final Object clientsLock = new Object();
    private ArrayList<Lobby> lobbies;

    private PreGameController preGameController;

    public ServerImpl() throws RemoteException {
        super();
        this.connectedClients = new HashMap<>();
        this.preGameController = new PreGameController(this);
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
        System.out.println("1");
        connectedClients.put(client, new ClientInfo(client));
        System.out.println("fnisngrs");
        try {
            sendMessage(client, new ConnectedToServerMessage(client));
            //client.update(new ConnectedToServerMessage(client));
        } catch (RemoteException e) {
            System.err.println("Unable to send preGameMessage "+ e);
        }
        startHeartBeat(client);
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
                connectedClients.get(client).setClientState(ClientState.IN_A_LOBBY);
                l.addClient(client);
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
    public void setUsername(Client client, String username) {
        connectedClients.get(client).setClientID(username);
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
        if( chosenNum > GameController.MAX_PLAYERS){
            try {
                sendMessage(client, new ChangeNumOfPlayerResponse(false, "Il numero massimo di giocatori è " + GameController.MAX_PLAYERS, chosenNum));
                //client.update(new ChangeNumOfPlayerResponse(false, "Il numero massimo di giocatori è " + GameController.MAX_PLAYERS));
            } catch (RemoteException e) {
                System.err.println("Unable to send ChangeNumOfPlayerResponse message");
            }
        } else if (chosenNum < GameController.MIN_PLAYERS){
            try {
                sendMessage(client, new ChangeNumOfPlayerResponse(false, "Il numero minimo di giocatori è " + GameController.MIN_PLAYERS, chosenNum));
                //client.update(new ChangeNumOfPlayerResponse(false, "Il numero minimo di giocatori è " + GameController.MIN_PLAYERS));
            } catch (RemoteException e) {
                System.err.println("Unable to send ChangeNumOfPlayerResponse message");
            }
        } else if (chosenNum < connectedClients.get(client).getLobby().getInLobbyClients().size()){
            try {
                sendMessage(client, new ChangeNumOfPlayerResponse(false, "Non puoi diminuire il numero di giocatori se ci sono giocatori in attesa", chosenNum));
                //client.update(new ChangeNumOfPlayerResponse(false, "Non puoi diminuire il numero di giocatori se ci sono giocatori in attesa"));
            } catch (RemoteException e) {
                System.err.println("Unable to send ChangeNumOfPlayerResponse message");
            }
        } else {
            connectedClients.get(client).getLobby().changeChosenNumOfPlayers(chosenNum);
            getLobbyOfClient(client).sendToAll(new ChangeNumOfPlayerResponse(true, "\nNumero di giocatori della partita cambiato a " + chosenNum,chosenNum));
        }
    }

    /**
     * @param client the client to get the ClientInfo
     * @return the ClientInfo of the client connected to the server
     */
    public ClientInfo getConnectedClientInfo(Client client) {
        return connectedClients.get(client);
    }


    private void clientExitsFromItsLobby(Client client) {
        connectedClients.get(client).getLobby().exitClient(client);
        connectedClients.get(client).setLobby(null);
        connectedClients.get(client).setClientState(ClientState.IN_SERVER);
    }


    public String getUsernameOfClient(Client client) {
        return getConnectedClientInfo(client).getClientID();
    }

    @Override
    public void update(Client client, Message message){
        if(message.getType()== MessageType.GAME_MSG) {
            System.out.println("Received message: " + message);
            Lobby lobby = getLobbyOfClient(client);
            if(message instanceof ExitGameRequest){
                clientExitsFromItsLobby(client);
            }
            lobby.getController().update(getUsernameOfClient(client), (MessageToServer) message);
        }
        else if(message.getType()==MessageType.CONNECTION_MSG) {
            System.out.println("Received message: " + message);
            this.preGameController.onConnectionMessage(client, (MessageToServer) message);
        }
        else if(message.getType()==MessageType.LOBBY_MSG){
            System.out.println("Received message: " + message);
            this.preGameController.onLobbyMessage(client, (MessageToServer) message);
        }
        else if(message instanceof HeartBeatMessage){
            receiveHeartBeat(client);
        }
        else if(message instanceof ChatMessage){
            System.out.println("Received message: " + message);
            Lobby lobby = getLobbyOfClient(client);
            if(lobby != null)
                lobby.onChatMessage(client, (ChatMessage) message);
        }
        else
            System.err.println("Message not recognized: " + message);
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

    public void receiveHeartBeat(Client client){
        Timer timer = getConnectedClientInfo(client).getHeartbeatTimer();
        if(timer != null){
            timer.cancel();
            startHeartBeat(client);
        }
    }

    public void stopHeartBeat(Client client){
        Timer timer = getConnectedClientInfo(client).getHeartbeatTimer();
        if(timer != null){
            timer.cancel();
            timer.purge();
        }
    }

    private void disconnect(Client client){
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
                lobby.sendToAll(new PlayerLeftMessage("", username + "left the game. His turn will be skipped until he reconnects."));
            }
            else if (getConnectedClientInfo(client).getClientState() == ClientState.IN_GAME) {
                getConnectedClientInfo(client).setConnected(false);
                getLobbyOfClient(client).disconnectClient(client);
            } else {
                connectedClients.remove(client);
            }
        }
    }

    public Client getClientByUsername(String playername) {
        for(Client client : connectedClients.keySet()){
            if(getUsernameOfClient(client).equals(playername))
                return client;
        }
        return null;
    }

    public void removeClient(Client client) {
        //WARNING : removes the client from the hashmap, but doesn't touch the lobby: use carefully
        connectedClients.remove(client);
    }

    public void sendMessage(Client client, Message message) throws RemoteException {
        System.err.println("Sending message to " + getUsernameOfClient(client) + " : " + message);
        client.update(message);
    }
}
