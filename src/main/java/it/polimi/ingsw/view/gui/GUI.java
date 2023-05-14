package it.polimi.ingsw.view.gui;
import it.polimi.ingsw.AppServer;
import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.view.gui.sceneControllers.GuiPhase;
import it.polimi.ingsw.view.gui.sceneControllers.Controller;
import it.polimi.ingsw.view.gui.sceneControllers.InLobbySceneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
    private String error;
    public GUI() throws RemoteException, NotBoundException {
        controllers = new HashMap<>();
        fxml = new HashMap<>();
        scenes = new HashMap<>();
        phase = GuiPhase.LOGIN;
        messageHandler = new MessageHandler(this);
        try{
            Registry registry = LocateRegistry.getRegistry(1099);
            AppServer server = (AppServer) registry.lookup("server");
            ClientImpl client = new ClientImpl(server.connect(), messageHandler);
        }catch(RemoteException | NotBoundException e){
            e.printStackTrace();
        }
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
        currentScene = scenes.get(GameFxml.MENU_SCENE.s);
    }

    public void initMenuStage(){
        currentStage.setTitle("My shelfie Board Game");
        currentStage.setScene(currentScene);
        currentStage.setWidth(1280d);
        currentStage.setHeight(720d);
        currentStage.setResizable(false);
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    public void setAllPlayersNames(ArrayList<String> allPlayersNames){
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof InLobbySceneController){
            ((InLobbySceneController)currentController).setPlayerNames(allPlayersNames);
        }
    }
    public void setAdminName(String username){
        Controller currentController = controllers.get(fxml.get(phase));
        if(currentController instanceof InLobbySceneController){
            ((InLobbySceneController)currentController).setAdminName(username);
        }
    }

}

