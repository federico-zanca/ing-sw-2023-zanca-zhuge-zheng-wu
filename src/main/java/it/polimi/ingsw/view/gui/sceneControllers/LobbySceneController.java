package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.network.message.connectionmessage.CreateLobbyRequest;
import it.polimi.ingsw.network.message.connectionmessage.JoinLobbyRequest;
import it.polimi.ingsw.network.message.connectionmessage.LobbyListRequest;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.JavaFXApp;
import it.polimi.ingsw.view.tui.LobbyDisplayInfo;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class LobbySceneController implements Controller{
    private Gui gui;
    private JavaFXApp app;
    private ArrayList<LobbyDisplayInfo> lobbies;
    @FXML
    private TextField LobbyName;
    @FXML
    private Button Unisciti;
    @FXML
    private Button CambiaUsername;
    @FXML
    private Button Esci;
    @FXML
    private Button Lobbies;
    @FXML
    private Button CreateGame;
    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }
    @Override
    public void setApp(JavaFXApp app){this.app = app;}
    @Override
    public void initialize() {
    }
    public void exitButton(){
        System.exit(0);
    }
    public void lobbyList(){
        gui.notifyObservers(new LobbyListRequest());
        lobbies = gui.getLobbies();
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
        //gui.notifyObservers(new JoinLobbyRequest(LobbyName.getText()));
        //TODO INLOBBYSCENE
    }
    public void createLobby(){
        //gui.notifyObservers(new CreateLobbyRequest(LobbyName.getText()));
        //TODO INLOBBYSCENE
    }
    public void changeName(){
        //TODO POP UP WINDOW TO CHANGE USERNAME, AFTERWARDS IT RETURNS TO THE LOBBYSCENE.
    }
}