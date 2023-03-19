package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.TurnPhase;

import java.util.ArrayList;

public class TurnController {
    private final Game game;

    private final GameController gameController;

    private ArrayList<Player> playerQueue;
    private Player currentPlayer;

    private TurnPhase turnPhase;

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
     * @return the active player this turn
     */
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * Set the current player
     * @param player the current player to be set
     */
    public void setCurrentPlayer(Player player){
        currentPlayer = player;
    }

    public void loadNextPlayer(){

        int nowCurrent = playerQueue.indexOf(currentPlayer);
        setCurrentPlayer(playerQueue.get((nowCurrent+1) % playerQueue.size()));
        setTurnPhase(TurnPhase.TWO);
    }

    /**
     * Set the current turn phase
     * @param phase the phase to be set
     */
    private void setTurnPhase(TurnPhase phase) {
        this.turnPhase = phase;
    }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }


}
