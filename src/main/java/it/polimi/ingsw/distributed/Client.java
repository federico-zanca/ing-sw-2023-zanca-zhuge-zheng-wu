package it.polimi.ingsw.distributed;

import it.polimi.ingsw.network.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Represents a client interface for receiving updates.
 */
public interface Client extends Remote {
    /**
     * Updates the client with a new message.
     *
     * @param message the message to be sent to the client
     * @throws RemoteException if a communication-related exception occurs
     */
    void update(Message message) throws RemoteException;


}
