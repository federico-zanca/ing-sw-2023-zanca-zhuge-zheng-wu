package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.AppServer;
import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.distributed.socket.middleware.ServerStub;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.MessageHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ConnectionController implements Controller{

    private MessageHandler messageHandler;
    private GUI gui;
    @FXML
    private Button RMIButton;

    @FXML
    private Button SocketButton;

    @Override
    public void setMessageHandler(MessageHandler messageHandler) {this.messageHandler = messageHandler;}

    @Override
    public void setGui(GUI gui) {this.gui = gui;}

    @Override
    public void initialize() {

    }

    @FXML
    void connectRMI(ActionEvent event) {
        try{
            Registry registry = LocateRegistry.getRegistry(1099);
            AppServer server = (AppServer) registry.lookup("server");
            ClientImpl client = new ClientImpl(server.connect(), messageHandler);
        }catch(RemoteException | NotBoundException e){
            e.printStackTrace();
        }
    }

    @FXML
    void connectSocket(ActionEvent event) throws RemoteException {
        ServerStub serverStub = new ServerStub("localhost", 1234);
        ClientImpl client = new ClientImpl(serverStub,messageHandler);
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
}
