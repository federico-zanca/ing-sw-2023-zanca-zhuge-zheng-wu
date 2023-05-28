package it.polimi.ingsw.model.personalgoals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.enumerations.ItemType;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class PersonalGoalCard implements Serializable {
    private static final long serialVersionUID = -4793133503754805698L;
    public static final int Rows=6;
    public static final int Columns=5;
    protected ItemType[][] objective;
    private int identificator;

    /**
     * Constructor of PersonalGoalCard
     * @param n identificator of PersonalGoalCard to create
     */
    public PersonalGoalCard(int n){
        this.identificator = n;
        objective = new ItemType[Rows][Columns];
        for(int i=0; i<Rows; i++){
            for(int j=0; j<Columns; j++){
                objective[i][j] = ItemType.EMPTY;
            }
        }
        String file_name = "src/main/resources/personalGoalJson/PersonalGoalCard" + n + ".json";
        ObjectMapper objectMapper = new ObjectMapper();
        List<PersonalGoalCell> goalcells = null;
        try {
            goalcells = objectMapper.readValue(
                    new File(file_name),
                    new TypeReference<List<PersonalGoalCell>>(){});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (PersonalGoalCell cell : goalcells){
            objective[cell.getRow()][cell.getCol()] = cell.getType();
        }
    }

    /**
     * Copy constructor to make deep copies of personal goals.
     * @param other
     */
    public PersonalGoalCard(PersonalGoalCard other) {
        this.identificator = other.getIdentificator();
        this.objective = new ItemType[Rows][Columns];
        for (int i = 0; i < Rows; i++) {
            for (int j = 0; j < Columns; j++) {
                this.objective[i][j] = other.objective[i][j];
            }
        }
    }
    public int getIdentificator(){
        return identificator;
    }


    /**
     * Calculate number of matches between personalGoalCard and shelfie
     * @param shelfie bookshelf to be compared
     * @return the number of matches
     */
    public int numOfMatches(Bookshelf shelfie){
        int count=0;
        for(int i=0; i<Rows; i++){
            for(int j=0; j<Columns; j++){
                if(objective[i][j]!=ItemType.EMPTY && shelfie.getSingleCell(i, j).getType()==objective[i][j]){
                    count++;
                }
            }
        }
        return count;
    }
    /**
     * Return the type with its n_row and n_col in PersonalGoalCard
     * @param row,col of cell in Objective
     * @return itemtype contained in cell
     */
    public ItemType getSingleObjective(int row, int col) {
        return objective[row][col];
    }
    public ItemType[][] getObjective() {
        return this.objective;
    }
}

