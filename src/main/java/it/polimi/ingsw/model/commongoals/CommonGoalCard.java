
package it.polimi.ingsw.model.commongoals;


import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Abstract class for the common goal cards.
 */
public abstract class CommonGoalCard implements Serializable {
private static final long serialVersionUID = 2348200362905242941L;
    //TODO switch to appropriate design pattern
    private Stack<Integer> points; //attenzione che Integer può anche essere null a differenza di int!!
    final int NROW = Bookshelf.Rows;
    final int NCOL = Bookshelf.Columns;

    private ArrayList<Player> thoseWhoAchieved;

    /**
     * Initializes the common goal card with the specified number of players.
     * Setting initial points stack based on number of players.
     * @param numPlayers the number of players in the game
     */
    public CommonGoalCard(int numPlayers){
        points = new Stack<Integer>();
        thoseWhoAchieved = new ArrayList<Player>();
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

    //deprecated
    /**
     * Takes points from the common goal card and assigns them to the player.
     * @param p the player who will receive the points
     */
    public void takePoints(Player p){
        p.addPoints(points.pop());
        thoseWhoAchieved.add(p);
    }

    /**
     * Removes and returns the topmost element from the stack of points.
     * @return the topmost element from the stack of points
     */
    public int pop(){
        return points.pop();
    }

    /**
     * Retrieves, but does not remove, the topmost element from the stack of points.
     *
     * @return the topmost element from the stack of points
     */
    public int peek(){
        return points.peek();
    }

    /**
     * Checks if the common goal card has been achieved by the player.
     * @param p the player to check
     * @return true if the player has achieved the goal, false otherwise
     */
    public boolean achievedBy(Player p){
        if(thoseWhoAchieved == null)
            return false;
        return thoseWhoAchieved.contains(p);
    }

    /**
     * Retrieves the list of players who have achieved the common goal card.
     * @return the list of players who have achieved the goal
     */
    public ArrayList<Player> getThoseWhoAchieved() {
        return thoseWhoAchieved;
    }

    /**
     * Checks if the common goal card is satisfied by the given bookshelf.
     * @param matrix the bookshelf matrix to check against
     * @return true if the goal is satisfied, false otherwise
     */
    public abstract boolean check(Bookshelf matrix);

    /**
     * Returns a string representation of the common goal card.
     * @return a string representation of the common goal card
     */
    public String toString(){
        return "This is the pattern!";
    }

    /**
     * Adds a player to the list of those who have achieved the common goal card.
     * @param p the player who achieved the goal
     */
    public void addAchiever(Player p){
        thoseWhoAchieved.add(p);
    }
}
