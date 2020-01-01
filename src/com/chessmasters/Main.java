package com.chessmasters;

import com.chessmasters.characters.Character;
import com.chessmasters.characters.ChessMen;
import com.chessmasters.helper.BoardHelper;
import com.chessmasters.helper.CharacterHelper;
import com.chessmasters.helper.Ranker;
import com.chessmasters.model.Board;
import com.chessmasters.model.Move;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    public static int comp(float a, float b){
        if(b - a > 0) {
            return 1;
        }else if(b - a < 0) {
            return -1;
        }
        return 0;
    }
    public static void main(String[] args) {
        Board board = new Board();
        board.setBoard(HashBasedTable.create());
        BoardHelper.positionCharactersOnBoard(board);
        BoardHelper.printBoard(board);
        System.out.println(board.getBoard());
        System.out.println(board.getJson());
        System.out.println(new Gson().toJson(board));

        List<Character> chs = CharacterHelper.getAllCharactersByTeam(board, Character.Team.WHITE);
        List<Character> collect = chs.stream()
                .filter((ch) -> ch.getType().equals(ChessMen.PAWN))
                .collect(Collectors.toList());
        System.out.println(collect);
        for(int i = 0; i < 00; i++) {
            Character.Team team = Character.Team.WHITE;
            if(i % 2 == 0)
                team = Character.Team.BLACK;

            List<Character> allPlayers = CharacterHelper.getAllCharactersByTeam(board, team);
            float maxScore = Float.NEGATIVE_INFINITY;
            List<Move> bestMoves = new ArrayList();
            PriorityQueue<Move> prioritisedMoves = new PriorityQueue<>((move1, move2) -> comp(move1.getScore(), move2.getScore()));
            for(Character player : allPlayers) {
                List<Move> moves = CharacterHelper.getAllValidMovesByCharacter(board, player);
                for(Move move : moves) {
                    float score = 0;
                    try {
                        score = Ranker.getScoreOfAMove(board, move, false);
                    }catch (Exception e) {
//                        e.printStackTrace();
                        System.out.println("Ex: " + move + player);
                    }
                    move.setScore(score);
                    prioritisedMoves.add(move);
/*
                    if(score > maxScore) {
                        bestMoves = new ArrayList();
                        bestMoves.add(move);
                        maxScore = score;
                    } else if(score == maxScore) {
                        bestMoves.add(move);
                    }
*/
                }
            }

            if(prioritisedMoves.size() <= 0){
                System.out.println("Found no moves for " + team + " at: itrn" + i);
                for(Character ch : board.getBoard().values()) {
                    System.out.println(ch.getTeam().toString() + ch.getType().toString() + " - " + ch.getId());
                }

            }

            Move theMove = prioritisedMoves.poll();
            System.out.println("1st: ");
            displayMoveScore(board, theMove);
            System.out.println();
            System.out.println("2nd: ");
            displayMoveScore(board, prioritisedMoves.poll());
            System.out.println();
            System.out.println("3rd: ");
            displayMoveScore(board, prioritisedMoves.poll());

            Character characterById = CharacterHelper.getCharacterById(board, theMove.getCharacterId());
            CharacterHelper.makeAMove(board, characterById, theMove);
            BoardHelper.printBoard(board);
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public static void displayMoveScore(Board board, Move theMove){
        Character characterById = CharacterHelper.getCharacterById(board, theMove.getCharacterId());
        CharacterHelper.enrichPositionInfoForCharacter(board, characterById);
//            Character opponent = board.getBoard().get(characterById.getX() + theMove.getDx(), characterById.getY() + theMove.getDy());
        int x = characterById.getX() + theMove.getDx(), y = characterById.getY() + theMove.getDy();
        System.out.println("Move: " + characterById.getId() + characterById.getTeam().toString() + characterById.getType().toString() + " -> " + x + ", " + y + " Score: " + theMove.getScore()+ " Move: " + theMove);
        Ranker.getScoreOfAMove(board, theMove, true);


    }

    /*

//Q        Character character = board.getBoard().get(4, 1);
    Character character = board.getBoard().get(6, 0);
    List<Move> allMovesForPawn = CharacterHelper.getAllValidMovesByCharacter(board, character);
        CharacterHelper.makeAMove(board, character, allMovesForPawn.get(1));
        BoardHelper.printBoard(board);


//        character = board.getBoard().get(3, 0);
    character = board.getBoard().get(5, 2);
    List<Move> validMoves = CharacterHelper.getAllValidMovesByCharacter(board, character);
        System.out.println(validMoves);
        System.out.println(character);
    HashMap<Move, Float> hashMap = new HashMap();
        for(Move move : validMoves) {
        float score = Ranker.getScoreOfAMove(board, move);
        hashMap.put(move, score);
        System.out.println("Move: " + move + " score: " + score);
    }
        hashMap.entrySet().forEach(System.out::println);



     */
}
