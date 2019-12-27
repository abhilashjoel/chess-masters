package com.chessmasters.characters;

//TODO remove all the Movement logic from here. CharacterHelper will take care of that.
public enum ChessMen {
    PAWN(Movements.STEP_1, Movements.STEP_2),
    QUEEN,
    KING,
    //TODO  we can't have this. Would be too lengthy for queen, ...
    KNIGHT(Movements.L_STEP_1, Movements.L_STEP_2, Movements.L_STEP_2, Movements.L_STEP_3, Movements.L_STEP_4, Movements.L_STEP_5, Movements.L_STEP_6, Movements.L_STEP_7, Movements.L_STEP_8),
    BISHOP(),
    ROOK(),
    ;



    Movements[] movements;

    ChessMen(Movements ...movements) {
        this.movements = movements;
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
