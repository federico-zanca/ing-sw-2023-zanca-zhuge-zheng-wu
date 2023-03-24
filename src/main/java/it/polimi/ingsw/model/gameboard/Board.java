package it.polimi.ingsw.model.gameboard;


import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Board {
    private ItemTile[][] gameboard;
    public final int Dimensions = 9;

    public Board(){
        gameboard = new ItemTile[Dimensions][Dimensions];
        for(int i=0; i<Dimensions; i++){
            for(int j=0; j<Dimensions; j++){
                gameboard[i][j] = new ItemTile(ItemType.EMPTY);
            }
        }
    }

    /**
     * Initializes the board depending on the number of players in the game
     * @param numPlayers
     */
    public void initBoard(int numPlayers){
        for(int i=0; i<Dimensions; i++){
            for(int j=0; j<Dimensions; j++) {
                if(i==0)    gameboard[i][j].setType(ItemType.FORBIDDEN);
                if(i==1 && (j<3 || j>4))    gameboard[i][j].setType(ItemType.FORBIDDEN);
                if(i==2 && (j<3 || j>5))    gameboard[i][j].setType(ItemType.FORBIDDEN);
                if(i==3 && (j<2 || j>7))    gameboard[i][j].setType(ItemType.FORBIDDEN);
                if(i==4 && (j<1 || j>7))    gameboard[i][j].setType(ItemType.FORBIDDEN);
                if(i==5 && (j<1 || j>6))    gameboard[i][j].setType(ItemType.FORBIDDEN);
                if(i==6 && (j<3 || j>5))    gameboard[i][j].setType(ItemType.FORBIDDEN);
                if(i==7 && (j<5 || j>6))    gameboard[i][j].setType(ItemType.FORBIDDEN);
                if(i==8)    gameboard[i][j].setType(ItemType.FORBIDDEN);
            }
        }
        if(numPlayers>2){
            gameboard[0][3].setType(ItemType.EMPTY);
            gameboard[2][2].setType(ItemType.EMPTY);
            gameboard[2][6].setType(ItemType.EMPTY);
            gameboard[3][8].setType(ItemType.EMPTY);
            gameboard[5][0].setType(ItemType.EMPTY);
            gameboard[3][2].setType(ItemType.EMPTY);
            gameboard[3][6].setType(ItemType.EMPTY);
            gameboard[8][5].setType(ItemType.EMPTY);
        }
        if(numPlayers == 4){
            gameboard[0][4].setType(ItemType.EMPTY);
            gameboard[1][5].setType(ItemType.EMPTY);
            gameboard[1][5].setType(ItemType.EMPTY);
            gameboard[3][1].setType(ItemType.EMPTY);
            gameboard[4][0].setType(ItemType.EMPTY);
            gameboard[4][8].setType(ItemType.EMPTY);
            gameboard[5][7].setType(ItemType.EMPTY);
            gameboard[7][3].setType(ItemType.EMPTY);
            gameboard[8][4].setType(ItemType.EMPTY);
        }
        /*
        for(int i=0; i< Dimensions; i++){
            for(int j=0; j<Dimensions; j++){
                if(gameboard[i][j].getType()==null ) gameboard[i][j].setType(ItemType.EMPTY);
            }
        }
        */

    }
// ITEMS è passato da turn e sono le tessere pescate da Bag

    /**
     *
     * @param items
     */
    public void refillBoard(ArrayList<ItemTile> items){
        //TODO implement here

        if(needsRefill()){
            for(int i=0; i<Dimensions; i++){
                for(int j=0; j<Dimensions; j++){
                    if (items.isEmpty()){ return;}
                    else if(gameboard[i][j].getType()==ItemType.EMPTY){
                        placeItem(items.remove(0), i, j);
                    }

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
        for(int i=0; i<Dimensions; i++){
            for(int j=0; j<Dimensions; j++){
                if(gameboard[i][j].getType()==ItemType.EMPTY) count++;
            }
        }
        return count;
    }

    /**
     * Returns true if board needs to be refilled
     * @return true if board needs refilling
     */
    private boolean needsRefill() {
        for(int i=1; i<Dimensions-1; i++){
            for(int j=1; j<Dimensions-1; j++){
                if(gameboard[i][j].getType()!=ItemType.EMPTY && gameboard[i][j].getType()!=ItemType.FORBIDDEN && ((gameboard[i-1][j].getType()!=ItemType.EMPTY && gameboard[i-1][j].getType()!=ItemType.FORBIDDEN) || (gameboard[i+1][j].getType()!=ItemType.EMPTY && gameboard[i+1][j].getType()!=ItemType.FORBIDDEN) ||
                        (gameboard[i][j-1].getType()!=ItemType.EMPTY && gameboard[i][j-1].getType()!=ItemType.FORBIDDEN) || (gameboard[i][j+1].getType()!=ItemType.EMPTY && gameboard[i][j+1].getType()!=ItemType.FORBIDDEN)))
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
        if(gameboard[row][column].getType()!=ItemType.FORBIDDEN)
            gameboard[row][column] = i;
    }

    /**
     * Returns the ItemTile in the target cell of the board (identified by row and column number)
     * @param row the row number of the target cell
     * @param column the column number of the target cell
     * @return the ItemTile corresponding to the cell specified through row and column parameters
     */
    public ItemTile pickItem(int row, int column){
        ItemTile tmp = new ItemTile(gameboard[row][column].getType());
        gameboard[row][column].setType(ItemType.EMPTY);
        return tmp;
    }

    /**
     * Before picking (Clicking on) the first tile, pass -1 and -1 as parameters to show the pickable/clickable first tiles,
     * by picking/clicking the first tile we insert its coordinates into the method and the method returns an array coordinates of
     * tiles that we can further pick/click
     * @param firstItem_x coordinates of the ItemTile to pick
     * @param firstItem_y
     * @return ArrayList of pickable ItemTiles
     */
    public ArrayList<Integer> pickableItems(int firstItem_x, int firstItem_y) {
        ArrayList<Integer> pickable=new ArrayList<>();
        if (firstItem_x==-1 && firstItem_y==-1){
            for(int i=1; i<Dimensions-1; i++){
                for(int j=1; j<Dimensions-1; j++){
                    if(gameboard[i][j].getType()!= ItemType.EMPTY && gameboard[i][j].getType()!=ItemType.FORBIDDEN && ((gameboard[i-1][j].getType()==ItemType.EMPTY || gameboard[i-1][j].getType()==ItemType.FORBIDDEN) || (gameboard[i+1][j].getType()==ItemType.EMPTY || gameboard[i+1][j].getType()==ItemType.FORBIDDEN) ||
                            (gameboard[i][j-1].getType()==ItemType.EMPTY || gameboard[i][j-1].getType()==ItemType.FORBIDDEN) || (gameboard[i][j+1].getType()==ItemType.EMPTY || gameboard[i][j+1].getType()==ItemType.FORBIDDEN))) {
                        pickable.add(i);
                        pickable.add(j);
                    }
                }
            }
        }
        else {
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

                while (gameboard[firstItem_x+x][firstItem_y+y].getType()!=ItemType.EMPTY && gameboard[firstItem_x+x][firstItem_y+y].getType()!=ItemType.FORBIDDEN && count>0){
                    pickable.add(firstItem_x+x);
                    pickable.add(firstItem_y+y);
                    x=2*x;
                    y=2*y;
                    count--;
                }
            }
        }
        return  pickable;
    }



}




