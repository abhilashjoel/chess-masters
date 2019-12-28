package com.chessmasters.model;

import com.google.common.base.MoreObjects;

public class Move {
    int dx;
    int dy;
    int characterId;

    public Move(int dx, int dy, int characterId) {
        this.dx = dx;
        this.dy = dy;
        this.characterId = characterId;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("dx", dx)
                .add("dy", dy)
                .toString();
    }
}
