package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commongoals.*;
import it.polimi.ingsw.model.enumerations.GamePhase;
import it.polimi.ingsw.model.enumerations.TurnPhase;
import it.polimi.ingsw.model.gameboard.Board;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.utils.Observer;
import it.polimi.ingsw.view.View;

import java.util.*;
import java.util.stream.Collectors;

public class GameView extends Observable implements Observer {
        private final Game model;

        public GameView(Game model){
            this.model = model;
            model.addObserver(this);
        }


        /**
         * Checks if game is ready to start
         * @return true if the number of players currently in game is equal to the number of players specified for the game
         */
        public boolean isGameReadyToStart(){
            return model.isGameReadyToStart();
        }

        /**
         * @return the GamePhase in which the game is
         */
        public GamePhase getGamePhase(){
            return model.getGamePhase();
        }


        /**
         * Extract a random personal goal card
         * @return random personalgoal
         */
        public PersonalGoalCard randomPersonalGoal(){
            return model.randomPersonalGoal();
        }

        public ArrayList<ItemTile> getDrawnTiles() {
            return model.getDrawnTiles();
        }

        //  PLAYERS RELATED STUFF

        /**
         * Returns a player given his {@code username}.
         * Only the first occurrence is returned because
         * the player username is considered to be unique.
         * If no player is found {@code null} is returned.
         *
         * @param username the username of the player to be found.
         * @return Returns the player given his {@code username}, {@code null} otherwise.
         */
        public Player getPlayerByUsername(String username) {
            return model.getPlayerByUsername(username);
        }


        /**
         * Returns the first player to play.
         * @return first player to play
         */
        public Player getFirstPlayer(){
            return model.getFirstPlayer();
        }

        /**Returns the number of players
         *
         * @return the number of players in game
         */
        public int getCurrNumOfPlayers() {
            return model.getCurrNumOfPlayers();
        }

        /** Returns the number of players chosen for the game.
         *
         * @return the number of players chosen for the game
         */
        public int getChosenPlayersNumber() {
            return model.getChosenPlayersNumber();
        }


        /**
         * @return the active player this turn
         */
        public Player getCurrentPlayer(){
            return model.getCurrentPlayer();
        }

        /**
         * Search a username in the current Game.
         *
         * @param username the username of the player.
         * @return {@code true} if the username is found, {@code false} otherwise.
         */
        public boolean isUsernameTaken(String username){
            return model.isUsernameTaken(username);
        }

        /**
         * Returns a list of player usernames that are already in-game.
         *
         * @return a list with all usernames in the Game
         */
        public ArrayList<String> getPlayersUsernames() {
            return model.getPlayersUsernames();
        }


        /**
         * Returns a list of players.
         *
         * @return the players.
         */
        public ArrayList<Player> getPlayers() {
            return model.getPlayers();
        }

        /**
         * Returns the current board.
         * @return the board of the game.
         */
        public Board getBoard() {
            return model.getBoard();
        }

        /** Returns the current bag.
         *
         * @return the bag of the game.
         */
        public Bag getBag() {
            return model.getBag();
        }

        /**
         * @return the TurnPhase of the game
         */
        public TurnPhase getTurnPhase() {
            return model.getTurnPhase();
        }

        /**
         * Getter for CommonGoals
         * @return CommonGoals of the game
         */
        public ArrayList<CommonGoalCard> getCommonGoals() {
            return model.getCommonGoals();
        }

        public boolean isLastTurn() {
            return model.isLastTurn();
        }


    @Override
    public void update(Message message, Observable o) {
        notifyObservers(message);
    }
}
    /*
    @Override
    public void askPlayerNumber() {

    }

    @Override
    public void showLoginResult(boolean valiUsername, String username) {

    }

    @Override
    public void showLobby() {

    }

    @Override
    public void showFirstPlayerThisGame() {

    }

    @Override
    public void showWinMessage() {

    }
*/


