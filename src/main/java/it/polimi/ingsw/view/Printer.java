package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.ItemType;
import it.polimi.ingsw.model.gameboard.Board;
import it.polimi.ingsw.model.gameboard.Square;
import it.polimi.ingsw.model.personalgoals.PersonalGoalCard;
import it.polimi.ingsw.network.message.ChatMessage;
import it.polimi.ingsw.network.message.gamemessage.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Printer {
    public Printer() {
    }

    /**
     * Prints the numbers from 0 to the passed parameter-1 as column indexes (separated by 5 spaces each)
     *
     * @param columns number of columns to print
     */
    void printColumnIndexes(int columns) {
        System.out.print(" ");
        for (int i = 0; i < columns; i++) {
            System.out.print("  " + i + "   ");
        }
    }

    /**
     * @param type ItemType
     * @return the ESC ColorCode to paint the background accordingly to the ItemType received
     */
    String paintBG(ItemType type) {

        StringBuilder stringBuilder = new StringBuilder();
        Color c;
        switch (type) {
            case CAT:
                c = Color.GREEN;
                break;
            case PLANT:
                c = Color.FUCSIA;
                break;
            case FRAME:
                c = Color.BLUE;
                break;
            case GAME:
                c = Color.YELLOW_BOLD;
                break;
            case TROPHY:
                c = Color.CYAN_BOLD;
                break;
            case BOOK:
                c = Color.WHITE;
                break;
            default:
                c = Color.NO_COLOR;
                break;
        }
        return stringBuilder.append(c).append("  ").append(type).append("  ").append(Color.NO_COLOR).toString();
    }

    /**
     * @param type ItemType
     * @return the ESC ColorCode to paint the foreground accordingly to the ItemType received
     */
    String paintFG(ItemType type) {
        StringBuilder stringBuilder = new StringBuilder();
        Color c;
        switch (type) {
            case CAT:
                c = Color.WGREEN;
                break;
            case PLANT:
                c = Color.WFUCSIA;
                break;
            case FRAME:
                c = Color.WBLUE;
                break;
            case GAME:
                c = Color.WYELLOW_BOLD;
                break;
            case TROPHY:
                c = Color.WCYAN_BOLD;
                break;
            case BOOK:
                c = Color.WWHITE;
                break;
            default:
                c = Color.NO_COLOR;
                break;
        }
        return stringBuilder.append(c).append("  ").append(type).append("  ").append(Color.NO_COLOR).toString();

    }

    /**
     * Prints the GameBoard
     *
     * @param board matrix of Squares to print
     */
    void showBoard(Square[][] board) {
        System.out.println();
        System.out.println("Game Board:");
        StringBuilder strBoard = new StringBuilder();
        for (int i = 0; i < Board.DIMENSIONS; i++) {
            strBoard.append("      ");
            for (int j = 0; j < Board.DIMENSIONS; j++) {
                strBoard.append("+-----");
            }
            strBoard.append("+\n");
            strBoard.append("  ").append(i).append("   |");
            for (int j = 0; j < Board.DIMENSIONS; j++) {
                //strBoard.append("  ");
                if (board[i][j].isPickable()) {
                    //strBoard.append(board[i][j].getItem().toColorString());
                    strBoard.append(paintBG(board[i][j].getItem().getType()));
                } else {
                    //strBoard.append("  ").append(board[i][j].getItem()).append("  ");
                    strBoard.append(paintFG(board[i][j].getItem().getType()));
                }
                //strBoard.append("  |");
                strBoard.append("|");
            }
            strBoard.append("\n");
        }
        strBoard.append("      ");
        for (int j = 0; j < Board.DIMENSIONS; j++) {
            strBoard.append("+-----");
        }
        strBoard.append("+");
        System.out.println(strBoard);
        System.out.print("      ");
        printColumnIndexes(Board.DIMENSIONS);
        System.out.println();
    }

    /**
     * Prints the bookshelf of the player
     *
     * @param username username of the player whose bookshelf is printed
     * @param bookshelf  bookshelf
     */
    void showBookshelf(String username, Bookshelf bookshelf) {
        ItemTile[][] shelf = bookshelf.getShelfie();
        System.out.println("Libreria di " + username);
        System.out.println();
        StringBuilder strShelf = new StringBuilder();
        for (int i = 0; i < Bookshelf.Rows; i++) {
            strShelf.append("      ");
            for (int j = 0; j < Bookshelf.Columns; j++) {
                strShelf.append("+-----");
            }
            strShelf.append("+\n");
            strShelf.append("  ").append(i).append("   |");
            for (int j = 0; j < Bookshelf.Columns; j++) {
                strShelf.append(paintFG(shelf[i][j].getType())).append("|");
            }
            strShelf.append("\n");
        }
        strShelf.append("      ");
        for (int j = 0; j < Bookshelf.Columns; j++) {
            strShelf.append("+-----");
        }
        strShelf.append("+");
        System.out.println(strShelf);
        System.out.print("      ");
        printColumnIndexes(Bookshelf.Columns);
        System.out.println();
    }

    /**
     * Displays the bookshelves of the given players in a tabular format.
     *
     * @param players an ArrayList of Player objects whose bookshelves are to be shown
     */
    public void showBookshelves(List<Player> players) {
        System.out.println();
        ArrayList<Bookshelf> bookshelves = players.stream().map(Player::getBookshelf).collect(Collectors.toCollection(ArrayList::new));
        for (int i = 0; i < bookshelves.size(); i++) {
            System.out.print("Libreria di " + players.get(i).getUsername() + "\t\t\t\t\t");
        }
        System.out.println();
        for (int i = 0; i < Bookshelf.Rows; i++) {
            printBookshelvesBorder(bookshelves);
            for (Bookshelf bookshelf : bookshelves) {
                System.out.print("|");
                for (int j = 0; j < Bookshelf.Columns; j++) {
                    System.out.print(paintFG(bookshelf.getShelfie()[i][j].getType()) + "|");
                }
                System.out.print("\t\t");
            }
            System.out.println();
        }
        printBookshelvesBorder(bookshelves);
        for (int k = 0; k < bookshelves.size(); k++) {
            printColumnIndexes(Bookshelf.Columns);
            System.out.print("\t\t");
        }
        System.out.println();
    }

    /**
     * Prints the border of all the bookshelves in the given ArrayList.
     * Each border is a string of plus signs and dashes that represent the
     * top and bottom edges of the shelves, and vertical bars that separate
     * the columns of books on each shelf.
     * @param bookshelves the ArrayList of Bookshelf objects to print borders for
     */
    private void printBookshelvesBorder(ArrayList<Bookshelf> bookshelves) {
        for(int k = 0; k < bookshelves.size(); k++) {
            for(int j=0; j<Bookshelf.Columns; j++) {
                System.out.print("+-----");
            }
            System.out.print("+\t\t");
        }
        System.out.println();
    }

    /**
     * Prints the number of points scored by the player when he/she has achieved a common goal
     *
     * @param message the message containing the number of points scored
     */
    void showAchievedCommonGoal(AchievedCommonGoalMessage message) {
        System.out.println(message.getContent());
    }

    /**
     * Prints the contents of a player's hand.
     *
     * @param username the name of the player whose hand is being displayed
     * @param hand     the ArrayList of ItemTiles representing the player's hand
     */
    void showHand(String username, ArrayList<ItemTile> hand) {
        System.out.println("Hand of player " + username);
        for (ItemTile item : hand) {
            System.out.print(" " + paintFG(item.getType()) + " ");
        }
        System.out.println();
    }

    /**
     * Displays a message indicating that the game has started, the game board, the common goals, and the leaderboard.
     *
     * @param model The GameView object containing information about the game.
     */
    void showGameStarted(GameView model) {
        System.out.println("######################################\n" +
                "#        La partita è iniziata!      #\n" +
                "######################################");
        showBoard(model.getBoard().getGameboard());
        showCommonGoals(model);
        showLeaderboard(model.getLeaderboard());
    }

    /**
     * Displays a message indicating that a new turn has started for a specific user.
     *
     * @param username The username of the user whose turn it is.
     */
    void showNewTurn(String username) {
        System.out.println();
        System.out.println(
                "######################################\n" +
                "    E' il turno di " + Color.CYANTEXT + username + Color.NO_COLOR + "\t\n" +           //migliorabile
                "######################################");
    }

    /**
     * Displays the leaderboard with each player's rank, username, and score.
     *
     * @param sortedMap A LinkedHashMap containing players' usernames and scores sorted in descending order.
     */
    void showLeaderboard(LinkedHashMap<String, Integer> sortedMap) {
        int rank = 1;
        System.out.println("Leaderboard :");
        for (String key : sortedMap.keySet()) {
            System.out.println("#" + rank + ". " + key + " : " + sortedMap.get(key));
            rank++;
        }
    }

    /**
     * Displays a message indicating that the client has successfully connected to the server.
     */
    void showConnectedToServer() {
        System.out.println("Connesso al server!");

    }

    /**
     * Prints the first and second common goals from the provided GameView object.
     *
     * @param model The GameView object containing the common goals to display.
     */
    void showCommonGoals(GameView model) {
        System.out.println("First Common Goal: " + model.getCommonGoals().get(0).toString() +
                "\nSecond Common Goal: " + model.getCommonGoals().get(1).toString());
    }

    /**
     * Prints a message with the username and points obtained for a personal goal.
     *
     * @param message The PersonalGoalPointsMessage object containing the username and points to display.
     */
    void showPersonalGoalPoints(PersonalGoalPointsMessage message) {
        System.out.println("######################################\n" +
                Color.CYANTEXT + message.getPlayerUsername() + Color.NO_COLOR + " ha ottenuto " + message.getPoints() + " punti per il suo obiettivo personale!\n" +
                "######################################");
    }

    /**
     * Prints a message with the username and points obtained for adjacent items in the library.
     *
     * @param message The AdjacentItemsPointsMessage object containing the username and points to display.
     */
    void showAdjacentItemsPoints(AdjacentItemsPointsMessage message) {
        System.out.println("######################################\n" +
                Color.CYANTEXT + message.getPlayerUsername() + Color.NO_COLOR + " ha ottenuto " + message.getPoints() + " punti per i gruppi di tessere uguali adiacenti nella libreria!\n" +
                "######################################");
    }

    /**
     * Prints a message indicating that the current turn is the last turn of the game for the provided player.
     *
     * @param message The LastTurnMessage object containing the username of the player who triggered the last turn.
     */
    void showLastTurn(LastTurnMessage message) {
        System.out.println("######################################\n" +
                message.getCurrentPlayer() + " ha riempito la sua libreria!\n" +
                "Questo è l'ultimo giro di gioco!\n" +
                "######################################");
    }

    /**
     * Prints the end game message as well as the leaderboard and the winner of the game.
     *
     * @param ranking The LinkedHashMap object containing the ranking and scores of all players.
     */
    void showEndGame(LinkedHashMap<String, Integer> ranking) {
        System.out.println("######################################\n" +
                "#        La partita è finita         #\n" +
                "######################################");
        showLeaderboard(ranking);
        System.out.println("Il vincitore della partita è " + ranking.keySet().toArray()[0] + " con " + ranking.values().toArray()[0] + " punti!");
        System.out.println();
        System.out.println("Grazie per aver giocato a MyShelfie!");
        System.out.println();
        System.out.println("Sei uscito dalla partita. Digita 'help' per vedere la lista di comandi disponibili.");
        System.out.println();
    }

    /**
     * Displays a list of available game lobbies.
     *
     * @param lobbies HashMap containing the available game lobbies.
     */
    void showLobbyList(ArrayList<LobbyDisplayInfo> lobbies) {

        if(lobbies.size() == 0) {
            System.out.println("Nessuna partita disponibile! Crea una nuova partita con il comando 'create'!");
        } else {
            for (LobbyDisplayInfo lobby : lobbies) {
                System.out.println("Nome: " + lobby.getLobbyName() + " | " + lobby.getNumPlayers() + "/" + lobby.getNumPlayersChosen() + " | " + "Stato: " + Color.BRIGHT_PURPLE_TEXT + lobby.isGameStarted() + Color.NO_COLOR);
            }
        }
    }

    /**
     * Displays the response for changing a username.
     *
     * @param successful A boolean indicating if the username change was successful or not.
     * @param username   The new username.
     */
    void showUsernameResponse(boolean successful, String username) {
        if (successful) {
            System.out.println("Username cambiato in " + Color.CYANTEXT + username + Color.NO_COLOR + "!");
        } else {
            System.out.println("Username " + Color.CYANTEXT + username + Color.NO_COLOR + " già in uso! Riprova con un altro username!");
        }
    }

    /**
     * Shows the response of joining a lobby
     *
     * @param content    the string content of the response
     */
    void showJoinLobbyResponse(String content) {
        System.out.println(content);
    }

    /**
     * Prints the response of creating a lobby
     *
     * @param successful a boolean to indicate if the creation was successful.
     */
    void printCreateLobbyResponse(boolean successful) {
        if (successful) {
            System.out.println("Partita creata con successo!");
            System.out.println("In attesa di altri giocatori...");


        } else {
            System.out.println("Partita non creata! Riprova con un altro nome!");
        }
    }

    /**
     * Prints a message to confirm the client has entered a lobby
     */
    void printEnteredLobby() {
        System.out.println("Sei entrato nella partita!");
    }

    /**
     * Displays a response indicating if the user has successfully exited the lobby.
     * If successful, update the client state to IN_SERVER.
     *
     * @param successful a boolean indicating if the exit was successful or not
     */
    void showExitLobbyResponse(boolean successful) {
        if (successful) {
            System.out.println("Uscito dalla lobby");
        } else {
            System.out.println("Errore nell'uscire dalla lobby, riprova");

        }
    }

    /**
     * Displays a message indicating that the game is not ready to start.
     */
    void showGameNotReady() {
        System.out.println("Non ci sono le condizioni per iniziare la partita");

    }

    /**
     * Shows a message indicating that the command entered is invalid.
     */
    void showInvalidCommand() {
        System.out.println("Comando non valido! Usa il comando help per vedere la lista dei comandi disponibili!");
    }

    /**
     * Shows a message indicating that the user is not the admin.
     */
    void showNotAdmin() {
        System.out.println("Non sei l' admin di questa lobby! Solo l'admin può usare questo comando.");
    }

    /**
     * Shows a message indicating that the old admin has been removed and a new admin has been set.
     *
     * @param old_admin The username of the old admin that was removed.
     * @param new_admin The username of the new admin that has been set.
     */
    void showNewAdmin(String old_admin, String new_admin) {
        System.out.println("\nIl vecchio admin " + old_admin + " è stato rimosso");
        System.out.println("Il nuovo admin è " + Color.MAGENTATEXT + new_admin + Color.NO_COLOR);
    }

    /**
     * Prints the list of players in the lobby.
     * @param clients an ArrayList of Strings containing the names of the players
     */
    void showLobbyPlayersList(ArrayList<String> clients) {
        System.out.println("Lista dei giocatori nella lobby:");
        for (String client : clients) {
            if (client.equals(clients.get(0)))
                System.out.print(Color.MAGENTATEXT + client + Color.NO_COLOR + "\t");
            else {
                System.out.print(client + "\t");
            }
        }
        System.out.println();
    }

    public void displayPrompt() {
        System.out.print(">>> ");
    }

    public void showNewPlayerInLobby(ArrayList<String> allPlayersUsernames, String content) {
        System.out.println();
        System.out.println(content);
        showLobbyPlayersList(allPlayersUsernames);
    }

    public void showPlayerRejoined(String username) {
        System.out.println();
        System.out.println(username + " si è riconnesso alla partita!");
    }

    public void showExitGameResponse(String content) {
        System.out.println();
        System.out.println(content);
    }

    public void showPlayerLeft(String content) {
        System.out.println();
        System.out.println(content);
    }

    public void showPersonalGoalCard(ItemType[][] personalObjective){
        System.out.println();
        System.out.println("Your personal goal:");
        for(int i = 0;i < PersonalGoalCard.Rows;i++){
            System.out.print("      ");
            for(int j = 0;j < PersonalGoalCard.Columns;j++){
                System.out.print("+-----");
            }
            System.out.println("+");
            System.out.print("  "+i+"   |");
            for(int j = 0;j < PersonalGoalCard.Columns;j++){
                System.out.print(paintFG(personalObjective[i][j]) + "|");
            }
            System.out.println();
        }
        System.out.print("      ");
        for (int j = 0; j < Bookshelf.Columns; j++) {
            System.out.print("+-----");
        }
        System.out.print("+");
        System.out.println();
    }

    public void showReconnection(GameView model, String content, PersonalGoalCard personalGoal) {
        System.out.println();
        System.out.println(content);
        System.out.println(Color.BRIGHT_RED_TEXT + "RIEPILOGO DELLA PARTITA:" + Color.NO_COLOR);

        showNewTurn(model.getCurrentPlayer().getUsername());
        showBookshelves(model.getPlayers());
        showBoard(model.getBoard().getGameboard());
        showPersonalGoalCard(personalGoal.getObjective());
    }

    public void printChatMessage(ChatMessage message) {
        String prefix = "";
        if(message.getReceiver() != null){
            prefix = Color.BRIGHT_RED_TEXT +"PRIVATE MESSAGE FROM " + Color.NO_COLOR;
        }
        System.out.println(prefix + Color.GOLD_TEXT + message.getSender() + Color.NO_COLOR + "\t" + message.getContent());
    }

    public void printChat(ArrayList<ChatMessage> chat) {
        System.out.println();
        System.out.println(Color.BRIGHT_RED_TEXT + "CHAT DI GIOCO - SCRIVI UN MESSAGGIO E PREMI INVIO PER INVIARLO - /quit PER USCIRE" + Color.NO_COLOR);
        for (ChatMessage message : chat) {
            printChatMessage(message);
        }
    }
}