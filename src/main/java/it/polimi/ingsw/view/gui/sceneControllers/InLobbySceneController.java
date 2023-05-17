package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.network.message.lobbymessage.ExitLobbyRequest;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.MessageHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;

public class InLobbySceneController implements Controller{
    private MessageHandler messageHandler;
    private GUI gui;
    @FXML
    private Label lobbyName;
    @FXML
    private VBox playerNames;
    @FXML
    private Button startGameButton;
    @FXML
    private Button exitLobbyButton;
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
            String lobbyN = messageHandler.getMyLobby();
            lobbyName.setText(lobbyN);
        }
    }
    public void startGame(){
        //TODO START GAME, FINISH ALL OTHER THINGS BEFORE THIS ONE!!
    }
    public void exitLobby(){
        messageHandler.notifyObservers(new ExitLobbyRequest());
    }
    public void setPlayerNames(ArrayList<String> allPlayerNames){
        Platform.runLater(()->{
            playerNames.getChildren().clear();
            for (int i = 0; i < allPlayerNames.size(); i++) {
                Label label = new Label(allPlayerNames.get(i));
                if (i == 0) {
                    label.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
                }
                else {
                    label.setStyle("-fx-text-fill: yellow;");
                }
                label.setFont(Font.font("System", FontWeight.NORMAL, 18));
                playerNames.getChildren().add(label);
            }

        });
    }
    public void setAdminName(String username){
        Platform.runLater(()->{
            playerNames.getChildren().clear();
            Label label = new Label(username);
            label.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            playerNames.getChildren().add(label);
        });
    }
}
