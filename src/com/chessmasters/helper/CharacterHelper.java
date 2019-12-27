package com.chessmasters.helper;

import com.chessmasters.characters.Characters;
import com.chessmasters.characters.Character;
import com.chessmasters.characters.ChessMen;
import com.chessmasters.model.Board;
import com.chessmasters.model.Move;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CharacterHelper {

    private static final Map<ChessMen, Integer> initialCharacterSet = new HashMap(){{
        put(ChessMen.KING, 1);
        put(ChessMen.QUEEN, 1);
        put(ChessMen.ROOK, 2);
        put(ChessMen.BISHOP, 2);
        put(ChessMen.KNIGHT, 2);
        put(ChessMen.PAWN, 8);
    }};

    public static Characters getInitialCharacterSet(Character.Team team)
    {
        int id = 0;
        Characters characters = new Characters();
        if(team == Character.Team.BLACK) {
            id = 17;
        }

        for(Map.Entry<ChessMen, Integer> entry : initialCharacterSet.entrySet()){
            for(int count = 0; count < entry.getValue(); count++){
                characters.getCharacters().add(new Character(entry.getKey(), team, id++));
            }
        }
        return characters;
    }

    public static List<Move> getAllMovesByCharacter(Board cBoard, Character character) {
        Table<Integer, Integer, Character> board = cBoard.getBoard();
        Character.Team team = character.getTeam();
        List<Move> moves = new ArrayList();
        ChessMen type = character.getType();
        enrichPositionInfoForCharacter(cBoard, character);
        int x = character.getX();
        int y = character.getY();
        switch (type) {
            case PAWN:
                int dy = team.equals(Character.Team.BLACK) ? -1 : 1;
                if(board.get(x, y+dy) == null) {
                    //Pawn can take 1 step ahead as that cell is empty
                    moves.add(new Move(0, dy));
                }
                if(character.isPrimodial() && board.get(x, y+dy+dy) == null){
                    //Pawn can take 2 steps ahead as its the 1st move
                    moves.add(new Move(0, dy*2));
                }
                Character diagonal1 = board.get(x + 1, dy);
                Character diagonal2 = board.get(x - 1, dy);
                if(diagonal1 != null && !diagonal1.getTeam().equals(team)){
                    //Found an enemy piece diagonal to the PAWN
                    moves.add(new Move(x+1, y+dy));
                }
                if(diagonal2 != null && !diagonal2.getTeam().equals(team)){
                    //Found an enemy piece diagonal to the PAWN
                    moves.add(new Move(x-1, y+dy));
                }
                break;
            case KING:
                //Sets.cartesianProduct also returns [0,0]. Shouldn't break for now, but ideally we shouldn't do this.
                Set<List<Integer>> kingMoves = Sets.cartesianProduct(Sets.newHashSet(0, 1, -1), Sets.newHashSet(0, 1, -1));
                for(List<Integer> move : kingMoves) {
                    Character c = board.get(x + move.get(0), y + move.get(1));
                    if(c != null && !c.getTeam().equals(team)){
                        moves.add(new Move(x + move.get(0), y + move.get(1)));
                    }
                }
                break;
            case KNIGHT:
                Set<List<Integer>> knightMoves = new HashSet(Sets.cartesianProduct(Sets.newHashSet(1, -1), Sets.newHashSet(2, -2)));
                knightMoves.addAll(Sets.cartesianProduct(Sets.newHashSet(2, -2), Sets.newHashSet(1, -1)));
                System.out.println(knightMoves);
                for (List<Integer> move : knightMoves) {
                    if (!isPositionWithinBoard(x + move.get(0), y + move.get(1)))
                        continue;
                    Character c = board.get(x + move.get(0), y + move.get(1));
                    if(c == null || !c.getTeam().equals(team)){
                        moves.add(new Move(move.get(0), move.get(1)));
                    }
                }
                break;
            case QUEEN:
                Set<List<Integer>> queenMoves = Sets.cartesianProduct(Sets.newHashSet(0, 1, -1), Sets.newHashSet(1, -1));
                queenMoves.addAll(Sets.cartesianProduct(Sets.newHashSet(1, -1), Sets.newHashSet( 0)));
                moves.addAll(getAllValidMovesForQRB(board, queenMoves, x, y, team));
                break;
            case BISHOP:
                Set<List<Integer>> bishopMoves = Sets.cartesianProduct(Sets.newHashSet(1, -1), Sets.newHashSet(1, -1));
                moves.addAll(getAllValidMovesForQRB(board, bishopMoves, x, y, team));
                break;
            case ROOK:
                Set<List<Integer>> rookMoves = Sets.newHashSet(Arrays.asList(0,1), Arrays.asList(0, -1), Arrays.asList(1, 0), Arrays.asList(-1, 0));
                moves.addAll(getAllValidMovesForQRB(board, rookMoves, x, y, team));
                break;
        }
        return moves;
    }

    public static boolean isPositionWithinBoard(int x, int y){
        return x >= 0 && x <= 7 && y >=0 && y <= 7;
    }

    public static List<Move> getAllValidMovesForQRB(Table<Integer, Integer, Character> board, Set<List<Integer>> baseMoves, int x, int y, Character.Team team){
        List<Move> moves = new ArrayList();
        for(List<Integer> baseMove : baseMoves) {
            for (int multiple = 1; multiple < 7; multiple++) {
                Move move = new Move(baseMove.get(0) * multiple, baseMove.get(1) * multiple);
                Character c = board.get(move.getDx() + x, move.getDy() + y);
                if (c != null) {
                    if (!c.getTeam().equals(team)) {
                        moves.add(new Move(move.getDx(), move.getDy()));
                    }
                    //Breaking out when a character is present in the path, as Queen, Bishop, Rook can't proceed any further.
                    break;
                }
            }
        }
        return moves;
    }


    public static void makeAMove(Board cBoard, Character character, Move move){
        System.out.println(character);
        System.out.println(move);
        Table<Integer, Integer, Character> board = cBoard.getBoard();
        board.remove(character.getX(), character.getY());
        board.put(character.getX() + move.getDx(), character.getY() + move.getDy(), character);
    }


    public static void enrichPositionInfoForCharacter(Board board, Character character) {
        for(int x = 0; x < 7; x++) {
            for (int y = 0; y < 7; y++) {
                Character c = board.getBoard().get(x, y);
                if (c != null && c.getId() == character.getId()) {
                    character.setX(x);
                    character.setY(y);
                }
            }
        }
    }
}
