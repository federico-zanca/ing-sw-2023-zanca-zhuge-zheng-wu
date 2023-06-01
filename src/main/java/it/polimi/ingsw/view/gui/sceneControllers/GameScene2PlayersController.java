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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.*;

public class GameScene2PlayersController implements Controller {
    private MessageHandler messageHandler;
    private GUI gui;
    private PlayerState state = PlayerState.WATCHING;
    private ArrayList<Player> playersList;
    private ArrayList<ItemTile> tilesToInsert;
    private ArrayList<Square> tilesToDraw;
    private ActionType actionType = ActionType.NONE;
    private InputValidator inputValidator = new InputValidator();
    private final double cellWidth = 46.5;
    private double cellHeight = 46.5;
    private ImageView[][] itemLivRoomCells = new ImageView[Board.DIMENSIONS][Board.DIMENSIONS];
    private Square[][] board;
    private DropShadow dropShadow = new DropShadow();
    private int maxNumItems;
    private ImageView[][] bookshelfCells = new ImageView[Bookshelf.Rows][Bookshelf.Columns];
    private Bookshelf bookshelf;
    private final List<String> messageHistory = new LinkedList<>();
    private boolean isExpanded = false;
    private final int expandedHeight = 200;
    private final TextField inputField = new TextField();
    private final ScrollPane chatScrollPane = new ScrollPane();
    private final VBox chatMessages = new VBox();
    private double chatMessagesHeight = 0;
    private ArrayList<ImageView> newHandOrder = new ArrayList<>();
    private ArrayList<Integer> order = new ArrayList<>();
    @FXML
    private VBox chatBox;
    @FXML
    private AnchorPane root;
    @FXML
    private Pane bookshelfPane;
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
    private Label namePlayerOnBookshelf;
    @FXML
    private VBox players;
    @FXML
    private Polygon selectCol1, selectCol2, selectCol3, selectCol4, selectCol5;
    @FXML
    private ImageView hand1, hand2, hand3;
    @FXML
    private Rectangle handRegion;
    @FXML
    private Label notification;
    @FXML
    private Button okButton;
    @FXML
    void translateTriangle (MouseEvent event){
        if(state.equals(PlayerState.ACTIVE)){
            if(actionType.equals(ActionType.INSERT_HAND)){
                Polygon polygon = (Polygon) event.getSource();
                Glow glow = new Glow();
                glow.setLevel(0.5);
                polygon.setEffect(glow);
            }
        }
    }
    @FXML
    void clearGlow(MouseEvent event){
        Polygon polygon = (Polygon) event.getSource();
        polygon.setEffect(null);
    }
    @FXML
    void clickedCol(MouseEvent event) {
        if(state.equals(PlayerState.ACTIVE)) {
            if(actionType.equals(ActionType.INSERT_HAND)){
                int colNum = (int) ((Polygon) event.getSource()).getUserData();
                System.out.println("Clicked column: " + colNum);
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
    void clickedHand(MouseEvent event){
        if(state.equals(PlayerState.ACTIVE)){
            if(actionType == ActionType.INSERT_HAND){
                ImageView hand = (ImageView) event.getSource();
                if(!(hand.getEffect() instanceof Glow)){
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
        if(tilesToInsert.size() == 2){
            if(newHandOrder.size() == 2){
                swapOrder();
                order.clear();
                newHandOrder.clear();
            }
        }else if(tilesToInsert.size() == 3) {
            if (newHandOrder.size() == 3) {
                changeOrder();
                order.clear();
                newHandOrder.clear();
            }
        }
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
        if(tilesToInsert.size() == 2){
            System.out.println("imhere");
            Collections.swap(tilesToInsert, 1, 0);
            Image temp = hand1.getImage();
            hand1.setImage(hand2.getImage());
            hand2.setImage(temp);
            hand1.setEffect(null);
            hand2.setEffect(null);
        }
    }

    public void setChatMessage(ChatMessage message) {
        String prefix = "";
        String messageContent = message.getContent();
        Color textColor = Color.BLACK;
        if (message.getReceiver() != null) {
            if(Objects.equals(message.getReceiver(), messageHandler.getMyUsername())){
                prefix = "PRIVATE MESSAGE FROM ";
                messageContent = message.getSender() + ": " + messageContent;

            }else{
                prefix = "PRIVATE MESSAGE TO ";
                messageContent = message.getReceiver() + ": " + messageContent;
            }
            textColor = Color.RED;
        } else{
            messageContent = message.getSender() + ": " + messageContent;
        }
        addMessage(prefix + messageContent);
        Label label = new Label(prefix + messageContent);
        label.setTextFill(textColor);
        label.setMaxWidth(chatMessages.getPrefWidth());
        label.setWrapText(true);
        chatMessages.getChildren().add(label);
        chatMessagesHeight += label.getHeight();
        if (isExpanded) {
            setChatTexts();
        }
    }
    private void startChatting(){
        isExpanded = true;
        setChatTexts();
        chatBox.getChildren().add(chatScrollPane);
        chatBox.getChildren().add(inputField);
        setInputField();
    }
    private void setChatTexts(){
        if(chatBox.getChildren().size() == 1){
            chatBox.getChildren().remove(inputField);
            chatScrollPane.vvalueProperty().bind(chatMessages.heightProperty());
            chatScrollPane.setContent(chatMessages);
            chatScrollPane.prefHeightProperty().bind(chatMessages.heightProperty().add(20));
            chatScrollPane.setFitToHeight(true);
            chatScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            chatScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        }
    }
    private void setInputField() {
        inputField.setAlignment(Pos.BOTTOM_LEFT);
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
        inputField.requestFocus();
    }
    private void collapse() {
        isExpanded = false;
        //chatContainer.getChildren().remove(chatMessages);
        inputField.setOnKeyPressed(null);
        chatBox.getChildren().removeAll(chatScrollPane);
    }
    public void addMessage(String message) {
        messageHistory.add(message);
    }
    public void sendMessage(String message, String recipientusername){
        ChatMessage chatMessage = new ChatMessage(message, recipientusername);
        messageHandler.notifyObservers(chatMessage);
    }
    private void setChatBox() {
        chatBox.setPrefSize(300,expandedHeight);
        chatBox.setAlignment(Pos.BOTTOM_CENTER);
        inputField.setOnMouseClicked(event -> {
            if (!isExpanded) {
                startChatting();
            } else {
                collapse();
            }
        });
        chatBox.getChildren().add(inputField);
        inputField.setPrefWidth(400);
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
        if(gui == null){
            return;
        }else{
            gui.getCurrentStage().setOnCloseRequest(e->{
                System.exit(0);
            });
            notification.setVisible(false);
        }
        setChatBox();
        tilesToDraw = new ArrayList<>();
        tilesToInsert = new ArrayList<>();
        playersList = new ArrayList<>();
        initLivingRoomGrid();
        initBookshelfGrid();
        initSelectCol();
    }
    public void initPlayerList(ArrayList<Player> players) {
        this.players.setSpacing(10);
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            Label label = new Label(p.getUsername());
            label.setStyle("-fx-font-weight: bold;");
            if (i == 0) {
                label.setStyle("-fx-text-fill: red");
            }
            else if (i % 2 == 0) {
                label.setStyle("-fx-background-color: white;");
            }
            else {
                label.setStyle("-fx-background-color: #f2f2f2;");
            }
            this.players.getChildren().add(label);
            label.setOnMousePressed(event ->{
                    String username = label.getText();
                    showOtherBookshelf(username);
            });
            label.setOnMouseReleased(event -> {
                setBookshelf(bookshelf);
            });
        }

    }
    private void showOtherBookshelf(String username) {
        for(Player p : playersList){
            if(p.getUsername().equals(username) && p.getBookshelf() != null && p.getBookshelf()!=bookshelf){
                setBookshelf(p.getBookshelf());
                break;
            }
        }
    }
    @FXML
    void clickedItemLivRoom(MouseEvent event,int row,int col){
        Glow glow = new Glow();
        if(state.equals(PlayerState.ACTIVE)){
            if(actionType == ActionType.DRAW_TILES){
                if(board[row][col].isPickable()){
                    System.out.println("tessere in pos :"+row+col);
                    if(inputValidator.isTileAlreadyOnHand(row,col,tilesToDraw)){
                        int tileIndex = getTileIndex(col, row, tilesToDraw);
                        tilesToDraw.remove(tileIndex);
                        itemLivRoomCells[row][col].setEffect(null);
                        setPickableEffect(itemLivRoomCells[row][col]);
                        System.out.println("null");
                        return;
                    }else if(tilesToDraw.size()>0 && !inputValidator.inLineTile(row,col,tilesToDraw)){
                        return;
                    }
                    tilesToDraw.add(new Square(new Coordinates(row, col), board[row][col].getItem().getType()));
                    glow.setLevel(0.5);
                    itemLivRoomCells[row][col].setEffect(glow);
                    if (inputValidator.isPossibleToDrawMore(tilesToDraw, board) && tilesToDraw.size()<Math.min(3,maxNumItems)){
                        return;
                    }
                    System.out.println(tilesToDraw);
                }else{
                    return;
                }
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
    @FXML
    void clickedOk(){
        if(state.equals(PlayerState.ACTIVE)){
            if(tilesToDraw.size() != 0){
                setPlayerState(PlayerState.WATCHING);
                messageHandler.notifyObservers(new DrawTilesMessage(messageHandler.getMyUsername(),tilesToDraw));
                System.out.println(tilesToDraw);
                clearEffects();
            }
        }
    }
    private void clearHand() {
        hand1.setImage(null);
        hand2.setImage(null);
        hand3.setImage(null);
    }
    public void setHand(ArrayList<ItemTile> hand){
        tilesToInsert = hand;
        URL url;
        for(int i=0;i<hand.size();i++){
            if(i == 0){
                url = getClass().getResource("/images/item_tiles/" + hand.get(i).getType().name() + "1.png");
                assert url != null;
                hand1.setImage(new Image(url.toString()));
                //handOrder.add(hand1);
            }else if(i == 1){
                url = getClass().getResource("/images/item_tiles/" + hand.get(i).getType().name() + "1.png");
                assert url != null;
                hand2.setImage(new Image(url.toString()));
               // handOrder.add(hand2);
            }else{
                url = getClass().getResource("/images/item_tiles/" + hand.get(i).getType().name() + "1.png");
                assert url != null;
                hand3.setImage(new Image(url.toString()));
              //  handOrder.add(hand3);
            }
        }
    }
    public void setActionType(ActionType actionType){
        this.actionType = actionType;
    }
    public void setNotification(String notification){
        this.notification.setText(notification);
        this.notification.setVisible(true);
    }
    public void setPlayerState(PlayerState state){
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
        for (int i = 0; i < Bookshelf.Rows; i++) {
            for (int j = 0; j < Bookshelf.Columns; j++) {
                ImageView cell = new ImageView();
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
        for (int i = 0; i < Board.DIMENSIONS; i++) {
            for (int j = 0; j < Board.DIMENSIONS; j++) {
                ImageView cell = new ImageView();
                GridPane.setRowIndex(cell, i);
                GridPane.setColumnIndex(cell, j);
                livingRoomGrid.getChildren().add(cell);
                itemLivRoomCells[i][j] = cell;
                itemLivRoomCells[i][j].setFitWidth(cellWidth);
                itemLivRoomCells[i][j].setFitHeight(cellHeight);
            }
        }
    }
    public void setBookshelfAttribute(Bookshelf bookshelf){
        this.bookshelf = bookshelf;
    }
    public void setBookshelf(Bookshelf bookshelf){
        if(bookshelf != null){
            ItemTile[][] shelfie = bookshelf.getShelfie();
            ItemType type;
            Image image;
            URL url;
            ImageView item;
            for(int i=0;i<Bookshelf.Rows;i++){
                for(int j=0;j<Bookshelf.Columns;j++){
                    item = bookshelfCells[i][j];
                    if(shelfie[i][j].hasSomething()){
                        type = shelfie[i][j].getType();
                        url = getClass().getResource("/images/item_tiles/" + type.name() +"1.png");
                        assert url != null;
                        image = new Image(url.toString());
                        item.setImage(image);
                        item.setFitWidth(cellWidth);
                        item.setFitHeight(cellHeight);
                        item.setPreserveRatio(true);
                    }else{
                        item.setImage(null);
                    }
                }
            }
        }else{
            ImageView item;
            for(int i=0;i<Bookshelf.Rows;i++){
                for(int j=0;j<Bookshelf.Columns;j++){
                        item = bookshelfCells[i][j];
                        item.setImage(null);
                }
            }
        }
    }
    public void setBoard(Square[][] board,int maxNumItems){
        this.board = board;
        this.maxNumItems = maxNumItems;
        ItemType type;
        Random random = new Random();
        Image image;
        URL url;
        int num;
        ImageView item;

        for(int i=0;i<Board.DIMENSIONS;i++){
            for(int j=0;j<Board.DIMENSIONS;j++){
                item = itemLivRoomCells[i][j];
                if(board[i][j].getItem().hasSomething()){
                    type = board[i][j].getItem().getType();
                    num = random.nextInt(3) + 1;
                    url = getClass().getResource("/images/item_tiles/" + type.name() + num + ".png");
                    assert url != null;
                    image = new Image(url.toString());
                    item.setImage(image);
                    item.setFitWidth(cellWidth); //cellWidth is the width of each cell
                    item.setFitHeight(cellHeight); //cellHeight is the height of each cell
                    item.setPreserveRatio(true); //cellHeight is the height of each cell
                    if(board[i][j].isPickable()){
                        ImageView finalItem = item;
                        item.setOnMouseClicked(mouseEvent -> {
                            int rowIndex = GridPane.getRowIndex(finalItem);
                            int colIndex = GridPane.getColumnIndex(finalItem);
                            clickedItemLivRoom(mouseEvent,rowIndex,colIndex);
                        });
                        setPickableEffect(item);
                    }
                }else{
                    item.setImage(null);
                }
            }
        }
    }
    private void setPickableEffect(ImageView image){
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
    public void clearTiles(){
        tilesToInsert.clear();
        tilesToDraw.clear();
    }
    public void setPersonalGoalCardImage(){
        URL url;
        Image image;
        int num = gui.getPersonalGoalCard().getIdentificator();
        url = getClass().getResource("/images/personal_goal_cards/Personal_Goals"+num+".png");
        assert url != null;
        image = new Image(url.toString());
        myPersonalGoal.setImage(image);
        myPersonalGoal.setVisible(true);
    }
    public void setCommonGoals(ArrayList<CommonGoalCard> commonGoals){
        URL url;
        Image image;
        int num = 1;
        url = getClass().getResource("/images/common_goal_cards/"+num+".jpg");
        assert url != null;
        image = new Image(url.toString());
        firstCommonGoal.setImage(image);
        url = getClass().getResource("/images/common_goal_cards/"+num+".jpg");
        assert url != null;
        image = new Image(url.toString());
        secondCommonGoal.setImage(image);
    }
    public void setPlayers(ArrayList<Player> players){
        this.playersList = players;
    }
}
