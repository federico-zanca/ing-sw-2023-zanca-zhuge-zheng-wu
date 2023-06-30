package it.polimi.ingsw.distributed;

import it.polimi.ingsw.network.message.HeartBeatMessage;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.view.gui.MessageHandler;
import it.polimi.ingsw.view.tui.TextualUI;

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

    /**
     * Constructs a new instance of the ClientImpl class with the specified {@code server} and {@code messageHandler}.
     *
     * @param server the server to connect to
     * @param messageHandler the message handler for the client
     * @throws RemoteException if a communication-related exception occurs
     */
    public ClientImpl(Server server, MessageHandler messageHandler) throws RemoteException {
        super();
        this.view = messageHandler;
        //view = new MessageHandler();
        this.server = server;
        initialize(server);
    }

    /**
     * Constructs a new instance of the {@code ClientImpl} class with the specified {@code server}.
     *
     * @param server the server to connect to
     * @throws RemoteException if a communication-related exception occurs
     */
    public ClientImpl(Server server) throws RemoteException {
        super();
        this.view = new TextualUI();
        //view = new MessageHandler();
        this.server = server;
        initialize(server);
    }

    /**
     * Constructs a new instance of the {@code ClientImpl} class with the specified {@code server} and {@code port}.
     *
     * @param server the server to connect to
     * @param port   the port to use for communication
     * @throws RemoteException if a communication-related exception occurs
     */
    public ClientImpl(Server server, int port) throws RemoteException {
        super(port);
        view = new TextualUI();
        this.server = server;
        initialize(server);
    }

    /**
     * Constructs a new instance of the {@code ClientImpl} class with the specified {@code server}, {@code port}, {@code csf}, and {@code ssf}.
     *
     * @param server the server to connect to
     * @param port   the port to use for communication
     * @param csf    the client-side socket factory
     * @param ssf    the server-side socket factory
     * @throws RemoteException if a communication-related exception occurs
     */
    public ClientImpl(Server server, int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
        view = new TextualUI();
        this.server = server;
        initialize(server);
    }

    /**
     * Initializes the client by adding an observer to the view and registering the client with the server.
     *
     * @param server the server to connect to
     * @throws RemoteException if a communication-related exception occurs
     */
    private void initialize(Server server) throws RemoteException {
        view.addObserver((arg) -> {
            try {
                server.update(this, arg);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        server.register(this);

        keepAlive();
    }

    /**
     * Sends a heartbeat message to the server at regular intervals to keep the connection alive.
     * This helps in maintaining the connection and detecting any network issues.
     */
    private void keepAlive() {
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

    @Override
    public void update(Message message) {
        view.update(message);
    }
    @Override
    public void run() {
        view.run();
    }


}

