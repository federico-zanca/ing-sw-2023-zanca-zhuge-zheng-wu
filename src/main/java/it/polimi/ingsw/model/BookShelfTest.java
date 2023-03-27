package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

import java.util.Objects;

public class BookShelfTest extends Bookshelf {
    public ItemTile[][] stringToMat(String[][] mat){
        ItemTile[][] B = new ItemTile[Rows][Columns];

        for(int i=0;i<Rows;i++){
            for(int j=0;j<Columns;j++){
                if(Objects.equals(mat[i][j], "G")){B[i][j] = new ItemTile(ItemType.GAME);}
                if(Objects.equals(mat[i][j], "B")){B[i][j] = new ItemTile(ItemType.BOOK);}
                if(Objects.equals(mat[i][j], "C")){B[i][j] = new ItemTile(ItemType.CAT);}
                if(Objects.equals(mat[i][j], "P")){B[i][j] = new ItemTile(ItemType.PLANT);}
                if(Objects.equals(mat[i][j], "F")){B[i][j] = new ItemTile(ItemType.FRAME);}
                if(Objects.equals(mat[i][j], "T")){B[i][j] = new ItemTile(ItemType.TROPHY);}
                if(Objects.equals(mat[i][j], "E")){B[i][j] = new ItemTile(ItemType.EMPTY);}
            }
        }
        return B;
    }

    public void printBookshelf(){
        for(int i=0; i<Rows; i++){
            for(int j=0; j<Columns; j++){
                if(getSingleCell(i,j).getType() == ItemType.GAME){System.out.print("G\t");}
                if(getSingleCell(i,j).getType() == ItemType.BOOK){System.out.print("B\t");}
                if(getSingleCell(i,j).getType() == ItemType.CAT){System.out.print("C\t");}
                if(getSingleCell(i,j).getType() == ItemType.PLANT){System.out.print("P\t");}
                if(getSingleCell(i,j).getType() == ItemType.FRAME){System.out.print("F\t");}
                if(getSingleCell(i,j).getType() == ItemType.TROPHY){System.out.print("T\t");}
                if(getSingleCell(i,j).getType() == ItemType.EMPTY){System.out.print("E\t");}
            }
            System.out.println();
        }
        System.out.println();
    }
}
