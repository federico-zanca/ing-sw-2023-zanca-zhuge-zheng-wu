package it.polimi.ingsw.distributed;

import it.polimi.ingsw.network.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    void update(Message message) throws RemoteException;

    /**
     * Sends a ping message to client
     *
     * @throws RemoteException in case of problems with communication with client
     */
    void ping() throws RemoteException;
}
