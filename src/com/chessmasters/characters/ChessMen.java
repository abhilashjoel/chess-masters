package com.chessmasters.characters;

public enum ChessMen {
    PAWN(Movements.STEP_1, Movements.STEP_2),
    QUEEN,
    KING,
    //TODO  we can't have this. Would be too lengthy for queen, ...
    HORSE(Movements.L_STEP_1, Movements.L_STEP_2, Movements.L_STEP_2, Movements.L_STEP_3, Movements.L_STEP_4, Movements.L_STEP_5, Movements.L_STEP_6, Movements.L_STEP_7, Movements.L_STEP_8);
    Movements[] movements;

    ChessMen(Movements ...movements) {
        this.movements = movements;
    }
}
