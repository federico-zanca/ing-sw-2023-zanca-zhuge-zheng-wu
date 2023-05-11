package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.network.message.connectionmessage.LoginRequest;
import it.polimi.ingsw.view.gui.JavaFXApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MenuSceneController implements Controller{
    private JavaFXApp app;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button play;
    @FXML
    private Button quit;

    @FXML
    private TextField usernameField;

    @Override
    public void setApp(JavaFXApp app) {
        this.app = app;
    }

    @Override
    public void initialize() {
        play.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> System.out.println("Not implemented yet"));
        quit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> System.exit(0));
    }
}

