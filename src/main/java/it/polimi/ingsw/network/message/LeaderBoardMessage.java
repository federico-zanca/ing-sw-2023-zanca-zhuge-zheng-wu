package it.polimi.ingsw.network.message;

import java.util.LinkedHashMap;

public class LeaderBoardMessage extends Message {
    private final LinkedHashMap<String, Integer> leaderboard;

    public LeaderBoardMessage(String username, LinkedHashMap<String, Integer> leaderboard) {
        super(username, MessageType.LEADERBOARD);
        this.leaderboard = leaderboard;
    }

    public LinkedHashMap<String, Integer> getLeaderboard() {
        return leaderboard;
    }
}

