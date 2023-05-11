package it.polimi.ingsw.view.gui;
import it.polimi.ingsw.view.tui.ActionType;
import it.polimi.ingsw.view.gui.sceneControllers.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class JavaFXApp extends Application{
    private Scene currentScene;
    private Scene oldScene;
    private Stage currentStage;
    private final HashMap<String,Controller> controllers;
    private final HashMap<ActionType,String> fxml;
    private final HashMap<String,Scene> scenes;

    private ActionType actionType;

    public JavaFXApp (){
        controllers = new HashMap<>();
        fxml = new HashMap<>();
        scenes = new HashMap<>();
        actionType = ActionType.LOGIN;
    }

    public static void main(String[] args){
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
                controller.setApp(this);
                controllers.put(path.s, controller);
                //phases.put(scene,path.getGamePhases());
                fxml.put(path.getActionType(),path.s);
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
            controllers.get(fxml.get(actionType)).initialize();
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

    public ActionType getActionType() {
        return actionType;
    }
}

