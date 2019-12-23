package com.chessmasters.model;

import com.chessmasters.characters.Characters;

import java.util.HashMap;

public class CharacterState {

    HashMap<Characters, State> stata = new HashMap();


    public HashMap<Characters, State> getStata() {
        return stata;
    }

    public static enum State {
        ALIVE,
        DEAD;
    }
}
