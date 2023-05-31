package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commongoals.CommonGoalCard;
import it.polimi.ingsw.model.gameboard.Board;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GameView implements Serializable {
    private static final long serialVersionUID = 1L;


    private final ArrayList<Player> players;
    private final Player currentPlayer;

    boolean lastTurn;
    private final LinkedHashMap<String, Integer> leaderboard;
    private final ArrayList<CommonGoalCard> commonGoals;
    private final Board board;


    //attributes for each single player, initialized thanks to the username passed to the constructor


    public GameView(Game model) {
        if (model.getPlayers() != null) {
            this.players = new ArrayList<>(model.getPlayers());
        } else {
            this.players = new ArrayList<>();
        }
        this.lastTurn = model.isLastTurn();

        if (model.getLeaderboard() != null) {
            this.leaderboard = new LinkedHashMap<>(model.getLeaderboard());
        } else {
            this.leaderboard = new LinkedHashMap<>();
        }

        if (model.getCommonGoals() != null) {
            this.commonGoals = new ArrayList<>(model.getCommonGoals());
        } else {
            this.commonGoals = new ArrayList<>();
        }

        if (model.getBoard() != null) {
            this.board = model.getBoard();
        } else {
            this.board = null;
        }

        if(model.getCurrentPlayer() != null) {
            this.currentPlayer = model.getCurrentPlayer();
        } else {
            this.currentPlayer = null;
        }
    }


    /**
     * Returns the current board.
     * @return the board of the game.
     */
    public Board getBoard() {
        return this.board;
    }


    /**
     * Getter for CommonGoals
     * @return CommonGoals of the game
     */
    public ArrayList<CommonGoalCard> getCommonGoals() {
        return this.commonGoals;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public LinkedHashMap<String, Integer> getLeaderboard() {
        return this.leaderboard;
    }

    public boolean isLastTurn() {
        return this.lastTurn;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public ArrayList<Bookshelf> getBookshelves() {
        ArrayList<Bookshelf> bookshelves = new ArrayList<>();
        for (Player p : players) {
            bookshelves.add(p.getBookshelf());
        }
        return bookshelves;
    }
}
/*
@Override
public void askPlayerNumber() {

}

@Override
public void showLoginResult(boolean valiUsername, String username) {

}

@Override
public void showLobby() {

}

@Override
public void showFirstPlayerThisGame() {

}

@Override
public void showWinMessage() {

}
*/


