package it.polimi.ingsw.model.gameboard;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

public class Board implements Serializable {
    private static final long serialVersionUID = 90503760638457723L;
    private Square[][] gameboard;
    public static final int DIMENSIONS = 9;

    public Board(){
        gameboard = new Square[DIMENSIONS][DIMENSIONS];
        for(int i=0; i<DIMENSIONS; i++){
            for(int j=0; j<DIMENSIONS; j++){
                gameboard[i][j] = new Square(i, j);
            }
        }
    }

    /**
     * Initializes the board depending on the number of players in the game
     * @param numPlayers
     */
    public void initBoard(int numPlayers){
        String jsonpath = "src/main/resources/gameboardJSON/";
        if(numPlayers<2 ||numPlayers>4){
            //TODO errore numero giocatori non valido -- lancia eccezione e gestisci
            System.err.println("Illegal number of players.");
            return;
        }
        switch(numPlayers){
            case 2:
                jsonpath += "board2.json";
                break;
            case 3:
                jsonpath += "board3.json";
                break;
            case 4:
                jsonpath += "board4.json";
                break;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            gameboard = objectMapper.readValue(
                    new File(jsonpath),
                    new TypeReference<Square[][]>(){});
        } catch (IOException e) {
            // TODO non va bene runtime exception
            throw new RuntimeException(e);
        }
        /*
        for(int i=0; i<DIMENSIONS; i++){
            for(int j=0; j<DIMENSIONS; j++) {
                if(i==0)    gameboard[i][j].getItem().setType(ItemType.FORBIDDEN);
                if(i==1 && (j<3 || j>4))    gameboard[i][j].getItem().setType(ItemType.FORBIDDEN);
                if(i==2 && (j<3 || j>5))    gameboard[i][j].getItem().setType(ItemType.FORBIDDEN);
                if(i==3 && (j<2 || j>7))    gameboard[i][j].getItem().setType(ItemType.FORBIDDEN);
                if(i==4 && (j<1 || j>7))    gameboard[i][j].getItem().setType(ItemType.FORBIDDEN);
                if(i==5 && (j<1 || j>6))    gameboard[i][j].getItem().setType(ItemType.FORBIDDEN);
                if(i==6 && (j<3 || j>5))    gameboard[i][j].getItem().setType(ItemType.FORBIDDEN);
                if(i==7 && (j<4 || j>5))    gameboard[i][j].getItem().setType(ItemType.FORBIDDEN);
                if(i==8)    gameboard[i][j].getItem().setType(ItemType.FORBIDDEN);
            }
        }
        if(numPlayers>2){
            gameboard[0][3].getItem().setType(ItemType.EMPTY);
            gameboard[2][2].getItem().setType(ItemType.EMPTY);
            gameboard[2][6].getItem().setType(ItemType.EMPTY);
            gameboard[3][8].getItem().setType(ItemType.EMPTY);
            gameboard[5][0].getItem().setType(ItemType.EMPTY);
            gameboard[6][2].getItem().setType(ItemType.EMPTY);
            gameboard[6][6].getItem().setType(ItemType.EMPTY);
            gameboard[8][5].getItem().setType(ItemType.EMPTY);
        }
        if(numPlayers == 4){
            gameboard[0][4].getItem().setType(ItemType.EMPTY);
            gameboard[1][5].getItem().setType(ItemType.EMPTY);
            gameboard[1][5].getItem().setType(ItemType.EMPTY);
            gameboard[3][1].getItem().setType(ItemType.EMPTY);
            gameboard[4][0].getItem().setType(ItemType.EMPTY);
            gameboard[4][8].getItem().setType(ItemType.EMPTY);
            gameboard[5][7].getItem().setType(ItemType.EMPTY);
            gameboard[7][3].getItem().setType(ItemType.EMPTY);
            gameboard[8][4].getItem().setType(ItemType.EMPTY);
        }
        */

        /*
        for(int i=0; i< DIMENSIONS; i++){
            for(int j=0; j<DIMENSIONS; j++){
                if(gameboard[i][j].getItem().getType()==null ) gameboard[i][j].getItem().setType(ItemType.EMPTY);
            }
        }
        */

    }
// ITEMS è passato da turn e sono le tessere pescate da Bag
    public Square[][] getGameboard() {
        return gameboard;
    }
    /**
     *
     * @param items available in bag
     */
    public void refillBoardWithItems(ArrayList<ItemTile> items){
        //TODO implement here


            for(int i=0; i<DIMENSIONS; i++) {
                for (int j = 0; j < DIMENSIONS; j++) {
                    if (items.isEmpty()) {
                        return;
                    } else if (gameboard[i][j].getItem().getType() == ItemType.EMPTY) {
                        placeItem(items.remove(0), i, j);
                    }
                }
            }

    }

    /**
     * Returns the number of empty cells in which thee will be a random ItemTile extracted from the Bag during refill.
     * This is also the number of ItemTiles to extract from Bag
     * @return number of items needed to refill the board
     */
    public int numCellsToRefill() {
        int count=0;
        for(int i=0; i<DIMENSIONS; i++){
            for(int j=0; j<DIMENSIONS; j++){
                if(gameboard[i][j].getItem().getType()==ItemType.EMPTY) count++;
            }
        }
        return count;
    }

    /**
     * Returns true if board needs to be refilled
     * @return true if board needs refilling
     */
    public boolean needsRefill() {
        for(int i=1; i<DIMENSIONS-1; i++){
            for(int j=1; j<DIMENSIONS-1; j++){
                if(gameboard[i][j].getItem().hasSomething() &&
                        (gameboard[i-1][j].getItem().hasSomething() ||
                                gameboard[i+1][j].getItem().hasSomething() ||
                                gameboard[i][j-1].getItem().hasSomething() ||
                                gameboard[i][j+1].getItem().hasSomething()))
                    return false;
            }
        }
        return true;
    }

    /**
     * Places an ItemTable on a GameBoard cell
     * @param i the IteTile to be added
     * @param row the row number of the target cell (in which the item will be placed)
     * @param column the row number of the target cell (in which the item will be placed)
     */
    public void placeItem(ItemTile i, int row, int column){
        if(gameboard[row][column].getItem().getType()!=ItemType.FORBIDDEN)
            gameboard[row][column].setItem(i);
    }

    /**
     * Returns the ItemTile in the target cell of the board (identified by row and column number)
     * @param row the row number of the target cell
     * @param column the column number of the target cell
     * @return the ItemTile corresponding to the cell specified through row and column parameters
     */
    public ItemTile pickItem(int row, int column){
        ItemTile tmp = new ItemTile(gameboard[row][column].getItem().getType());
        if(tmp.getType() == ItemType.FORBIDDEN){
            //TODO implementare meglio.
            System.err.println("Forbidden square, this square is not part of the board.");
            return tmp;
        }
        gameboard[row][column].getItem().setType(ItemType.EMPTY);
        return tmp;
    }

    public ArrayList<ItemTile> pickItems(ArrayList<Square> squares){
        ArrayList<ItemTile> hand = new ArrayList<>();
        for(Square sq : squares){
            hand.add(pickItem(sq.getRow(), sq.getColumn()));
        }
        return hand;
    }

    /**
     *
     * Set ItemTiles pickable as First ItemTiles
     */
    public void enableSquaresWithFreeSide(){
        for(int row=0; row<DIMENSIONS; row++){
            for(int column=0; column<DIMENSIONS; column++){
                gameboard[row][column].setPickable(gameboard[row][column].getItem().hasSomething() && doesSquareHaveFreeSide(row, column));
            }
        }
    }

    public ArrayList<Integer> pickableFirstItems(){
        ArrayList<Integer> pickable=new ArrayList<>();
            for(int i=1; i<DIMENSIONS-1; i++){
                for(int j=1; j<DIMENSIONS-1; j++){
                    if(gameboard[i][j].getItem().hasSomething() && doesSquareHaveFreeSide(i, j))
                    {
                        pickable.add(i);
                        pickable.add(j);
                    }
                }
            }
        return  pickable;
    }

    /**
     *
     * @param firstItem_x coordinate of the first clicked ItemTile
     * @param firstItem_y coordinate of the first clicked ItemTile
     * @return ArrayList of pickable ItemTiles given the coordinate of the ItemTile picked as first
     */
    public ArrayList<Integer> pickableItems(int firstItem_x, int firstItem_y) {
        ArrayList<Integer> pickable=new ArrayList<>();

            Stack<Integer> nx =new Stack<>();
            Stack<Integer> ny =new Stack<>();
            nx.push(0);
            nx.push(-1);
            nx.push(0);
            nx.push(1);
            ny.push(-1);
            ny.push(0);
            ny.push(1);
            ny.push(0);

            while (!nx.isEmpty() && !ny.isEmpty()){
                int x=nx.pop();
                int y=ny.pop();
                int count=2;

                while (gameboard[firstItem_x+x][firstItem_y+y].getItem().getType()!=ItemType.EMPTY && gameboard[firstItem_x+x][firstItem_y+y].getItem().getType()!=ItemType.FORBIDDEN && count>0){
                    pickable.add(firstItem_x+x);
                    pickable.add(firstItem_y+y);
                    x=2*x;
                    y=2*y;
                    count--;
                }
            }
        return  pickable;
    }

    /**
     *
     * @param row row number of the square to check
     * @param column column number of the square to check
     * @return true if the square has a free side (not containing a real item)
     */
    public boolean doesSquareHaveFreeSide(int row, int column){
        if(row == 0 || row == DIMENSIONS-1 || column == 0 || column == DIMENSIONS-1) return true;
        return !gameboard[row][column - 1].getItem().hasSomething() || !gameboard[row][column + 1].getItem().hasSomething()
                || !gameboard[row - 1][column].getItem().hasSomething() || !gameboard[row + 1][column].getItem().hasSomething();
    }

    /**
     * for testing purpose.
     */
    public void printBoard(){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                System.out.print(getGameboard()[i][j].getItem());
            }
            System.out.println();
        }
    }

    public Square getSquare(Coordinates coords) {
        return gameboard[coords.getRow()][coords.getColumn()];
    }

    public void disableAllSquares() {
        for(int i=0; i<DIMENSIONS; i++){
            for(int j=0; j<DIMENSIONS; j++){
                gameboard[i][j].setPickable(false);
            }
        }
    }
}




