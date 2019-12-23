package com.chessmasters.characters;

import com.google.common.collect.Sets;

//TODO rename to Movement
public enum Movements {
    STEP_1(0, 1),
    STEP_2(0, 2),
    STEP_1_black(0, -1),
    STEP_2_black(0, 2),

    //TODO Find a way to compute this.
    L_STEP_1(1, 2),
    L_STEP_2(1, -2),
    L_STEP_3(-1, 2),
    L_STEP_4(-1, -2),
    L_STEP_5(2, 1),
    L_STEP_6(2, -1),
    L_STEP_7(-2, 1),
    L_STEP_8(-2, -1);



    int dx, dy;

    Movements(int dx, int dy) {

        Sets.cartesianProduct(Sets.newHashSet(1, -1, 2, -2));
        this.dx = dx;
        this.dy = dy;
    }
}
