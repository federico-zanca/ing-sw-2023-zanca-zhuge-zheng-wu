package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.GamePhase;
import it.polimi.ingsw.model.enumerations.TurnPhase;
import it.polimi.ingsw.model.exceptions.FullColumnException;
import it.polimi.ingsw.model.gameboard.Board;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class TurnController {
    private final Game game;
    private final GameController gameController;
    private ArrayList<Player> playerQueue;
    private Player currentPlayer;

    private boolean lastTurn = false;
    private ItemTile[] tiles;
    /**
     * Constructor of the TurnController
     * @param gameController Game controller
     */
    public TurnController(GameController gameController) {
        this.game = Game.getInstance();
        this.gameController = gameController;
        this.playerQueue = new ArrayList<>(game.getPlayers());
        this.currentPlayer = playerQueue.get(0);
    }

    /**
     * Creates a new turn
     */
    public void newTurn() {
        game.handleDrawPhase();
        //game.setGamePhase(TurnPhase.DRAW);
    }

    public void drawPhase(Message message) {
        if(message.getType()== MessageType.DRAW_TILES){
            DrawTilesMessage m = (DrawTilesMessage) message;
            if(isDrawHandValid(m.getSquares())){
                game.drawFromBoard(m.getSquares());
            }
            else{
                System.err.println("Tessere non valide");
            }
        }
        else{
            game.notifyObservers(new ErrorMessage(message.getUsername(), "Messaggio non valido"));
        }
        //nextTurnPhase();
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
            if(!game.getBoard().getSquare(sq.getCoordinates()).isPickable()) return false;
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
        int nowCurrent = playerQueue.indexOf(currentPlayer);
        if(nowCurrent == playerQueue.size()-1 && lastTurn){
            //Basta giocare, si calcolano i punteggi
        }
        else{
            game.setCurrentPlayer(playerQueue.get((nowCurrent+1) % playerQueue.size()));
            newTurn();
        }
    }

    /**
     * Checks if current player filled his bookshelf and adds EndToken points if deserved
     */
    private void booleanWinCondition(){
        if(!lastTurn && game.getCurrentPlayer().endTrigger()){
            lastTurn=true;
            currentPlayer.addPoints(1);
        }
    }

 public void playerInsert() throws FullColumnException {
        int dummyInput=0;
        currentPlayer.getBookshelf().insertItem(tiles[0],dummyInput);

        //loadNextPhase();
    }
    public void refill(){
        //game.getBoard().refillBoard(game.getBag().drawItems(game.getBoard().numCellsToRefill()));
        game.handleRefillPhase();
        //loadNextPhase();
    }
    public void calculateCommonGoal(){
        int first=0,second=1;
        if(game.getCommonGoals().get(first).check(currentPlayer.getBookshelf())){
            game.getCommonGoals().get(first).takePoints(currentPlayer);
        } else if(game.getCommonGoals().get(second).check(currentPlayer.getBookshelf())){
            game.getCommonGoals().get(second).takePoints(currentPlayer);
        }
        //loadNextPhase();
    }
}
