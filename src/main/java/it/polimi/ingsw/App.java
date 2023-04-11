package it.polimi.ingsw;

import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.local.ClientImpl;
import it.polimi.ingsw.distributed.local.ServerImpl;
//import it.polimi.ingsw.view.VirtualView;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main(String[] args){
        Server server = new ServerImpl();
        ClientImpl client = new ClientImpl(server);

        client.run();
    }
}
