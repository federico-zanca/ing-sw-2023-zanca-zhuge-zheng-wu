package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.view.gui.MessageHandler;
import it.polimi.ingsw.view.gui.GUI;

public interface Controller {
    void setMessageHandler(MessageHandler messageHandler);
    void setGui(GUI gui);
    void initialize();
}
