package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.GamePhase;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.gamemessage.*;

import java.util.*;
import java.util.stream.Collectors;

public class TurnController {
    private final Game model;
    private final GameController gameController;
    private ArrayList<Player> playerQueue;
    private ArrayList<Player> playersToSkip;

    /**
     * Constructor of the TurnController
     * @param gameController Game controller
     */
    public TurnController(GameController gameController) {
        this.model = gameController.getModel();
        this.gameController = gameController;
        this.playersToSkip = new ArrayList<>();
    }

    /**
     * Creates a new turn
     */
    public void newTurn() {
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

    protected ArrayList<String> getPlayerQueueUsernames() {
        ArrayList<String> usernames = new ArrayList<>();
        for (Player p : playerQueue) {
            usernames.add(p.getUsername());
        }
        return usernames;
    }

    public void drawPhase(GameMessage message) {
        if(message.getType()== GameMessageType.DRAW_TILES){
            DrawTilesMessage m = (DrawTilesMessage) message;
            if(isDrawHandValid(m.getSquares())){
                model.drawFromBoard(m.getSquares());
            }
            else{
                System.err.println("Tessere non valide");
            }
        }
        else{
            model.notifyObservers(new ErrorMessage(message.getUsername(), "Messaggio non valido"));
        }
        model.prepareForInsertPhase();
    }



    private boolean allCoordsAreEqual( ArrayList<Integer> x){
        for(int i=0; i<x.size()-1; i++){
            if (x.get(i)!=x.get(i + 1)) {
                return false;
            }
        }
        return true;
    }
    private boolean allCoordsAreAdjacent(ArrayList<Integer> x){
        Collections.sort(x);
        for(int i=0; i<x.size()-1; i++){
            if(x.get(i)!=x.get(i+1)-1) return false;
        }
        return true;
    }
    private boolean inLineTile(ArrayList<Square> hand) {
        ArrayList<Integer> rows = new ArrayList<>();
        ArrayList<Integer> columns = new ArrayList<>();
        for(Square sq : hand){
            rows.add(sq.getRow());
            columns.add(sq.getColumn());
        }
        return (allCoordsAreAdjacent(rows) && allCoordsAreEqual(columns)) || (allCoordsAreAdjacent(columns) && allCoordsAreEqual(rows));
    }
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
    public void loadNextPlayer(){
        int nowCurrent = playerQueue.indexOf(model.getCurrentPlayer());
        if(nowCurrent == playerQueue.size()-1 && isLastTurn()){
            model.nextGamePhase();
            gameController.awardPhase();
        }
        else{
            model.setCurrentPlayer(playerQueue.get((nowCurrent+1) % playerQueue.size()));
            newTurn();
        }
    }

    public boolean isLastTurn() {
        return model.isLastTurn();
    }

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

    public void refillPhase(){
        model.handleRefillPhase();
        loadNextPlayer();
    }

    public void calculateCommonGoal(){
        model.handleCalculatePhase();
        refillPhase();
    }

    /**
     * Inserts the tiles in the hand in the bookshelf
     * @param message Message containing the tiles to insert and where to insert them (column)
     */
    public void insertPhase(GameMessage message) {
        if(message.getType()== GameMessageType.INSERT_TILES){
            InsertTilesMessage m = (InsertTilesMessage) message;
            if(isInsertHandValid(m.getItems(), m.getColumn())){
                model.insertTiles(m.getItems(), m.getColumn());
                lastTurnCondition();
            }
            else{
                System.err.println("Tessere non valide");
            }
        }
        else{
            model.notifyObservers(new ErrorMessage(message.getUsername(), "Messaggio non valido"));
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

    public void setPlayersQueue(ArrayList<Player> players) {
        this.playerQueue = players;
    }

    public void addPlayerToSkip(Player player) {
        playersToSkip.add(player);
        if(playersToSkip.size()==playerQueue.size())
            //TODO leggi bene
            model.nextGamePhase(); //se esce l'ultimo giocatore, passa alla fase AWARDS (che non vedrà nessuno, si può cambiare)
        else if(player.equals(model.getCurrentPlayer()))
            loadNextPlayer();
    }


    public List<String> getPlayersToSkipUsernames(){
        return playersToSkip.stream().map(Player::getUsername).collect(Collectors.toList());
    }

    public void reconnectExitedPlayer(Player player) {
        playersToSkip.remove(player);
        model.playerRejoined(player);
    }
}
