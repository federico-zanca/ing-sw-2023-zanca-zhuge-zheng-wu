package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MsgToClient;
import it.polimi.ingsw.view.View;

public class NewAdminMessage extends LobbyMessage implements MsgToClient {
    private final String old_admin;
    private final String new_admin;

    public NewAdminMessage(String old_admin, String new_admin) {
        super(LobbyMessageType.NEW_ADMIN);
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
