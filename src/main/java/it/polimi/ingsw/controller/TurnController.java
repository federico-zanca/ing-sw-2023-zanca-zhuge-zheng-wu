package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.TurnPhase;
import it.polimi.ingsw.model.exceptions.FullColumnException;
import it.polimi.ingsw.model.gameboard.Board;

import java.util.ArrayList;
import java.util.Scanner;

public class TurnController {
    private final Game game;
    private final GameController gameController;
    private ArrayList<Player> playerQueue;
    private Player currentPlayer;
    private TurnPhase turnPhase;
    private boolean lastTurn = false;
    private ItemTile[] tiles;
    /**
     * Constructor of the TurnController
     * @param gameController Game controller
     */
    public TurnController(GameController gameController) {
        this.game = gameController.getGame();
        this.currentPlayer = playerQueue.get(0);
        this.gameController = gameController;
        this.playerQueue = new ArrayList<>(game.getPlayers());
    }
    /**
     * Set the current turn phase
     * @param phase the phase to be set
     */
    private void setTurnPhase(TurnPhase phase) {
        this.turnPhase = phase;
    }
    /**
     * @return current turn phase
     */
    public TurnPhase getTurnPhase() {
        return turnPhase;
    }
    /**
     * Creates a new turn
     */
    public void newTurn(){
        setTurnPhase(TurnPhase.DRAW);
        drawPhase();

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
            setCurrentPlayer(playerQueue.get((nowCurrent+1) % playerQueue.size()));
            newTurn();
        }
    }
    /**
     * Checks if current player filled his bookshelf and adds EndToken points if deserved
     */
    private void booleanWinCondition(){
        if(!lastTurn && getCurrentPlayer().endTrigger()){
            lastTurn=true;
            currentPlayer.addPoints(1);
        }
    }

    private void loadNextPhase(){
        switch (turnPhase) {
            case DRAW: setTurnPhase(TurnPhase.INSERT);
            case INSERT:
                if(game.getBoard().needsRefill()){setTurnPhase(TurnPhase.REFILL);}
                else{setTurnPhase(TurnPhase.CALCULATE);}
            case REFILL: setTurnPhase(TurnPhase.CALCULATE);
            //case CALCULATE: if(booleanWinCondition()) {
               // currentPlayer.addPoints(1);
              //  setTurnPhase(TurnPhase.LAST);
           // }else{
           //     loadNextPlayer();
            //}
            //TODO last round turnphase case
            case LAST:
        }
    }
    /*public void playerDraw(){
        int dummyinput1=0,dummyinput2=0,dummyinput3=0,dummyinput4=0;
        tiles[0] = currentPlayer.takeItem(game.getBoard(),dummyinput1,dummyinput2,dummyinput3,dummyinput4);
        loadNextPhase();

    }*/
    public void drawPhase() {
        game.getBoard().enableSquaresWithFreeSide();


    }

    public void playerDraw(){
        int playerIn_x, playerIn_y;
        ItemTile tile;
        Scanner input=new Scanner(System.in);
        boolean InputStop=false;
        Board board=game.getBoard();

        do{
            playerIn_x=Integer.parseInt(input.nextLine());
            playerIn_y=Integer.parseInt(input.nextLine());
            tile=currentPlayer.takeFirstItem(board, playerIn_x, playerIn_y);

        }while (playerIn_x<0 || playerIn_y<0 || tile==null);

        tiles[0]=tile;

        for(int i=1; i<3; i++){
            if(InputStop){ break;  }
            do{
                playerIn_x=Integer.parseInt(input.nextLine());
                playerIn_y=Integer.parseInt(input.nextLine());
                tile=currentPlayer.takeOtherItem(board, playerIn_x, playerIn_y);

            }while (playerIn_x<0 || playerIn_y<0 || tile==null);
            tiles[i]=tile;
        }

        loadNextPhase();
    }


    public void playerInsert() throws FullColumnException {
        int dummyInput=0;
        currentPlayer.getBookshelf().insertItem(tiles[0],dummyInput);

        loadNextPhase();
    }
    public void refill(){
        game.getBoard().refillBoard(game.getBag().drawItems(game.getBoard().numCellsToRefill()));
        loadNextPhase();
    }
    public void calculateCommonGoal(){
        int first=0,second=1;
        if(game.getCommonGoals().get(first).check(currentPlayer.getBookshelf())){
            game.getCommonGoals().get(first).takePoints(currentPlayer);
        } else if(game.getCommonGoals().get(second).check(currentPlayer.getBookshelf())){
            game.getCommonGoals().get(second).takePoints(currentPlayer);
        }
        loadNextPhase();
    }
}
