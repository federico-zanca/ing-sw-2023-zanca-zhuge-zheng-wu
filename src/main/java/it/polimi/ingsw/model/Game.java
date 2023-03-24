package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commongoals.CommonGoalCard;
import it.polimi.ingsw.model.gameboard.Board;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard2;

import java.util.ArrayList;
import it.polimi.ingsw.model.commongoals.*;
public class Game {
    //NOTE : creo il campo instance rendendo Game un singleton perché devo poi permettere al game di avere un solo controller che lo comandi (o è un bordello)
    //private static Game instance;

    private static final int MAX_PLAYERS = 4;
    private int chosenNumOfPlayers;
    private ArrayList<Player> players;
    private ArrayList<CommonGoalCard> commonGoals;
    private Board board;
    private Bag bag;
    private Game(){
        init();
    }

    /** Game initialization
     *
     */
    public void init(){
        board = null;
        bag = new Bag();
        players = new ArrayList<>();
        commonGoals = new ArrayList<>();
    }
    /**
     *
     * @return the singleton instance of the game
     */
    /*
    public static Game getInstance() {
        if(instance == null)
            instance = new Game();
        return instance;
    }
    */

    /**
     * Resets the game instance, all game data is lost after this operation.
     */
    /*
    public static void resetInstance(){
        Game.instance=null;
    }
    */

    /**
     * Sets the Board for the game
     * @param numPlayers number of players, used to create the board accordingly
     */
    public void setGameBoard(int numPlayers){
        this.board = new Board();
        board.initBoard(numPlayers);
    }

    /**
     * Checks if game is ready to start
     * @return true if the number of players currently in game is equal to the number of players specified for the game
     */
    public boolean isGameReadyToStart(){
        return getNumCurrentPlayers()==chosenNumOfPlayers;
    }

    public void startGame(){
        setGameBoard(chosenNumOfPlayers);
        fillBoard();
        pickCommonGoalsForThisGame();

        for(Player player : players){
            player.setPersonalGoal(randomPersonalGoal());
        }
    }

    /**
     * Randomly picks 2 different commongoalCards and sets them as commonGoals for the game
     */
    private void pickCommonGoalsForThisGame() {
        //TODO update with design pattern for CommonGoal
        int firstGoalType = (int) (Math.random() * 12 + 1) +1;
        int secondGoalType = firstGoalType;
        while(firstGoalType == secondGoalType)
            secondGoalType = (int) (Math.random() * 12 + 1) +1;
        commonGoals.add(intToCommonGoal(firstGoalType));
        commonGoals.add(intToCommonGoal(secondGoalType));

    }

    /**
     * Given a number, returns the correspondent CommonGoalCard
     * @param n type of commongoalcard
     * @return commongoalcard
     */
    private CommonGoalCard intToCommonGoal(int n){
        switch(n){
            case 1: return new CommonGoalCard1(chosenNumOfPlayers);
            case 2: return new CommonGoalCard2(chosenNumOfPlayers);
            case 3: return new CommonGoalCard3(chosenNumOfPlayers);
            case 4: return new CommonGoalCard4(chosenNumOfPlayers);
            case 5: return new CommonGoalCard5(chosenNumOfPlayers);
            case 6: return new CommonGoalCard6(chosenNumOfPlayers);
            case 7: return new CommonGoalCard7(chosenNumOfPlayers);
            case 8: return new CommonGoalCard8(chosenNumOfPlayers);
            case 9: return new CommonGoalCard9(chosenNumOfPlayers);
            case 10: return new CommonGoalCard10(chosenNumOfPlayers);
            case 11: return new CommonGoalCard11(chosenNumOfPlayers);
            default: return new CommonGoalCard12(chosenNumOfPlayers);
        }
    }

    /**
     * Extract a random personal goal card
     * @return random personalgoal
     */
    public PersonalGoalCard randomPersonalGoal(){
        //TODO replace with real random extractor
        return new PersonalGoalCard2();
    }

    /**
     * Fills the board with random ItemTiles as the game starts
     */
    public void fillBoard(){
        ArrayList<ItemTile> items;
        items = bag.drawItems(board.numCellsToRefill());

        board.refillBoard(items);
    }

    /**
     * Returns a player given his {@code username}.
     * Only the first occurrence is returned because
     * the player username is considered to be unique.
     * If no player is found {@code null} is returned.
     *
     * @param username the username of the player to be found.
     * @return Returns the player given his {@code username}, {@code null} otherwise.
     */
    public Player getPlayerByUsername(String username) {
        return players.stream()
                .filter(player -> username.equals(player.getUsername()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds new Player to the list of players.
     * @param p player to add
     */
    public void addPlayer(Player p){
        players.add(p);
    }

    /**
     * Returns the first player to play.
     * @return first player to play
     */
    public Player getFirstPlayer(){
        return players.get(0);
    }

    /**Returns the number of players
     *
     * @return the number of players in game
     */
    public int getNumCurrentPlayers() {
        return players.size();
    }

    /** Returns the number of players chosen for the game.
     *
     * @return the number of players chosen for the game
     */
    public int getChosenPlayersNumber() {
        return chosenNumOfPlayers;
    }

    /**
     * Search a username in the current Game.
     *
     * @param username the username of the player.
     * @return {@code true} if the username is found, {@code false} otherwise.
     */
    public boolean isUsernameTaken(String username){
        return getPlayersUsernames().contains(username);
    }

    /**
     * Returns a list of player usernames that are already in-game.
     *
     * @return a list with all usernames in the Game
     */
    public ArrayList<String> getPlayersUsernames() {
        ArrayList<String> usernames = new ArrayList<>();
        for (Player p : players) {
            usernames.add(p.getUsername());
        }
        return usernames;
    }

    /**
     * Returns a list of players.
     *
     * @return the players.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the current board.
     *
     * @return the board of the game.
     */
    public Board getBoard() {
        return board;
    }

    /** Returns the current bag.
     *
     * @return the bag of the game.
     */
    public Bag getBag() {
        return bag;
    }

    /**
     * Adds a CommonGoalCard to Game.commonGoals list
     * @param cg the CommonGoalCard to be added to the game
     */
    public void addCommonGoal(CommonGoalCard cg){
            commonGoals.add(cg);
    }
    public ArrayList<CommonGoalCard> getCommonGoals() {
        return commonGoals;
    }
}
