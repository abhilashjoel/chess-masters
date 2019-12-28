package com.chessmasters;

import com.chessmasters.characters.Character;
import com.chessmasters.helper.BoardHelper;
import com.chessmasters.helper.CharacterHelper;
import com.chessmasters.model.Board;
import com.chessmasters.model.Move;
import com.google.common.collect.HashBasedTable;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Board board = new Board();
        board.setBoard(HashBasedTable.create());


        BoardHelper.positionCharactersOnBoard(board);

        BoardHelper.printBoard(board);

        Character character = board.getBoard().get(6, 0);
        List<Move> allMovesForPawn = CharacterHelper.getAllMovesByCharacter(board, character);
        System.out.println(allMovesForPawn);

        CharacterHelper.makeAMove(board, character, allMovesForPawn.get(0));

        BoardHelper.printBoard(board);



    }
}
