
package it.polimi.ingsw.model.commongoals;


import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Stack;

public abstract class CommonGoalCard {
    //TODO switch to appropriate design pattern
    private Stack<Integer> points; //attenzione che Integer può anche essere null a differenza di int!!
    final int NROW = 6;
    final int NCOL = 5;

    private ArrayList<Player> thoseWhoAchieved;

    public CommonGoalCard(int numPlayers){
        points = new Stack<Integer>();

        switch(numPlayers){
            case 2:
                points.push(4);
                points.push(8);
                break;
            case 3:
                points.push(4);
                points.push(6);
                points.push(8);
                break;
            case 4:
                points.push(2);
                points.push(4);
                points.push(6);
                points.push(8);
        }
    }

    public void takePoints(@NotNull Player p){
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

    public abstract boolean check(Bookshelf matrix);
}
