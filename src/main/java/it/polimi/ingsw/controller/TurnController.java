package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.exceptions.FullColumnException;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.*;

import java.util.ArrayList;
import java.util.Collections;

public class TurnController {
    private final Game model;
    private final GameController gameController;
    private ArrayList<Player> playerQueue;

    private boolean lastTurn = false;
    /**
     * Constructor of the TurnController
     * @param gameController Game controller
     */
    public TurnController(GameController gameController) {
        this.model = Game.getInstance();
        this.gameController = gameController;
        this.playerQueue = new ArrayList<>(model.getPlayers());
        model.setCurrentPlayer(playerQueue.get(0));
    }

    /**
     * Creates a new turn
     */
    public void newTurn() {
        model.handleDrawPhase();
        //game.setGamePhase(TurnPhase.DRAW);
    }

    public void drawPhase(Message message) {
        if(message.getType()== MessageType.DRAW_TILES){
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
        if(nowCurrent == playerQueue.size()-1 && lastTurn){
            //Basta giocare, si calcolano i punteggi
        }
        else{
            model.setCurrentPlayer(playerQueue.get((nowCurrent+1) % playerQueue.size()));
            newTurn();
        }
    }

    /**
     * Checks if current player filled his bookshelf and adds EndToken points if deserved
     */
    private void booleanWinCondition(){
        if(!lastTurn && model.getCurrentPlayer().endTrigger()){
            lastTurn=true;
            model.getCurrentPlayer().addPoints(1);
        }
    }

    public void refill(){
        //game.getBoard().refillBoard(game.getBag().drawItems(game.getBoard().numCellsToRefill()));
        model.handleRefillPhase();
        //loadNextPhase();
    }
    public void calculateCommonGoal(){
        int first=0,second=1;
        if(model.getCommonGoals().get(first).check(model.getCurrentPlayer().getBookshelf())){
            model.getCommonGoals().get(first).takePoints(model.getCurrentPlayer());
        } else if(model.getCommonGoals().get(second).check(model.getCurrentPlayer().getBookshelf())){
            model.getCommonGoals().get(second).takePoints(model.getCurrentPlayer());
        }
        //loadNextPhase();
    }

    public void insertPhase(Message message) {
        if(message.getType()== MessageType.INSERT_TILES){
            InsertTilesMessage m = (InsertTilesMessage) message;
            if(isInsertHandValid(m.getItems(), m.getColumn())){
                model.insertTiles(m.getItems(), m.getColumn());
            }
            else{
                System.err.println("Tessere non valide");
            }
        }
        else{
            model.notifyObservers(new ErrorMessage(message.getUsername(), "Messaggio non valido"));
        }
    }

    /**
     * Checks if the tiles in the message are valid
     * @param items tiles in the message
     * @param column column in the message
     * @return true if the tiles are valid, false otherwise
     */
    private boolean isInsertHandValid(ArrayList<ItemTile> items, int column) {
        if(items.size()!=model.getCurrentPlayer().getHand().size()) return false; //return false if the number of tiles in the hand is different from the number of tiles in the message
        if(!items.containsAll(model.getCurrentPlayer().getHand())) return false; //return false if the tiles in the hand are different from the tiles in the message
        return model.getCurrentPlayer().getBookshelf().availableSlotsForColumn(column)>=items.size(); //return false if the column has less available slots than the number of tiles in the message
    }
}
