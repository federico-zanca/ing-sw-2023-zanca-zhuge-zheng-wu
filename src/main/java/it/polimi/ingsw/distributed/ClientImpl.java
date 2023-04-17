package it.polimi.ingsw.distributed;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.view.TextualUI;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl extends UnicastRemoteObject implements Client, Runnable{

    TextualUI view = new TextualUI();

    public ClientImpl(Server server) throws RemoteException {
        super();

        initialize(server);
    }

    public ClientImpl(Server server, int port) throws RemoteException {
        super(port);

        initialize(server);
    }

    public ClientImpl(Server server, int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);

        initialize(server);
    }

    private void initialize(Server server) throws RemoteException {
        server.register(this);

        view.addObserver((arg) -> {
            try {
                server.update(this, arg);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void update(Message message) {
        view.update(message);
    }
    @Override
    public void run() {
        view.run();
    }
}

