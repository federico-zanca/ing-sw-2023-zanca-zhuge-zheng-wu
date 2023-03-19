package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Player {
    private String username;
    private Bookshelf bookshelf;
    private PersonalGoalCard personalGoal;
    private int score;

    /**
     * Default constructor of Player
     * @param goal the personalGoalCard of the player
     * @param username the Username of the player
     */
    public Player(PersonalGoalCard goal, String username) {
        this.username = username;
        bookshelf = new Bookshelf();
        personalGoal = goal;
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
     * Returns the items present on board at the passed coordinates
     * @param board the board from which the items are taken
     * @param rows coordinates of the rows of the items to take
     * @param columns coordinates of the columns of the items to take
     * @return items taken from the board
     */
    public ArrayList<ItemTile> takeItems(Board board, ArrayList<Integer> rows, ArrayList<Integer> columns){
        ArrayList<ItemTile> itemsTaken = new ArrayList<ItemTile>();
        for(int i=0; i<3 && i<itemsTaken.size(); i++){
            itemsTaken.add(board.pickItem(rows.get(i), columns.get(i)));
        }
        return itemsTaken;
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
}
