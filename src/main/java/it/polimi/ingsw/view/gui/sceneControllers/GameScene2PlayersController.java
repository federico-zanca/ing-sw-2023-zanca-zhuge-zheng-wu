package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.commongoals.CommonGoalCard;
import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.gameboard.Board;
import it.polimi.ingsw.model.gameboard.Coordinates;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.ChatMessage;
import it.polimi.ingsw.network.message.gamemessage.DrawTilesMessage;
import it.polimi.ingsw.network.message.gamemessage.InsertInfoMessage;
import it.polimi.ingsw.network.message.gamemessage.InsertTilesMessage;
import it.polimi.ingsw.view.InputValidator;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.MessageHandler;
import it.polimi.ingsw.view.tui.ActionType;
import it.polimi.ingsw.view.tui.PlayerState;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GameScene2PlayersController implements Controller {
    public AnchorPane root;
    public TextFlow txtFlow;
    public ImageView last1;
    public ImageView last2;
    public ImageView last3;
    public ImageView last4;
    private boolean drawn;
    public ImageView backGround;
    public Button okButton;
    private MessageHandler messageHandler;
    private GUI gui;
    private PlayerState state = PlayerState.WATCHING;
    private ArrayList<Player> playersList;
    private ArrayList<ItemTile> tilesToInsert;
    private ArrayList<Square> tilesToDraw;
    private ActionType actionType = ActionType.NONE;
    private final InputValidator inputValidator = new InputValidator();
    private final double cellWidth = 55;
    private final double cellHeight = 55;
    private ImageView[][] itemLivRoomCells = new ImageView[Board.DIMENSIONS][Board.DIMENSIONS];
    private Square[][] board;
    private final DropShadow dropShadow = new DropShadow();
    private int maxNumItems;
    private ImageView[][] bookshelfCells = new ImageView[Bookshelf.Rows][Bookshelf.Columns];;
    private Bookshelf bookshelf;
    private ArrayList<ImageView> newHandOrder;
    private ArrayList<Integer> order;
    private ArrayList<Label> pList;
    @FXML
    public HBox player1;
    @FXML
    public ImageView player1F;
    @FXML
    public ImageView player1S;
    @FXML
    public HBox player2;
    @FXML
    public ImageView player2F;
    @FXML
    public ImageView player2S;
    @FXML
    public HBox player3;
    @FXML
    public ImageView player3F;
    @FXML
    public ImageView player3S;
    @FXML
    public HBox player4;
    @FXML
    public ImageView player4F;
    @FXML
    public ImageView player4S;
    @FXML
    public Button exitButton;
    @FXML
    private TextArea chat;
    @FXML
    private TextField inputField;
    @FXML
    private ImageView firstCommonGoal, secondCommonGoal;
    @FXML
    private ImageView firstCommonGoalScoreBox, secondCommonGoalScoreBox;
    @FXML
    private ImageView endGameTokenBox;
    @FXML
    private GridPane livingRoomGrid;
    @FXML
    private GridPane myBookShelfGrid;
    @FXML
    private ImageView myPersonalGoal;
    @FXML
    private Label nameOfBookshelf;
    @FXML
    private VBox players;
    @FXML
    private Polygon selectCol1, selectCol2, selectCol3, selectCol4, selectCol5;
    @FXML
    private ImageView hand1, hand2, hand3;
    @FXML
    private Label notification;
    @FXML
    private Polygon turnIndicator;
    /**

     Translates a triangle shape on a mouse event.
     The translation is performed only if the player state is active and the action type is insert hand.
     When the triangle shape is clicked, it applies a glow effect to the shape.
     @param event The mouse event that triggered the translation.
     */
    private ArrayList<Player> playersQueue;

    @FXML
    void translateTriangle(MouseEvent event) {
        if (state.equals(PlayerState.ACTIVE)) {
            if (actionType.equals(ActionType.INSERT_HAND)) {
                Polygon polygon = (Polygon) event.getSource();
                Glow glow = new Glow();
                glow.setLevel(0.5);
                polygon.setEffect(glow);
            }
        }
    }
    /**
     * Clears the glow effect applied to a polygon shape on a mouse event.
     *
     * @param event The mouse event that triggered the clearing of the glow effect.
     */
    @FXML
    void clearGlow(MouseEvent event) {
        Polygon polygon = (Polygon) event.getSource();
        polygon.setEffect(null);
    }
    /**
     * Handles the click event on a column shape.
     * If the player is in the active state and the action type is to insert tiles from the hand,
     * it processes the click event to insert the tiles into the specified column.
     *
     * @param event The mouse event that triggered the column click.
     */
    @FXML
    void clickedCol(MouseEvent event) {
        if (state.equals(PlayerState.ACTIVE)) {
            if (actionType.equals(ActionType.INSERT_HAND)) {
                int colNum = (int) ((Polygon) event.getSource()).getUserData();
                if (inputValidator.columnHasLessSpace(colNum, ((InsertInfoMessage) messageHandler.getLastMessage()).getEnabledColumns())) {
                    return;
                } else {
                    setActionType(ActionType.NONE);
                    setPlayerState(PlayerState.WATCHING);
                    messageHandler.notifyObservers(new InsertTilesMessage(messageHandler.getMyUsername(), tilesToInsert, colNum));
                }
                clearHand();
            }
        }
    }
    /**
     * Handles the click event on a tile in the hand.
     * If the player is in the active state and the action type is to insert tiles from the hand,
     * it processes the click event to select the tile for insertion.
     *
     * @param event The mouse event that triggered the hand click.
     */
    @FXML
    void clickedHand(MouseEvent event) {
        if (state.equals(PlayerState.ACTIVE)) {
            if (actionType == ActionType.INSERT_HAND) {
                ImageView hand = (ImageView) event.getSource();
                if (!(hand.getEffect() instanceof Glow)) {
                    if (hand.equals(hand1)) {
                        order.add(1);
                    } else if (hand.equals(hand2)) {
                        order.add(2);
                    } else if (hand.equals(hand3)) {
                        order.add(3);
                    }
                    Glow glow = new Glow();
                    glow.setLevel(0.5);
                    hand.setEffect(glow);
                    newHandOrder.add(hand);
                }
            }
        }
        if (tilesToInsert.size() == 2) {
            if (newHandOrder.size() == 2) {
                swapOrder();
                order.clear();
                newHandOrder.clear();
            }
        } else if (tilesToInsert.size() == 3) {
            if (newHandOrder.size() == 3) {
                changeOrder();
                order.clear();
                newHandOrder.clear();
            }
        }
    }
/*
    @FXML
    void clickedExit() {
        setPlayerState(PlayerState.WATCHING);
        messageHandler.notifyObservers(new ExitGameRequest(messageHandler.getMyUsername()));
    }

 */
    /**
     * Handles the click event on an item in the living room area.
     * If the player is in the active state and the action type is to draw tiles,
     * it processes the click event to select or deselect the item for drawing.
     * If the action type is to order the hand and the selected item is already on the hand,
     * it processes the click event to deselect the item and switch back to drawing tiles action.
     *
     * @param row The row index of the clicked item.
     * @param col The column index of the clicked item.
     */
    @FXML
    void clickedItemLivRoom(int row, int col) {
        Glow glow = new Glow();
        DropShadow pickedShadow = new DropShadow();
        if (state.equals(PlayerState.ACTIVE)) {
            if (actionType == ActionType.DRAW_TILES) {
                if (board[row][col].isPickable()) {
                    if (inputValidator.isTileAlreadyOnHand(row, col, tilesToDraw)) {
                        int tileIndex = getTileIndex(col, row, tilesToDraw);
                        tilesToDraw.remove(tileIndex);
                        itemLivRoomCells[row][col].setEffect(null);
                        setPickableEffect(itemLivRoomCells[row][col]);
                        return;
                    } else if (tilesToDraw.size() > 0 && !inputValidator.inLineTile(row, col, tilesToDraw)) {
                        return;
                    }
                    Square square = new Square(new Coordinates(row, col), board[row][col].getItem().getType());
                    square.getItem().setImageId(board[row][col].getItem().getImageId());
                    tilesToDraw.add(square);
                    glow.setLevel(0.5);
                    pickedShadow.setRadius(25);
                    pickedShadow.setColor(Color.web("#800080"));
                    //itemLivRoomCells[row][col].setEffect(glow);
                    itemLivRoomCells[row][col].setEffect(pickedShadow);
                    if (inputValidator.isPossibleToDrawMore(tilesToDraw, board) && tilesToDraw.size() < Math.min(3, maxNumItems)) {
                        return;
                    } else {
                        setActionType(ActionType.ORDER_HAND);
                    }
                } else {
                    return;
                }
            } else if (actionType == ActionType.ORDER_HAND && hand1.getImage() == null) {
                if (board[row][col].isPickable() && inputValidator.isTileAlreadyOnHand(row, col, tilesToDraw)) {
                    int tileIndex = getTileIndex(col, row, tilesToDraw);
                    tilesToDraw.remove(tileIndex);
                    itemLivRoomCells[row][col].setEffect(null);
                    setPickableEffect(itemLivRoomCells[row][col]);
                    setActionType(ActionType.DRAW_TILES);
                    return;
                }
            }
        }
    }
    /**
     * Handles the click event on the "OK" button.
     * If the player is in the active state and has not drawn tiles yet,
     * it notifies the server to draw the selected tiles and updates the player state.
     * If there are no tiles selected to draw, it keeps the player in the active state.
     */
    @FXML
    void clickedOk() {
        if (state.equals(PlayerState.ACTIVE) && !drawn) {
            setPlayerState(PlayerState.WATCHING);
            if (tilesToDraw.size() != 0) {
                drawn = true;
                messageHandler.notifyObservers(new DrawTilesMessage(messageHandler.getMyUsername(), tilesToDraw));
                clearEffects();
                drawTiles();
            } else {
                setPlayerState(PlayerState.ACTIVE);
            }
        }
    }
    /**
     * Clears the images of the drawn tiles from the living room grid.
     */
    private void drawTiles() {
        for (Square tile : tilesToDraw) {
            itemLivRoomCells[tile.getRow()][tile.getColumn()].setImage(null);
        }
    }
    /**
     * Sets the message handler for the GUI.
     *
     * @param messageHandler The message handler to be set.
     */
    @Override
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * Sets the GUI for the message handler.
     *
     * @param gui The GUI to be set.
     */
    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
    /**
     * Initializes the message handler.
     * This method sets up the necessary components and configurations for the message handler.
     * It adjusts the size of the background and root elements based on the screen size.
     * It also initializes various lists and objects used in the message handler.
     * Additionally, it sets the chat box and clears the hand.
     * Finally, it initializes the living room grid, bookshelf grid, and select column.
     */
    @Override
    public void initialize() {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = primaryScreenBounds.getWidth();
        double screenHeight = primaryScreenBounds.getHeight();
        backGround.setFitWidth(screenWidth);
        root.setPrefSize(screenWidth,screenHeight);
        drawn = true;
        if (gui != null) {
            clearHand();
            pList = new ArrayList<>();
            order = new ArrayList<>();
            newHandOrder = new ArrayList<>();
            tilesToDraw = new ArrayList<>();
            tilesToInsert = new ArrayList<>();
            playersList = new ArrayList<>();
            playersQueue = new ArrayList<>();
            bookshelf = new Bookshelf();
            nameOfBookshelf.setText(messageHandler.getMyUsername());
            setChatBox(messageHandler.getChatLog());
            initLivingRoomGrid();
            initBookshelfGrid();
            initSelectCol();
            destroy();
        }
    }
    /**
     * Resets the personal goals.
     * This method clears the image of the personal goal card displayed on the GUI.
     * It is called when the personal goals need to be reset, such as at the start of a new game.
     */
    private void resetPersonalGoals() {
        myPersonalGoal.setImage(null);
    }
    /**
     * Resets the common goals.
     * This method sets the image of the common goal score boxes to the default image.
     * It is called when the common goals need to be reset, such as at the start of a new game.
     */
    private void resetCommonGoals() {
        firstCommonGoalScoreBox.setImage(new Image("/images/scoring_tokens/scoring_8.jpg"));
        secondCommonGoalScoreBox.setImage(new Image("/images/scoring_tokens/scoring_8.jpg"));
    }
    /**
     * Clears the player list.
     * This method clears the content of each player's pane in the GUI.
     * It is called when the player list needs to be cleared, such as at the start of a new game or when players leave the game.
     */
    private void clearPlayerList() {
        player1.getChildren().clear();
        player2.getChildren().clear();
        player3.getChildren().clear();
        player4.getChildren().clear();
    }

    /**
     * Clears the board.
     * This method clears the images on each cell of the game board in the GUI.
     * It is called when the board needs to be cleared, such as at the start of a new game or when tiles are removed from the board.
     */
    private void clearboard() {
        for (int i = 0; i < Board.DIMENSIONS; i++) {
            for (int j = 0; j < Board.DIMENSIONS; j++) {
                itemLivRoomCells[i][j].setImage(null);
            }
        }
    }

    /**
     * Initializes the player list in the GUI.
     * This method creates the visual representation of the player list in the GUI based on the provided list of players.
     * It sets the player names, styles, and event handlers for interacting with the player bookshelves.
     * @param players The list of players
     */
    public void initPlayerList(ArrayList<Player> players) {
        HBox hBox = null;
        this.players.setSpacing(3);
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            hBox = switch (i) {
                case 0 -> player1;
                case 1 -> player2;
                case 2 -> player3;
                case 3 -> player4;
                default -> null;
            };
            assert hBox != null;
            hBox.setPadding(new Insets(3));
            hBox.setPrefSize(200, 45);
            hBox.setStyle("-fx-background-color: #D08C4D;");
            hBox.setEffect(new DropShadow());
            hBox.setAlignment(Pos.CENTER_LEFT);

            Label label = new Label(p.getUsername());
            label.setPrefSize(130,40);
            label.setStyle("-fx-font-family: system;");
            label.setStyle("-fx-font-size: 22px;");
            label.setStyle("-fx-font-weight: bold;");
            hBox.getChildren().clear();
            hBox.getChildren().add(label);
            if(i == 0){
                hBox.getChildren().addAll(player1F,player1S);
            }else if(i == 1){
                hBox.getChildren().addAll(player2F,player2S);
            }else if(i == 2){
                hBox.getChildren().addAll(player3F,player3S);
            }else if(i == 3){
                hBox.getChildren().addAll(player4F,player4S);
            }
            hBox.setVisible(true);

            label.setOnMousePressed(event -> {
                String username = label.getText();
                showOtherBookshelf(username);
                nameOfBookshelf.setText(username);
            });
            label.setOnMouseReleased(event -> {
                setBookshelf(bookshelf);
                nameOfBookshelf.setText(messageHandler.getMyUsername());
            });
            pList.add(label);
        }
    }

    private void showOtherBookshelf(String username) {
        for (Player p : playersList) {
            if (p.getUsername().equals(username) && p.getBookshelf() != null && p.getBookshelf() != bookshelf) {
                setBookshelf(p.getBookshelf());
                break;
            }
        }
    }
    /**
     * Returns the index of the tile in the tilesToDraw list based on its column and row position.
     *
     * @param col          The column position of the tile.
     * @param row          The row position of the tile.
     * @param tilesToDraw  The list of tiles being drawn.
     * @return The index of the tile in the list, or -1 if the tile is not found.
     */
    private int getTileIndex(int col, int row, ArrayList<Square> tilesToDraw) {
        for (int i = 0; i < tilesToDraw.size(); i++) {
            Square tile = tilesToDraw.get(i);
            if (tile.getColumn() == col && tile.getRow() == row) {
                return i;
            }
        }
        return -1;
    }
    /**
     * Clears the images of all the tiles in the hand.
     */
    private void clearHand() {
        hand1.setImage(null);
        hand2.setImage(null);
        hand3.setImage(null);
    }
    /**

     Sets the images of the tiles in the hand based on the given list of ItemTile objects.
     @param hand The list of ItemTile objects representing the tiles in the hand.
     */
    public void setHand(ArrayList<ItemTile> hand) {
        tilesToInsert = hand;
        URL url;
        for (int i = 0; i < hand.size(); i++) {
            if (i == 0) {
                url = getClass().getResource("/images/item_tiles/" + hand.get(i).getType().name() + hand.get(0).getImageId() + ".png");
                assert url != null;
                hand1.setImage(new Image(url.toString()));
                //handOrder.add(hand1);
            } else if (i == 1) {
                url = getClass().getResource("/images/item_tiles/" + hand.get(i).getType().name() + hand.get(1).getImageId() + ".png");
                assert url != null;
                hand2.setImage(new Image(url.toString()));
                // handOrder.add(hand2);
            } else {
                url = getClass().getResource("/images/item_tiles/" + hand.get(i).getType().name() + hand.get(2).getImageId() + ".png");
                assert url != null;
                hand3.setImage(new Image(url.toString()));
                //  handOrder.add(hand3);
            }
        }
    }
    /**

     Sets the action type for the player.
     @param actionType The ActionType to set.
     */
    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }
    /**
     * Sets the notification text and makes it visible in the GUI.
     *
     * @param notification The text of the notification.
     */
    public void setNotification(String notification) {
        this.notification.setText(notification);
        this.notification.setVisible(true);
    }
    /**

     Sets the state of the player.
     @param state The PlayerState to set.
     */
    public void setPlayerState(PlayerState state) {
        this.state = state;
    }
    /**

     Initializes the select column buttons and sets their user data.
     The user data represents the column index.
     */
    private void initSelectCol() {
        selectCol1.setUserData(0);
        selectCol2.setUserData(1);
        selectCol3.setUserData(2);
        selectCol4.setUserData(3);
        selectCol5.setUserData(4);
    }
    /**
     * Initializes the bookshelf grid by creating ImageView cells and adding them to the grid.
     * The method also sets the appropriate row and column indices for each cell.
     * Existing cells in the grid are removed before creating new cells.
     */
    private void initBookshelfGrid() {
        ImageView cell;
        for (int i = 0; i < Bookshelf.Rows; i++) {
            for (int j = 0; j < Bookshelf.Columns; j++) {
                if(!myBookShelfGrid.getChildren().isEmpty()){
                    myBookShelfGrid.getChildren().remove(bookshelfCells[i][j]);
                    bookshelfCells[i][j] = null;
                }
                cell = new ImageView();
                GridPane.setRowIndex(cell, i);
                GridPane.setColumnIndex(cell, j);
                GridPane.setHalignment(cell, javafx.geometry.HPos.CENTER); // Horizontal alignment
                GridPane.setValignment(cell, javafx.geometry.VPos.CENTER); // Vertical alignment
                myBookShelfGrid.getChildren().add(cell);
                bookshelfCells[i][j] = cell;
                bookshelfCells[i][j].setFitWidth(cellWidth-3.5);
                bookshelfCells[i][j].setFitHeight(cellHeight-3.5);
            }
        }
    }
    /**
     * Initializes the living room grid by creating ImageView cells and adding them to the grid.
     * The method also sets the appropriate row and column indices for each cell.
     * Existing cells in the grid are removed before creating new cells.
     */
    private void initLivingRoomGrid() {
        ImageView cell;
        for (int i = 0; i < Board.DIMENSIONS; i++) {
            for (int j = 0; j < Board.DIMENSIONS; j++) {
                if(!livingRoomGrid.getChildren().isEmpty()){
                    livingRoomGrid.getChildren().remove(itemLivRoomCells[i][j]);
                    itemLivRoomCells[i][j] = null;
                }
                cell = new ImageView();
                GridPane.setRowIndex(cell, i);
                GridPane.setColumnIndex(cell, j);
                GridPane.setHalignment(cell, javafx.geometry.HPos.CENTER); // Horizontal alignment
                GridPane.setValignment(cell, javafx.geometry.VPos.CENTER); // Vertical alignment
                livingRoomGrid.getChildren().add(cell);
                itemLivRoomCells[i][j] = cell;
                itemLivRoomCells[i][j].setFitWidth(cellWidth);
                itemLivRoomCells[i][j].setFitHeight(cellHeight);
            }
        }
    }
    /**
     * Sets the bookshelf attribute for the player.
     *
     * @param bookshelf The Bookshelf object to set.
     */
    public void setBookshelfAttribute(Bookshelf bookshelf) {
        this.bookshelf = bookshelf;
    }
    /**
     * Sets the bookshelf for the player.
     *
     * @param bookshelf The Bookshelf object to set.
     */
    public void setBookshelf(Bookshelf bookshelf) {
        if (bookshelf != null) {
            ItemTile[][] shelfie = bookshelf.getShelfie();
            ItemType type;
            Image image;
            URL url;
            ImageView item;
            for (int i = 0; i < Bookshelf.Rows; i++) {
                for (int j = 0; j < Bookshelf.Columns; j++) {
                    item = bookshelfCells[i][j];
                    if (shelfie[i][j].hasSomething()) {
                        type = shelfie[i][j].getType();
                        url = getClass().getResource("/images/item_tiles/" + type.name() + shelfie[i][j].getImageId() + ".png");
                        assert url != null;
                        image = new Image(url.toString());
                        item.setImage(image);
                        item.setFitWidth(cellWidth-3.5);
                        item.setFitHeight(cellHeight-3.5);
                        item.setPreserveRatio(true);
                    } else {
                        item.setImage(null);
                    }
                }
            }
        } else {
            ImageView item;
            for (int i = 0; i < Bookshelf.Rows; i++) {
                for (int j = 0; j < Bookshelf.Columns; j++) {
                    item = bookshelfCells[i][j];
                    item.setImage(null);
                }
            }
        }
    }
    /**
     * Sets the board state and displays the items on the living room grid.
     *
     * @param board       The 2D array representing the board state.
     * @param maxNumItems The maximum number of items allowed on the board.
     */
    public void setBoard(Square[][] board, int maxNumItems) {
        this.board = board;
        this.maxNumItems = maxNumItems;
        ItemType type;
        Image image;
        URL url;
        ImageView item;

        for (int i = 0; i < Board.DIMENSIONS; i++) {
            for (int j = 0; j < Board.DIMENSIONS; j++) {
                item = itemLivRoomCells[i][j];
                if (board[i][j].getItem().hasSomething()) {
                    type = board[i][j].getItem().getType();
                    url = getClass().getResource("/images/item_tiles/" + type.name() + board[i][j].getItem().getImageId() + ".png");
                    assert url != null;
                    image = new Image(url.toString());
                    item.setImage(image);
                    item.setFitWidth(cellWidth); //cellWidth is the width of each cell
                    item.setFitHeight(cellHeight); //cellHeight is the height of each cell
                    item.setPreserveRatio(true); //cellHeight is the height of each cell
                    if (state == PlayerState.ACTIVE) {
                        if (board[i][j].isPickable()) {
                            ImageView finalItem = item;
                            item.setOnMouseClicked(mouseEvent -> {
                                int rowIndex = GridPane.getRowIndex(finalItem);
                                int colIndex = GridPane.getColumnIndex(finalItem);
                                clickedItemLivRoom(rowIndex, colIndex);
                            });
                            setPickableEffect(item);
                        }
                    }
                } else {
                    item.setImage(null);
                }
            }
        }
    }
    /**
     * Applies a pickable effect to the specified ImageView.
     *
     * @param image The ImageView to apply the effect to.
     */
    private void setPickableEffect(ImageView image) {
        dropShadow.setRadius(25);
        dropShadow.setColor(Color.web("#006400"));
        image.setEffect(dropShadow);
    }
    /**
     * Clears any visual effects applied to the item cells in the living room grid.
     */
    public void clearEffects() {
        ImageView item;
        for (int i = 0; i < Board.DIMENSIONS; i++) {
            for (int j = 0; j < Board.DIMENSIONS; j++) {
                item = itemLivRoomCells[i][j];
                item.setEffect(null);
            }
        }
    }
    /**
     * Clears the lists of tiles to be inserted and drawn.
     */
    public void clearTiles() {
        tilesToInsert.clear();
        tilesToDraw.clear();
    }
    /**

     Sets the image of the personal goal card for the player.
     The image is determined based on the player's personal goal card identifier.
     */
    public void setPersonalGoalCardImage() {
        URL url;
        Image image;
        int num = messageHandler.getPersonalGoalCard().getIdentificator();
        url = getClass().getResource("/images/personal_goal_cards/Personal_Goals" + num + ".png");
        assert url != null;
        image = new Image(url.toString());
        myPersonalGoal.setImage(image);
        myPersonalGoal.setVisible(true);
    }

    /**

     Sets the images for the common goal cards.

     The images are determined based on the provided common goal cards.

     @param commonGoals The list of common goal cards.
     */
    public void setCommonGoals(ArrayList<CommonGoalCard> commonGoals) {
        URL url;
        Image image;
        int score;
        url = getClass().getResource("/images/common_goal_cards/" + commonGoals.get(0).getImageId() + ".jpg");
        assert url != null;
        image = new Image(url.toString());
        firstCommonGoal.setImage(image);
        score = commonGoals.get(0).peek();
        url = getClass().getResource("/images/scoring_tokens/scoring_"+ score +".jpg");
        assert url != null;
        firstCommonGoalScoreBox.setImage(new Image(url.toString()));
        url = getClass().getResource("/images/common_goal_cards/" + commonGoals.get(1).getImageId() + ".jpg");
        assert url != null;
        image = new Image(url.toString());
        secondCommonGoal.setImage(image);
        score = commonGoals.get(0).peek();
        url = getClass().getResource("/images/scoring_tokens/scoring_"+ score +".jpg");
        assert url != null;
        firstCommonGoalScoreBox.setImage(new Image(url.toString()));
    }

    public void setPlayerQueue(ArrayList<Player> players) {
        this.playersQueue = players;
        initPlayerList(players);
    }
    public void setPlayers(ArrayList<Player> players) {
        this.playersList = players;
        for (int i = 0; i < playersList.size(); i++) {
            int index = playersQueue.indexOf(playersList.get(i));
            if (index >= 0) {
                swap(playersList, i, index);
            }
        }
    }
    private void swap(List<Player> list, int index1, int index2) {
        Player tmp = list.get(index1);
        list.set(index1, list.get(index2));
        list.set(index2, tmp);
    }
    /**

     Changes the order of the tiles in the player's hand.

     Swaps the positions of the first and second tiles in the hand.
     */
    private void changeOrder() {
        int first = order.get(0);
        Image one = newHandOrder.get(0).getImage();
        int second = order.get(1);
        Image two = newHandOrder.get(1).getImage();
        int third = order.get(2);
        Image three = newHandOrder.get(2).getImage();
        clearHand();
        hand1.setImage(one);
        hand2.setImage(two);
        hand3.setImage(three);
        Collections.swap(tilesToInsert, first - 1, 0);
        if (second != 1) {
            Collections.swap(tilesToInsert, second - 1, 1);
        } else {
            Collections.swap(tilesToInsert, third - 1, 2);
        }
        hand1.setEffect(null);
        hand2.setEffect(null);
        hand3.setEffect(null);
    }
    /**

     Swaps the order of the tiles in the player's hand.

     Swaps the positions of the two tiles in the hand.
     */
    private void swapOrder() {
        if (tilesToInsert.size() == 2) {
            Collections.swap(tilesToInsert, 1, 0);
            Image temp = hand1.getImage();
            hand1.setImage(hand2.getImage());
            hand2.setImage(temp);
            hand1.setEffect(null);
            hand2.setEffect(null);
        }
    }
    /*
    private void setChatBox(ArrayList<ChatMessage> chatLog) {
        inputField.clear();
        chat.clear();
        chat.setWrapText(true);
        for (ChatMessage message : chatLog) {
            setChat(message);
        }
        inputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String message = inputField.getText();
                message = message.trim();
                String recipientusername = null;
                if (message.isEmpty()) {
                    return;
                }
                if (message.startsWith("@")) {
                    if (!message.contains(" ")) {
                        return;
                    }
                    recipientusername = message.substring(1, message.indexOf(" "));
                    message = message.substring(message.indexOf(" ") + 1);
                }
                sendMessage(message, recipientusername);
                inputField.clear();
            }
        });
    }
    */
    /**
     * Sets up the chat box with the given chat log.
     * Clears the input field and the chat flow, and populates the chat flow with messages from the chat log.
     * Private messages are indicated with a "(PRIVATE MESSAGE)" prefix.
     * Allows sending messages by pressing the Enter key.
     *
     * @param chatLog The list of chat messages to populate the chat box with.
     */
    private void setChatBox(ArrayList<ChatMessage> chatLog){
        inputField.clear();
        txtFlow.getChildren().clear();
        txtFlow.setPrefWidth(300);
        for(ChatMessage message:chatLog) {
            Text text = new Text(message.getContent() + "\n");
            Text username = new Text(message.getSender()+": ");
            Text prefix = new Text();
            username.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
            username.setFill(javafx.scene.paint.Color.CYAN);
            if(message.getReceiver() != null){
                //if(Objects.equals(message.getReceiver(), messageHandler.getMyUsername())){
                prefix = new Text("(PRIVATE MESSAGE)\n");
                prefix.setFill(Color.GREEN);
                prefix.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
                txtFlow.getChildren().addAll(prefix,username,text);

                //}else{
                //   prefix = new Text("");
                //}
            }
            else{
                txtFlow.getChildren().addAll(username,text);
            }
            //text.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        }
        inputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String message = inputField.getText();
                message = message.trim();
                String recipientusername = null;
                if(message.isEmpty()) {
                    return;
                }
                if(message.startsWith("@")) {
                    if (!message.contains(" ")) {
                        return;
                    }
                    recipientusername = message.substring(1, message.indexOf(" "));
                    message = message.substring(message.indexOf(" ") + 1);
                }
                sendMessage(message,recipientusername);
                inputField.clear();
            }
        });
    }

    /*
    public void setChat(ChatMessage message) {
        String prefix = "";
        String messageContent = message.getContent();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        if (message.getReceiver() != null) {
            if (Objects.equals(message.getReceiver(), messageHandler.getMyUsername())) {
                prefix = "[" + formattedDateTime + "] " + "PRIVATE MESSAGE FROM ";
                messageContent = message.getSender() + ": " + messageContent;

            } else {
                prefix = "[" + formattedDateTime + "] " + "PRIVATE MESSAGE TO ";
                messageContent = message.getReceiver() + ": " + messageContent;
            }
        } else {
            messageContent = "[" + formattedDateTime + "] " + message.getSender() + ": " + messageContent;
        }
        chat.appendText(prefix + messageContent + "\n");
    }
    */
    /**
     * Adds a chat message to the chat flow.
     * The chat message is displayed with the sender's username and content.
     * Private messages are indicated with a "(PRIVATE MESSAGE)" prefix.
     *
     * @param message The chat message to add to the chat flow.
     */
    public void setChat(ChatMessage message){
        Text text = new Text(message.getContent() + "\n");
        Text username = new Text(message.getSender() + ": ");
        Text prefix = new Text();
        text.setFill(javafx.scene.paint.Color.WHITE);
        username.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        username.setFill(javafx.scene.paint.Color.CYAN);
        if(message.getReceiver() != null){
            prefix = new Text("(PRIVATE MESSAGE)\n");
            prefix.setFill(Color.GREEN);
            prefix.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
            txtFlow.getChildren().addAll(prefix,username,text);
        }
        else{
            txtFlow.getChildren().addAll(username,text);
        }
    }
    /**
     * Sends a chat message with the given content to the specified recipient.
     * If a recipient username is provided, the message is sent as a private message.
     *
     * @param message           The content of the message to send.
     * @param recipientusername The username of the message recipient, or null if it's a public message.
     */
    public void sendMessage(String message, String recipientusername) {
        ChatMessage chatMessage = new ChatMessage(message, recipientusername);
        messageHandler.notifyObservers(chatMessage);
    }
    /*
    public void moveTurnIndicator(String player) {
        double x, y;
        for (Label l : pList) {
            if (Objects.equals(l.getText(), player)) {
                x = l.localToScene(l.getBoundsInLocal()).getMinX();
                y = l.localToScene(l.getBoundsInLocal()).getMinY();
                turnIndicator.setLayoutX(x-20);
                turnIndicator.setLayoutY(y+30);
                turnIndicator.setVisible(true);
                break;
            }
        }
    }

     */
    /**
     * Sets the icons for the common goal based on the given parameters.
     * Determines the URL of the image based on the value of `cg.peek()`.
     * If `commonGoal` is 1, sets the image for the first common goal score box.
     * If `commonGoal` is 2, sets the image for the second common goal score box.
     * The icons for players are set based on their username.
     *
     * @param username     The username of the player.
     * @param commonGoal   The common goal number (1 or 2).
     * @param cg           The CommonGoalCard object to get the scoring token value from.
     */
    public void setIcons(String username,int commonGoal,CommonGoalCard cg){
        String url;
        Image image;
        if(cg.peek() == 2 || cg.peek() == 4 || cg.peek() == 6 || cg.peek() == 8){
            url = ("/images/scoring_tokens/scoring_"+ cg.peek() +".jpg");
        }else if(cg.peek() == 0){
            url = ("/images/scoring_tokens/scoring.jpg");
        }else{
            return;
        }
        if(commonGoal == 1){
            image = firstCommonGoalScoreBox.getImage();
            firstCommonGoalScoreBox.setImage(new Image(url));
        }else if(commonGoal == 2){
            image = secondCommonGoalScoreBox.getImage();
            secondCommonGoalScoreBox.setImage(new Image(url));
        }else{
            image = null;
        }
        for(int i=0;i<pList.size();i++){
            if(Objects.equals(pList.get(i).getText(), username)){
                if(i==0){
                    if(player1F.getImage()==null){
                        player1F.setImage(image);
                    } else if(player1S.getImage()==null){
                        player1S.setImage(image);
                    }
                }else if(i==1) {
                    if (player2F.getImage() == null){
                        player2F.setImage(image);
                    } else if(player2S.getImage()==null){
                        player2S.setImage(image);
                    }
                }else if(i==2){
                    if(player3F.getImage()==null){
                        player3F.setImage(image);
                    }else if(player3S.getImage()==null){
                        player3S.setImage(image);
                    }
                }else if(i == 3){
                    if(player4F.getImage() == null){
                        player4F.setImage(image);
                    }else if(player4S.getImage()==null){
                        player4S.setImage(image);
                    }
                }
            }
        }
    }

    /**
     * Clears the images in the item living room cells and bookshelf cells.
     * Used to destroy/reset the state of the board.
     */
    public void destroy(){
        for (int i = 0; i < Board.DIMENSIONS; i++) {
            for (int j = 0; j < Board.DIMENSIONS; j++) {
                itemLivRoomCells[i][j].setImage(null);
            }
        }
        for (int i = 0; i < Bookshelf.Rows; i++) {
            for (int j = 0; j < Bookshelf.Columns; j++) {
                bookshelfCells[i][j].setImage(null);
            }
        }
    }
    /**
     * Sets the value of the 'drawn' flag.
     *
     * @param drawn The value to set for the 'drawn' flag.
     */
    public void setDrawn(boolean drawn){
        this.drawn = drawn;
    }

    public void setLastTurnIcon(String currentPlayer){
        Image tmp = endGameTokenBox.getImage();
        endGameTokenBox.setImage(null);
        if(Objects.equals(messageHandler.getMyUsername(), currentPlayer)){
            for(int i=0;i<pList.size();i++){
                if(Objects.equals(pList.get(i).getText(), currentPlayer)){
                    if(i==0){
                        last1.setImage(tmp);
                    }else if(i==1) {
                        last2.setImage(tmp);
                    }else if(i==2){
                        last3.setImage(tmp);
                    }else if(i == 3){
                        last4.setImage(tmp);
                    }
                }
            }
        }
    }
    public void setCurrentPlayerLabel(String currentPlayer){
        clearPlistEffect();
        for(Label l : pList){
            if(Objects.equals(l.getText(), currentPlayer)){
                l.setStyle("-fx-text-fill: lightgreen;");            }
        }
    }

    private void clearPlistEffect() {
        for(Label l : pList){
            l.setStyle("-fx-text-fill: black");
        }
    }
}