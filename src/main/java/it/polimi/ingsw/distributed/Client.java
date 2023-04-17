package it.polimi.ingsw.distributed;

import it.polimi.ingsw.network.message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    void update(Message message) throws RemoteException;
}
