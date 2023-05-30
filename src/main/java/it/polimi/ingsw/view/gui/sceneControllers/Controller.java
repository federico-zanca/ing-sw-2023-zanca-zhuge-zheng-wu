package it.polimi.ingsw.view.gui.sceneControllers;

import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.MessageHandler;

public interface Controller {
    void setMessageHandler(MessageHandler messageHandler);
    void setGui(GUI gui);
    void initialize();
}
