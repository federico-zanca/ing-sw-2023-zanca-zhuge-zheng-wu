package it.polimi.ingsw.distributed;

import it.polimi.ingsw.model.exceptions.ClientAlreadyInLobbyException;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.LobbyNotFoundException;
import it.polimi.ingsw.network.message.connectionmessage.ConnectedToServerMessage;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.connectionmessage.ConnectionMessage;
import it.polimi.ingsw.network.message.gamemessage.GameMessage;
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

    public ArrayList<Lobby> getLobbies() {
        return lobbies;
    }

    public boolean addLobby(Lobby lobby) {
        for(Lobby l : lobbies)
            if(l.getName().equals(lobby.getName()))
                return false;
        lobbies.add(lobby);
        connectedClients.get(lobby.getAdmin()).setLobby(lobby);
        connectedClients.get(lobby.getAdmin()).setClientState(ClientState.IN_A_LOBBY);
        return true;
    }

    public HashMap<String, Map.Entry<Integer, Integer>> getLobbiesInfo() {
        HashMap <String, Map.Entry<Integer, Integer>> availableLobbies = new HashMap<>();
        for(Lobby l : lobbies)
            availableLobbies.put(l.getName(), new AbstractMap.SimpleEntry<>(l.getInLobbyClients().size(), l.getMaxNumClients()));
        return availableLobbies;
    }

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

    public boolean isUsernameAvailable(String username) {
        for(ClientInfo c : connectedClients.values())
            if(c.getClientID().equals(username))
                return false;
        return true;
    }

    public void setUsername(Client client, String username) {
        connectedClients.get(client).setClientID(username);
    }

    public Lobby getLobbyByName(String lobbyName) {
        for(Lobby l : lobbies)
            if(l.getName().equals(lobbyName))
                return l;
        return null;
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

    public void removeClientFromLobby(Client client) {
        connectedClients.get(client).getLobby().removeClient(client);
        connectedClients.get(client).setLobby(null);
        connectedClients.get(client).setClientState(ClientState.IN_SERVER);
    }

    public String getLobbyNameOfClient(Client client) {
        return getLobbyOfClient(client).getName();
    }

    public Lobby getLobbyOfClient(Client client) {
        return connectedClients.get(client).getLobby();
    }

    public void startGame(Client client) {
        if(connectedClients.get(client).getLobby().getAdmin().equals(client)
                && connectedClients.get(client).getLobby().getInLobbyClients().size() == connectedClients.get(client).getLobby().getMaxNumClients()) {
            connectedClients.get(client).getLobby().startGame();
        }
    }

    public void changeNumOfPlayers(Client client, int chosenNum){
        if(connectedClients.get(client).getLobby().getAdmin().equals(client) && chosenNum != connectedClients.get(client).getLobby().getMaxNumClients()){
            connectedClients.get(client).getLobby().changeMaxNumClients(chosenNum);
        }
    }

    public ClientInfo getConnectedClientInfo(Client client) {
        return connectedClients.get(client);
    }
}
