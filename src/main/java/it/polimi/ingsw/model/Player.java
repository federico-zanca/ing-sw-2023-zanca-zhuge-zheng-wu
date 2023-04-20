package it.polimi.ingsw.model;

import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private static final long serialVersionUID = 5681952653692686369L;
    private String username;
    private Bookshelf bookshelf;
    ArrayList<ItemTile> hand;
    private int score;
    /**
     * Default constructor of Player
     * @param username the Username of the player
     */
    public Player(String username) {
        this.username = username;
        bookshelf = new Bookshelf();
        hand = new ArrayList<>();
        score = 0;
    }

    /**
     * Returns true if the player fulfilled its bookshelf, triggering the LAST_TURN phase of the game
     * @return true if player's bookshelf is full
     */
    public boolean endTrigger() {
        return bookshelf.isFull();
    }

    /**
     * Returns the current score of the player
     * @return this.score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sums the passed parameter to she score of the player
     * @param points the points to be added to the player's score
     */
    public void addPoints(int points) {
        score+=points;
    }

    /**
     * Returns the bookshelf of the player
     * @return  the bookshelf of the player
     */
    public Bookshelf getBookshelf() {
        return bookshelf;
    }


    /**
     * Returns the username of the player
     * @return this.username
     */
    public String getUsername() {
        return username;
    }


    /**
     * Calculate how many points must be added to the player
     * @return points to add due to personal goal accomplishment
     */
    public int calculateScorePersonalGoal(PersonalGoalCard personalGoal) {
        int matches;
        matches = personalGoal.numOfMatches(bookshelf);
        switch(matches){
            case 1: return 1;
            case 2: return 2;
            case 3: return 4;
            case 4: return 6;
            case 5: return 9;
            case 6: return 12;
            default: return 0;
        }
    }

    /**
     * Calculates how many points the player deserves based on the quantity and size of groups of adjacent items found in bookshelf
     * @return points to be added to the player's score
     */
    public int calculateAdjacentItemsPoints() {
        int points=0;
        int numAdjacentGroups = bookshelf.adjacentGroupsElaboration();
        for(int gidToCheck=0; gidToCheck<numAdjacentGroups; gidToCheck++){
            switch(bookshelf.countGIDoccurrencies(gidToCheck)){
                case 0: break;
                case 1: break;
                case 2: break;
                case 3: {
                    points+=2;
                    break;
                }
                case 4: {
                    points+=3;
                    break;
                }
                case 5: {
                    points+=5;
                    break;
                }
                default: points+=8;
            }
        }
        return points;

    }

    public void setHand(ArrayList<ItemTile> hand){
        this.hand = hand;
    }

    public String toString(){
        return "Player : " + username;
    }

    public ArrayList<ItemTile> getHand() {
        return hand;
    }
}
