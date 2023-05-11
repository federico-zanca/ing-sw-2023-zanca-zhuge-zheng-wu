package it.polimi.ingsw.view.tui;

import java.io.Serializable;

public class LobbyDisplayInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String lobbyName;
    private final int numPlayers;
    private final int numPlayersChosen;
    private final String gameStarted;

    public LobbyDisplayInfo(String lobbyName, int numPlayers, int numPlayersChosen, String gameStarted) {
        this.lobbyName = lobbyName;
        this.numPlayers = numPlayers;
        this.numPlayersChosen = numPlayersChosen;
        this.gameStarted = gameStarted;
    }

    public String getLobbyName() {
        return lobbyName;
    }


    public String isGameStarted() {
        return gameStarted;
    }

    public int getNumPlayersChosen() {
        return numPlayersChosen;
    }

    public int getNumPlayers() {
        return numPlayers;
    }
}
