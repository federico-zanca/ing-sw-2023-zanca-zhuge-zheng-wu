package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GameFxml;
import it.polimi.ingsw.view.gui.MessageHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.LinkedHashMap;


public class EndGameController implements Controller{
    public ImageView fourthRow;
    public ImageView bronze;
    public ImageView thirdRow;
    public Label quattro;
    public BorderPane root;
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
            return;
        }else{
            thirdRow.setVisible(false);
            fourthRow.setVisible(false);
            bronze.setVisible(false);
            quattro.setVisible(false);
            clear();
            exit.setOnAction(e->{
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
            }else if(count == 1){
                secondPlace.setText(key);
                secondScores.setText((ranking.get(key).toString()));
            }else if(count == 2){
                if(ranking.get(key) == null){
                    break;
                }else{
                    thirdRow.setVisible(true);
                    bronze.setVisible(true);
                }
                thirdPlace.setText(key);
                thirdScores.setText((ranking.get(key).toString()));
            }else{
                if(ranking.get(key) == null){
                    break;
                }else{
                    fourthRow.setVisible(true);
                    quattro.setVisible(true);
                }
                fourthPlace.setText(key);
                fourthScores.setText((ranking.get(key).toString()));
            }
            count++;
        }
    }
}
