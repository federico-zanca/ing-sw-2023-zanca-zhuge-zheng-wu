package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.MessageHandler;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class GameScene2PlayersController implements Controller {
    private MessageHandler messageHandler;
    private GUI gui;
    @FXML
    private ImageView ItemLivRoomR0C3, ItemLivRoomR0C4,ItemLivRoomR1C3, ItemLivRoomR1C4, ItemLivRoomR1C5,ItemLivRoomR2C2,
            ItemLivRoomR2C3,ItemLivRoomR2C4,ItemLivRoomR2C5,ItemLivRoomR2C6,ItemLivRoomR3C1,ItemLivRoomR3C2,ItemLivRoomR3C3,
            ItemLivRoomR3C4,ItemLivRoomR3C5, ItemLivRoomR3C6,ItemLivRoomR3C7,ItemLivRoomR3C8,ItemLivRoomR4C0,ItemLivRoomR4C1,
            ItemLivRoomR4C2,ItemLivRoomR4C3,ItemLivRoomR4C4,ItemLivRoomR4C5,ItemLivRoomR4C6,ItemLivRoomR4C7,ItemLivRoomR4C8,
            ItemLivRoomR5C0,ItemLivRoomR5C1,ItemLivRoomR5C2,ItemLivRoomR5C3,ItemLivRoomR5C4,ItemLivRoomR5C5,ItemLivRoomR5C6,
            ItemLivRoomR5C7,ItemLivRoomR6C2,ItemLivRoomR6C3,ItemLivRoomR6C4,ItemLivRoomR6C5,ItemLivRoomR6C6,ItemLivRoomR7C3,
            ItemLivRoomR7C4,ItemLivRoomR7C5,ItemLivRoomR8C4,ItemLivRoomR8C5;
    @FXML
    private ImageView myBS00, myBS01, myBS02, myBS03, myBS04, myBS10, myBS11, myBS12, myBS13, myBS14, myBS20, myBS21, myBS22,
            myBS23, myBS24, myBS30, myBS31, myBS32, myBS33, myBS34, myBS40, myBS41, myBS42, myBS43, myBS44, myBS50, myBS51,
            myBS52, myBS53, myBS54;
    @FXML
    private ImageView oth1BS00, oth1BS01, oth1BS02, oth1BS03, oth1BS04, oth1BS10, oth1BS11, oth1BS12, oth1BS13, oth1BS14,
            oth1BS20, oth1BS21, oth1BS22, oth1BS23, oth1BS24, oth1BS30, oth1BS31, oth1BS32, oth1BS33, oth1BS34, oth1BS40,
            oth1BS41, oth1BS42, oth1BS43, oth1BS44, oth1BS50, oth1BS51, oth1BS52, oth1BS53, oth1BS54;

    @FXML
    private ImageView bag;

    @FXML
    private ImageView firstCommonGoal, secondCommonGoal;

    @FXML
    private ImageView firstCommonGoalScoreBox, secondCommonGoalScoreBox;

    @FXML
    private ImageView livingRoomBoard;

    @FXML
    private ImageView endGameTokenBox;

    @FXML
    private GridPane livingRoomGrid;

    @FXML
    private GridPane myBookShelfGrid;

    @FXML
    private ImageView myBookshelf;

    @FXML
    private ImageView myPersonalGoal;

    @FXML
    private Label nameOrth1OnBookshelf;

    @FXML
    private Label namePlayerOnBookshelf;

    @FXML
    private ImageView oth1BookShelf;

    @FXML
    private VBox players;

    @FXML
    private Polygon selectCol1, selectCol2, selectCol3, selectCol4, selectCol5;

    @FXML
    private ImageView hand1, hand2, hand3;

    @FXML
    private Rectangle handRegion;

    @FXML
    private Label orderHand1, orderHand2, orderHand3;

    @FXML
    void clicked1stCol(MouseEvent event) {
        translateTriangle(selectCol1);
    }

    @FXML
    void clicked2ndCol(MouseEvent event) {

    }

    @FXML
    void clicked3rdCol(MouseEvent event) {

    }

    @FXML
    void clicked4thCol(MouseEvent event) {

    }

    @FXML
    void clicked5thCol(MouseEvent event) {

    }

    void translateTriangle (Polygon polyg){
        TranslateTransition tr = new TranslateTransition();
        tr.setNode(polyg);
        tr.setToY(polyg.getLayoutY()+20);
    }
    @FXML
    void clickedItemLivRoomR0C3(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR0C4(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR1C3(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR1C4(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR1C5(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR2C2(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR2C3(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR2C4(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR2C5(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR2C6(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR3C1(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR3C2(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR3C3(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR3C4(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR3C5(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR3C6(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR3C7(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR3C8(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR4C0(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR4C1(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR4C2(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR4C3(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR4C4(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR4C5(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR4C6(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR4C7(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR4C8(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR5C0(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR5C1(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR5C2(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR5C3(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR5C4(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR5C5(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR5C6(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR5C7(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR6C2(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR6C3(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR6C4(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR6C5(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR6C6(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR7C3(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR7C4(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR7C5(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR8C4(MouseEvent event) {

    }

    @FXML
    void clickedItemLivRoomR8C5(MouseEvent event) {

    }
    @FXML
    void clickHand1(MouseEvent event) {

    }

    @FXML
    void clickHand2(MouseEvent event) {

    }

    @FXML
    void clickHand3(MouseEvent event) {

    }

    @Override
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void initialize() {

    }
}
