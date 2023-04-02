package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.GamePhase;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.utils.Observer;
import it.polimi.ingsw.view.ProtoCli;

import java.util.ArrayList;

public class GameController implements Observer {
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 3;
    private Game game;
    private GameLobby lobby;

    //stuff for the view
    public final ProtoCli view;
    private GamePhase gamePhase;
    private TurnController turnController;

    /**
     * Controller of the game
     */
    public GameController(ProtoCli view){
        this.view = view;
        setupGameController();
    }
    /**
     * Initializes the Game Controller getting a in instance of the new game
     */
    public void setupGameController(){
        this.game = Game.getInstance();
        //set views here
        setGamePhase(GamePhase.LOGIN);
    }

    public void play(){
        //Inizia partita
        game.startGame();
        // Avvia il primo turno di gioco

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
        winner = declareWinner();
    }

    /**
     * Declares the winner of the game comparing every player's score
     * @return winner of the game
     */
    private Player declareWinner() {
        //TODO improve with playersqueue from turncontroller
        ArrayList<Player> players = game.getPlayers();
        Player winner = players.get(0);
        for(Player p : players){
            if(p.getScore()>=winner.getScore())
                winner = p;
        }
        return winner;
    }

    /**
     * Adds adjacent items extra points to each player
     */
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

    @Override
    public void update(Message message, Observable o) {
        //ricevo notifiche solo dalla mia view
        if(o != view){
            System.err.println("Discarding notification");
            return;
        }
        switch (message.getType()) {
            case LOGINREQUEST:
                game.addPlayer(new Player(message.getUsername()));
                game.setChosenNumOfPlayers(2);
                game.startGame();
                game.getBoard().enableSquaresWithFreeSide();
                view.askDraw(game.getBoard().getGameboard());
                break;
            case DRAW_TILES:
                view.showGreeting();
                //turnController.drawPhase();
                break;
            default:
                System.err.println("Discarding event ");
        }
    }
    //public

    //game initialization : preparing board, extracting common goals...


}
