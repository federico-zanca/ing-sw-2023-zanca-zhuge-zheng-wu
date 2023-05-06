package it.polimi.ingsw.distributed;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.HeartBeatMessage;
import it.polimi.ingsw.view.TextualUI;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.TimerTask;

public class ClientImpl extends UnicastRemoteObject implements Client, Runnable{

    private static final int HEARTBEAT_INTERVAL = 5000;
    private final Server server;
    private VirtualView view;

    public ClientImpl(Server server) throws RemoteException {
        super();
        view = new TextualUI();
        this.server = server;
        initialize(server);

        //TODO spostare in un thread/metodo a parte e farlo partire solo quando il client è connesso
        Timer heartbeatTimer = new Timer();
        heartbeatTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    server.update(ClientImpl.this, new HeartBeatMessage());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0 , HEARTBEAT_INTERVAL);
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


}

