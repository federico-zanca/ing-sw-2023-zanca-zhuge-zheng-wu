package it.polimi.ingsw.view.tui;

import java.io.Serializable;
/**
 * Represents information about a lobby for display purposes.
 */
public class LobbyDisplayInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String lobbyName;
    private final int numPlayers;
    private final int numPlayersChosen;
    private final String gameStarted;
    /**
     * Constructs a LobbyDisplayInfo object with the specified lobby information.
     *
     * @param lobbyName the name of the lobby.
     * @param numPlayers the total number of players in the lobby.
     * @param numPlayersChosen the number of players that have been chosen in the lobby.
     * @param gameStarted a string representation indicating whether the game has started.
     */
    public LobbyDisplayInfo(String lobbyName, int numPlayers, int numPlayersChosen, String gameStarted) {
        this.lobbyName = lobbyName;
        this.numPlayers = numPlayers;
        this.numPlayersChosen = numPlayersChosen;
        this.gameStarted = gameStarted;
    }
    /**
     * Gets the name of the lobby.
     *
     * @return the name of the lobby.
     */
    public String getLobbyName() {
        return lobbyName;
    }

    /**
     * Checks if the game has started.
     *
     * @return a string representation indicating whether the game has started.
     */
    public String isGameStarted() {
        return gameStarted;
    }
    /**
     * Gets the number of players that have been chosen in the lobby.
     *
     * @return the number of players chosen in the lobby.
     */
    public int getNumPlayersChosen() {
        return numPlayersChosen;
    }
    /**
     * Retrieves the total number of players in the lobby.
     *
     * @return the total number of players in the lobby.
     */
    public int getNumPlayers() {
        return numPlayers;
    }
}
