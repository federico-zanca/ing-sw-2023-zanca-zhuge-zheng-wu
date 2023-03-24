package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.GamePhase;

public class GameController {

    private Game game;
    private GameLobby lobby;
    private GamePhase gamePhase;
    private TurnController turnController;

    /**
     * Controller of the game
     * @param game game to control
     */
    public GameController(Game game){
        this.game = game;
        setGamePhase(GamePhase.LOGIN);
    }

    /**
     * Set the Phase of the current 
     * @param phase
     */
    private void setGamePhase(GamePhase phase) {
        gamePhase = phase;
    }
    /**
     * @return current game controlled
     */
    public Game getGame() {
        return game;
    }


    //public

    //game initialization : preparing board, extracting common goals...


}
