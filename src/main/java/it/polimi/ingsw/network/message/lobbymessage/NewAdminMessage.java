package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

public class NewAdminMessage extends MessageToClient {
    private final String old_admin;
    private final String new_admin;

    public NewAdminMessage(String old_admin, String new_admin) {
        super(MessageType.LOBBY_MSG);
        this.old_admin = old_admin;
        this.new_admin = new_admin;
    }

    public String getOld_admin() {
        return old_admin;
    }

    public String getNew_admin() {
        return new_admin;
    }

    @Override
    public void execute(View view) {
        view.onNewAdminMessage(this);
    }
}
