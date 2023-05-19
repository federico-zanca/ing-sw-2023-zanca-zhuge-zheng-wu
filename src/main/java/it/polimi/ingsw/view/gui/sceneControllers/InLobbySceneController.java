package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.MessageHandler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class InLobbySceneController implements Controller{
    private MessageHandler messageHandler;
    private GUI gui;
    @FXML
    private Label lobbyName;
    @FXML
    private VBox playerNames;

    @FXML
    private Spinner<Integer> numPlayersSpinner;

    SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(2,4,2);

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
            numPlayersSpinner.setValueFactory(svf);
            playerNames.setStyle("-fx-alignment: top-left");
        }
    }
    public void startGame(){
        //TODO START GAME, FINISH ALL OTHER THINGS BEFORE THIS ONE!!
    }
    public void exitLobby(){
        //TODO EXIT FROM LOBBY TO SERVER
    }
    public void setPlayerNames(ArrayList<String> allPlayerNames){
        Platform.runLater(()->{
            playerNames.getChildren().clear();
            for (int i = 0; i < allPlayerNames.size(); i++) {
                Label label = new Label(allPlayerNames.get(i));
                if (i == 0) {
                    label.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
                }
                playerNames.getChildren().add(label);
            }
        });
    }
    public void setAdminName(String username){
        Platform.runLater(()->{
            playerNames.getChildren().clear();
            Label label = new Label(username);
            label.setStyle("-fx-font-weight: regular;-fx-font-style: italic; -fx-text-fill: #000000; -fx-font-size: 18; -fx-text-alignment: left; -fx-pref-width: 600;");
            playerNames.getChildren().add(label);
        });
    }
}
