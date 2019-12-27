package com.chessmasters.characters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Characters {

    List<Character> characters = new ArrayList<>();


    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    public List<Character> getCharactersByType(ChessMen type) {
        return characters
                .stream()
                .filter((character) -> character.getType().equals(type))
                .collect(Collectors.toList());
    }

}
