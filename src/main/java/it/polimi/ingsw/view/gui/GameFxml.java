package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.sceneControllers.GuiPhase;
import it.polimi.ingsw.view.tui.ActionType;

public enum GameFxml {
    MENU_SCENE("menu_scene.fxml"),
    LOBBY_SCENE("lobby_scene.fxml");

    private GuiPhase phase;

    public final String s;
    static{
        MENU_SCENE.phase = GuiPhase.LOGIN;
        LOBBY_SCENE.phase = GuiPhase.LOBBY;
    }
    GameFxml(String s) {
        this.s=s;
    }

    GuiPhase getGuiPhase(){
        return phase;
    }

}
