package it.polimi.ingsw.model.personalgoals;

import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;
import it.polimi.ingsw.model.enumerations.ItemType;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

class PersonalGoalCardTest {

    @Test
    void testJsonRead() {
        int n = 12;
        PersonalGoalCard card = new PersonalGoalCard(n);
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                System.out.printf("%s \t", card.getObjective(i,j).toString());
            }
            System.out.print("\n");
        }
    }

    @Test
    void NumOfMatches() {
        Bookshelf bookshelf = new Bookshelf();
        ItemTile[][] item_matrix;
        String[][] matrix ={
                {"E","E","B","E","E"},
                {"E","E","G","E","E"},
                {"E","G","F","E","E"},
                {"E","G","G","C","E"},
                {"E","F","T","C","G"},
                {"F","C","T","C","C"}
        };
        item_matrix = bookshelf.stringToMat(matrix);
        bookshelf.setShelfie(item_matrix);
        bookshelf.printBookshelf();
        System.out.print("\n");
        int n = 12;
        PersonalGoalCard card = new PersonalGoalCard(n);
        assertEquals(3,card.numOfMatches(bookshelf));
        bookshelf.getShelfie()[3][3].setType(ItemType.TROPHY);
        assertEquals(4,card.numOfMatches(bookshelf));
        bookshelf.getShelfie()[1][1].setType(ItemType.PLANT);
        assertEquals(5,card.numOfMatches(bookshelf));
        bookshelf.getShelfie()[5][0].setType(ItemType.CAT);
        assertEquals(6,card.numOfMatches(bookshelf));

        n = 5;
        card = new PersonalGoalCard(n);
        assertEquals(1,card.numOfMatches(bookshelf));
        bookshelf.getShelfie()[3][2].setType(ItemType.BOOK);
        assertEquals(2,card.numOfMatches(bookshelf));
        bookshelf.getShelfie()[1][1].setType(ItemType.TROPHY);
        assertEquals(3,card.numOfMatches(bookshelf));
        bookshelf.getShelfie()[5][0].setType(ItemType.GAME);
        assertEquals(4,card.numOfMatches(bookshelf));
    }

}