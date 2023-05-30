package it.polimi.ingsw.network.message.connectionmessage;

import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import it.polimi.ingsw.network.message.ChatMessage;
import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class ReconnectionMessage extends MessageToClient {
    private final GameView model;
    private final String content;
    private final PersonalGoalCard personalGoal;
    private final ArrayList<ChatMessage> chat;

    public ReconnectionMessage(GameView gameView, String content, PersonalGoalCard personalGoal, ArrayList<ChatMessage> chat) {
        super(MessageType.CONNECTION_MSG);
        this.model = gameView;
        this.content = content;
        this.personalGoal = personalGoal;
        this.chat = chat;
    }

    public GameView getModel() {
        return model;
    }

    public String getContent() {
        return content;
    }

    public PersonalGoalCard getPersonalGoal() {
        return personalGoal;
    }

    @Override
    public void execute(View view) {
        view.onReconnectionMessage(this);
    }

    public ArrayList<ChatMessage> getChat() {
        return chat;
    }
}
