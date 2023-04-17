package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.gamemessage.GameMessageType;

import java.util.LinkedHashMap;

public class LeaderBoardMessage extends GameMessage {
    private final LinkedHashMap<String, Integer> leaderboard;

    public LeaderBoardMessage(String username, LinkedHashMap<String, Integer> leaderboard) {
        super(username, GameMessageType.LEADERBOARD);
        this.leaderboard = leaderboard;
    }

    public LinkedHashMap<String, Integer> getLeaderboard() {
        return leaderboard;
    }
}

