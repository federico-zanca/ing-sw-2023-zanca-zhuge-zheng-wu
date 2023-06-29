package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.network.message.ChatMessage;
import it.polimi.ingsw.network.message.lobbymessage.ChangeNumOfPlayersRequest;
import it.polimi.ingsw.network.message.lobbymessage.ExitLobbyRequest;
import it.polimi.ingsw.network.message.lobbymessage.StartGameRequest;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.MessageHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class InLobbySceneController implements Controller{
    public Button startGameButton;
    public Button exitLobbyButton;
    public ImageView backGround;
    public HBox titoli;
    private MessageHandler messageHandler;
    private GUI gui;
    @FXML
    private TextArea chat;
    @FXML
    private TextField inputField;
    @FXML
    private Label lobbyName;
    @FXML
    private VBox playerNames;
    @FXML
    private Spinner<Integer> numPlayersSpinner;
    @FXML
    private Text error;
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
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = primaryScreenBounds.getWidth();
        lobbyName.setPrefWidth(213);
        backGround.setFitWidth(screenWidth);
        if(gui == null){
            return;
        }else{
            setError(null);
            String lobbyN = messageHandler.getMyLobby();
            lobbyName.setText(lobbyN);
            SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(2,4,2);
            numPlayersSpinner.setValueFactory(svf);
            playerNames.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7);");
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
            setChatBox(messageHandler.getChatLog());
        }
    }
    private void setChatBox(ArrayList<ChatMessage> chatLog) {
        inputField.clear();
        chat.clear();
        chat.setWrapText(true);
        for(ChatMessage message:chatLog) {
            chat.appendText(message.getContent());
        }
        inputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String message = inputField.getText();
                message = message.trim();
                String recipientusername = null;
                if(message.isEmpty()) {
                    return;
                }
                if(message.startsWith("@")) {
                    if (!message.contains(" ")) {
                        return;
                    }
                    recipientusername = message.substring(1, message.indexOf(" "));
                    message = message.substring(message.indexOf(" ") + 1);
                }
                sendMessage(message,recipientusername);
                inputField.clear();
            }
        });
    }
    public void setChat(ChatMessage message){
        String prefix = "";
        String messageContent = message.getContent();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        if (message.getReceiver() != null) {
            if(Objects.equals(message.getReceiver(), messageHandler.getMyUsername())){
                prefix = "["+formattedDateTime + "] " + "PRIVATE MESSAGE FROM ";
                messageContent = message.getSender() + ": " + messageContent;

            }else{
                prefix = "["+formattedDateTime + "] " + "PRIVATE MESSAGE TO ";
                messageContent = message.getReceiver() + ": " + messageContent;
            }
        } else{
            messageContent = "["+formattedDateTime + "] " + message.getSender() + ": " + messageContent;
        }
        chat.appendText(prefix+messageContent+"\n");
    }
    public void sendMessage(String message, String recipientusername){
        ChatMessage chatMessage = new ChatMessage(message, recipientusername);
        messageHandler.notifyObservers(chatMessage);
    }
    private void handleNumPlayersChange(Integer newValue) {
        messageHandler.notifyObservers(new ChangeNumOfPlayersRequest(newValue));
    }
    public void setSpinnerValue(Integer value){
        numPlayersSpinner.getValueFactory().setValue(value);
        error.setVisible(false);
    }
    public void setSpinner(){
        numPlayersSpinner.setDisable(true);
    }
    public void startGame(){
        messageHandler.notifyObservers(new StartGameRequest());
    }
    public void setError(String error) {
        /*this.error.setText(error);
        this.error.setVisible(true);*/
        chat.appendText(error+"\n");
    }
    public void exitLobby(){
        messageHandler.notifyObservers(new ExitLobbyRequest());
    }
    public void setPlayerNames(ArrayList<String> allPlayerNames){
        playerNames.getChildren().clear();
        for (int i = 0; i < allPlayerNames.size(); i++) {
            Label label = new Label(allPlayerNames.get(i));
            if (i == 0) {
                label.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            }
            else {
                label.setStyle("-fx-text-fill: black;");
            }
            label.setFont(Font.font("System", FontWeight.NORMAL, 18));
            playerNames.getChildren().add(label);
        }
    }
    public void setAdminName(String username){
        playerNames.getChildren().clear();
        Label label = new Label(username);
        label.setStyle("-fx-font-weight: normal;-fx-font-style: italic; -fx-text-fill: #000000; -fx-font-size: 18; -fx-text-alignment: left; -fx-pref-width: 600;-fx-pref-height: 40;");
        playerNames.getChildren().add(label);
    }
}
