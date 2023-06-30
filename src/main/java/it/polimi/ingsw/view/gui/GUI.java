package it.polimi.ingsw.view.gui;
import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.commongoals.CommonGoalCard;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import it.polimi.ingsw.network.message.ChatMessage;
import it.polimi.ingsw.view.gui.sceneControllers.*;
import it.polimi.ingsw.view.tui.ActionType;
import it.polimi.ingsw.view.tui.LobbyDisplayInfo;
import it.polimi.ingsw.view.tui.PlayerState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

public class GUI extends Application{
    private Scene currentScene;

    private Stage currentStage;
    private final HashMap<String,Controller> controllers;
    private final HashMap<GuiPhase,String> fxml;
    private final HashMap<String,Scene> scenes;
    private final MessageHandler messageHandler;
    private HashMap<String,EndGameScores> scores;
    private GuiPhase phase;
    private PersonalGoalCard personalGoalCard;

    public static void main(String[] args) throws NotBoundException, RemoteException {
        new GUI();
        System.setProperty("prism.allowhidpi", "true"); // Enable DPI scaling awareness
        launch(args);
    }
    /**

     Constructs a new instance of the GUI.
     @throws RemoteException If a remote exception occurs.
     @throws NotBoundException If a not bound exception occurs.
     */
    public GUI() throws RemoteException, NotBoundException {
        controllers = new HashMap<>();
        fxml = new HashMap<>();
        scenes = new HashMap<>();
        phase = GuiPhase.LOGIN;
        messageHandler = new MessageHandler(this);
        personalGoalCard = null;
    }

    /**

     Starts the GUI by initializing the stage and loading the appropriate FXML file.
     @param stage The JavaFX stage to be used for the GUI.
     @throws Exception If an exception occurs during the GUI initialization.
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.currentStage = stage;
        initializationFXMLParameter();
        initMenuStage();
        String fxmlPath = fxml.get(phase);
        controllers.get(fxmlPath).initialize();
    }

    /**

     Initializes the parameters related to FXML files and controllers.
     Loads the FXML files, creates scenes, and assigns controllers to each scene.
     Sets up the message handler and assigns the GUI to each controller.
     Populates the scenes, controllers, and fxml maps accordingly.
     */
    public void initializationFXMLParameter() {
        List<GameFxml> fxmlFiles = new ArrayList<>(Arrays.asList(GameFxml.values()));
        URL url = null;
        FXMLLoader loader = null;
        Scene scene = null;
        Controller controller = null;
        try {
            Font.loadFont(Objects.requireNonNull(this.getClass().getResource("/font/CurlzMT.ttf")).toExternalForm(),10);
            for (GameFxml path : fxmlFiles) {
                url = getClass().getClassLoader().getResource("fxml/" + path.s);
                if(url != null){
                    loader = new FXMLLoader(url);
                    scene = new Scene(loader.load());
                    scenes.put(path.s,scene);
                    controller = loader.getController();
                    controller.setMessageHandler(messageHandler);
                    controller.setGui(this);
                    controllers.put(path.s, controller);
                    //phases.put(scene,path.getGamePhases());
                    fxml.put(path.getGuiPhase(),path.s);
                }else{
                    throw new FileNotFoundException("FXML file not found: " + path.s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentScene = scenes.get(GameFxml.CONNECTION_SCENE.s);
    }

    /**

     Initializes the menu stage by setting its title, scene, size, and position.
     Displays the menu stage.
     */
    public void initMenuStage(){
        currentStage.setTitle("My shelfie Board Game");
        currentStage.setScene(currentScene);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = primaryScreenBounds.getWidth();
        double screenHeight = primaryScreenBounds.getHeight();
        currentStage.setHeight(screenHeight);
        currentStage.setWidth(screenWidth);
        currentStage.getScene().getWindow().centerOnScreen();
        currentStage.show();
    }

    /**

     Changes the current scene of the GUI to the specified scene.
     Updates the stage with the new scene and initializes the corresponding controller.
     This method is designed to be run on the JavaFX application thread using Platform.runLater().
     */
    public void changeScene() {
        Platform.runLater(() -> {
            currentStage.setScene(currentScene);
            currentScene.getWindow().setWidth(currentScene.getWindow().getWidth());
            String fxmlPath = fxml.get(phase);
            controllers.get(fxmlPath).initialize();
        });
    }
    /**

     Sets the current scene of the GUI to the specified scene.
     @param currentScene The new scene to set as the current scene.
     */
    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }
    /**

     Returns the current scene of the GUI.
     @return The current scene of the GUI.
     */
    public Scene getCurrentScene() {
        return currentScene;
    }
    /**

     Sets the phase of the GUI to the specified phase.
     @param phase The new phase to set.
     */
    public void setPhase(GuiPhase phase) {
        this.phase = phase;
    }

    /**
     * Returns the scene associated with the given name.
     *
     * @param s The name of the scene.
     * @return The scene associated with the given name, or null if not found.
     */
    public Scene getScene(String s) {
        return scenes.get(s);
    }

    /**
     * Sets the error message to be displayed in the current scene.
     *
     * @param error The error message to be displayed.
     */
    public void setError(String error) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof MenuSceneController){
            Platform.runLater(()->{
                ((MenuSceneController)currentController).setError(error);
            });
        }
        if(currentController instanceof ServerSceneController){
            Platform.runLater(()->{
                ((ServerSceneController)currentController).setError(error);
            });
        }
        if(currentController instanceof InLobbySceneController){
            Platform.runLater(()->{
                ((InLobbySceneController)currentController).setError(error);
            });
        }
    }
    /**
     * Sets the names of all players to be displayed in the current scene.
     *
     * @param allPlayersNames The list of player names.
     */
    public void setAllPlayersNames(ArrayList<String> allPlayersNames){
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof InLobbySceneController){
            Platform.runLater(()->{
                ((InLobbySceneController)currentController).setPlayerNames(allPlayersNames);
            });
        }
    }
    /**
     * Sets the name of the admin to be displayed in the current scene.
     *
     * @param username The admin's username.
     */
    public void setAdminName(String username){
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof InLobbySceneController){
            Platform.runLater(()-> {
                ((InLobbySceneController) currentController).setAdminName(username);
            });
        }
    }
    /**
     * Retrieves the current stage.
     *
     * @return The current stage.
     */
    public Stage getCurrentStage() {
        return currentStage;
    }
    /**
     * Sets the list of lobby display information.
     *
     * @param lobbies The list of lobby display information.
     */
    public void setLobbies(ArrayList<LobbyDisplayInfo> lobbies){
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof ServerSceneController){
            Platform.runLater(()->{
                ((ServerSceneController)currentController).showLobbies(lobbies);
            });
        }
    }
    /**
     * Updates the server scene controller to display the list of lobbies.
     */
    public void lobbyList(){
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof ServerSceneController){
            Platform.runLater(()->{
                ((ServerSceneController)currentController).lobbyList();
            });
        }
    }
    /**
     * Sets the game scene with the provided game model.
     *
     * @param model The game model containing the necessary information.
     */
    public void setGameScene(GameView model) {
        Controller currentController = controllers.get(fxml.get(phase));
        if (currentController instanceof GameScene2PlayersController) {
            Platform.runLater(() -> {
                ((GameScene2PlayersController) currentController).setCommonGoals(model.getCommonGoals());
                ((GameScene2PlayersController) currentController).setBoard(model.getBoard().getGameboard(), 0);
                ((GameScene2PlayersController) currentController).setPersonalGoalCardImage();
                //((GameScene2PlayersController) currentController).initPlayerList(model.getPlayers());
            });
        }
    }
    /**
     * Sets the reconnected game scene with the provided game model and reconnected player.
     *
     * @param model The game model containing the necessary information.
     * @param reconnectedPlayer The reconnected player.
     */
    public void setReconnectedGameScene(GameView model, Player reconnectedPlayer) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()->{
                ((GameScene2PlayersController)currentController).setCommonGoals(model.getCommonGoals());
                ((GameScene2PlayersController)currentController).setBoard(model.getBoard().getGameboard(),0);
                ((GameScene2PlayersController)currentController).setBookshelfAttribute(reconnectedPlayer.getBookshelf());
                ((GameScene2PlayersController)currentController).setBookshelf(reconnectedPlayer.getBookshelf());
                ((GameScene2PlayersController)currentController).setPersonalGoalCardImage();
                ((GameScene2PlayersController)currentController).initPlayerList(model.getPlayers());
            });
        }
    }
    /**
     * Sets the game board in the game scene with the provided board and maximum number of items.
     *
     * @param board The game board represented by a 2D array of Square objects.
     * @param maxNumItems The maximum number of items allowed on the board.
     */
    public void setGameBoard(Square[][] board, int maxNumItems){
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()->{
                ((GameScene2PlayersController)currentController).setBoard(board,maxNumItems);
            });
        }
    }
    /**
     * Sets the player state in the game scene to the provided state.
     *
     * @param state The player state to set.
     */
    public void setPlayerState(PlayerState state){
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()->{
                ((GameScene2PlayersController)currentController).setPlayerState(state);
            });
        }
    }
    /**
     * Sets the game notification in the game scene to the provided notification.
     *
     * @param notification The game notification to set.
     */
    public void setGameNotification(String notification) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()->{
                ((GameScene2PlayersController)currentController).setNotification(notification);
            });
        }
    }
    /**
     * Sets the hand of the player in the game scene to the provided list of item tiles.
     *
     * @param hand The list of item tiles representing the player's hand.
     */
    public void setHand(ArrayList<ItemTile> hand) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()->{
                ((GameScene2PlayersController)currentController).setHand(hand);
            });
        }
    }
    /**
     * Sets the action type for the player in the game scene.
     *
     * @param actionType The action type to be set for the player.
     */
    public void setActionType(ActionType actionType) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()->{
                ((GameScene2PlayersController)currentController).setActionType(actionType);
            });
        }
    }
    /**
     * Sets the bookshelf for the player in the game scene.
     *
     * @param bookshelf The bookshelf to be set for the player.
     */
    public void setBookshelf(Bookshelf bookshelf) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            ((GameScene2PlayersController)currentController).setBookshelfAttribute(bookshelf);
            Platform.runLater(()->{
                ((GameScene2PlayersController)currentController).setBookshelf(bookshelf);
            });
        }
    }
    /**
     * Clears the tiles form the array of drawn tiles and tiles to be inserted.
     */
    public void clearTiles(){
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()->{
                ((GameScene2PlayersController)currentController).clearTiles();
            });
        }
    }
    /**
     * Sets the chat message in the game scene or lobby scene.
     *
     * @param message The chat message to be displayed.
     */
    public void setChatMessage(ChatMessage message) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()->{
                ((GameScene2PlayersController)currentController).setChat(message);
            });
        }
        if(currentController instanceof InLobbySceneController){
            Platform.runLater(()->{
                ((InLobbySceneController)currentController).setChat(message);
            });
        }
    }

    /**
     * Sets the value of the spinner in the lobby scene.
     *
     * @param value The value to be set in the spinner.
     */
    public void setSpinnerValue(Integer value) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof InLobbySceneController){
            Platform.runLater(()-> {
                ((InLobbySceneController) currentController).setSpinnerValue(value);
            });
        }
    }
    /**
     * Disables the spinner icon (reload button) in the lobby scene.
     */
    public void setSpinnerDisable() {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof InLobbySceneController){
            Platform.runLater(()-> {
                ((InLobbySceneController) currentController).setSpinner();
            });
        }
    }
    /**
     * Sets the list of players in the game scene.
     *
     * @param players The list of players.
     */
    public void setPlayers(ArrayList<Player> players) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()->{
                ((GameScene2PlayersController)currentController).setPlayers(players);
            });
        }
    }
    /**
     * Sets the leaderboard with the ranking of players at the end of the game.
     *
     * @param ranking The ranking of players with their scores.
     */
    public void setLeaderBoard(LinkedHashMap<String,Integer> ranking) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof EndGameController){
            Platform.runLater(()->{
                ((EndGameController)currentController).setRanking(ranking);
            });
        }
    }

    /**
     * Sets the turn indicator to the current player in the game scene.
     *
     * @param currentPlayer The username of the current player.
     */
    /*
    public void setTurnIndicator(String currentPlayer) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()-> {
                ((GameScene2PlayersController) currentController).moveTurnIndicator(currentPlayer);
            });
        }
    }

     */
    /**
     * Sets an alert in the server scene.
     * This is used to notify the user about important server-related events or messages.
     */
    public void setAlert() {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof ServerSceneController){
            Platform.runLater(()-> {
                ((ServerSceneController) currentController).setAlert();
            });
        }
    }
    /**
     * Sets icons for a player in the game scene.
     * This method is used to display the player's username, their assigned common goal,
     * and the image of the common goal card.
     *
     * @param username The username of the player.
     * @param commonGoal The index of the assigned common goal.
     * @param cg The CommonGoalCard object representing the common goal card.
     */
    public void setIcons(String username, int commonGoal,CommonGoalCard cg) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()-> {
                ((GameScene2PlayersController) currentController).setIcons(username,commonGoal,cg);
            });
        }
    }
    /**
     * Sets the flag indicating whether a player has already drawn some item tiles during its turn or not.
     * This method is used to update the UI and eventually disable the DRAW button when the player should not use it anymore.
     *
     * @param drawn A boolean value indicating whether the player has already drawn the item tiles.
     */
    public void setDrawn(boolean drawn){
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            ((GameScene2PlayersController) currentController).setDrawn(drawn);
        }
    }

    public void addLastTurnScore(String player) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()-> {
                ((GameScene2PlayersController) currentController).addLastTurnScores(player);
            });
        }
    }

    public void addPersonalScore(String playerUsername, int points) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()-> {
                ((GameScene2PlayersController) currentController).addPersonalScores(playerUsername,points);
            });
        }
    }

    public void addAdjacentPoints(String playerUsername, int points) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()-> {
                ((GameScene2PlayersController) currentController).addAdjacentScores(playerUsername,points);
            });
        }
    }

    public HashMap<String,EndGameScores> getScores() {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            scores = ((GameScene2PlayersController) currentController).getScores();
        }
        return scores;
    }

    public void setScoreBoard(HashMap<String, EndGameScores> scores) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof EndGameController){
            Platform.runLater(()->{
                ((EndGameController)currentController).setScores(scores);
            });
        }
    }

    public void setLastTurnIcon(String currentPlayer) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()-> {
                ((GameScene2PlayersController) currentController).setLastTurnIcon(currentPlayer);
            });
        }
    }
}

