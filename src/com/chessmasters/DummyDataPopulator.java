package com.chessmasters;

import com.chessmasters.characters.Characters;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import java.util.List;
import java.util.Set;

public class DummyDataPopulator {

    public static void initBoard(Table<Integer, Integer, Characters> board) {

        for(int i = 0; i < 8; i++){
            //TODO initialize this board.
//            board.put()
        }

    }


    public static void main(String[] args) {
        Set<List<Integer>> lists = Sets.cartesianProduct(Sets.newHashSet(1, -1), Sets.newHashSet(2, -2));
        lists.addAll(Sets.cartesianProduct(Sets.newHashSet(2, -2), Sets.newHashSet(1, -1)));
        System.out.println(lists);

    }


}
