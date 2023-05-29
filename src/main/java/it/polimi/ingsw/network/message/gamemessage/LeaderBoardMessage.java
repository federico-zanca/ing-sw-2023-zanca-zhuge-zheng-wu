package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

import java.util.LinkedHashMap;

public class LeaderBoardMessage extends GameMessage implements MsgToClient {
    private final LinkedHashMap<String, Integer> leaderboard;

    public LeaderBoardMessage(String username, LinkedHashMap<String, Integer> leaderboard) {
        super(username, GameMessageType.LEADERBOARD);
        this.leaderboard = leaderboard;
    }

    public LinkedHashMap<String, Integer> getLeaderboard() {
        return leaderboard;
    }

    @Override
    public void execute(View view) {
        view.onLeaderBoardMessage(this);
    }
}

