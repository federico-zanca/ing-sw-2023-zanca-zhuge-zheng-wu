package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commongoals.CommonGoalCard;

import java.util.ArrayList;

public class Game {
    //NOTE : creo il campo instance rendendo Game un singleton perché devo poi permettere al game di avere un solo controller che lo comandi (o è un bordello)
    private static Game instance;

    private static final int MAX_PLAYERS = 4;

    private int chosenNumOfPlayers;
    private ArrayList<Player> players;
    private ArrayList<CommonGoalCard> commonGoals;
    private Board board;
    private Bag bag;
    private Game(){
        board = new Board();
        bag = new Bag();
        players = new ArrayList<Player>();
    }

    /**
     *
     * @return the singleton instance of the game
     */
    public static Game getInstance() {
        if(instance == null)
            instance = new Game();
        return instance;
    }

    /**
     * Resets the game instance, all game data is lost after this operation.
     */
    public static void resetInstance(){
        Game.instance=null;
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


}
