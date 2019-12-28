package com.chessmasters.characters;

public enum ChessMen {
    PAWN(1),
    QUEEN(9),
    KING(10),
    KNIGHT(3),
    BISHOP(3),
    ROOK(5),
    ;

    //Values are from https://www.chess.com/article/view/chess-piece-value
    int charcterValue;

    ChessMen(int charcterValue) {
        this.charcterValue = charcterValue;
    }

    public int getCharcterValue() {
        return charcterValue;
    }

    @Override
    public String toString() {
        switch (this){
            case PAWN:
                return "P";
            case KING:
                return "K";
            case KNIGHT:
                return "Kn";
            case QUEEN:
                return "Q";
            case ROOK:
                return "R";
            case BISHOP:
                return "B";
        }
        return "";
    }
}
