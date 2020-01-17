package com.chessmasters.helper;

import com.chessmasters.Main;
import com.chessmasters.characters.Character;
import com.chessmasters.characters.ChessMen;
import com.chessmasters.model.Board;
import com.chessmasters.model.Constants;
import com.chessmasters.model.Move;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.sun.deploy.util.OrderedHashSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

public class StrategyHelper {


    /**
     * @param cBoard - current state of Board
     * @param character
     * @param team
     * @return List of Characters from `team` who can attack said `character`
     *  Creates a clone of the board,
     *  Flips the team of the `character` if it belongs to `team`,
     *  Gets a list of all the chessMen who belong to `team`
     *  Checks if any of these chessMen can attack the said `character`
     */
    public static List<Character> whoCanAttackMe(Board cBoard, Character character, Character.Team team) {
        Board cloneBoard = new Board(cBoard);
        Table<Integer, Integer, Character> board = cloneBoard.getBoard();
        CharacterHelper.enrichPositionInfoForCharacter(cBoard, character);
        int x = character.getX(), y = character.getY();
        List<Character> attackers = new ArrayList();
        Character clone = new Character(character);
        if(Character.Team.WHITE.equals(team)) {
            clone.setTeam(Character.Team.BLACK);
        }else{
            clone.setTeam(Character.Team.WHITE);
        }
        board.put(character.getX(), character.getY(), clone);
        List<Character> opponents = CharacterHelper.getAllCharactersByTeam(cloneBoard, team);
        for(Character opponent : opponents) {
            List<Move> moves = CharacterHelper.getAllValidMovesByCharacter(cloneBoard, opponent);
            for(Move move : moves) {
                if(x == opponent.getX() + move.getDx() && y == opponent.getY() + move.getDy()){
                    attackers.add(opponent);
//                    break;
                }
            }
        }
        return attackers;
    }

    public static List<Character> whoIsProtectingMe(Board cBoard, Character character) {
        return whoCanAttackMe(cBoard, character, character.getTeam());
    }

    public static List<Character> whomCanIAttack(Board cBoard, Character character) {
        Table<Integer, Integer, Character> board = cBoard.getBoard();
        List<Character> victims = new ArrayList();
        List<Move> moves = CharacterHelper.getAllValidMovesByCharacter(cBoard, character);
        for(Move move : moves) {
            int x = character.getX() + move.getDx(), y = character.getY() + move.getDy();
            Character victim = board.get(x, y);
            if(victim != null && !victim.getTeam().equals(character.getTeam())){
                victims.add(victim);
            }
        }
        return victims;
    }

    public static List<Character> whomCanMyTeamAttack(Board board, Character.Team team) {
        List<Character> victims = new ArrayList();
        List<Character> allies = CharacterHelper.getAllCharactersByTeam(board, team);
        for(Character ally : allies) {
            victims.addAll(whomCanIAttack(board, ally));
        }
        return victims;
    }

    public static List<Character> whomAmIProtecting(Board cBoard, Character character) {
        Board cloneBoard = new Board(cBoard);
        Table<Integer, Integer, Character> board = cloneBoard.getBoard();
        List<Character> protectees = new ArrayList();
        CharacterHelper.enrichPositionInfoForCharacter(cBoard, character);
        int x = character.getX(), y = character.getY();
        Character clone = new Character(character);
        if(Character.Team.WHITE.equals(character.getTeam())) {
            clone.setTeam(Character.Team.BLACK);
        }else{
            clone.setTeam(Character.Team.WHITE);
        }
        board.put(character.getX(), character.getY(), clone);
        List<Move> moves = CharacterHelper.getAllValidMovesByCharacter(cloneBoard, clone);
        for(Move move : moves) {
            Character protectee = board.get(clone.getX() + move.getDx(), clone.getY() + move.getDy());
            if(protectee != null){
                protectees.add(protectee);
            }
        }
        return protectees;
    }

    public static boolean isBoardUnderCheck(Board board, Character.Team team) {
        Character.Team opponent = CharacterHelper.getOpponent(team);
        Character king = board.getBoard().values()
                .stream()
                .filter(character -> character.getType().equals(ChessMen.KING) && character.getTeam().equals(team))
                .findFirst().get();
        return whoCanAttackMe(board, king, opponent).size() > 0;
    }

    public static int tilesUnderMyInfluence(Board board, Character.Team team) {
        List<Character> allies = CharacterHelper.getAllCharactersByTeam(board, team);
        HashSet<Move> allMoves = new HashSet<>();
        int count = 0;

        for(Character ally : allies) {
            List<Move> moves = CharacterHelper.getAllValidMovesByCharacter(board, ally);
            moves = moves.stream()
                    .map(move -> {
                        move.setCharacterId(0);
                        return move;
                    })
                    .collect(Collectors.toList());
            count += moves.size();
            allMoves.addAll(moves);
        }
//        System.out.println("count: " + count + "   allMoves.size(): " + allMoves.size());
        return allMoves.size();
    }

    public static List<Character> protecteesAtRiskWithoutMe(Board board, Character character, List<Character> protectees) {
        Board clone = new Board(board);
        CharacterHelper.enrichPositionInfoForCharacter(clone, character);
        clone.getBoard().remove(character.getX(), character.getY());
        return protectees.stream()
                .filter((protectee) -> whoCanAttackMe(clone, protectee, CharacterHelper.getOpponent(character.getTeam())).size() > 0)
                .collect(Collectors.toList());
    }


    public static List<Move> getTopMoves(Board board, Character.Team team, boolean log) {

        List<Character> allPlayers = CharacterHelper.getAllCharactersByTeam(board, team);
        PriorityQueue<Move> prioritisedMoves = new PriorityQueue<>((move1, move2) -> Main.comp(move1.getScore(), move2.getScore()));
        for(Character player : allPlayers) {
            List<Move> moves = CharacterHelper.getAllValidMovesByCharacter(board, player);
            for(Move move : moves) {
                float score = 0;
                try {
                    score = Ranker.getScoreOfAMove(board, move, log);
                }catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Ex: " + move + player);
                }
                move.setScore(score);
                prioritisedMoves.add(move);
            }
        }

        if(prioritisedMoves.size() <= 0){
            for(Character ch : board.getBoard().values()) {
                System.out.println(ch.getTeam().toString() + ch.getType().toString() + " - " + ch.getId());
            }
        }

        List<Move> topMoves = new ArrayList();
        while(prioritisedMoves.peek() != null) {
            //TODO should we return all the moves? Might be required in case of check mate
            if(topMoves.size() == Constants.MAX_BRANCHES) {
                break;
            }
            Move move = prioritisedMoves.poll();
            topMoves.add(move);
        }
        return topMoves;
    }
}
