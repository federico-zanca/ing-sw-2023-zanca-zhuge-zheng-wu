package it.polimi.ingsw.network.message;

import it.polimi.ingsw.controller.PreGameController;
import it.polimi.ingsw.distributed.Client;

import java.rmi.RemoteException;

/**
 * The interface representing a message sent to server.
 */
public interface MsgToServer {

    void execute(Client client, PreGameController preGameController) throws RemoteException;
}
