package com.chessmasters.model;

import com.google.common.base.MoreObjects;

public class Move {
    int dx;
    int dy;

    public Move(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("dx", dx)
                .add("dy", dy)
                .toString();
    }
}
