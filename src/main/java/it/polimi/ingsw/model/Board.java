package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.ItemType;

import java.util.ArrayList;

public class Board {
    private ItemTile[][] gameboard;
    final int Dimensions = 9;

    public Board(){
        gameboard = new ItemTile[Dimensions][Dimensions];
    }
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
        for(int i=0; i< Dimensions; i++){
            for(int j=0; j<Dimensions; j++){
                if(gameboard[i][j].getType()==null ) gameboard[i][j].setType(ItemType.EMPTY);
            }
        }
    }
// ITEMS è passato da turn e sono le tessere pescate da Bag
    public void refillBoard(ArrayList<ItemTile> items){
        //TODO implement here
        int tessera=0;

        if(needsRefill()){
            for(int i=0; i<Dimensions; i++){
                for(int j=0; j<Dimensions; j++){
                    if(gameboard[i][j].getType()==ItemType.EMPTY){
                        gameboard[i][j]=items.get(tessera);
                        tessera++;
                    }
                }
            }
        }
    }

    private int numCellsToRefill() {
        int count=0;
        for(int i=0; i<Dimensions; i++){
            for(int j=0; j<Dimensions; j++){
                if(gameboard[i][j].getType()==ItemType.EMPTY) count++;
            }
        }
        return count;
    }

    private boolean needsRefill() {
        //TODO manca la gestione nel caso stia accedendo a FORBIDDEN
        for(int i=1; i<Dimensions-1; i++){
            for(int j=1; j<Dimensions-1; j++){
                if(gameboard[i][j].getType()!=ItemType.EMPTY && (gameboard[i-1][j].getType()!=ItemType.EMPTY || gameboard[i+1][j].getType()!=ItemType.EMPTY ||
                        gameboard[i][j-1].getType()!=ItemType.EMPTY || gameboard[i][j+1].getType()!=ItemType.EMPTY))
                    return false;
            }
        }
        return true;
    }

    public void placeItem(ItemTile i, int x, int y){
        if(gameboard[x][y].getType()!=ItemType.FORBIDDEN)
            gameboard[x][y] = i;
    }

    public ItemTile pickItem(int x, int y){
        ItemTile tmp = gameboard[x][y];
        gameboard[x][y].setType(ItemType.EMPTY);
        return tmp;
    }

}


