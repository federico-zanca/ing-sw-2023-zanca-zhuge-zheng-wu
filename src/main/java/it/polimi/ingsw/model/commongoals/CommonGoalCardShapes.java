package it.polimi.ingsw.model.commongoals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.Bookshelf;
import it.polimi.ingsw.model.ItemTile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class CommonGoalCardShapes extends CommonGoalCard {

    private final String shape;
    public CommonGoalCardShapes(int numPlayers, String shape){
        super(numPlayers);
        this.shape = shape;
    }
    public boolean check(Bookshelf bookshelf){
        ItemTile[][] matrix;
        int dimRow;
        int dimCol;
        boolean flag = false;
        matrix = bookshelf.getShelfie();
        String file_name = "src/main/resources/commonGoalShapesJson/" + shape + ".json";
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<ArrayList<ShapeCoordinates>> shapecells;
        try {
            shapecells = objectMapper.readValue(
                    new File(file_name),
                    new TypeReference<ArrayList<ArrayList<ShapeCoordinates>>>(){});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dimRow = shapecells.get(0).get(0).getRow();
        dimCol = shapecells.get(0).get(0).getColumn();
        for (int i = 0; i <= NROW-dimRow && !flag; i++){
            for (int j = 0; j <= NCOL - dimCol && !flag; j++){
                flag = false;
                for(int h = 1; h < shapecells.size() && !flag; h++){
                    if (!matrix[shapecells.get(h).get(0).getRow()+i][shapecells.get(h).get(0).getColumn()+j].isEmpty()){
                        for(int k = 1; k < shapecells.get(h).size(); k++){
                            if(matrix[shapecells.get(h).get(k).getRow()+i][shapecells.get(h).get(k).getColumn()+j].getType() !=
                                    matrix[shapecells.get(h).get(0).getRow()+i][shapecells.get(h).get(0).getColumn()+j].getType()){
                                flag = false;
                                break;
                            }else{
                                flag = true;
                            }
                        }
                    }
                }
            }
        }
        return flag;
    }

    @Override
    public String toString() {
        if (Objects.equals(shape, "4Corners")){
            return "Quattro tessere dello stesso tipo ai quattro angoli della Libreria.";
        }else if(Objects.equals(shape, "X")){
            return "Cinque tessere dello stesso tipo che formano una X";
        } else if (Objects.equals(shape, "Diagonal")) {
            return "Cinque tessere dello stesso tipo che formano una diagonale.";
        } else{
            return "Goal non riconosciuto";
        }

    }
}
