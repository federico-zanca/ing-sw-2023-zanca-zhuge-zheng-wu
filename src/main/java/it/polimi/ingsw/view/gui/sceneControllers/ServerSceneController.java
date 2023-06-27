package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.network.message.connectionmessage.CreateLobbyRequest;
import it.polimi.ingsw.network.message.connectionmessage.JoinLobbyRequest;
import it.polimi.ingsw.network.message.connectionmessage.LobbyListRequest;
import it.polimi.ingsw.network.message.connectionmessage.UsernameRequest;
import it.polimi.ingsw.view.InputValidator;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.MessageHandler;
import it.polimi.ingsw.view.tui.LobbyDisplayInfo;
import it.polimi.ingsw.view.tui.TextColor;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;

public class ServerSceneController implements Controller{
    private MessageHandler messageHandler;
    private GUI gui;
    private InputValidator inputValidator = new InputValidator();
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
    private ListView<String> lobbyListView;
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
        if(gui == null){
            return;
        }else{
            setError(null);
            newLobbyName.clear();
            gui.getCurrentStage().setOnCloseRequest(e->{
                System.exit(0);
            });
        }
    }
    public void exitButton(){
        System.exit(0);
    }
    public void lobbyList(){
        messageHandler.notifyObservers(new LobbyListRequest());
    }
    public void showLobbies(ArrayList<LobbyDisplayInfo> lobbies){
        ArrayList<String> lobbyNames = new ArrayList<>();
        for (LobbyDisplayInfo lobby : lobbies) {
            String lobbyNameWithStatus = lobby.getLobbyName() + " (" + lobby.getNumPlayers() + "/" + lobby.getNumPlayersChosen() + ") " + "Stato: " + lobby.isGameStarted();
            lobbyNames.add(lobbyNameWithStatus);
        }
        lobbyListView.setItems(FXCollections.observableList(lobbyNames));
    }
    public void joinLobby(){
        if(lobbyListView.getSelectionModel().getSelectedItem() != null){
            String myLobby = lobbyListView.getSelectionModel().getSelectedItem().trim();
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