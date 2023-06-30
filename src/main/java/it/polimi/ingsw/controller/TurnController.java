package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.GamePhase;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.gamemessage.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a controller for the in-game-phase (manages turns).
 */
public class TurnController {
    public static final int TIMEOUT_LASTPLAYER = 30000;
    private final Game model;
    private Timer timer;
    private final GameController gameController;
    private ArrayList<Player> playerQueue;
    private ArrayList<Player> playersToSkip;

    /**
     * Constructor of the TurnController
     * @param gameController Game controller
     */
    public TurnController(GameController gameController) {
        this.model = gameController.getModel();
        this.timer = null;
        this.gameController = gameController;
        this.playersToSkip = new ArrayList<>();
    }

    /**
     * Creates a new turn
     */
    public void newTurn() {
        System.err.println(playersToSkip);
        if (model.getGamePhase() != GamePhase.PLAY) {
            return;
        }
        if(!playersToSkip.contains(model.getCurrentPlayer())) {
            model.updateLeaderBoard(getPlayerQueueUsernames());
            model.handleDrawPhase();
        } else {
            loadNextPlayer();
        }
        //game.setGamePhase(TurnPhase.DRAW);
    }

    /**
     * Returns a list of player usernames in the playerQueue.
     * @return List of player usernames
     */
    protected ArrayList<String> getPlayerQueueUsernames() {
        ArrayList<String> usernames = new ArrayList<>();
        for (Player p : playerQueue) {
            usernames.add(p.getUsername());
        }
        return usernames;
    }

    /**
     * Handles the draw phase based on the received message.
     * If the message is not a valid DrawTilesMessage, an error message is sent.
     * Otherwise, if the drawn tiles are valid, it draws them from the board and prepares for the insert phase.
     * @param senderUsername Username of the message sender
     * @param message Message containing the drawn tiles
     */
    public void drawPhase(String senderUsername, MessageToServer message) {
        DrawTilesMessage m;
        try{
             m = (DrawTilesMessage) message;
        } catch (ClassCastException e) {
            model.notifyObservers(new ErrorMessage(senderUsername, "Messaggio non valido"));
            return;
        }
        if(isDrawHandValid(m.getSquares())) {
            model.drawFromBoard(m.getSquares());
        }
        else{
            model.notifyObservers(new ErrorMessage(senderUsername, "Tessere non valide"));
            model.notifyObservers(new DrawInfoMessage(model.getCurrentPlayer().getUsername(), new GameView(model,null), model.getCurrentPlayer().getBookshelf().maxSlotsAvailable()));
        }
        model.prepareForInsertPhase();
    }


    /**
     * Checks if all the coordinates in the given list are equal.
     * @param x List of integers representing the coordinates
     * @return True if all coordinates are equal, false otherwise
     */
    private boolean allCoordsAreEqual( ArrayList<Integer> x){
        for(int i=0; i<x.size()-1; i++){
            if (x.get(i)!=x.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if all the coordinates in the given list are adjacent.
     * @param x List of integers representing the coordinates
     * @return True if all coordinates are adjacent, false otherwise
     */
    private boolean allCoordsAreAdjacent(ArrayList<Integer> x){
        Collections.sort(x);
        for(int i=0; i<x.size()-1; i++){
            if(x.get(i)!=x.get(i+1)-1) return false;
        }
        return true;
    }

    /**
     * Checks if the tiles in the hand are in a line (either rows or columns).
     * @param hand List of squares representing the hand
     * @return True if the tiles are in a line, false otherwise
     */
    private boolean inLineTile(ArrayList<Square> hand) {
        ArrayList<Integer> rows = new ArrayList<>();
        ArrayList<Integer> columns = new ArrayList<>();
        for(Square sq : hand){
            rows.add(sq.getRow());
            columns.add(sq.getColumn());
        }
        return (allCoordsAreAdjacent(rows) && allCoordsAreEqual(columns)) || (allCoordsAreAdjacent(columns) && allCoordsAreEqual(rows));
    }

    /**
     * Checks if the drawn hand is valid.
     * It verifies if each square in the hand is pickable from the board and if they are in a line.
     * @param items List of squares representing the drawn hand
     * @return True if the drawn hand is valid, false otherwise
     */
    public boolean isDrawHandValid(ArrayList<Square> items){
        for(Square sq : items){
            if(!model.getBoard().getSquare(sq.getCoordinates()).isPickable()) return false;
        }
        return inLineTile(items);
    }

    /**
     * @return List of players in queue
     */
    public ArrayList<Player> getPlayerQueue() {
        return playerQueue;
    }

    /**
     * Loads the next player in the playerQueue.
     * If it is the last turn and the current player is the last player in the queue, it advances the game phase to AWARDS.
     * Otherwise, it sets the next player as the current player and starts a new turn.
     */
    public void loadNextPlayer(){
        int nowCurrent = playerQueue.indexOf(model.getCurrentPlayer());
        if(nowCurrent == playerQueue.size()-1 && isLastTurn()){
            model.nextGamePhase();
            gameController.awardPhase(null);
        }
        else{
            model.setCurrentPlayer(playerQueue.get((nowCurrent+1) % playerQueue.size()));
            newTurn();
        }
    }

    /**
     * Checks if it is the last turn in the game.
     * @return True if it is the last turn, false otherwise
     */
    public boolean isLastTurn() {
        return model.isLastTurn();
    }

    /**
     * Triggers the last turn in the game.
     */
    public void triggerLastTurn() {
        model.setLastTurn(true);
    }
    /**
     * Checks if current player filled his bookshelf and adds EndToken points if deserved
     */
    private void lastTurnCondition(){
        if(!isLastTurn() && model.getCurrentPlayer().endTrigger()){
            triggerLastTurn();
            model.addPointsToPlayer(model.getCurrentPlayer(), 1);
            //model.getCurrentPlayer().addPoints(1);
        }
    }

    /**
     * Handles the refill phase and loads the next player.
     */
    public void refillPhase(){
        model.handleRefillPhase();
        loadNextPlayer();
    }

    /**
     * Calculates the common goal and prepares for the refill phase.
     */
    public void calculateCommonGoal(){
        model.handleCalculatePhase();
        refillPhase();
    }

    /**
     * Inserts the tiles in the hand in the bookshelf
     * @param message Message containing the tiles to insert and where to insert them (column)
     */
    public void insertPhase(String senderUsername, MessageToServer message) {
        InsertTilesMessage m;
        try{
            m = (InsertTilesMessage) message;
        } catch (ClassCastException e) {
            model.notifyObservers(new ErrorMessage(senderUsername, "Messaggio non valido"));
            return;
        }
        if(isInsertHandValid(m.getItems(), m.getColumn())){
            model.insertTiles(m.getItems(), m.getColumn());
            lastTurnCondition();
        }
        else{
            System.err.println("Tessere non valide");
            Player currentPlayer = model.getCurrentPlayer();
            ArrayList<Integer> insertableColumns = currentPlayer.getBookshelf().enableColumns(currentPlayer.getHand().size());
            model.notifyObservers(new InsertInfoMessage(currentPlayer.getUsername(),
                            currentPlayer.getHand(),
                            currentPlayer.getBookshelf(),
                            insertableColumns));
            model.notifyObservers(new ErrorMessage(senderUsername, "Inserimento non valido: riprova"));
            return;
        }

        model.prepareForRefillPhase();
        calculateCommonGoal();
    }

    /**
     * Checks if the tiles in the message are valid
     * @param items tiles in the message
     * @param column column in the message
     * @return true if the tiles are valid, false otherwise
     */
    private boolean isInsertHandValid(ArrayList<ItemTile> items, int column) {
        if(items.size()!=model.getCurrentPlayer().getHand().size()) return false; //return false if the number of tiles in the hand is different from the number of tiles in the message
        if(!areTheSameHand(items, model.getCurrentPlayer().getHand())) return false; //return false if the tiles in the hand are different from the tiles in the message
        return model.getCurrentPlayer().getBookshelf().availableSlotsForColumn(column)>=items.size(); //return false if the column has less available slots than the number of tiles in the message
    }

    /**
     * Checks if the items list contains the same tiles as the hand list, regardless of their order.
     * @param items List of tiles to compare
     * @param hand List of tiles representing the hand
     * @return True if the items list contains the same tiles as the hand list, false otherwise
     */
    private boolean areTheSameHand(ArrayList<ItemTile> items, ArrayList<ItemTile> hand) {
        ArrayList<ItemTile> handCopy = new ArrayList<>(hand);
        Collections.sort(handCopy, Comparator.comparing(ItemTile::getType));
        for (ItemTile it : items) {
            int index = Collections.binarySearch(handCopy, it, Comparator.comparing(ItemTile::getType));
            if (index < 0) return false;
            handCopy.remove(index);
        }
        return true;
    }

    /**
     * Sets the player queue to the specified list of players.
     * @param players List of players in the player queue
     */
    public void setPlayersQueue(ArrayList<Player> players) {
        this.playerQueue = players;
    }

    /**
     * Adds the specified player to the playersToSkip list.
     * If all players have been added to the list, the game phase is advanced to AWARDS.
     * If the player is the current player, the next player is loaded.
     * @param player Player to skip
     */
    public void addPlayerToSkip(Player player) {
        playersToSkip.add(player);
        if(playersToSkip.size()==playerQueue.size()-1){
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Player champion=null;
                    for(Player p : playerQueue){
                        if(!playersToSkip.contains(p)){
                            champion = p;
                            break;
                        }
                    }
                    model.nextGamePhase();
                    gameController.awardPhase(champion);
                }
            }, TIMEOUT_LASTPLAYER);
        }
        if(playersToSkip.size()==playerQueue.size())
            model.nextGamePhase(); //se esce l'ultimo giocatore, passa alla fase AWARDS (che non vedrà nessuno, si può cambiare)
        else if(player.equals(model.getCurrentPlayer()))
            loadNextPlayer();
    }


    /**
     * Returns the playersToSkip list.
     * @return List of players to skip
     */
    public List<String> getPlayersToSkipUsernames(){
        return playersToSkip.stream().map(Player::getUsername).collect(Collectors.toList());
    }

    /**
     * Reconnects a player who has previously exited the game.
     * Removes the player from the playersToSkip list and notifies the model that the player has rejoined.
     * @param player The player to reconnect
     */
    public void reconnectExitedPlayer(Player player) {
        if(timer!=null) {
            timer.cancel();
        }
        playersToSkip.remove(player);
        model.playerRejoined(player);
    }
}
