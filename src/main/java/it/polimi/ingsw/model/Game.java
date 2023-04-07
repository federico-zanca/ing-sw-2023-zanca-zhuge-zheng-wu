package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commongoals.CommonGoalCard;
import it.polimi.ingsw.model.enumerations.GamePhase;
import it.polimi.ingsw.model.enumerations.TurnPhase;
import it.polimi.ingsw.model.gameboard.Board;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;

import java.util.ArrayList;
import java.util.Random;

import it.polimi.ingsw.model.commongoals.*;
import it.polimi.ingsw.network.message.BoardMessage;
import it.polimi.ingsw.network.message.DrawInfoMessage;
import it.polimi.ingsw.network.message.InsertTilesMessage;
import it.polimi.ingsw.utils.Observable;

public class Game extends Observable {
    //NOTE : creo il campo instance rendendo Game un singleton perché devo poi permettere al game di avere un solo controller che lo comandi (o è un bordello)
    private static Game instance;

    private static final int MAX_PLAYERS = 4;
    private int chosenNumOfPlayers;
    private ArrayList<Player> players;

    boolean lastTurn;
    private GamePhase gamePhase;

    private TurnPhase turnPhase;
    private Player currentPlayer;
    private ArrayList<CommonGoalCard> commonGoals;
    private Board board;
    private Bag bag;

    private Game(){
        init();
    }


    //METHODS FOR GAME INITIALIZATION
    /** Game initialization
     *
     */
    public void init(){
        board = null;
        bag = new Bag();
        players = new ArrayList<>();
        players.add(new Player("BOT0"));
        players.add(new Player("BOT1"));
        players.add(new Player("BOT2"));
        commonGoals = new ArrayList<>();
        chosenNumOfPlayers=4; //andrà rimosso, solo per testing
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
        return getCurrNumOfPlayers()==chosenNumOfPlayers;
    }

    /**
     * @return the GamePhase in which the game is
     */
    public GamePhase getGamePhase(){
        return gamePhase;
    }

    /**
     * Set the Phase of the current game
     * @param phase
     */
    public void setGamePhase(GamePhase phase) {
        this.gamePhase = phase;
        //notifyObservers(new Message(currentPlayer.getUsername(), phase));
        //notifyObservers(new ChangedPhaseMessage(currentPlayer.getUsername(), phase));
    }

    /**
     * Switch GamePhase to the next one
     */
    private void nextGamePhase() {
        switch(gamePhase){
            case LOGIN:
                setGamePhase(GamePhase.INIT);
                break;
            case INIT:
                setGamePhase(GamePhase.PLAY);
                break;
            case PLAY:
                setGamePhase(GamePhase.AWARDS);
                break;
            case AWARDS:
                setGamePhase(GamePhase.ENDED);
                break;
            default:
                System.err.println("Invalid gamephase");
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
     * Fills the board with random ItemTiles as the game starts
     */
    private void fillBoard(){
        ArrayList<ItemTile> items;
        items = bag.drawItems(board.numCellsToRefill());

        board.refillBoardWithItems(items);
    }

    /**
     * Extract a random personal goal card
     * @return random personalgoal
     */
    public PersonalGoalCard randomPersonalGoal(){
        //TODO fai in modo che
        Random rand = new Random();
        int n = rand.nextInt(12) + 1;
        return new PersonalGoalCard(n);
    }

    /**
     * Adds a CommonGoalCard to Game.commonGoals list
     * @param cg the CommonGoalCard to be added to the game
     */
    public void addCommonGoal(CommonGoalCard cg){
        commonGoals.add(cg);
    }

    /**
     * Starts the Game by executing these steps:
     * - initializes the GameBoard
     * - Fills the board with ItemTiles
     * - Randomly selects the two commongoals
     * - assigns a personal goal to each player
     */
    public void startGame(){
        setGamePhase(GamePhase.INIT);
        setGameBoard(chosenNumOfPlayers);
        fillBoard();
        pickCommonGoalsForThisGame();

        for(Player player : players){
            player.setPersonalGoal(randomPersonalGoal()); //TODO due player non dovrebbero avere lo stesso commongoal
        }
        setCurrentPlayer(getFirstPlayer());
        //board.enableSquaresWithFreeSide();
        notifyObservers(new BoardMessage(currentPlayer.getUsername(), board.getGameboard()));
        nextGamePhase();
        //fai vedere personal goal
        //fai vedere commongoals
    }



    //  METHODS FOR DRAW

    /**
     * Prepares the info used by current player to draw items from the board
     */
    public void handleDrawPhase() {
        setTurnPhase(TurnPhase.DRAW);
        int maxItemTiles = currentPlayer.getBookshelf().maxSlotsAvailable();
        board.enableSquaresWithFreeSide();
        DrawInfoMessage m = new DrawInfoMessage(currentPlayer.getUsername(), board.getGameboard(), currentPlayer.getBookshelf().getShelfie(), maxItemTiles);
        notifyObservers(m);
    }

    /**
     * Calls all the methods used to remove the selected squares from the board (and puts them in the player's hand)
     * @param squares containing the items to remove from the board
     */
    public void drawFromBoard(ArrayList<Square> squares) {
       setPlayerHand(currentPlayer.getUsername(), getBoard().pickItems(squares));
       notifyObservers(new BoardMessage(currentPlayer.getUsername(), getBoard().getGameboard()));
    }

    /**
     * Sets a player's hand
     * @param username of the player whose hand is to be set
     * @param hand ItemTiles taken from the board (those which will be inserted in the bookshelf)
     */
    private void setPlayerHand(String username, ArrayList<ItemTile> hand) {
        getPlayerByUsername(username).setHand(hand);
        //notifyObservers();
    }

    //  PLAYERS RELATED STUFF

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
        //notifica view perché la lobby si aggiorna
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
    public int getCurrNumOfPlayers() {
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
     * Set the current player
     * @param player the current player to be set
     */
    public void setCurrentPlayer(Player player){
        currentPlayer = player;
    }

    /**
     * @return the active player this turn
     */
    public Player getCurrentPlayer(){
        return currentPlayer;
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
     * Sets the number of players this game will be composed of once started
     * @param chosenNum number of players
     */
    public void setChosenNumOfPlayers(int chosenNum) {
        chosenNumOfPlayers = chosenNum;
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

    //  MISC
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
     * @return the TurnPhase of the game
     */
    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    /**
     * Sets the turnphase
     * @param turnPhase to set
     */
    public void setTurnPhase(TurnPhase turnPhase) {
        this.turnPhase = turnPhase;
    }

    /**
     * Switch TurnPhase to the next one
     */
    private void nextTurnPhase() {
        switch(turnPhase){
            case DRAW: setTurnPhase(TurnPhase.INSERT); break;
            case INSERT: setTurnPhase(TurnPhase.REFILL); break;
            case REFILL: setTurnPhase(TurnPhase.CALCULATE); break;
            default:
        }
    }

    /**
     * Getter for CommonGoals
     * @return CommonGoals of the game
     */
    public ArrayList<CommonGoalCard> getCommonGoals() {
        return commonGoals;
    }

    //  REFILL PHASE METHODS

    /**
     * Handles the refill phase by placing ItemTiles randomly extracted from the Bag (only if the board needs to be refilled)
     */
    public void handleRefillPhase() {
        if(board.needsRefill()){
            board.refillBoardWithItems(bag.drawItems(board.numCellsToRefill()));
        }
        notifyObservers(new BoardMessage("", board.getGameboard()));
    }
}


//ARRIVO TRA UN ATTIMO OK BRO