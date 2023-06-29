package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.view.gui.EndGameScores;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GameFxml;
import it.polimi.ingsw.view.gui.MessageHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class EndGameController implements Controller{
    public Label first;
    public Label second;
    public Label third;
    public Label fourth;
    public ImageView gold;
    public ImageView silver;
    public ImageView bronze;
    public TableView<EndGameScores> scoreTable;
    public TableColumn<EndGameScores,Integer> adjacent;
    public TableColumn<EndGameScores,Integer> personal;
    public TableColumn<EndGameScores,Integer> lastpoint;
    public TableColumn<EndGameScores,Integer> total;
    private MessageHandler messageHandler;
    private GUI gui;
    private HashMap<String,EndGameScores> scores;
    @FXML
    private Button exit;
    @Override
    public void setMessageHandler(MessageHandler messageHandler) {this.messageHandler = messageHandler;}

    @Override
    public void setGui(GUI gui) {this.gui = gui;}

    @Override
    public void initialize() {
        if (gui == null) {
        }else{
            scores = new HashMap<>();
            //adjacent = new TableColumn<>("Adjacent Points");
            adjacent.setCellValueFactory(new PropertyValueFactory<EndGameScores,Integer>("adjacent points"));
            //personal = new TableColumn<>("Personal Points");
            personal.setCellValueFactory(new PropertyValueFactory<EndGameScores,Integer>("personal points"));
            //lastpoint = new TableColumn<>("Last Point");
            lastpoint.setCellValueFactory(new PropertyValueFactory<EndGameScores,Integer>("last point"));
            //total = new TableColumn<>("Total Points");
            total.setCellValueFactory(new PropertyValueFactory<EndGameScores,Integer>("total points"));

        }
    }

    private void clear() {
    }

    public void setRanking(LinkedHashMap<String, Integer> ranking){
        int count=0;
        for (String key : ranking.keySet()) {
            if(count == 0){
                first.setText(key);
            }else if(count == 1){
                second.setText(key);
            }else if(count == 2){
                if(ranking.get(key) == null){
                    break;
                }else{
                    //thirdRow.setVisible(true);
                    bronze.setVisible(true);
                }
                third.setText(key);
                //thirdScores.setText((ranking.get(key).toString()));
            }else{
                if(ranking.get(key) == null){
                    break;
                }else{
                    //fourthRow.setVisible(true);
                    //quattro.setVisible(true);
                }
                fourth.setText(key);
                //fourthScores.setText((ranking.get(key).toString()));
            }
            count++;
        }
    }
    public void setScores(HashMap<String, EndGameScores> scores){
        this.scores = scores;
        Set<String> stringSet = scores.keySet();
        List<String> stringList = new ArrayList<>(stringSet);
        for(String name : stringList){
            scoreTable.getItems().add(scores.get(name));
        }
    }
}
