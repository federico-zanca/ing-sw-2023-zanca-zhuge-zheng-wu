package it.polimi.ingsw.network.message.gamemessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

import java.util.LinkedHashMap;

public class LeaderBoardMessage extends MessageToClient {
    private final LinkedHashMap<String, Integer> leaderboard;
    private final String username;

    public LeaderBoardMessage(String username, LinkedHashMap<String, Integer> leaderboard) {
        super(MessageType.GAME_MSG);
        this.username = username;
        this.leaderboard = leaderboard;
    }

    public LinkedHashMap<String, Integer> getLeaderboard() {
        return leaderboard;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void execute(View view) {
        view.onLeaderBoardMessage(this);
    }
}

