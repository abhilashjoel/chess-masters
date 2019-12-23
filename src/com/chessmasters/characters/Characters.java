package com.chessmasters.characters;

import java.util.Arrays;
import java.util.List;

//TODO rename to Character
public enum Characters {
    BLACK_PAWN_1(ChessMen.PAWN),
    BLACK_PAWN_2(ChessMen.PAWN),
    BLACK_PAWN_3(ChessMen.PAWN),
    BLACK_PAWN_4(ChessMen.PAWN),
    BLACK_PAWN_5(ChessMen.PAWN),
    BLACK_KING(ChessMen.KING),
    BLACK_QUEEN(ChessMen.QUEEN),
    BLACK_HORSE_1(ChessMen.HORSE);


    ChessMen type;

    Characters(ChessMen type) {
        this.type = type;
    }

    public ChessMen getType() {
        return type;
    }

    public List<Movements> getMoves() {
        return Arrays.asList(type.movements);
    }
}
