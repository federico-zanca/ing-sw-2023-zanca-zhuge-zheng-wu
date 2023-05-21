package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.gameboard.Board;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.MessageHandler;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.Objects;
import java.util.Random;

public class GameScene2PlayersController implements Controller {
    private MessageHandler messageHandler;
    private GUI gui;
    private double cellWidth=46.5;
    private double cellHeight=46.5;
    private ImageView[][] itemLivRoomCells = new ImageView[Board.DIMENSIONS][Board.DIMENSIONS];
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
    void translateTriangle (Polygon polyg){
        TranslateTransition tr = new TranslateTransition();
        tr.setNode(polyg);
        tr.setToY(polyg.getLayoutY()+20);
    }
    @FXML
    void clickedCol(MouseEvent event) {
        translateTriangle(selectCol1);
    }
    @FXML
    void clickedHand(MouseEvent event){}
    @FXML
    void clickedItemLivRoom(MouseEvent event){}
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
        if(gui == null){
            return;
        }else{
            gui.getCurrentStage().setOnCloseRequest(e->{
                System.exit(0);
            });
        }
        for (int i = 0; i < Board.DIMENSIONS; i++) {
            for (int j = 0; j < Board.DIMENSIONS; j++) {
                ImageView cell = new ImageView();
                GridPane.setRowIndex(cell, i);
                GridPane.setColumnIndex(cell, j);
                livingRoomGrid.getChildren().add(cell);
                itemLivRoomCells[i][j] = cell;
                itemLivRoomCells[i][j].setFitWidth(cellWidth);
                itemLivRoomCells[i][j].setFitHeight(cellHeight);//add a mouse click event handler to each cell
                cell.setOnMouseClicked(this::clickedItemLivRoom);
            }
        }

    }
    public void setBoard(Square[][] board){
        ItemType type;
        Random random = new Random();
        Image image;
        URL url;
        int num;
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10.0);
        dropShadow.setColor(Color.web("#228B22"));
        Glow glow = new Glow();
        glow.setLevel(9000);
        for(int i=0;i<Board.DIMENSIONS;i++){
            for(int j=0;j<Board.DIMENSIONS;j++){
                if(board[i][j].getItem().hasSomething()){
                    type = board[i][j].getItem().getType();
                    num = random.nextInt(3) + 1;
                    url = getClass().getResource("/images/item_tiles/" + type.name() + num + ".png");
                    assert url != null;
                    image = new Image(url.toString());
                    itemLivRoomCells[i][j].setImage(image);
                    itemLivRoomCells[i][j].setFitWidth(cellWidth); //cellWidth is the width of each cell
                    itemLivRoomCells[i][j].setFitHeight(cellHeight); //cellHeight is the height of each cell
                    itemLivRoomCells[i][j].setPreserveRatio(true); //cellHeight is the height of each cell
                    if(board[i][j].isPickable()){
                        itemLivRoomCells[i][j].setEffect(dropShadow);
                        itemLivRoomCells[i][j].setEffect(glow);
                    }
                }
            }
        }
    }
}
