package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.GamePhase;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.DrawTilesMessage;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.utils.Observer;
import it.polimi.ingsw.view.ProtoCli;

import java.util.ArrayList;
import java.util.Collections;

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
        String username = message.getUsername();
       // Player player = game.getPlayerByUsername(username);
        switch (message.getType()) {
            case LOGINREQUEST:
                game.addPlayer(new Player(username));
                System.out.println(game.getPlayersUsernames());
                game.setChosenNumOfPlayers(4);
                game.startGame();
                game.getBoard().enableSquaresWithFreeSide();
                int maxNumItems = game.getPlayerByUsername(username).getBookshelf().maxSlotsAvailable();
                view.askDraw(username, game.getBoard().getGameboard(), game.getPlayerByUsername(username).getBookshelf().getShelfie(), maxNumItems);
                break;
            case DRAW_TILES:
                if(isDrawPhaseValid((DrawTilesMessage) message)){
                    ArrayList<Integer> columns = game.getPlayerByUsername(username).getBookshelf().insertableColumns(((DrawTilesMessage) message).getSquares().size());
                    view.askInsert(username, ((DrawTilesMessage) message).getSquares(), game.getPlayerByUsername(username).getBookshelf(), columns);
                }else{
                    view.rejectDrawRequest(message.getUsername(), game.getBoard().getGameboard(), game.getPlayerByUsername(username).getBookshelf().getShelfie(), game.getPlayerByUsername(username).getBookshelf().maxSlotsAvailable());
                }
                view.showGreeting();
                //turnController.drawPhase();
                view.showBookshelf(username, game.getPlayerByUsername(username).getBookshelf().getShelfie());
                break;
            case INSERT_TILES:
                break;
            default:
                System.err.println("Discarding event ");
        }
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
    private boolean isDrawPhaseValid(DrawTilesMessage message){
        String username = message.getUsername();
        ArrayList<Square> hand = message.getSquares();

        for(Square sq : hand){
            if(sq.isPickable()) return false;
        }
        return inLineTile(hand);
    }
    //public

    //game initialization : preparing board, extracting common goals...


}
