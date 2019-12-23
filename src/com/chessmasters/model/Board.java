package com.chessmasters.model;

import com.chessmasters.characters.Characters;
import com.google.common.collect.Table;

public class Board {
    Table<Integer, Integer, Characters> board;


    public Board() {
    }

    public Table<Integer, Integer, Characters> getBoard() {
        return board;
    }

    public void setBoard(Table<Integer, Integer, Characters> board) {
        this.board = board;
    }
}