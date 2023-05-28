package it.polimi.ingsw.view.gui;
import it.polimi.ingsw.AppServer;
import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.gameboard.Board;
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
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GUI extends Application{
    private Scene currentScene;
    private Stage currentStage;
    private final HashMap<String,Controller> controllers;
    private final HashMap<GuiPhase,String> fxml;
    private final HashMap<String,Scene> scenes;
    private final MessageHandler messageHandler;
    private GuiPhase phase;
    private PersonalGoalCard personalGoalCard;
    public GUI() throws RemoteException, NotBoundException {
        controllers = new HashMap<>();
        fxml = new HashMap<>();
        scenes = new HashMap<>();
        phase = GuiPhase.LOGIN;
        messageHandler = new MessageHandler(this);
        personalGoalCard = null;
    }

    public static void main(String[] args) throws NotBoundException, RemoteException {
        new GUI();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.currentStage = stage;
        initializationFXMLParameter();
        initMenuStage();
    }

    public void initializationFXMLParameter() {
        List<GameFxml> fxmlFiles = new ArrayList<>(Arrays.asList(GameFxml.values()));
        URL url = null;
        FXMLLoader loader = null;
        Scene scene = null;
        Controller controller = null;
        try {
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

    public void initMenuStage(){
        currentStage.setTitle("My shelfie Board Game");
        currentStage.setScene(currentScene);
        currentStage.setWidth(1280d);
        currentStage.setHeight(720d);
        currentStage.setResizable(true);
        currentStage.show();
    }

    public void changeScene() {
        Platform.runLater(()->{
            currentStage.setScene(currentScene);
            String fxmlPath = fxml.get(phase);
            controllers.get(fxmlPath).initialize();
        });
    }
    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public GuiPhase getPhase() {
        return phase;
    }

    public void setPhase(GuiPhase phase) {
        this.phase = phase;
    }

    public Scene getScene(String s) {
        return scenes.get(s);
    }

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
    public void setAllPlayersNames(ArrayList<String> allPlayersNames){
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof InLobbySceneController){
            Platform.runLater(()->{
                ((InLobbySceneController)currentController).setPlayerNames(allPlayersNames);
            });
        }
    }
    public void setAdminName(String username){
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof InLobbySceneController){
            Platform.runLater(()-> {
                ((InLobbySceneController) currentController).setAdminName(username);
            });
        }
    }

    public Stage getCurrentStage() {
        return currentStage;
    }
    public void setLobbies(ArrayList<LobbyDisplayInfo> lobbies){
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof ServerSceneController){
            Platform.runLater(()->{
                ((ServerSceneController)currentController).showLobbies(lobbies);
            });
        }
    }

    public void setGameScene(GameView model) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()->{
                ((GameScene2PlayersController)currentController).setCommonGoals(model.getCommonGoals());
                ((GameScene2PlayersController)currentController).setBoard(model.getBoard().getGameboard());
                ((GameScene2PlayersController)currentController).setPersonalGoalCardImage();
            });
        }
    }
    public void setGameBoard(Square[][] board){
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()->{
                ((GameScene2PlayersController)currentController).setBoard(board);
            });
        }
    }
    public void setPlayerState(PlayerState state){
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()->{
                ((GameScene2PlayersController)currentController).setPlayerState(state);
            });
        }
    }

    public void setGameNotification(String notification) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()->{
                ((GameScene2PlayersController)currentController).setNotification(notification);
            });
        }
    }

    public void setHand(ArrayList<ItemTile> hand) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()->{
                ((GameScene2PlayersController)currentController).setHand(hand);
            });
        }
    }

    public void setActionType(ActionType actionType) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()->{
                ((GameScene2PlayersController)currentController).setActionType(actionType);
            });
        }
    }

    public void setBookshelf(Bookshelf bookshelf) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()->{
                ((GameScene2PlayersController)currentController).setBookshelf(bookshelf);
            });
        }
    }
    public void clearTiles(){
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()->{
                ((GameScene2PlayersController)currentController).clearTiles();
            });
        }
    }

    public void setChatMessage(ChatMessage message) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof GameScene2PlayersController){
            Platform.runLater(()->{
                ((GameScene2PlayersController)currentController).setChatMessage(message);
            });
        }
    }
    public void setSpinnerValue(Integer value) {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof InLobbySceneController){
            Platform.runLater(()-> {
                ((InLobbySceneController) currentController).setSpinnerValue(value);
            });
        }
    }
    public void setSpinnerDisable() {
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof InLobbySceneController){
            Platform.runLater(()-> {
                ((InLobbySceneController) currentController).setSpinner();
            });
        }
    }

    public void setPersonalGoalCard(PersonalGoalCard personalGoalCard) {
        this.personalGoalCard = personalGoalCard;
    }

    public PersonalGoalCard getPersonalGoalCard() {
        return personalGoalCard;
    }
}

