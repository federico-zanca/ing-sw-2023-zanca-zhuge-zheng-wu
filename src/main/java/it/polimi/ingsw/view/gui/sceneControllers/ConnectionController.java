package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.MessageHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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

    }

    @FXML
    void connectSocket(ActionEvent event) {

    }
}
