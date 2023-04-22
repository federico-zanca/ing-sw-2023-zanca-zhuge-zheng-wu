package it.polimi.ingsw;

import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.distributed.socket.middleware.ServerStub;

import java.rmi.RemoteException;

public class DifferentClient {

    public static void main(String[] args) throws RemoteException {

        ServerStub serverStub = new ServerStub("localhost", 1234);

        ClientImpl client = new ClientImpl(serverStub);

        new Thread(){
            @Override
            public void run() {
                while(true) {
                    try {
                        serverStub.receive(client);
                    } catch (RemoteException e) {
                        System.err.println("Error while receiving message: " + e);
                        //Close socket
                        try {
                            serverStub.close();
                        } catch (RemoteException ex) {
                            System.err.println("Cannot close connection with server. Halting... " + ex);
                        }
                        //Chiudi l'app
                        System.exit(1);
                    }
                }
            }
        }.start();

        client.run();
    }

    //Problema sulla view, avendo più thread le cose non arrivano per forza in ordine
}
