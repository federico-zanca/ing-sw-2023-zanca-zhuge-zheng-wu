package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.sceneControllers.GuiPhase;
public enum GameFxml {
    MENU_SCENE("menu_scene.fxml"),
    SERVER_SCENE("server_scene.fxml"),
    IN_LOBBY_SCENE("in_lobby_scene.fxml"),
    GAME_SCENE("game_scene_2players.fxml");

    private GuiPhase phase;

    public final String s;

    static{
        MENU_SCENE.phase = GuiPhase.LOGIN;
        SERVER_SCENE.phase = GuiPhase.SERVER;
        IN_LOBBY_SCENE.phase = GuiPhase.IN_LOBBY;
        GAME_SCENE.phase = GuiPhase.GAME;
    }
    GameFxml(String s) {
        this.s=s;
    }

    GuiPhase getGuiPhase(){
        return phase;
    }

}

