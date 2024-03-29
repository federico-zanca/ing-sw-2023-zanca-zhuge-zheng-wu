package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.commongoals.*;
import it.polimi.ingsw.model.enumerations.GamePhase;
import it.polimi.ingsw.model.enumerations.TurnPhase;
import it.polimi.ingsw.model.exceptions.IllegalDrawException;
import it.polimi.ingsw.model.gameboard.Board;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import it.polimi.ingsw.network.message.gamemessage.*;
import it.polimi.ingsw.utils.Observable;

import java.util.*;
import java.util.stream.Collectors;
/**
 * This Class represents the whole Game.
 * It manages the players and their turns.
 */
public class Game extends Observable {
    private int chosenNumOfPlayers;
    private ArrayList<Player> players;

    boolean lastTurn=false;
    private GamePhase gamePhase;

    private LinkedHashMap<String, Integer> leaderboard;
    private TurnPhase turnPhase;
    private Player currentPlayer;
    private ArrayList<CommonGoalCard> commonGoals;
    private HashMap<String,PersonalGoalCard> personalGoals;
    private Board board;
    private Bag bag;
    private boolean gameStarted;

    /**
     * Constructs a new Game instance and initializes its properties.
     */
    public Game(){
        init();
    }


    //METHODS FOR GAME INITIALIZATION
    /**
     * Initializes the game by setting up the initial state.
     * This method is called during construction of a Game object.
     */
    public void init(){
        board = null;
        bag = new Bag();
        players = new ArrayList<>();
        commonGoals = new ArrayList<>();
        leaderboard = new LinkedHashMap<>();
        chosenNumOfPlayers= GameController.MIN_PLAYERS;
        personalGoals = new HashMap<>();
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
     * @param phase the phase to set
     */
    public void setGamePhase(GamePhase phase) {
        this.gamePhase = phase;
    }

    /**
     * Switch GamePhase to the next one
     */
    public void nextGamePhase() {
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
        int firstGoalType = (int) (Math.random() * 12 ) +1;
        int secondGoalType = (int) (Math.random() * 12) + 1;
        while(firstGoalType == secondGoalType) {
            secondGoalType = (int) (Math.random() * 12) + 1;
        }
        CommonGoalCard first = intToCommonGoal(firstGoalType);
        first.setImageId(firstGoalType);
        CommonGoalCard second = intToCommonGoal(secondGoalType);
        second.setImageId(secondGoalType);
        commonGoals.add(first);
        commonGoals.add(second);
    }

    /**
     * Given a number, returns the correspondent CommonGoalCard
     * @param n type of commongoalcard
     * @return commongoalcard
     */
    private CommonGoalCard intToCommonGoal(int n){
        switch(n){
            case 1: return new CommonGoalCardGroups(chosenNumOfPlayers,6,2);
            case 2: return new CommonGoalCardShapes(chosenNumOfPlayers,"4Corners");
            case 3: return new CommonGoalCardGroups(chosenNumOfPlayers,4,4);
            case 4: return new CommonGoalCardSquare(chosenNumOfPlayers);
            case 5: return new CommonGoalCardLimitTypes(chosenNumOfPlayers,3,1,3,"col");
            case 6: return new CommonGoalCard8SameTiles(chosenNumOfPlayers);
            case 7: return new CommonGoalCardShapes(chosenNumOfPlayers,"Diagonal");
            case 8: return new CommonGoalCardLimitTypes(chosenNumOfPlayers,4,1,3,"row");
            case 9: return new CommonGoalCardLimitTypes(chosenNumOfPlayers,2,6,6,"col");
            case 10: return new CommonGoalCardLimitTypes(chosenNumOfPlayers,2,5,5,"row");
            case 11: return new CommonGoalCardShapes(chosenNumOfPlayers,"X");
            default: return new CommonGoalCardStairs(chosenNumOfPlayers);
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
        Random rand = new Random();
        int n = rand.nextInt(12) + 1;
        return new PersonalGoalCard(n);
    }


    /**
     * Starts the Game by executing these steps:
     * - initializes the GameBoard
     * - Fills the board with ItemTiles
     * - Randomly selects the two commongoals
     * - assigns a personal goal to each player
     */
    public void startGame(ArrayList<Player> playerQueue){
        setGameStarted(true);
        setGamePhase(GamePhase.INIT);
        setGameBoard(chosenNumOfPlayers);
        fillBoard();
        pickCommonGoalsForThisGame();

        for(Player player : players){
            PersonalGoalCard personal = randomPersonalGoal();
            while(personalGoals.containsValue(personal)){
                personal = randomPersonalGoal();
            }
            personalGoals.put(player.getUsername(),personal);
            notifyObservers(new PersonalGoalCardMessage(player.getUsername(),personalGoals.get(player.getUsername())));
        }

        initLeaderBoard();
        notifyObservers(new GameStartedMessage("", new GameView(this,playerQueue)));
        nextGamePhase();
        //fai vedere personal goal
        //fai vedere commongoals
    }

    /**
     * Sets the game started status.
     * @param b The new game started status to set.
     */
    private void setGameStarted(boolean b) {
        this.gameStarted = b;
    }


    //  METHODS FOR DRAW
    /**
     * Prepares the info used by current player to draw items from the board
     */
    public void handleDrawPhase() {
        setTurnPhase(TurnPhase.DRAW);
        notifyObservers(new NewTurnMessage("", currentPlayer.getUsername()));
        int maxItemTiles = currentPlayer.getBookshelf().maxSlotsAvailable();
        board.enableSquaresWithFreeSide();
        DrawInfoMessage m = new DrawInfoMessage(currentPlayer.getUsername(), new GameView(this,null), maxItemTiles);
        notifyObservers(m);
    }

    /**
     * Calls all the methods used to remove the selected squares from the board (and puts them in the player's hand)
     * @param squares containing the items to remove from the board
     */
    public void drawFromBoard(ArrayList<Square> squares) {
        try {
            setPlayerHand(currentPlayer.getUsername(), getBoard().pickItems(squares));
        } catch (IllegalDrawException e) {
            notifyObservers(new DrawInfoMessage(currentPlayer.getUsername(), new GameView(this,null), currentPlayer.getBookshelf().maxSlotsAvailable()));
            notifyObservers(new ErrorMessage(currentPlayer.getUsername(), e.getMessage()));
        }
        board.disableAllSquares();
        //notifyObservers(new BoardMessage("", getBoard().getGameboard()));
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

    /**
     * Returns the current board.
     *
     * @return the board of the game.
     */

    //  MISC

    public ArrayList<ItemTile> getDrawnTiles() {
        return currentPlayer.getHand();
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
     * @return the board of the game.
     */
    public Board getBoard() {
        return board;
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
            case DRAW: {
                setTurnPhase(TurnPhase.INSERT);
                break;
            }

            case INSERT: setTurnPhase(TurnPhase.CALCULATE); break;
            case CALCULATE: setTurnPhase(TurnPhase.REFILL); break;
            //case REFILL: setTurnPhase(TurnPhase.DRAW); break;
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
     * Prepares the game for the insert phase by sending the currentplayer all the info he needs to insert his tiles
     */

    //  INSERT PHASE METHODS

    /**
     * Prepares the game for the insert phase by sending the currentplayer all the info he needs to insert his tiles
     */
    public void prepareForInsertPhase() {
        nextTurnPhase();
        ArrayList<Integer> insertableColumns = currentPlayer.getBookshelf().enableColumns(currentPlayer.getHand().size());
        notifyObservers(new InsertInfoMessage(currentPlayer.getUsername(), getDrawnTiles(), currentPlayer.getBookshelf(), insertableColumns));
    }

    /**
     * Inserts the tiles in the bookshelf
     * @param items to insert
     * @param column where to insert them
     */
    public void insertTiles(ArrayList<ItemTile> items, int column) {
        currentPlayer.getBookshelf().insertItems(items, column);
        currentPlayer.getHand().clear();
        notifyObservers(new BookshelfMessage(currentPlayer.getUsername(), currentPlayer.getBookshelf()));
    }

    /**
     * Handles calculate phase by checking if the player has achieved any common goal
     */
    public void handleCalculatePhase() {
        int count=0;
        for(int i=0;i< commonGoals.size();i++){
            if(!commonGoals.get(i).achievedBy(currentPlayer) && commonGoals.get(i).check(currentPlayer.getBookshelf())){
                int points = commonGoals.get(i).pop();
                addPointsToPlayer(currentPlayer, points);
                commonGoals.get(i).addAchiever(currentPlayer);
                //cg.takePoints(currentPlayer);
                notifyObservers(new AchievedCommonGoalMessage("", currentPlayer.getUsername() + " ha completato l'Obiettivo Comune:\n\"" + commonGoals.get(i) + "\"\n e ha ottenuto " + points + "punti!",i+1,commonGoals.get(i))); //send a message containing the info of the achieved common goal
                count++;
            }
        }
        if(count==0){
            notifyObservers(new AchievedCommonGoalMessage("", currentPlayer.getUsername()+" non ha completato nessun Obiettivo Comune questo turno.",0,commonGoals.get(0)));
        }
    }

    //  REFILL PHASE METHODS

    /**
     * Prepares the game for the refill phase by setting the turnphase to refill
     */
    public void prepareForRefillPhase() {
        nextTurnPhase();
        handleRefillPhase();
    }

    /**
     * Handles the refill phase by placing ItemTiles randomly extracted from the Bag (only if the board needs to be refilled)
     */
    public void handleRefillPhase() {
        if(board.needsRefill()){
            board.refillBoardWithItems(bag.drawItems(board.numCellsToRefill()));
        }
        notifyObservers(new BoardMessage("", board.getGameboard()));
    }

    /**
     * Ends the game and notifies the observers with the winner and leaderboard information.
     * @param winner  The player who won the game.
     * @param playersQueue  The list of players in the game.
     */
    public void endGame(Player winner, ArrayList<String> playersQueue) {
        sortLeaderBoard(playersQueue);
        if(!leaderboard.keySet().iterator().next().equals(winner.getUsername())) {
            leaderboard.clear();
            leaderboard.put(winner.getUsername(), winner.getScore());
            ArrayList<Player> players_ranked = new ArrayList<>(players);
            players_ranked.sort((p1, p2) -> p2.getScore() - p1.getScore());
            for(Player p : players_ranked){
                if(!p.getUsername().equals(winner.getUsername())){
                    leaderboard.put(p.getUsername(), p.getScore());
                }
            }
        }
        notifyObservers(new EndGameMessage("", leaderboard));
    }


    //points calculators
    /**
     * Adds adjacent items extra points to each player
     */
    public void assignAdjacentItemsPoints() {
        int points;
        for(Player p : players){
            points = p.calculateAdjacentItemsPoints();
            //p.addPoints(points);
            addPointsToPlayer(p, points);
            notifyObservers(new AdjacentItemsPointsMessage("", p.getUsername(), points));
        }
    }
    /**
     * Add personal goal points for each player
     */
    public void assignPersonalGoalPoints(){
        int points;
        for(Player p : players){
            points = p.calculateScorePersonalGoal(personalGoals.get(p.getUsername()));
            addPointsToPlayer(p, points);
            notifyObservers(new PersonalGoalPointsMessage("", p.getUsername(), personalGoals.get(p.getUsername()), points));
            //p.addPoints(points);
        }
    }

    /**
     * Adds points to a player and updates the leaderboard accordingly.
     * @param p      The player to whom points are being added.
     * @param points The number of points to add.
     **/
    public void addPointsToPlayer(Player p, int points){
        p.addPoints(points);
        leaderboard.put(p.getUsername(), leaderboard.get(p.getUsername()) + points);
    }

    /**
     * Updates the leaderboard.
     * @param usernamesQueue The list of usernames representing the order of players in the game.
     */
    public void updateLeaderBoard(ArrayList<String> usernamesQueue){
        sortLeaderBoard(usernamesQueue);
       // notifyObservers(new LeaderBoardMessage(currentPlayer.getUsername(), leaderboard));
    }

    /**
     * Sorts the leaderboard based on the players' scores in descending order and their positions in the usernames queue.
     * @param usernamesQueue The list of usernames representing the order of players in the game.
     */
    public void sortLeaderBoard(ArrayList<String> usernamesQueue){
        leaderboard = leaderboard.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(e -> -usernamesQueue.indexOf(e.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    /**
     * Initializes the leaderboard with default scores for all players.
     * Each player's initial score is set to 0.
     */
    private void initLeaderBoard(){
        for(Player p : players){
            leaderboard.put(p.getUsername(), 0);
        }
    }

    /**
     * Checks if it is the last turn of the game.
     * @return True if it is the last turn, false otherwise.
     */
    public boolean isLastTurn() {
        return lastTurn;
    }

    /**
     * Sets the last turn status and notifies the observers.
     * @param b The new last turn status to set.
     */
    public void setLastTurn(boolean b) {
        lastTurn = b;
        notifyObservers(new LastTurnMessage("", currentPlayer.getUsername()));
    }

    /**
     * Adds an observer to the list of observers for this object.
     * @param o The observer to be added.
     */
    public synchronized void addObserver(it.polimi.ingsw.utils.Observer o) {
        super.addObserver(o);
    }

    /**
     * Gets the leaderboard containing the scores of all players.
     * @return The leaderboard as a LinkedHashMap, where the keys are usernames and the values are scores.
     */
    public LinkedHashMap<String, Integer> getLeaderboard() {
        return leaderboard;
    }

    /**
     * Removes a player from the game.
     * @param player The player to be removed.
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * Notifies the observers that a player has rejoined the game.
     * @param player The player who has rejoined the game.
     */
    public void playerRejoined(Player player) {
        notifyObservers(new PlayerRejoinedMessage("", player.getUsername()));
    }

    /**
     * Checks if the game has started.
     * @return True if the game has started, false otherwise.
     */
    public boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Retrieves the personal goal card of the player by username.
     * @param username The username of the player.
     * @return The personal goal card associated with the player, or null if the player does not have a personal goal card.
     */
    public PersonalGoalCard getPersonalGoalOfPlayer(String username) {
        return personalGoals.get(username);
    }

    /**
     * Resets the list of players in the game.
     * This method clears the existing list of players and creates a new empty list.
     */
    public void resetPlayers() {
        players = new ArrayList<>();
    }
}