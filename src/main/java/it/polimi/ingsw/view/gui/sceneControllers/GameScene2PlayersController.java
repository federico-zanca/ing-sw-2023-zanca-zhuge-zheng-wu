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
import it.polimi.ingsw.network.message.gamemessage.ExitGameRequest;
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
import javafx.stage.Screen;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GameScene2PlayersController implements Controller {
    public AnchorPane root;
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
    private final double cellWidth = 46.5;
    private final double cellHeight = 46.5;
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

    @FXML
    void clearGlow(MouseEvent event) {
        Polygon polygon = (Polygon) event.getSource();
        polygon.setEffect(null);
    }

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

    @FXML
    void clickedExit() {
        setPlayerState(PlayerState.WATCHING);
        messageHandler.notifyObservers(new ExitGameRequest(messageHandler.getMyUsername()));
    }

    @FXML
    void clickedItemLivRoom(int row, int col) {
        Glow glow = new Glow();
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
                    itemLivRoomCells[row][col].setEffect(glow);
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

    private void drawTiles() {
        for (Square tile : tilesToDraw) {
            itemLivRoomCells[tile.getRow()][tile.getColumn()].setImage(null);
        }
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
            bookshelf = new Bookshelf();
            nameOfBookshelf.setText(messageHandler.getMyUsername());
            setChatBox(messageHandler.getChatLog());
            initLivingRoomGrid();
            initBookshelfGrid();
            initSelectCol();
            destroy();
            System.out.println("i initialized");
        }
    }

    private void resetPersonalGoals() {
        myPersonalGoal.setImage(null);
    }

    private void resetCommonGoals() {
        firstCommonGoalScoreBox.setImage(new Image("/images/scoring_tokens/scoring_8.jpg"));
        secondCommonGoalScoreBox.setImage(new Image("/images/scoring_tokens/scoring_8.jpg"));
    }

    private void clearPlayerList() {
        player1.getChildren().clear();
        player2.getChildren().clear();
        player3.getChildren().clear();
        player4.getChildren().clear();
    }

    private void clearboard() {
        for (int i = 0; i < Board.DIMENSIONS; i++) {
            for (int j = 0; j < Board.DIMENSIONS; j++) {
                itemLivRoomCells[i][j].setImage(null);
            }
        }
    }

    public void initPlayerList(ArrayList<Player> players) {
        playersList = players;
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
            label.setStyle("-fx-font-weight: bold;");
            if (i == 0) {
                label.setStyle("-fx-font-weight: bold;");
                label.setStyle("-fx-text-fill: red");
            }
            /*else if (i % 2 == 0) {
                label.setStyle("-fx-background-color: white;");
            }
            else {
                label.setStyle("-fx-background-color: #f2f2f2;");
            }*/
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

    private int getTileIndex(int col, int row, ArrayList<Square> tilesToDraw) {
        for (int i = 0; i < tilesToDraw.size(); i++) {
            Square tile = tilesToDraw.get(i);
            if (tile.getColumn() == col && tile.getRow() == row) {
                return i;
            }
        }
        return -1;
    }

    private void clearHand() {
        hand1.setImage(null);
        hand2.setImage(null);
        hand3.setImage(null);
    }

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

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public void setNotification(String notification) {
        this.notification.setText(notification);
        this.notification.setVisible(true);
    }

    public void setPlayerState(PlayerState state) {
        this.state = state;
    }

    private void initSelectCol() {
        selectCol1.setUserData(0);
        selectCol2.setUserData(1);
        selectCol3.setUserData(2);
        selectCol4.setUserData(3);
        selectCol5.setUserData(4);
    }

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
                myBookShelfGrid.getChildren().add(cell);
                bookshelfCells[i][j] = cell;
                bookshelfCells[i][j].setFitWidth(cellWidth);
                bookshelfCells[i][j].setFitHeight(cellHeight);
            }
        }
    }

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
                livingRoomGrid.getChildren().add(cell);
                itemLivRoomCells[i][j] = cell;
                itemLivRoomCells[i][j].setFitWidth(cellWidth);
                itemLivRoomCells[i][j].setFitHeight(cellHeight);
            }
        }
    }

    public void setBookshelfAttribute(Bookshelf bookshelf) {
        this.bookshelf = bookshelf;
    }

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
                        item.setFitWidth(cellWidth);
                        item.setFitHeight(cellHeight);
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

    private void setPickableEffect(ImageView image) {
        dropShadow.setRadius(25);
        dropShadow.setColor(Color.web("#006400"));
        image.setEffect(dropShadow);
    }

    public void clearEffects() {
        ImageView item;
        for (int i = 0; i < Board.DIMENSIONS; i++) {
            for (int j = 0; j < Board.DIMENSIONS; j++) {
                item = itemLivRoomCells[i][j];
                item.setEffect(null);
            }
        }
    }

    public void clearTiles() {
        tilesToInsert.clear();
        tilesToDraw.clear();
    }

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

    public void setCommonGoals(ArrayList<CommonGoalCard> commonGoals) {
        URL url;
        Image image;
        url = getClass().getResource("/images/common_goal_cards/" + commonGoals.get(0).getImageId() + ".jpg");
        assert url != null;
        image = new Image(url.toString());
        firstCommonGoal.setImage(image);
        url = getClass().getResource("/images/common_goal_cards/" + commonGoals.get(1).getImageId() + ".jpg");
        assert url != null;
        image = new Image(url.toString());
        secondCommonGoal.setImage(image);
    }

    public void setPlayers(ArrayList<Player> players) {
        this.playersList = players;
    }

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
    public void sendMessage(String message, String recipientusername) {
        ChatMessage chatMessage = new ChatMessage(message, recipientusername);
        messageHandler.notifyObservers(chatMessage);
    }
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
                    } else{
                        player1S.setImage(image);
                    }
                }else if(i==1) {
                    if (player2F.getImage() == null){
                        player2F.setImage(image);
                    } else {
                        player2S.setImage(image);
                    }
                }else if(i==2){
                    if(player3F.getImage()==null){
                        player3F.setImage(image);
                    }else{
                        player3S.setImage(image);
                    }
                }else if(i == 3){
                    if(player4F.getImage() == null){
                        player4F.setImage(image);
                    }else{
                        player4S.setImage(image);
                    }
                }
            }
        }
    }
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
    public void setDrawn(boolean drawn){
        this.drawn = drawn;
    }
}