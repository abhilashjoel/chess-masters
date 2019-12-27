package com.chessmasters.characters;

import com.chessmasters.model.CharacterState;

public class Character {
    ChessMen type;
    Team team;
    int id;
    CharacterState.State state;
    int x;
    int y;
    boolean isPrimodial;

    public Character(ChessMen type, Team team, int id) {
        this.type = type;
        this.team = team;
        this.id = id;
    }

    public ChessMen getType() {
        return type;
    }

    public Team getTeam() {
        return team;
    }

    public int getId() {
        return id;
    }

    public CharacterState.State getState() {
        return state;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isPrimodial() {
        return isPrimodial;
    }

    public void setPrimodial(boolean primodial) {
        isPrimodial = primodial;
    }

    public static enum Team {
        BLACK,
        WHITE;

        @Override
        public String toString() {
            return BLACK.equals(this) ? "B" : "W";
        }
    }
}
