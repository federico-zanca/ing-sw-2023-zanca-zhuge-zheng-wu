package it.polimi.ingsw.view;

public enum LobbyCommand {
    HELP("mostra la lista dei comandi"),
    EXIT("termina la connessione"),
    START("avvia la partita"),
    READY("indica che sei pronto"),
    UNREADY("indica che non sei più pronto"),
    KICK("<username> espelli un giocatore dalla partita"),
    CHAT("<messaggio> invia un messaggio nella chat"),
    CHANGE_NUMBER_OF_PLAYERS("<numero giocatori> cambia il numero di giocatori della partita");

    private String description;

    private LobbyCommand(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
