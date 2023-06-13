package it.polimi.ingsw.network.message.lobbymessage;

import it.polimi.ingsw.network.message.MessageToClient;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.View;

/**
 * Represents a message sent to the client indicating a change in the lobby administrator.
 * Inherits from the {@link MessageToClient} class.
 */
public class NewAdminMessage extends MessageToClient {
    private final String old_admin;
    private final String new_admin;
    /**
     * Constructs a new NewAdminMessage.
     *
     * @param old_admin The username of the previous lobby administrator.
     * @param new_admin The username of the new lobby administrator.
     */
    public NewAdminMessage(String old_admin, String new_admin) {
        super(MessageType.LOBBY_MSG);
        this.old_admin = old_admin;
        this.new_admin = new_admin;
    }
    /**
     * Gets the username of the previous lobby administrator.
     *
     * @return The username of the previous lobby administrator.
     */
    public String getOld_admin() {
        return old_admin;
    }

    /**
     * Gets the username of the new lobby administrator.
     *
     * @return The username of the previous lobby administrator.
     */
    public String getNew_admin() {
        return new_admin;
    }

    /**
     * Executes the message on the provided view, triggering the onNewAdminMessage callback.
     * @param view The view on which to execute the message.
     */
    @Override
    public void execute(View view) {
        view.onNewAdminMessage(this);
    }
}
