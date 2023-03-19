package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.GamePhase;

public class GameController {

    private Game game;

    private GamePhase gamePhase;
    private TurnController turnController;

    public GameController(Game game){
        this.game = game;
        setGamePhase(GamePhase.LOGIN);
    }

    private void setGamePhase(GamePhase phase) {
        gamePhase = phase;
    }

    private boolean booleanWinCondition(){
        return turnController.getCurrentPlayer().endTrigger();
    }

    public Game getGame() {
        return game;
    }

    //public

    //game initialization : preparing board, extracting common goals...


}
