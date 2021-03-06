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
import java.util.stream.Collectors;

public class CharacterHelper {

    private static final Map<ChessMen, Integer> initialCharacterSet = new HashMap(){{
        put(ChessMen.KING, 1);
        put(ChessMen.QUEEN, 1);
        put(ChessMen.ROOK, 2);
        put(ChessMen.BISHOP, 2);
        put(ChessMen.KNIGHT, 2);
        put(ChessMen.PAWN, 8);
    }};

    public static final Map<String, Character> repToCharacterMap = new HashMap() {{
        put("WK", new Character(ChessMen.KING, Character.Team.WHITE));
        put("BK", new Character(ChessMen.KING, Character.Team.BLACK));
        put("BQ", new Character(ChessMen.QUEEN, Character.Team.BLACK));
        put("WQ", new Character(ChessMen.QUEEN, Character.Team.WHITE));
        put("BR", new Character(ChessMen.ROOK, Character.Team.BLACK));
        put("WR", new Character(ChessMen.ROOK, Character.Team.WHITE));
        put("BKn", new Character(ChessMen.KNIGHT, Character.Team.BLACK));
        put("WKn", new Character(ChessMen.KNIGHT, Character.Team.WHITE));
        put("BB", new Character(ChessMen.BISHOP, Character.Team.BLACK));
        put("WB", new Character(ChessMen.BISHOP, Character.Team.WHITE));
        put("BP", new Character(ChessMen.PAWN, Character.Team.BLACK));
        put("WP", new Character(ChessMen.PAWN, Character.Team.WHITE));
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
        int characterId = character.getId();
        List<Move> moves = new ArrayList();
        ChessMen type = character.getType();
        enrichPositionInfoForCharacter(cBoard, character);
        int x = character.getX();
        int y = character.getY();
        switch (type) {
            case PAWN:
                int dy = team.equals(Character.Team.BLACK) ? -1 : 1;
                moves.add(new Move(0, dy, characterId));
                moves.add(new Move(0, dy*2, characterId));
                moves.add(new Move(1, dy, characterId));
                moves.add(new Move(1, dy, characterId));
                break;
            case KING:
                //Sets.cartesianProduct also returns [0,0]. Shouldn't break for now, but ideally we shouldn't do this.
                Set<List<Integer>> kingMoves = Sets.cartesianProduct(Sets.newHashSet(0, 1, -1), Sets.newHashSet(0, 1, -1));
                for(List<Integer> move : kingMoves) {
                    moves.add(new Move(move.get(0), move.get(1), characterId));
                }
                break;
            case KNIGHT:
                Set<List<Integer>> knightMoves = new HashSet(Sets.cartesianProduct(Sets.newHashSet(1, -1), Sets.newHashSet(2, -2)));
                knightMoves.addAll(Sets.cartesianProduct(Sets.newHashSet(2, -2), Sets.newHashSet(1, -1)));
                for (List<Integer> move : knightMoves) {
                    moves.add(new Move(move.get(0), move.get(1), characterId));
                }
                break;
            case QUEEN:
                Set<List<Integer>> queenMoves = Sets.cartesianProduct(Sets.newHashSet(0, 1, -1), Sets.newHashSet(1, -1));
                queenMoves.addAll(Sets.cartesianProduct(Sets.newHashSet(1, -1), Sets.newHashSet( 0)));
                moves.addAll(getAllMovesForQRB(queenMoves, character));
                break;
            case BISHOP:
                Set<List<Integer>> bishopMoves = Sets.cartesianProduct(Sets.newHashSet(1, -1), Sets.newHashSet(1, -1));
                moves.addAll(getAllMovesForQRB(bishopMoves, character));
                break;
            case ROOK:
                Set<List<Integer>> rookMoves = Sets.newHashSet(Arrays.asList(0,1), Arrays.asList(0, -1), Arrays.asList(1, 0), Arrays.asList(-1, 0));
                moves.addAll(getAllMovesForQRB(rookMoves, character));
                break;
        }
        return moves;
    }

    public static List<Move> getAllValidMovesByCharacter(Board cBoard, Character character) {
        Table<Integer, Integer, Character> board = cBoard.getBoard();
        Character.Team team = character.getTeam();
        int characterId = character.getId();
        List<Move> moves = new ArrayList();
        ChessMen type = character.getType();
        enrichPositionInfoForCharacter(cBoard, character);
        int x = character.getX();
        int y = character.getY();
        switch (type) {
            case PAWN:
                int dy = team.equals(Character.Team.BLACK) ? 1 : -1;
                if(board.get(x, y+dy) == null) {
                    //Pawn can take 1 step ahead as that cell is empty
                    moves.add(new Move(0, dy, characterId));
                }
                if(character.isPrimodial() && board.get(x, y+dy+dy) == null){
                    //Pawn can take 2 steps ahead as its the 1st move
                    moves.add(new Move(0, dy*2, characterId));
                }
                Character diagonal1 = board.get(x + 1, y + dy);
                Character diagonal2 = board.get(x - 1, y + dy);
                if(diagonal1 != null && !diagonal1.getTeam().equals(team)){
                    //Found an enemy piece diagonal to the PAWN
                    moves.add(new Move(1, dy, characterId));
                }
                if(diagonal2 != null && !diagonal2.getTeam().equals(team)){
                    //Found an enemy piece diagonal to the PAWN
                    moves.add(new Move(-1, dy, characterId));
                }
                break;
            case KING:
                //Sets.cartesianProduct also returns [0,0]. Shouldn't break for now, but ideally we shouldn't do this.
                Set<List<Integer>> kingMoves = new HashSet(Sets.cartesianProduct(Sets.newHashSet(0, 1, -1), Sets.newHashSet(1, -1)));
                kingMoves.addAll(Sets.cartesianProduct(Sets.newHashSet( 1, -1), Sets.newHashSet(0)));
                for(List<Integer> move : kingMoves) {
                    if(!isPositionWithinBoard(x + move.get(0), y + move.get(1))){
                        continue;
                    }
                    Character c = board.get(x + move.get(0), y + move.get(1));
                    if(c == null || !c.getTeam().equals(team)){
                        moves.add(new Move(move.get(0), move.get(1), characterId));
                    }
//                    moves.add(new Move(x + move.get(0), y + move.get(1), characterId));
                }
                break;
            case KNIGHT:
                Set<List<Integer>> knightMoves = new HashSet(Sets.cartesianProduct(Sets.newHashSet(1, -1), Sets.newHashSet(2, -2)));
                knightMoves.addAll(Sets.cartesianProduct(Sets.newHashSet(2, -2), Sets.newHashSet(1, -1)));
                for (List<Integer> move : knightMoves) {
                    if (!isPositionWithinBoard(x + move.get(0), y + move.get(1)))
                        continue;
                    Character c = board.get(x + move.get(0), y + move.get(1));
                    if(c == null || !c.getTeam().equals(team)){
                        moves.add(new Move(move.get(0), move.get(1), characterId));
                    }
                }
                break;
            case QUEEN:
                Set<List<Integer>> queenMoves = new HashSet(Sets.cartesianProduct(Sets.newHashSet(0, 1, -1), Sets.newHashSet(1, -1)));
                queenMoves.addAll(Sets.cartesianProduct(Sets.newHashSet(1, -1), Sets.newHashSet( 0)));
                moves.addAll(getAllValidMovesForQRB(board, queenMoves, character));
                break;
            case BISHOP:
                Set<List<Integer>> bishopMoves = new HashSet(Sets.cartesianProduct(Sets.newHashSet(1, -1), Sets.newHashSet(1, -1)));
                moves.addAll(getAllValidMovesForQRB(board, bishopMoves, character));
                break;
            case ROOK:
                Set<List<Integer>> rookMoves = new HashSet(Sets.newHashSet(Arrays.asList(0,1), Arrays.asList(0, -1), Arrays.asList(1, 0), Arrays.asList(-1, 0)));
                moves.addAll(getAllValidMovesForQRB(board, rookMoves, character));
                break;
        }
        return moves;
    }


    public static List<Move> getAllValidMovesForQRB(Table<Integer, Integer, Character> board, Set<List<Integer>> baseMoves, Character character){
        List<Move> moves = new ArrayList();
        int x = character.getX(), y = character.getY();
        for(List<Integer> baseMove : baseMoves) {
            for (int multiple = 1; multiple < 7; multiple++) {
                Move move = new Move(baseMove.get(0) * multiple, baseMove.get(1) * multiple, character.getId());
                if(!isPositionWithinBoard(move.getDx() + x, move.getDy() + y)){
                    break;
                }
                Character c = board.get(move.getDx() + x, move.getDy() + y);
                if (c != null) {
                    if (!c.getTeam().equals(character.getTeam())) {
                        moves.add(new Move(move.getDx(), move.getDy(), character.getId()));
                    }
                    //Breaking out when a character is present in the path, as Queen, Bishop, Rook can't proceed any further.
                    break;
                } else {
                    moves.add(new Move(move.getDx(), move.getDy(), character.getId()));
                }
            }
        }
        return moves;
    }

    public static boolean isPositionWithinBoard(int x, int y){
        return x >= 0 && x <= 7 && y >=0 && y <= 7;
    }

    public static List<Move> getAllMovesForQRB(Set<List<Integer>> baseMoves, Character character){
        List<Move> moves = new ArrayList();
        for(List<Integer> baseMove : baseMoves) {
            for (int multiple = 1; multiple <= 7; multiple++) {
                moves.add(new Move(baseMove.get(0) * multiple, baseMove.get(1) * multiple, character.getId()));
            }
        }
        return moves;
    }


    public static void makeAMove(Board cBoard, Character character, Move move){
        enrichPositionInfoForCharacter(cBoard, character);
        if(!isPositionWithinBoard(character.getX() + move.getDx(), character.getY() + move.getDy())) {
            System.out.println("This move is outside the board");
        }
        Table<Integer, Integer, Character> board = cBoard.getBoard();
        board.remove(character.getX(), character.getY());
        board.put(character.getX() + move.getDx(), character.getY() + move.getDy(), character);
    }


    public static void enrichPositionInfoForCharacter(Board board, Character character) {
        for(int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 7; y++) {
                Character c = board.getBoard().get(x, y);
                if (c != null && c.getId() == character.getId()) {
                    character.setX(x);
                    character.setY(y);
                    return;
                }
            }
        }
//        System.out.println("Unable to enrich position info for : " + character);
    }

    public static List<Move> getAllMovesByTeam(Board cBoard, Character character,List<Move> listOfAllMoves, Character.Team team){

        return null;
    }

    public static List<Character> getAllCharactersByTeam(Board board, Character.Team team){
        return board.getBoard().values().stream().filter((character -> character.getTeam().equals(team))).collect(Collectors.toList());
    }

    public static Character.Team getOpponent(Character.Team team) {
        return team.equals(Character.Team.WHITE) ? Character.Team.BLACK : Character.Team.WHITE;
    }

    public static Character getCharacterById(Board cBoard, int characterId){
        Table<Integer, Integer, Character> board = cBoard.getBoard();
        for(int i = 0; i <= 7; i++) {
            for(int j = 0; j <= 7; j++) {
                if(board.get(i, j) != null && board.get(i, j).getId() == characterId) {
                    Character character = board.get(i, j);
                    enrichPositionInfoForCharacter(cBoard, character);
                    return new Character(character);
                }
            }
        }
        return null;
    }

}
