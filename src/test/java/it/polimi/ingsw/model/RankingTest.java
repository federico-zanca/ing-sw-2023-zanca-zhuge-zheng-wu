package it.polimi.ingsw.model;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class RankingTest {

    @org.junit.jupiter.api.Test
    void getRanking() {


        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("user1", 8);
        map.put("user2", 5);
        map.put("user3", 20);
        map.put("user4", 15);
        map.put("user5", 20);
        map.put("user6", 20);
        map.put("user7", 40);

        // Creating a list of usernames
        List<String> usernames = Arrays.asList("user1", "user2", "user3", "user4", "user5", "user6", "user7");

        // Sorting HashMap by values in descending order and usernames in ascending order
        LinkedHashMap<String, Integer> sortedMap = map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(e -> -usernames.indexOf(e.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        int rank = 1;
        System.out.println("Leaderboard :");
        for (String key : sortedMap.keySet()) {
            System.out.println("#" + rank + ". " + key + " : " + sortedMap.get(key));
            rank++;
        }
        // Printing the leaderboard
        //PrintLeaderboard.printLeaderboard(sortedMap);
        //ranking.printLeaderboard();
    }

    @org.junit.jupiter.api.Test
    void printRanking() {
    }

    @org.junit.jupiter.api.Test
    void sortRankingIntegers() {
    }
}

