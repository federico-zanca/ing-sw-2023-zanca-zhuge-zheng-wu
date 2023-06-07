package it.polimi.ingsw.distributed;

import it.polimi.ingsw.network.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Represents a server interface for handling client registrations and updates.
 */
public interface Server extends Remote {
    /**
     * Registers a client with the server.
     *
     * @param client the client to be registered
     * @throws RemoteException if a communication-related exception occurs
     */
    void register(Client client) throws RemoteException;

    /**
     * Updates a client with a new message.
     *
     * @param client  the client to be updated
     * @param message the message to be sent to the client
     * @throws RemoteException if a communication-related exception occurs
     */
    void update(Client client, Message message) throws RemoteException;
}
