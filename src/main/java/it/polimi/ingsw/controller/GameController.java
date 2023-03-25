package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.GamePhase;

import java.util.ArrayList;

public class GameController {
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 3;
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

    /** Method called to calculate points to add to each player and declare the winner
     */
    public void endGame(){
        Player winner;
        assignPersonalGoalPoints();
        assignAdjacentItemsPoints();
        //TODO fai funzione scelta vincitore
        //winner = declareWinner();

    }

    private void assignAdjacentItemsPoints() {
        int points;
        ArrayList<Player> players = game.getPlayers();
        for(Player p : players){
            points = p.calculateAdjacentItemsPoints();
            p.addPoints(points);
        }
    }


    /**
     * Add personal goal points for each player
     */
    public void assignPersonalGoalPoints(){
        int points;
        ArrayList<Player> players = game.getPlayers();
        for(Player p : players){
            points = p.calculateScorePersonalGoal();
            p.addPoints(points);
        }
    }
    //public

    //game initialization : preparing board, extracting common goals...


}
