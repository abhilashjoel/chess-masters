package com.chessmasters.model;

import com.chessmasters.characters.Characters;
import com.chessmasters.characters.Movements;

import java.util.PriorityQueue;

public class MoveRanks {

    PriorityQueue<MoveRank> ranks = new PriorityQueue<MoveRank>(MoveRank::compare);


    public PriorityQueue<MoveRank> getRanks() {
        return ranks;
    }

    public static class MoveRank {
        Characters characters;
        Movements move;
        long rank;


        static int compare(MoveRank o1, MoveRank o2) {
            return (int) (o1.rank - o2.rank);
        }


    }

}
