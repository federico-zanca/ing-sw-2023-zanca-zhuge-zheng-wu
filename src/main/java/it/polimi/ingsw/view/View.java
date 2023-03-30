package it.polimi.ingsw.view;

public interface View {
    /**
     * Asks the user to choose a Username
     */
    void askUsername();

    /**
     * Asks the user how many players he wants to play with
     */
    void askPlayerNumber();

    /**
     * Tells the user if the login suceeded or not
     */
    void showLoginResult(boolean valiUsername, String username);
    //TODO gestisci anche Login Failed se la connessione salta

    /**
     * Shows the lobby with the connected players, max num of players accepted, admin ecc
     */
    void showLobby();

    /**
     * Shows who plays first this game
     */
    void showFirstPlayerThisGame();

    /**
     * Shows who won the game
     */
    void showWinMessage();
}
