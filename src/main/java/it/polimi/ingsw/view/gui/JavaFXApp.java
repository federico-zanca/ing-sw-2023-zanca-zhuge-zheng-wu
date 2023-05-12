package it.polimi.ingsw.view.gui;
import it.polimi.ingsw.AppServer;
import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.view.gui.sceneControllers.GuiPhase;
import it.polimi.ingsw.view.tui.ActionType;
import it.polimi.ingsw.view.gui.sceneControllers.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

public class JavaFXApp extends Application{
    private Scene currentScene;
    private Scene oldScene;
    private Stage currentStage;
    private final HashMap<String,Controller> controllers;
    private final HashMap<GuiPhase,String> fxml;
    private final HashMap<String,Scene> scenes;
    private final Gui gui;
    private GuiPhase phase;

    public JavaFXApp () throws RemoteException, NotBoundException {
        controllers = new HashMap<>();
        fxml = new HashMap<>();
        scenes = new HashMap<>();
        phase = GuiPhase.LOGIN;
        Registry registry = LocateRegistry.getRegistry(1099);
        AppServer server = (AppServer) registry.lookup("server");
        gui = new Gui();
        ClientImpl client = new ClientImpl(server.connect(),gui);
    }

    public static void main(String[] args) throws NotBoundException, RemoteException {
        new JavaFXApp();
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
        try {
            for (GameFxml path : fxmlFiles) {
                URL url = getClass().getClassLoader().getResource("fxml/" + path.s);
                FXMLLoader loader = new FXMLLoader(url);
                Scene scene = new Scene(loader.load());
                scenes.put(path.s,scene);
                Controller controller = loader.getController();
                controller.setGui(gui);
                controller.setApp(this);
                controllers.put(path.s, controller);
                //phases.put(scene,path.getGamePhases());
                fxml.put(path.getGuiPhase(),path.s);
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
            controllers.get(fxml.get(phase)).initialize();
            currentStage.show();
        });
    }
    public Scene getCurrentScene() {
        return currentScene;
    }
    public Scene getOldScene() {
        return oldScene;
    }

    public Stage getCurrentStage() {
        return currentStage;
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
}

