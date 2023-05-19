package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.network.message.lobbymessage.ExitLobbyRequest;
import it.polimi.ingsw.network.message.lobbymessage.StartGameRequest;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.MessageHandler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.Objects;

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
        messageHandler.notifyObservers(new StartGameRequest());
        checkForErrors();
    }

    private void checkForErrors() {
        Platform.runLater(()->{
            if(Objects.equals(gui.getError(), "Non sei l'admin di questa lobby! Solo l'admin può usare questo comando.")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(gui.getError());
                alert.showAndWait();
            }else if(Objects.equals(gui.getError(),"Non ci sono le condizioni per iniziare la partita!")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(gui.getError());
                alert.showAndWait();
            }
            gui.setError("");
        });
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
            label.setStyle("-fx-font-weight: regular;-fx-font-style: italic; -fx-text-fill: #000000; -fx-font-size: 18; -fx-text-alignment: left; -fx-pref-width: 600;-fx-pref-height: 40;");
            playerNames.getChildren().add(label);
        });
    }
}
