<<<<<<<< HEAD:src/main/java/it/polimi/ingsw/model/commongoals/CommonGoalCard.java
package it.polimi.ingsw.model.commongoals;
========
package it.polimi.ingsw.model.CommonGoalCards;
>>>>>>>> c7ffb5150689a86c88a1956d9b79d75593ea373e:src/main/java/it/polimi/ingsw/model/CommonGoalCards/CommonGoalCard.java

import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.Stack;

public abstract class CommonGoalCard {
    private Stack<Integer> points; //attenzione che Integer può anche essere null a differenza di int!!


    private ArrayList<Player> thoseWhoAchieved;

    public CommonGoalCard(int numPlayers){
        points = new Stack<Integer>();

        switch(numPlayers){
            case 2:
                points.push(8);
                points.push(4);
                break;
            case 3:
                points.push(8);
                points.push(6);
                points.push(4);
                break;
            case 4:
                points.push(8);
                points.push(6);
                points.push(4);
                points.push(2);
        }
    }

    public void takePoints(Player p){
        p.addPoints(points.pop());
    }

    public int peek(){
        return points.peek();
    }

    public boolean achievedBy(Player p){
        return thoseWhoAchieved.contains(p);
    }


    public ArrayList<Player> getThoseWhoAchieved() {
        return thoseWhoAchieved;
    }

    public abstract boolean check(ItemTile[][] matrix);
}
