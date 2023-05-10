package it.polimi.ingsw.view;

public enum LobbyCommand {
    HELP("mostra la lista dei comandi"),
    EXIT("termina la connessione"),
    START("avvia la partita"),
    PLAYERLIST("mostra la lista dei giocatori"),
    CHAT("apre la chat"),
    NUMPLAYERS("<numero giocatori> cambia il numero di giocatori della partita");

    private String description;

    private LobbyCommand(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
