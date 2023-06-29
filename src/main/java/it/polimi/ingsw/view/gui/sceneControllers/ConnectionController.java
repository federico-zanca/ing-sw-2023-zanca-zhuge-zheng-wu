package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.AppServer;
import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.distributed.socket.middleware.ServerStub;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.MessageHandler;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;



import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ConnectionController implements Controller{

    public AnchorPane root;
    @FXML
    public ImageView backGround;
    public ImageView Title;
    private MessageHandler messageHandler;
    private GUI gui;
    private String ip;
    @FXML
    private Button RMIButton;
    @FXML
    private Button SocketButton;
    @FXML
    private TextField ServerIp;

    @Override
    public void setMessageHandler(MessageHandler messageHandler) {this.messageHandler = messageHandler;}

    @Override
    public void setGui(GUI gui) {this.gui = gui;}

    @Override
    public void initialize() {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = primaryScreenBounds.getWidth();
        backGround.setFitWidth(screenWidth);
    }
    @FXML
    void connectRMI() {
        if(askIp()){
            try{
                Registry registry = LocateRegistry.getRegistry(ip,1099);
                AppServer server = (AppServer) registry.lookup("server");
                ClientImpl client = new ClientImpl(server.connect(), messageHandler);
            }catch(RemoteException | NotBoundException e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    void connectSocket() throws RemoteException {
        if(askIp()){
            ServerStub serverStub = new ServerStub(ip, 1234);
            //ServerStub serverStub = new ServerStub("172.20.10.6", 1234);
            ClientImpl client = new ClientImpl(serverStub,messageHandler);
            new Thread(() -> {
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
            }).start();
            client.run();
        }
    }

    private boolean askIp() {
        ip = ServerIp.getText();
        return ip.matches("localhost") || ip.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$");
    }
}
