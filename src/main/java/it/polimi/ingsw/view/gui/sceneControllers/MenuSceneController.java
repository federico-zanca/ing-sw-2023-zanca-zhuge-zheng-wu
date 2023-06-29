package it.polimi.ingsw.view.gui.sceneControllers;


import it.polimi.ingsw.network.message.connectionmessage.LoginRequest;
import it.polimi.ingsw.view.InputValidator;
import it.polimi.ingsw.view.gui.MessageHandler;
import it.polimi.ingsw.view.gui.GUI;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;

import java.util.Objects;

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
    public ImageView backGround;
    public ImageView title;
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
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = primaryScreenBounds.getWidth();
        backGround.setFitWidth(screenWidth);
        if(gui == null){
            return;
        }else{
            usernameField.clear();
            gui.getCurrentStage().setOnCloseRequest(e->{
                System.exit(0);
            });
        }
    }
    public void checkUsername(){
        if(Objects.equals(usernameField.getText(), "")){
            setError("Il tuo username non può essere vuoto!");
            return;
        }
        if(!inputValidator.isValidUsername(usernameField.getText())){
            setError("Username not valid!");
        }else{
            messageHandler.setMyUsername(usernameField.getText());
            messageHandler.notifyObservers(new LoginRequest(usernameField.getText()));
        }
    }
    @FXML
    void enterCheckUserName(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            checkUsername();
        }
    }
    public void setError(String error){
        this.error.setText(error);
        this.error.setVisible(true);
    }
    public void quitGame(){
        System.exit(0);
    }
}

