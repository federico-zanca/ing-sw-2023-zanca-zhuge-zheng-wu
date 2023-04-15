package it.polimi.ingsw;

import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.ServerImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AppServerRMI {
    public static void main(String[] args) throws RemoteException {
        Server server = new ServerImpl();

        Registry registry = LocateRegistry.getRegistry();
        registry.rebind("server", server);
    }
}
