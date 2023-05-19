package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.network.message.connectionmessage.*;
import it.polimi.ingsw.view.InputValidator;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.MessageHandler;
import it.polimi.ingsw.view.tui.LobbyDisplayInfo;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Optional;

public class ServerSceneController implements Controller{
    private MessageHandler messageHandler;
    private GUI gui;
    private ArrayList<LobbyDisplayInfo> lobbies;
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
    @Override
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }
    @Override
    public void setGui(GUI gui){this.gui = gui;}
    @Override
    public void initialize() {
    }
    public void exitButton(){
        System.exit(0);
    }
    public void lobbyList(){
        messageHandler.notifyObservers(new LobbyListRequest());
        Platform.runLater(this::showLobbies);
    }
    public void showLobbies(){
        lobbies = messageHandler.getLobbies();
        ArrayList<String> lobbyNames = new ArrayList<>();
        for (LobbyDisplayInfo lobby : lobbies) {
            lobbyNames.add(lobby.getLobbyName());
        }
        lobbySelectBox.setItems(FXCollections.observableList(lobbyNames));
    }

    public void joinLobby(){
        messageHandler.setMyLobby(lobbySelectBox.getValue());
        messageHandler.notifyObservers(new JoinLobbyRequest(lobbySelectBox.getValue()));
    }
    public void createLobby(){
        messageHandler.setMyLobby(newLobbyName.getText());
        messageHandler.notifyObservers(new CreateLobbyRequest(newLobbyName.getText()));
    }
    public void changeName(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Username");
        dialog.setHeaderText("Enter new username:");
        Optional<String> result = dialog.showAndWait();

        // Check if the user entered input; if yes, send the request to the server with the new username
        result.ifPresent(newName -> {
            if (inputValidator.isValidUsername(newName)) {
                // Send the request to change the username
                messageHandler.notifyObservers(new UsernameRequest(newName));
                // Show the successful message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Name changed successfully.");
                alert.showAndWait();
            } else {
                // Show an error message if the input is invalid
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setContentText("Username must be between 1-12 characters long and contain only letters, numbers, or underscores.");
                alert.showAndWait();
            }
        });
    }
}