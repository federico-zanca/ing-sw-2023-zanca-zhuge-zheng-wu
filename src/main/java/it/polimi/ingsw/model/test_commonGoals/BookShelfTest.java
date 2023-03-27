package it.polimi.ingsw.model.test_commonGoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;

public class BookShelfTest extends Bookshelf {
    public ItemTile[][] stringToMat(String[][] mat){
        ItemTile[][] B = new ItemTile[Rows][Columns];

        for(int i=0;i<Rows;i++){
            for(int j=0;j<Columns;j++){
                if(mat[i][j] == "G"){B[i][j] = new ItemTile(ItemType.GAME);}
                if(mat[i][j] == "B"){B[i][j] = new ItemTile(ItemType.BOOK);}
                if(mat[i][j] == "C"){B[i][j] = new ItemTile(ItemType.CAT);}
                if(mat[i][j] == "P"){B[i][j] = new ItemTile(ItemType.PLANT);}
                if(mat[i][j] == "F"){B[i][j] = new ItemTile(ItemType.FRAME);}
                if(mat[i][j] == "T"){B[i][j] = new ItemTile(ItemType.TROPHY);}
                if(mat[i][j] == "E"){B[i][j] = new ItemTile(ItemType.EMPTY);}
            }
        }
        return B;
    }

    public void printBookshelf(){
        for(int i=0; i<Rows; i++){
            for(int j=0; j<Columns; j++){
                if(getSingleCell(i,j).getType() == ItemType.GAME){System.out.print("G");}
                if(getSingleCell(i,j).getType() == ItemType.BOOK){System.out.print("B");}
                if(getSingleCell(i,j).getType() == ItemType.CAT){System.out.print("C");}
                if(getSingleCell(i,j).getType() == ItemType.PLANT){System.out.print("P");}
                if(getSingleCell(i,j).getType() == ItemType.FRAME){System.out.print("F");}
                if(getSingleCell(i,j).getType() == ItemType.TROPHY){System.out.print("T");}
                if(getSingleCell(i,j).getType() == ItemType.EMPTY){System.out.print("E");}
            }
            System.out.println();
        }
        System.out.println();
    }
}
