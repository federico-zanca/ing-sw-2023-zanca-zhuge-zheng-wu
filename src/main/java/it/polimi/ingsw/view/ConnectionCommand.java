package it.polimi.ingsw.view;

public enum ConnectionCommand {
    HELP("mostra la lista dei comandi"),
    EXIT("termina la connessione"),
    GAMES("mostra la lista di partite disponibili"),
    JOIN("<nome partita> entra in una partita"),
    CREATE("<nome partita> crea una nuova partita"),
    USERNAME("<username> cambia il tuo username");

    private String description;

    private ConnectionCommand(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}


