package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GameFxml;
import it.polimi.ingsw.view.gui.MessageHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.LinkedHashMap;

public class EndGameController implements Controller{
    private MessageHandler messageHandler;
    private GUI gui;
    @FXML
    private Label firstPlace;
    @FXML
    private Label secondPlace;
    @FXML
    private Label thirdPlace;
    @FXML
    private Label fourthPlace;
    @FXML
    private Text firstScores;
    @FXML
    private Text secondScores;
    @FXML
    private Text thirdScores;
    @FXML
    private Text fourthScores;
    @FXML
    private Button exit;
    @Override
    public void setMessageHandler(MessageHandler messageHandler) {this.messageHandler = messageHandler;}

    @Override
    public void setGui(GUI gui) {this.gui = gui;}

    @Override
    public void initialize() {
        if (gui == null) {
            System.out.println("do nothing");
        }else{
            System.out.println("do nothing for now");
            Stage stage = (Stage) exit.getScene().getWindow();
            stage.sizeToScene();
            stage.centerOnScreen();
            clear();
            gui.getCurrentStage().setOnCloseRequest(e->{
                gui.setPhase(GuiPhase.SERVER);
                gui.setCurrentScene(gui.getScene(GameFxml.SERVER_SCENE.s));
                gui.changeScene();
            });
        }
    }

    private void clear() {
        firstPlace.setText(null);
        firstScores.setText(null);
        secondPlace.setText(null);
        secondScores.setText(null);
        thirdPlace.setText(null);
        thirdScores.setText(null);
        fourthPlace.setText(null);
        fourthScores.setText(null);
    }

    public void setRanking(LinkedHashMap<String, Integer> ranking){
        int count=0;
        for (String key : ranking.keySet()) {
            if(count == 0){
                firstPlace.setText(key);
                firstScores.setText((ranking.get(key).toString()));
            }else if(count ==1){
                secondPlace.setText(key);
                secondScores.setText((ranking.get(key).toString()));
            }else if(count == 2){
                thirdPlace.setText(key);
                thirdScores.setText((ranking.get(key).toString()));
            }else{
                fourthPlace.setText(key);
                fourthScores.setText((ranking.get(key).toString()));
            }
            count++;
        }
    }

}
