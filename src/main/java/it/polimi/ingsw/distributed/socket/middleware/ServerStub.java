package it.polimi.ingsw.distributed.socket.middleware;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.network.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
/*
    * Questo server stub è quello che lato client mi permette di comunicare con il server
 */
public class ServerStub implements Server {

    String ip;
    int port;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public ServerStub(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void register(Client client) throws RemoteException {
        try {
            this.socket = new Socket(ip, port);
            //costruisco i due stream per leggere e scrivere dal socket
            try {
                this.oos = new ObjectOutputStream(socket.getOutputStream()); //importante aprire prima l'output stream! evito possibili deadlock
            } catch (IOException e) {
                throw new RemoteException("Cannot create output stream: " + e.getMessage());
            }
            try {
                this.ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                throw new RemoteException("Cannot create input stream: " + e.getMessage());
            }
        } catch (IOException e) {
            throw new RemoteException("Error while connecting to the server: " + e.getMessage());
        }
    }

    @Override
    public void update(Client client, Message message) throws RemoteException {
        //fare l'update significa mandare al server il messaggio
        try {
            oos.writeObject(message);
            oos.reset();
        } catch (IOException e) {
            throw new RemoteException("Cannot send message: " + e.getMessage());
        }
    }

    public void receive(Client client) throws RemoteException {
        GameView o;
        try {
            o = (GameView) ois.readObject();
        } catch (IOException e) {
            throw new RemoteException("Cannot receive game view: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot cast game view: " + e.getMessage());
        }

        Message m;
        try {
            m = (Message) ois.readObject();
            //ois.reset();
        } catch (IOException e) {
            throw new RemoteException("Cannot receive message: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot cast message: " + e.getMessage());
        }
        client.update(o, m);
    }

    public void close() throws RemoteException {
        try{
            socket.close();
        } catch (IOException e) {
            throw new RemoteException("Cannot close socket: " + e);
        }
    }
}
