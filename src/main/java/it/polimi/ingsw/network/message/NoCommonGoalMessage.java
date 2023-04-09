package it.polimi.ingsw.network.message;

public class NoCommonGoalMessage extends Message {

    private final String content = "No common goal achieved this round :(";
    public NoCommonGoalMessage(String username) {
        super(username, MessageType.NO_COMMON_GOAL);
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "NoCommonGoalMessage{" +
                "username='" + getUsername() + '\'' +
                '}';
    }
}
