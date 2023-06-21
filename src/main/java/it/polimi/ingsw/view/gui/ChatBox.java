package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.message.ChatMessage;
import java.util.ArrayList;

public class ChatBox {
    private ArrayList<ChatMessage> chatLog;

    public ChatBox (){
        chatLog = new ArrayList<>();
    }

    public void addMessage(ChatMessage message){
        chatLog.add(message);
    }
    public void setChatLog(ArrayList<ChatMessage> chatLog) {
        this.chatLog = chatLog;
    }
    public ArrayList<ChatMessage> getChatLog(){
        return chatLog;
    }
}
