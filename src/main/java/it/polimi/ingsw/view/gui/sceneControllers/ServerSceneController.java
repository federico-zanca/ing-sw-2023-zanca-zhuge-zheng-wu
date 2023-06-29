package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.network.message.connectionmessage.CreateLobbyRequest;
import it.polimi.ingsw.network.message.connectionmessage.JoinLobbyRequest;
import it.polimi.ingsw.network.message.connectionmessage.LobbyListRequest;
import it.polimi.ingsw.network.message.connectionmessage.UsernameRequest;
import it.polimi.ingsw.view.InputValidator;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.MessageHandler;
import it.polimi.ingsw.view.tui.LobbyDisplayInfo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Optional;

public class ServerSceneController implements Controller{
    public Button refresh;
    public ImageView backGround;
    private MessageHandler messageHandler;
    private GUI gui;
    private InputValidator inputValidator = new InputValidator();
    private ArrayList<String> lobbyNames = new ArrayList<>();
    @FXML
    private TextField newLobbyName;
    @FXML
    private ComboBox<String> lobbySelectBox;
    @FXML
    private Button Unisciti;
    @FXML
    private Button CambiaUsername;
    @FXML
    private Button Esci;
    @FXML
    private Button CreateGame;
    @FXML
    private Text error;
    @FXML
    private ListView<HBox> lobbyListView;
    @FXML
    void enterCreateLobby(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            createLobby();
        }
    }
    @Override
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }
    @Override
    public void setGui(GUI gui){this.gui = gui;}
    @Override
    public void initialize() {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = primaryScreenBounds.getWidth();
        backGround.setFitWidth(screenWidth);
        if(gui == null){
            return;
        }else{
            setError(null);
            lobbyNames = new ArrayList<>();
            newLobbyName.clear();
        }
    }
    public void exitButton(){
        System.exit(0);
    }
    public void lobbyList(){
        messageHandler.notifyObservers(new LobbyListRequest());
    }
    public void showLobbies(ArrayList<LobbyDisplayInfo> lobbies) {
        ArrayList<HBox> updatedLobbyNames = new ArrayList<>(); // Create a copy of the existing lobbyNames

        for (LobbyDisplayInfo lobby : lobbies) {

            HBox hbox=createHBox(lobby.getLobbyName(), Integer.toString(lobby.getNumPlayers())+"/"+lobby.getNumPlayersChosen(), lobby.isGameStarted());

            if(!lobbyNames.contains(lobby.getLobbyName())){
                updatedLobbyNames.add(hbox); // Add new lobby to the updatedLobbyNames
            }
        }

        lobbyListView.setItems(FXCollections.observableList(updatedLobbyNames)); // Set the updated lobbyNames to the lobbyListView
    }

    private HBox createHBox(String lobbyName, String numPlayers, String status) {
        HBox hbox = new HBox();
        Label nameLabel=new javafx.scene.control.Label(lobbyName);
        nameLabel.setPrefWidth(276);
        nameLabel.setTextFill(Color.WHITE);

        Label numPlayersLabel=new javafx.scene.control.Label(numPlayers);
        numPlayersLabel.setPrefWidth(276);
        numPlayersLabel.setTextFill(Color.WHITE);
        numPlayersLabel.setAlignment(Pos.CENTER);

        Label statusLabel=new javafx.scene.control.Label(status);
        statusLabel.setPrefWidth(276);
        statusLabel.setTextFill(Color.WHITE);
        statusLabel.setAlignment(Pos.BASELINE_RIGHT);

        hbox.getChildren().addAll(nameLabel, numPlayersLabel, statusLabel);
        return hbox;

    }

    private String extractLobbyNameFromHBox(HBox hbox) {
        String lobbyName = "";

        for (Node node : hbox.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                lobbyName = label.getText();
                break;
            }
        }

        return lobbyName;
    }

    public void joinLobby(){

        HBox selectedHBox = lobbyListView.getSelectionModel().getSelectedItem();
        if(selectedHBox != null){
            String myLobby = extractLobbyNameFromHBox(selectedHBox);
            int index = myLobby.indexOf(" ");
            if (index >= 0) {
                myLobby = myLobby.substring(0, index);
            }
            messageHandler.setMyLobby(myLobby);
            messageHandler.notifyObservers(new JoinLobbyRequest(myLobby));
        }
    }
    public void createLobby(){
        String name = newLobbyName.getText();
        if(name.equals("")){
            setError("La lobby non può avere nome vuoto!");
            return;
        }
        if(inputValidator.isValidUsername(name)){
            messageHandler.setMyLobby(name);
            messageHandler.notifyObservers(new CreateLobbyRequest(name));
        }else{
            setError("La lobby non può avere nome vuoto!");
        }
    }
    public void changeName(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Username");
        dialog.setHeaderText("Enter new username:");
        Optional<String> result = dialog.showAndWait();

        // Check if the user entered input; if yes, send the request to the server with the new username
        result.ifPresent(newName -> {
            if (inputValidator.isValidUsername(newName)) {
                messageHandler.notifyObservers(new UsernameRequest(newName));
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setContentText("Username must be between 1-12 characters long and contain only letters, numbers, or underscores.");
                alert.showAndWait();
            }
        });
    }
    public void setAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText("Name changed successfully.");
        alert.showAndWait();
    }
    public void setError(String error){
        this.error.setText(error);
        this.error.setVisible(true);
    }
}