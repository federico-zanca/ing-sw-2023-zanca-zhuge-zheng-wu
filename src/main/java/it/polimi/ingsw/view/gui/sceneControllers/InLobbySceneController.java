package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.network.message.lobbymessage.ChangeNumOfPlayersRequest;
import it.polimi.ingsw.network.message.lobbymessage.ExitLobbyRequest;
import it.polimi.ingsw.network.message.lobbymessage.StartGameRequest;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.MessageHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
            gui.getCurrentStage().setOnCloseRequest(e->{
                System.exit(0);
            });
            String lobbyN = messageHandler.getMyLobby();
            lobbyName.setText(lobbyN);
            SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(2,4,2);
            numPlayersSpinner.setValueFactory(svf);
            playerNames.setStyle("-fx-alignment: top-left");
            for (Node node : numPlayersSpinner.lookupAll(".repeat-buttons .increment-button, .repeat-buttons .decrement-button")) {
                Button btn = (Button) node;
                int step = btn.getStyleClass().contains("increment-button") ? +1 : -1;

                btn.setOnAction(evt -> {
                    evt.consume();
                    handleNumPlayersChange(numPlayersSpinner.getValue() + step);
                });
            }
            numPlayersSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                handleNumPlayersChange(newValue);
            });
        }
    }

    private void handleNumPlayersChange(Integer newValue) {
        messageHandler.notifyObservers(new ChangeNumOfPlayersRequest(newValue));
        Platform.runLater(() -> {
            String error = gui.getError();
            if (!error.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, error).showAndWait();
                gui.setError("");
            } else {
                numPlayersSpinner.getValueFactory().setValue(newValue);
            }
        });
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
