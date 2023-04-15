package it.polimi.ingsw;

import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.ServerImpl;
import it.polimi.ingsw.distributed.socket.middleware.ClientSkeleton;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;

public class AppServerSocket {
    public static void main(String[] args) throws RemoteException {
        //un server dovrà esporre un qualcosa che ascolti le connessioni in entrata, ovvero un SocketServer
        try(ServerSocket serverSocket = new ServerSocket(1234)) {
            while (true) {
                try(Socket socket = serverSocket.accept()) {
                    //ogni volta che un client si connette, il server dovrà creare un ClientSkeleton passandogli il socket (usato per connettersi al client)
                    ClientSkeleton clientSkeleton = new ClientSkeleton(socket);
                    //genero il server
                    Server server = new ServerImpl();
                    server.register(clientSkeleton);
                    while(true) {
                        //aspetto che il client mi invii un messaggio
                        clientSkeleton.receive(server);
                    }
                }catch (IOException e) {
                    System.err.println("Socket failed: " + e.getMessage() + ". Closing connection and waiting for a new one...");
                }
            }
        }catch(IOException e) {
            throw new RemoteException("Error while creating server socket: " + e);
        }
    }
}

