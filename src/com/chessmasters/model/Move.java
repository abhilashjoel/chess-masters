package com.chessmasters.model;

import com.chessmasters.helper.CharacterHelper;
import com.google.common.base.MoreObjects;

import java.util.Objects;

public class Move {
    int dx;
    int dy;
    int characterId;
    float score;

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

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return dx == move.dx &&
                dy == move.dy &&
                characterId == move.characterId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dx, dy, characterId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("dx", dx)
                .add("dy", dy)
                .add("Score", score)
                .add("characterId", characterId)
                .toString();
    }
}
