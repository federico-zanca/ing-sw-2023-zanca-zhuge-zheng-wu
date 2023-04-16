package it.polimi.ingsw;

import it.polimi.ingsw.distributed.Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AppServer extends Remote {
    Server connect() throws RemoteException;
}
