package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.MessageHandler;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class GameSceneController implements Controller{
    private MessageHandler messageHandler;
    private GUI gui;
    @FXML
    private ImageView firstCommonGoal;
    @FXML
    private ImageView secondCommonGoal;
    @FXML
    private ImageView livingRoomBoard;
    @Override
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }
    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
    @Override
    public void initialize() {
        if(gui == null){
            return;
        }else{
            System.out.println("do something");
        }
    }
}
