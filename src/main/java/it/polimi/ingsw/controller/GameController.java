package it.polimi.ingsw.controller;


import it.polimi.ingsw.Server;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.GamePhase;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.utils.Observer;
import it.polimi.ingsw.view.ProtoCli;
import it.polimi.ingsw.view.View;
//import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.Collections;

public class GameController implements Observer {
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 3;
    private Game model;
    private GameLobby lobby;

    //stuff for the view
    public final ProtoCli view;
    private TurnController turnController;

    /**
     * Controller of the game
     */
    public GameController(ProtoCli view){
        this.view = view; //TODO migrate to model and remove
        setupGameController();
    }
    /**
     * Initializes the Game Controller getting a in instance of the new game
     */
    public void setupGameController(){
        this.model = Game.getInstance();
        this.lobby = new GameLobby();
        this.turnController = new TurnController(this);
        setGamePhase(GamePhase.LOGIN);
    }

    /**
     * @return current game controlled
     */
    public Game getModel() {
        return model;
    }
    /**
     * @return the instance of the {@link TurnController turnController}
     */
    public TurnController getTurnController(){
        return turnController;
    }

    /** Method called to calculate points to add to each player and declare the winner
     */
    public void endGame(){
        Player winner;
        assignPersonalGoalPoints();
        assignAdjacentItemsPoints();
        winner = declareWinner();
        setGamePhase(GamePhase.ENDED);
        //TODO send broadcast message
    }
    /** Method called to calculate points to add to each player and declare the winner
     */
    public void endGame(){
        Player winner;
        assignPersonalGoalPoints();
        assignAdjacentItemsPoints();
        winner = declareWinner();
        setGamePhase(GamePhase.ENDED);
        //TODO send broadcast message
    }

    //points calculators
    /**
     * Adds adjacent items extra points to each player
     */
    private void assignAdjacentItemsPoints() {
        int points;
        ArrayList<Player> players = model.getPlayers();
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
        ArrayList<Player> players = model.getPlayers();
        for(Player p : players){
            points = p.calculateScorePersonalGoal();
            p.addPoints(points);
        }
    }
    //will be removed and reimplemented in Server class

    //phases handling methods
    public void loginPhase(Message message){
        if(message.getType()== MessageType.LOGINREQUEST) {
            handleLoginRequest((LoginRequest) message);
        }
        else{
            model.notifyObservers(new ErrorMessage(message.getUsername(), "Messaggio non valido"));
        }
    }
    public void handleLoginRequest(LoginRequest message){
        String username = message.getUsername();
        if(validUsernameFormat(username)){
            if(model.getPlayerByUsername(username)==null){
                model.addPlayer(new Player(username));
            }
            else{
                System.err.println("Errore critico: esiste già qualcuno loggato con questo username\n"); //TODO invia messaggio a client: player già esistente
            }
        }
        else{
            System.err.println("Errore critico: formato dell'username non valido"); //TODO invia messaggio al client per reinserire (formato non valido)
        }
        if(model.isGameReadyToStart()){
            System.err.println("IL GIOCO INIZIA");
            model.startGame();
            //nextGamePhase();
            turnController.newTurn();
        }
    }
    private void playPhase(Message message) {
        switch(model.getTurnPhase()){
            case DRAW:
                turnController.drawPhase(message);
                break;
            case INSERT:
                break;
            case REFILL:
                break;
            case CALCULATE:
                break;
            default:
                System.err.println("Invalid Turn Phase: should never reach this state");
        }

    }

    //validity check methods
    /**
     * Checks whether the given username is valid according to certain criteria.
     *
     * @param username the string representing the username to be validated.
     * @return true if the username meets all of the validation criteria outlined below, false otherwise.
     *
     * Criteria for a valid username:
     * 1. Does not contain any spaces.
     * 2. Does not start with a special character (-, _, or .).
     * 3. Does not end with - or .
     * 4. Does not contain any characters that are not letters or digits or one of the allowed special characters (-, _, or .).
     */
    private boolean validUsernameFormat(String username) {
        // Check for spaces in username
        if (username.contains(" ")) {
            return false;
        }

        // Check if username starts or ends with a special character or if it is solely composed of numbers
        char firstChar = username.charAt(0);
        if (firstChar == '-' || firstChar == '_' || firstChar == '.'  || username.endsWith("-") || username.endsWith(".") || username.matches("[0-9]+")) {
            return false;
        }

        // Check for non-literal or non-numeric characters other than '-', '_' and '.'
        String pattern = "[^a-zA-Z0-9\\-_\\.]";
        if (username.matches(".*" + pattern + ".*")) {
            return false;
        }

        // All checks passed, username is valid
        return true;
    }

    /**
     * Switch on Game State.
     * @param receivedMessage Message from Active Player.
     */
    public void onMessageReceived(Message receivedMessage) {

        //VirtualView virtualView = virtualViewMap.get(receivedMessage.getNickname());
        switch (model.getGamePhase()) {
            case LOGIN:
                loginPhase(receivedMessage);
                break;
            case INIT:
                //if (inputController.checkUser(receivedMessage)) {
                //    initState(receivedMessage, virtualView);
                //}
                break;
            case PLAY:
                //if (inputController.checkUser(receivedMessage)) {
                //inGameState(receivedMessage);
                //}
                playPhase(receivedMessage);
                break;
            case AWARDS:
                System.err.println("AWARDS phase not handled yet");
                break;
            default: // Should never reach this condition
                //Server.LOGGER.warning(STR_INVALID_STATE);
                break;
        }
    }

    @Override
    public void update(Message message, Observable o) {
        //ricevo notifiche solo dalla mia view
        if(o != view){
            System.err.println("Discarding notification from "+o);
            return;
        }
        onMessageReceived(message);
        /*
        String username = message.getUsername();
       // Player player = game.getPlayerByUsername(username);

        //DEPRECATED PART
        switch (message.getType()) {
            case LOGINREQUEST:
                game.setChosenNumOfPlayers(4); //andrà tolta
                handleLoginRequest((LoginRequest) message);
                //game.startGame();
                //game.getBoard().enableSquaresWithFreeSide();
                //int maxNumItems = game.getPlayerByUsername(username).getBookshelf().maxSlotsAvailable();
                //view.askDraw(username, game.getBoard().getGameboard(), game.getPlayerByUsername(username).getBookshelf().getShelfie(), maxNumItems);
                break;
            case DRAW_TILES:
                if(isDrawHandValid((DrawTilesMessage) message)){
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


         */
    }




    //public

    //game initialization : preparing board, extracting common goals...


}
