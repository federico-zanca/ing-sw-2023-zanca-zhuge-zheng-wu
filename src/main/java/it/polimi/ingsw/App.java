package it.polimi.ingsw;

import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.ServerImpl;

import java.rmi.RemoteException;
//import it.polimi.ingsw.view.VirtualView;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main(String[] args) throws RemoteException {
        Server server = new ServerImpl();

        ClientImpl client = new ClientImpl(server);
        client.run();
    }
}
