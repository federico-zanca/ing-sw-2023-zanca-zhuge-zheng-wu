package it.polimi.ingsw.view.gui.scene;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MenuSceneController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button play;
    @FXML
    private Button quit;

    @FXML
    public void initialize() {
        play.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> System.out.println("not yet"));
        quit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> System.exit(0));
    }
}
