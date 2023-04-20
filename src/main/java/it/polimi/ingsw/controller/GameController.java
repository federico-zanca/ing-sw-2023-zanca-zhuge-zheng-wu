package it.polimi.ingsw.controller;


import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.GamePhase;
import it.polimi.ingsw.model.exceptions.GameNotReadyException;
import it.polimi.ingsw.model.exceptions.InvalidComandException;
import it.polimi.ingsw.model.exceptions.InvalidUsernameException;
import it.polimi.ingsw.network.message.gamemessage.ErrorMessage;
import it.polimi.ingsw.network.message.gamemessage.GameMessage;
import it.polimi.ingsw.network.message.gamemessage.GameMessageType;
import it.polimi.ingsw.network.message.gamemessage.LoginRequest;

import java.util.ArrayList;

public class GameController {
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;
    private Game model;
    //stuff for the view
    private TurnController turnController;

    //constructors and setters
    /**
     * Controller of the game
     */
    public GameController(Game model){
        setupGameController(model);
    }
    /**
     * Initializes the Game Controller getting a in instance of the new game
     */
    public void setupGameController(Game model){
        this.model = model;
        this.turnController = new TurnController(this);
        setGamePhase(GamePhase.LOGIN);
    }
    /**
     * Set the Phase of the current
     * @param phase
     */
    private void setGamePhase(GamePhase phase) {
        model.setGamePhase(phase);
    }

    //getters
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

    //endgame methods
    /**
     * Declares the winner of the game comparing every player's score
     * @return winner of the game
     */
    private Player declareWinner() {
        //TODO improve with playersqueue from turncontroller
        ArrayList<Player> players = model.getPlayers();
        Player winner = players.get(0);
        for(Player p : players){
            if(p.getScore()>=winner.getScore())
                winner = p;
        }
        return winner;
    }
    /** Method called to calculate points to add to each player and declare the winner
     */
    public void awardPhase(){
        Player winner;
        model.assignPersonalGoalPoints();
        model.assignAdjacentItemsPoints();
        winner = declareWinner();
        setGamePhase(GamePhase.ENDED);
        model.endGame(winner, turnController.getPlayerQueueUsernames());
        //TODO send broadcast message
    }


    //will be removed and reimplemented in Server class

    //phases handling methods
    /**
     * Handles the LoginPhase related messages received from the view
     * @param message
     */
    public void loginPhase(GameMessage message){
        if(message.getType()== GameMessageType.LOGINREQUEST) {
            handleLoginRequest((LoginRequest) message);
        }
        else{
            model.notifyObservers(new ErrorMessage(message.getUsername(), "Messaggio non valido"));
        }
    }
    public void handleLoginRequest(LoginRequest message){
        String username = message.getUsername();
        if(validUsernameFormat(username)){
            if(!model.isUsernameTaken(username)){
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
            turnController.setPlayersQueue(model.getPlayers());
            model.setCurrentPlayer(turnController.getPlayerQueue().get(0));

            //nextGamePhase();
            turnController.newTurn();
        }
    }
    /**
     * Handles the PlayPhase related messages received from the view
     * @param message
     */
    private void playPhase(GameMessage message) {
        switch(model.getTurnPhase()){
            case DRAW:
                turnController.drawPhase(message);
                break;
            case INSERT:
                turnController.insertPhase(message);
                break;
                /*
            case CALCULATE:
                turnController.calculateCommonGoal();
                break;
            case REFILL:
                turnController.refillPhase();
                break;
                 */
            default:
                System.err.println("Invalid Turn Phase: should never reach this state");
        }
        if(model.getGamePhase()==GamePhase.AWARDS) {
            awardPhase();
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
    public void onMessageReceived(GameMessage receivedMessage) {

        //VirtualView virtualView = virtualViewMap.get(receivedMessage.getNickname());
        switch (model.getGamePhase()) {
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

    public void update(Client client, GameMessage message) {
        //ricevo notifiche solo dalla mia view
        /*
        if(client != view){
            System.err.println("Discarding notification from "+ client);
            return;
        }
        */
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

    public void startGame() throws GameNotReadyException {
        if(model.isGameReadyToStart()){
            System.err.println("IL GIOCO INIZIA");
            model.startGame();
            turnController.setPlayersQueue(model.getPlayers());
            model.setCurrentPlayer(turnController.getPlayerQueue().get(0));

            //nextGamePhase();
            turnController.newTurn();
        } else{
            throw new GameNotReadyException("Game not ready to start");
        }
    }

    public void changeChosenNumOfPlayers(int chosenNum) throws InvalidComandException{
        model.setChosenNumOfPlayers(chosenNum);
    }

    public void addPlayer(String username) throws InvalidUsernameException {
        if(validUsernameFormat(username)){
            model.addPlayer(new Player(username));
        } else{
            throw new InvalidUsernameException();
        }
    }

    public void removePlayer(String username) {
        model.removePlayer(model.getPlayerByUsername(username));
    }

    public String getCurrentPlayerUsername() {
        return model.getCurrentPlayer().getUsername();
    }


    //public

    //game initialization : preparing board, extracting common goals...


}
