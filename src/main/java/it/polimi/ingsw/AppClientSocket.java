package it.polimi.ingsw;

import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.distributed.socket.middleware.ServerStub;

import java.rmi.RemoteException;

public class AppClientSocket {

    public static void main(String[] args) throws RemoteException {
        System.out.println(
                "███╗   ███╗██╗   ██╗███████╗██╗  ██╗███████╗██╗     ███████╗██╗███████╗\n" +
                "████╗ ████║╚██╗ ██╔╝██╔════╝██║  ██║██╔════╝██║     ██╔════╝██║██╔════╝\n" +
                "██╔████╔██║ ╚████╔╝ ███████╗███████║█████╗  ██║     █████╗  ██║█████╗  \n" +
                "██║╚██╔╝██║  ╚██╔╝  ╚════██║██╔══██║██╔══╝  ██║     ██╔══╝  ██║██╔══╝  \n" +
                "██║ ╚═╝ ██║   ██║   ███████║██║  ██║███████╗███████╗██║     ██║███████╗\n" +
                "╚═╝     ╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝╚══════╝╚═╝     ╚═╝╚══════╝\n" +
                "                                                                       \n" +
                "\n");
        //ServerStub serverStub = new ServerStub("192.168.1.87", 1234);
        ServerStub serverStub = new ServerStub("172.20.10.10", 1234);

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
