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

public class ClientSkeleton implements Client {

    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;

    public ClientSkeleton(Socket socket) throws RemoteException {
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
    }

    @Override
    public void update(Message message) throws RemoteException {
        try {
            oos.writeObject(message);
            oos.reset();
        } catch (IOException e) {
            throw new RemoteException("Cannot send message: " + e.getMessage());
        }
    }

    public void receive(Server server) throws RemoteException {
        Message m;
        try {
            m = (Message) ois.readObject();
        } catch (IOException e) {
            throw new RemoteException("Cannot receive message from client: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot deserialize message from client: " + e.getMessage());
        }
        //se ricevo correttamente l'oggetto...
        server.update(this, m);
    }
}
