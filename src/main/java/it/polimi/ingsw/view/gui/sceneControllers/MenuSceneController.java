package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.network.message.connectionmessage.LoginRequest;
import it.polimi.ingsw.view.InputValidator;
import it.polimi.ingsw.view.gui.GameFxml;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.JavaFXApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class MenuSceneController implements Controller{
    private Gui gui;
    private JavaFXApp app;
    private final InputValidator inputValidator = new InputValidator();
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button play;
    @FXML
    private Button quit;
    @FXML
    private Text error;
    @FXML
    private TextField usernameField;

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }
    @Override
    public void setApp(JavaFXApp app){this.app = app;}

    @Override
    public void initialize() {
        play.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> checkUsername());
        quit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> System.exit(0));
    }
    public void checkUsername(){
        if(!inputValidator.isValidUsername(usernameField.getText())){
            System.out.println("error shows");
            error.setVisible(true);
            return;
        }else{
            app.setPhase(GuiPhase.LOBBY);
            gui.notifyObservers(new LoginRequest(usernameField.getText()));
            app.setCurrentScene(app.getScene(GameFxml.LOBBY_SCENE.s));
            app.changeScene();
        }
    }
}

