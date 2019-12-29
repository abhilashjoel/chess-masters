package com.chessmasters.characters;

import com.chessmasters.model.CharacterState;
import com.google.common.base.MoreObjects;

import java.util.Objects;

public class Character {
    ChessMen type;
    Team team;
    int id;
    CharacterState.State state;
    int x;
    int y;
    boolean isPrimodial;
    int moveValue = 0;


    public Character(ChessMen type, Team team, int id) {
        this.type = type;
        this.team = team;
        this.id = id;
    }

    public Character(ChessMen type, Team team) {
        this(type, team, 0);
    }

    public ChessMen getType() {
        return type;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
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

    public Character(Character other) {
        this.type = other.type;
        this.team = other.team;
        this.id = other.id;
        this.state = other.state;
        this.x = other.x;
        this.y = other.y;
        this.isPrimodial = other.isPrimodial;
        this.moveValue = other.moveValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return id == character.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", type)
                .add("team", team)
                .add("id", id)
                .add("state", state)
                .add("x", x)
                .add("y", y)
                .add("isPrimodial", isPrimodial)
                .add("moveValue", moveValue)
                .toString();
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
