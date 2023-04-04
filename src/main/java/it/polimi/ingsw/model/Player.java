package it.polimi.ingsw.model;

import it.polimi.ingsw.model.gameboard.Board;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;

import java.util.ArrayList;
import java.util.Comparator;

public class Player {
    private String username;
    private Bookshelf bookshelf;
    private PersonalGoalCard personalGoal;
    private int score;
    /**
     * Default constructor of Player
     * @param username the Username of the player
     */
    public Player(String username) {
        this.username = username;
        bookshelf = new Bookshelf();
        personalGoal = null;
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
    /*
    /**
     * Returns the items present on board at the passed coordinates
     * @param board the board from which the items are taken
     * @param rows coordinates of the rows of the items to take
     * @param columns coordinates of the columns of the items to take
     * @return items taken from the board
     */
    /*public ArrayList<ItemTile> takeItems(Board board, ArrayList<Integer> rows, ArrayList<Integer> columns){
        ArrayList<ItemTile> itemsTaken = new ArrayList<ItemTile>();
        for(int i=0; i<3 && i<itemsTaken.size(); i++){
            itemsTaken.add(board.pickItem(rows.get(i), columns.get(i)));
        }
        return itemsTaken;
    }*/

    private ArrayList<Integer> takeableOtherItems;
    private ArrayList<Integer>takedItemsX=new ArrayList<>();
    private ArrayList<Integer>takedItemsY=new ArrayList<>();

    public ItemTile takeFirstItem (Board board, int x, int y){
        takeableOtherItems=null;
        ArrayList<Integer> coord_list=board.pickableFirstItems();

        for (int i=0; i<coord_list.size()-1; i=i+2) {
            if (coord_list.get(i) == x && coord_list.get(i + 1) == y) {
                takeableOtherItems=board.pickableItems(x,y);
                takedItemsX.add(x);
                takedItemsY.add(y);
                return board.pickItem(x,y);
            }
        }
        return null;

    }

    public ItemTile takeOtherItem (Board board, int x, int y){

        for (int i=0; i<takeableOtherItems.size()-1; i=i+2) {
            if (takeableOtherItems.get(i) == x && takeableOtherItems.get(i + 1) == y) {
                takedItemsX.add(x);
                takedItemsY.add(y);
                if((takedItemsY.size()<2 && takedItemsX.size()<2) || checkThirdItem(takedItemsX, takedItemsY)) {
                    takeableOtherItems.remove(i);
                    takeableOtherItems.remove(i);
                    return board.pickItem(x, y);
                }
            }
        }
        return null;

    }

    public boolean checkThirdItem(ArrayList<Integer>takedItemsX, ArrayList<Integer>takedItemsY){
        takedItemsX.sort(Comparator.naturalOrder());
        takedItemsY.sort(Comparator.naturalOrder());
        int middleX=takedItemsX.get(1);
        int middleY=takedItemsY.get(1);
        boolean flagX=true, flagY=true;

        for(int i=0; i<takedItemsX.size(); i++){
            takedItemsX.set(i, takedItemsX.get(i)-middleX);
            takedItemsY.set(i, takedItemsY.get(i)-middleY);
        }
        for(int i=0; i<takedItemsX.size(); i++){
            if(takedItemsX.get(i)!=0){
                flagX=false;
                break;
            }
        }

        for(int i=0; i<takedItemsY.size(); i++){
            if(takedItemsY.get(i)!=0){
                flagY=false;
                break;
            }
        }

        if((takedItemsX.get(0)*takedItemsX.get(2)==-1 && flagY) || (takedItemsY.get(0)*takedItemsY.get(2)==-1 && flagX)){
            return true;
        }
        return false;
    }

    /**
     * Returns the personalGoalCard of the player
     * @return this.personalGoal
     */
    public PersonalGoalCard getPersonalGoal() {
        return personalGoal;
    }

    /**
     * Returns the username of the player
     * @return this.username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the players' PersonalGoal
     * @param personalGoal PersonalGoalCard to set
     */
    public void setPersonalGoal(PersonalGoalCard personalGoal) {
        this.personalGoal = personalGoal;
    }

    /**
     * Calculate how many points must be added to the player
     * @return points to add due to personal goal accomplishment
     */
    public int calculateScorePersonalGoal() {
        int matches;
        matches = getPersonalGoal().numOfMatches(bookshelf);
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

    public String toString(){
        return "Player : " + username;
    }
}


