package it.polimi.ingsw.controller;


import it.polimi.ingsw.distributed.Lobby;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.GamePhase;
import it.polimi.ingsw.model.exceptions.GameNotReadyException;
import it.polimi.ingsw.model.exceptions.InvalidCommandException;
import it.polimi.ingsw.model.exceptions.InvalidUsernameException;
import it.polimi.ingsw.network.message.MessageToServer;
import it.polimi.ingsw.network.message.gamemessage.ExitGameRequest;

import java.util.ArrayList;
import java.util.Random;

public class GameController {
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;
    private final Lobby lobby;
    private Game model;
    //stuff for the view
    private TurnController turnController;

    //constructors and setters
    /**
     * Controller of the game
     */
    public GameController(Game model, Lobby lobby){
        this.lobby = lobby;
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
        lobby.endGame();
    }


    //will be removed and reimplemented in Server class

    //phases handling methods


    /**
     * Handles the PlayPhase related messages received from the view
     * @param message
     */
    private void playPhase(String senderUsername, MessageToServer message) {
        switch(model.getTurnPhase()){
            case DRAW:
                turnController.drawPhase(senderUsername, message);
                break;
            case INSERT:
                turnController.insertPhase(senderUsername, message);
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
    public void onMessageReceived(String senderUsername, MessageToServer receivedMessage) {
        switch (model.getGamePhase()) {
            case INIT:
                System.err.println("Should not receive messages during this phase");
                break;
            case PLAY:
                playPhase(senderUsername, receivedMessage);
                break;
            case AWARDS:
                System.err.println("AWARDS phase not handled yet");
                break;
            default:
                System.err.println("Invalid Game Phase: should never reach this state");
                break;
        }
    }

    public void update(String senderUsername, MessageToServer message) {
        if(message instanceof ExitGameRequest){
            turnController.addPlayerToSkip(model.getPlayerByUsername(senderUsername));
        }
        else if(model.getGamePhase()==GamePhase.PLAY && senderUsername.equals(model.getCurrentPlayer().getUsername())) {
                onMessageReceived(senderUsername, message);
        } else {
            System.err.println("Discarding game message: wrong GamePhase or not CurrentPlayer ");
        }
    }

    public void startGame() throws GameNotReadyException {
        if(model.isGameReadyToStart()){
            System.err.println("IL GIOCO INIZIA");
            turnController.setPlayersQueue(scramble(model.getPlayers()));
            model.setCurrentPlayer(turnController.getPlayerQueue().get(0));
            model.startGame();

            //nextGamePhase();
            turnController.newTurn();
        } else{
            throw new GameNotReadyException("Game not ready to start");
        }
    }

    private ArrayList<Player> scramble(ArrayList<Player> modelPlayers) {
        ArrayList<Player> players = new ArrayList<>(modelPlayers);
        ArrayList<Player> scrambled = new ArrayList<>();
        while(players.size()>0){
            Random random = new Random();
            int index = random.nextInt(players.size());
            scrambled.add(players.get(index));
            players.remove(index);
        }
        return scrambled;
    }

    /**
     * Changes the chosen number of players in the model.
     *
     * @param chosenNum the new number of players chosen
     * @throws InvalidCommandException if the chosen number is invalid
     */
    public void changeChosenNumOfPlayers(int chosenNum) throws InvalidCommandException {
        model.setChosenNumOfPlayers(chosenNum);
    }

    /**
     * Adds a new player to the game with the specified username.
     *
     * @param username the username of the new player
     * @throws InvalidUsernameException if the username is invalid
     */
    public void addPlayer(String username) throws InvalidUsernameException {
        if(validUsernameFormat(username)){
            model.addPlayer(new Player(username));
        } else{
            throw new InvalidUsernameException();
        }
    }

    /**
     * Removes the player with the specified username from the game.
     *
     * @param username the username of the player to be removed
     */
    public void removePlayer(String username) {
        model.removePlayer(model.getPlayerByUsername(username));
    }

    /**
     * Gets the username of the current player.
     *
     * @return the username of the current player
     */
    public String getCurrentPlayerUsername() {
        return model.getCurrentPlayer().getUsername();
    }

    public void reconnectPlayer(String username) {
        turnController.reconnectExitedPlayer(model.getPlayerByUsername(username));
    }

    public void disconnectPlayer(String username) {
        Player player = model.getPlayerByUsername(username);
        turnController.addPlayerToSkip(player);
    }

    public void resetPlayers() {
        model.resetPlayers();
    }
}
