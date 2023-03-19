package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Player {
    private String username;
    private Bookshelf bookshelf;
    private PersonalGoalCard personalGoal;
    private int score;

    public Player(PersonalGoalCard goal, String username) {
        this.username = username;
        bookshelf = new Bookshelf();
        personalGoal = goal;
        score = 0;
    }

    public boolean endTrigger() {
        return bookshelf.isFull();
    }

    public int getScore() {
        return score;
    }

    public void addPoints(int points) {
        score+=points;
    }
    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    public ArrayList<ItemTile> takeItems(Board board, ArrayList<Integer> x, ArrayList<Integer> y){
        ArrayList<ItemTile> itemsTaken = new ArrayList<ItemTile>();
        for(int i=0; i<3 && i<itemsTaken.size(); i++){
            itemsTaken.add(board.pickItem(x.get(i), y.get(i)));
        }
        return itemsTaken;
    }

    public PersonalGoalCard getPersonalGoal() {
        return personalGoal;
    }

    public String getUsername() {
        return username;
    }
}
