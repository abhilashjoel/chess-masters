package com.chessmasters.model;

import com.google.common.collect.Table;
import com.chessmasters.characters.Character;

public class Board {
    Table<Integer, Integer, Character> board;


    public Board() {
    }

    public Table<Integer, Integer, Character> getBoard() {
        return board;
    }

    public void setBoard(Table<Integer, Integer, Character> board) {
        this.board = board;
    }
}