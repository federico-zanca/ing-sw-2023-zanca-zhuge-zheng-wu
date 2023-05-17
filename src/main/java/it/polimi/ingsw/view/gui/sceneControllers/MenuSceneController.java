package it.polimi.ingsw.view.gui.sceneControllers;


import it.polimi.ingsw.network.message.connectionmessage.LoginRequest;
import it.polimi.ingsw.view.InputValidator;
import it.polimi.ingsw.view.gui.MessageHandler;
import it.polimi.ingsw.view.gui.GUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class MenuSceneController implements Controller{
    private MessageHandler messageHandler;
    private GUI gui;
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
            System.out.println(messageHandler.getMyLobby());
        }
    }
    public void checkUsername(){
        if(!inputValidator.isValidUsername(usernameField.getText())){
            error.setVisible(true);
            return;
        }else{
            messageHandler.setMyUsername(usernameField.getText());
            messageHandler.notifyObservers(new LoginRequest(usernameField.getText()));
            Platform.runLater(this::checkForError);
            //TODO spostare le due righe nella messageHandler
        }
    }
    public void checkForError(){
        error.setText(gui.getError());
        error.setVisible(true);
    }
    public void quitGame(){
        System.exit(0);
    }
}

