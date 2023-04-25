package it.polimi.ingsw.distributed;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.PingMessage;
import it.polimi.ingsw.view.TextualUI;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl extends UnicastRemoteObject implements Client, Runnable{

    private final Server server;
    private TextualUI view;

    public ClientImpl(Server server) throws RemoteException {
        super();
        view = new TextualUI();
        this.server = server;
        initialize(server);
    }

    public ClientImpl(Server server, int port) throws RemoteException {
        super(port);
        view = new TextualUI();
        this.server = server;
        initialize(server);
    }

    public ClientImpl(Server server, int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
        view = new TextualUI();
        this.server = server;
        initialize(server);
    }

    private void initialize(Server server) throws RemoteException {
        view.addObserver((arg) -> {
            try {
                server.update(this, arg);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        server.register(this);

    }

    @Override
    public void update(Message message) {
        view.update(message);
    }
    @Override
    public void run() {
        view.run();
    }

    @Override
    public void ping(){
        //TODO togli system.err quando non serve più
        try {
            server.update(this, new PingMessage());
        } catch (RemoteException e) {
            System.err.println("Megaerrore pinging");
            throw new RuntimeException(e);
        }
    }
}

