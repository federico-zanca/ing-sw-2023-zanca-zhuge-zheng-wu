package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.network.message.connectionmessage.*;
import it.polimi.ingsw.view.InputValidator;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.MessageHandler;
import it.polimi.ingsw.view.tui.LobbyDisplayInfo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("List of Lobbies");
        dialog.setHeaderText("Available Lobbies:");

        // Create a VBox to display lobby information
        VBox vBox = new VBox();
        for (LobbyDisplayInfo lobby : lobbies) {
            Text lobbyName = new Text(lobby.getLobbyName());
            Text playersNum = new Text(lobby.getNumPlayers() + " out of " + lobby.getNumPlayersChosen() + " players");
            vBox.getChildren().addAll(lobbyName, playersNum);
        }

        // Set the content of the dialog box to be the VBox
        dialog.getDialogPane().setContent(vBox);

        // Add a close button to the dialog box
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);

        // Display the dialog box
        dialog.showAndWait();
        //TODO move the creation of these elements(vbox and dialog in the fxml file)
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