package com.chessmasters.helper;

import com.chessmasters.characters.Character;
import com.chessmasters.characters.ChessMen;
import com.chessmasters.model.Board;
import com.chessmasters.model.Constants;
import com.chessmasters.model.Move;
import com.google.gson.Gson;

import java.util.List;

public class BruteForceMoveRanker {

    BruteForceMoveRanker parent;
    Board board;
    int currentIteration = 1;
    Character.Team team;
    Character.Team childTeam;
    Move move;
    Character character;
    float weight = 0;

    public BruteForceMoveRanker(Board board, Character.Team team, Move move) {
        this.board = new Board(board);
        this.team = team;
        this.childTeam = CharacterHelper.getOpponent(this.team);
        this.move = move;
        this.character = CharacterHelper.getCharacterById(this.board, move.getCharacterId());
        if(character == null) {
            System.out.println("Character is null");
            System.out.println(move);
            BoardHelper.printBoard(board);
            System.out.println(team);
            System.out.println(CharacterHelper.getAllCharactersByTeam(board, team));
            System.out.println(new Gson().toJson(board.getBoard()));
        }
        CharacterHelper.enrichPositionInfoForCharacter(this.board, character);
        CharacterHelper.makeAMove(this.board, character, move);
        CharacterHelper.enrichPositionInfoForCharacter(this.board, character);
    }

    public BruteForceMoveRanker(BruteForceMoveRanker parent, Move move) {
        this(parent.board, parent.childTeam, move);
        this.parent = parent;
        this.currentIteration = parent.currentIteration + 1;
        computeWeight();
    }

    public float computeAndGetWeight() {
        computeWeight();
        float scoreOfCharacterAtPosition = Ranker.getScoreOfCharacterAtPosition(board, character, false);
        weight += scoreOfCharacterAtPosition;
        return weight;
    }

    public void computeWeight() {
        if(move.getScore() >= Constants.WEIGHT_KING) {
            System.out.println("Looks like we've hit the king..." + move.getScore()+ " at iteration + " + currentIteration + "  Weight: " + (1000 / (currentIteration * Constants.MAX_BRANCHES)));
            printPathOnBoard();
            weight = 1000 * currentIteration * Constants.MAX_BRANCHES;
            if(parent != null) {
                parent.incrementWeight(weight);
            }
            return;
        }
        if(move.getScore() == Constants.WEIGHT_KING * -1) {
            weight = 1000 * currentIteration * Constants.MAX_BRANCHES * -1;
            if(parent != null) {
                parent.incrementWeight(weight);
            }
            return;
        }
        if(parent != null) {
            Character victim = parent.board.getBoard().get(character.getX(), character.getY());
            if (victim != null) {
                if (victim.getType().equals(ChessMen.KING)) {
                    System.out.println("Looks like we've hit the king..." + move.getScore() + " at iteration + " + currentIteration + "  Weight: " + (1000 / (currentIteration * Constants.MAX_BRANCHES)));
                    System.out.println(move);
                    printPathOnBoard();
                    weight += 100;
                } else {
                    weight += victim.getType().getCharcterValue();
                }
            }
        }

        if(parent != null) {
            parent.incrementWeight(weight);
        }

        if(currentIteration == Constants.MAX_ITERATIONS) {
            return;
        }

        String originalBoard = board.getJson();
        List<Move> topMoves = null;
        try {
            topMoves = StrategyHelper.getTopMoves(board, childTeam, false);
        } catch (Exception e) {
            BoardHelper.printBoard(board);
        }
        String currentBoard = board.getJson();
        float moveScoreSum = 0;
        for(Move move : topMoves) {
            moveScoreSum += move.getScore();
        }

        if(moveScoreSum <= Constants.WEIGHT_KING * -3) {
            System.out.println("This could be a checkMate..." + moveScoreSum + " at iteration + " + currentIteration + "  Weight: " + (1000 / (currentIteration * Constants.MAX_BRANCHES)));
//            System.out.println(getPath());
            printPathOnBoard();
            parent.incrementWeight(1000 * currentIteration * Constants.MAX_BRANCHES);
            return;
        }
        if(moveScoreSum <= Constants.WEIGHT_KING * -2) {
//            System.out.println("This could be a 2/3 possible checkMate..." + moveScoreSum + " at iteration + " + currentIteration);
////            System.out.println(getPath());
//            System.out.println(topMoves);
//            printPathOnBoard();
        }
        if(originalBoard.equals(currentBoard) == false) {
            System.out.println("Board state has been compromised...");
        }
        for (Move move : topMoves) {
            if(move.getScore() == Constants.WEIGHT_KING * -1) {
                continue;
            }
            new BruteForceMoveRanker(this, move);
            currentBoard = board.getJson();
            if(originalBoard.equals(currentBoard) == false) {
                System.out.println("Board state has been compromised...");
            }
        }
    }

    public void incrementWeight(float weight) {
        weight = (weight * -1) / Constants.MAX_BRANCHES;
        if(parent != null) {
            parent.incrementWeight(weight);
        }
        this.weight += weight;
    }


    public static Move getBestMove(Board board, Character.Team team) {
        List<Move> topMoves = StrategyHelper.getTopMoves(board, team, false);
        float topScore = Float.MAX_VALUE * -1;
        Move topMove = null;


        for (Move move : topMoves) {
            BruteForceMoveRanker moveRanker = new BruteForceMoveRanker(board, team, move);
            float weightOfMove = moveRanker.computeAndGetWeight();
            System.out.println(CharacterHelper.getCharacterById(board, move.getCharacterId()).str() + move + move.getScore() + " ->" + weightOfMove);
            Character character = CharacterHelper.getCharacterById(board, move.getCharacterId());
            weightOfMove += move.getScore();
            if(weightOfMove > topScore) {
                topMove = move;
                topScore = weightOfMove;
            }
        }
        return topMove;
    }

    public String getPath() {
        String path = "";
        if(parent != null) {
            path = parent.getPath();
        }
        path += character.str() + move.toString() + " -> ";
        return path;
    }

    public void printPathOnBoard() {
        if(parent != null) {
            parent.printPathOnBoard();
        }
        BoardHelper.printBoard(board);
    }

}
