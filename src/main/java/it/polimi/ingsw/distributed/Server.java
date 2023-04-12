package it.polimi.ingsw.distributed;

import it.polimi.ingsw.network.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    void registerClient(Client client) throws RemoteException;
    void update(Client client, Message message) throws RemoteException;
}
