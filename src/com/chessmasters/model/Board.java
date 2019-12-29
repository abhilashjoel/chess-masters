package com.chessmasters.model;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.chessmasters.characters.Character;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Board {
    Table<Integer, Integer, Character> board;


    public Board() {
    }

    public Board(Board board) {
        this.board = HashBasedTable.create(board.getBoard());
    }

    public Table<Integer, Integer, Character> getBoard() {
        return board;
    }

    public void setBoard(Table<Integer, Integer, Character> board) {
        this.board = board;
    }

    public String getJson() {
        List<List<String>> json = new ArrayList();
        for(int x = 0; x <= 7; x++) {
            List<String>  row = new ArrayList();
            json.add(row);
            for(int y = 0; y <= 7; y++) {
                Character c = board.get(y, x);
                if(c != null) {
                    row.add(c.getTeam().toString()+c.getType().toString());
                } else {
                    row.add("");
                }

            }
        }
        return new Gson().toJson(json);
    }
}