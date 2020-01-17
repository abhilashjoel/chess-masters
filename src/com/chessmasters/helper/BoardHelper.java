package com.chessmasters.helper;

import com.chessmasters.characters.Character;
import com.chessmasters.characters.Characters;
import com.chessmasters.characters.ChessMen;
import com.chessmasters.model.Board;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardHelper {

    private static int PADDING_SIZE = 2;
    private static String NEW_LINE = "\n";
    private static String TABLE_JOINT_SYMBOL = "+";
    private static String TABLE_V_SPLIT_SYMBOL = "|";
    private static String TABLE_H_SPLIT_SYMBOL = "-";

    public static String generateTable(List<String> headersList, List<List<String>> rowsList,int... overRiddenHeaderHeight)
    {
        StringBuilder stringBuilder = new StringBuilder();

        int rowHeight = overRiddenHeaderHeight.length > 0 ? overRiddenHeaderHeight[0] : 1;

        Map<Integer,Integer> columnMaxWidthMapping = getMaximumWidhtofTable(headersList, rowsList);

        stringBuilder.append(NEW_LINE);
        stringBuilder.append(NEW_LINE);
        createRowLine(stringBuilder, headersList.size(), columnMaxWidthMapping);

        for (List<String> row : rowsList) {

            for (int i = 0; i < rowHeight; i++) {
                stringBuilder.append(NEW_LINE);
            }

            for (int cellIndex = 0; cellIndex < row.size(); cellIndex++) {
                fillCell(stringBuilder, row.get(cellIndex), cellIndex, columnMaxWidthMapping);
            }
            stringBuilder.append(NEW_LINE);
            createRowLine(stringBuilder, headersList.size(), columnMaxWidthMapping);

        }


        return stringBuilder.toString();
    }

    private static void fillSpace(StringBuilder stringBuilder, int length)
    {
        for (int i = 0; i < length; i++) {
            stringBuilder.append(" ");
        }
    }

    private static void createRowLine(StringBuilder stringBuilder,int headersListSize, Map<Integer,Integer> columnMaxWidthMapping)
    {
        for (int i = 0; i < headersListSize; i++) {
            if(i == 0)
            {
                stringBuilder.append(TABLE_JOINT_SYMBOL);
            }

            for (int j = 0; j < columnMaxWidthMapping.get(i) + PADDING_SIZE * 2 ; j++) {
                stringBuilder.append(TABLE_H_SPLIT_SYMBOL);
            }
            stringBuilder.append(TABLE_JOINT_SYMBOL);
        }
    }


    private static Map<Integer,Integer> getMaximumWidhtofTable(List<String> headersList, List<List<String>> rowsList)
    {
        Map<Integer,Integer> columnMaxWidthMapping = new HashMap<>();

        for (int columnIndex = 0; columnIndex < headersList.size(); columnIndex++) {
            columnMaxWidthMapping.put(columnIndex, 0);
        }

        for (int columnIndex = 0; columnIndex < headersList.size(); columnIndex++) {

            if(headersList.get(columnIndex).length() > columnMaxWidthMapping.get(columnIndex))
            {
                columnMaxWidthMapping.put(columnIndex, headersList.get(columnIndex).length());
            }
        }


        for (List<String> row : rowsList) {

            for (int columnIndex = 0; columnIndex < row.size(); columnIndex++) {

                if(row.get(columnIndex).length() > columnMaxWidthMapping.get(columnIndex))
                {
                    columnMaxWidthMapping.put(columnIndex, row.get(columnIndex).length());
                }
            }
        }

        for (int columnIndex = 0; columnIndex < headersList.size(); columnIndex++) {

            if(columnMaxWidthMapping.get(columnIndex) % 2 != 0)
            {
                columnMaxWidthMapping.put(columnIndex, columnMaxWidthMapping.get(columnIndex) + 1);
            }
        }


        return columnMaxWidthMapping;
    }

    private static int getOptimumCellPadding(int cellIndex,int datalength,Map<Integer,Integer> columnMaxWidthMapping,int cellPaddingSize)
    {
        if(datalength % 2 != 0)
        {
            datalength++;
        }

        if(datalength < columnMaxWidthMapping.get(cellIndex))
        {
            cellPaddingSize = cellPaddingSize + (columnMaxWidthMapping.get(cellIndex) - datalength) / 2;
        }

        return cellPaddingSize;
    }

    private static void fillCell(StringBuilder stringBuilder,String cell,int cellIndex,Map<Integer,Integer> columnMaxWidthMapping)
    {

        int cellPaddingSize = getOptimumCellPadding(cellIndex, cell.length(), columnMaxWidthMapping, PADDING_SIZE);

        if(cellIndex == 0)
        {
            stringBuilder.append(TABLE_V_SPLIT_SYMBOL);
        }

        fillSpace(stringBuilder, cellPaddingSize);
        stringBuilder.append(cell);
        if(cell.length() % 2 != 0)
        {
            stringBuilder.append(" ");
        }

        fillSpace(stringBuilder, cellPaddingSize);

        stringBuilder.append(TABLE_V_SPLIT_SYMBOL);

    }


    public static void printBoard(Board board){
        Table<Integer, Integer, Character> table = board.getBoard();
        List<List<String>> pattern = new ArrayList();
        for(int i = 0; i < 8; i++) {
            List<String> row = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                if(table.get(j, i) != null) {
                    Character character = table.get(j, i);
                    row.add(character.getId()+character.getTeam().toString()+character.getType().toString());
                }else{
                    row.add("");
                }
            }
            pattern.add(row);
        }
//        System.out.println(pattern);

        String s = generateTable(Arrays.asList("h", "h", "h", "h", "h", "h", "h", "h"), pattern);
        System.out.println(s);
    }


    public static void positionCharactersOnBoard(Board board){
        Characters whiteChessMen = CharacterHelper.getInitialCharacterSet(Character.Team.WHITE);
        Characters blackChessMen = CharacterHelper.getInitialCharacterSet(Character.Team.BLACK);

        int x = 0;
        List<Character> whitePawns = whiteChessMen.getCharactersByType(ChessMen.PAWN);
        for(Character pawn : whitePawns) board.getBoard().put(x++, 1, pawn);

        x = 0;
        List<Character> blackPawns = blackChessMen.getCharactersByType(ChessMen.PAWN);
        for(Character pawn : blackPawns) board.getBoard().put(x++, 6, pawn);

        List<Character> whiteRooks = whiteChessMen.getCharactersByType(ChessMen.ROOK);
        board.getBoard().put(0, 0, whiteRooks.get(0));
        board.getBoard().put(7, 0, whiteRooks.get(1));

        List<Character> blackRooks = blackChessMen.getCharactersByType(ChessMen.ROOK);
        board.getBoard().put(0, 7, blackRooks.get(0));
        board.getBoard().put(7, 7, blackRooks.get(1));


        List<Character> whiteKnights = whiteChessMen.getCharactersByType(ChessMen.KNIGHT);
        board.getBoard().put(1, 0, whiteKnights.get(0));
        board.getBoard().put(6, 0, whiteKnights.get(1));

        List<Character> blackKnights = blackChessMen.getCharactersByType(ChessMen.KNIGHT);
        board.getBoard().put(1, 7, blackKnights.get(0));
        board.getBoard().put(6, 7, blackKnights.get(1));


        List<Character> whiteBishops = whiteChessMen.getCharactersByType(ChessMen.BISHOP);
        board.getBoard().put(2, 0, whiteBishops.get(0));
        board.getBoard().put(5, 0, whiteBishops.get(1));

        List<Character> blackBishops = blackChessMen.getCharactersByType(ChessMen.BISHOP);
        board.getBoard().put(2, 7, blackBishops.get(0));
        board.getBoard().put(5, 7, blackBishops.get(1));


        board.getBoard().put(3, 0, whiteChessMen.getCharactersByType(ChessMen.QUEEN).get(0));
        board.getBoard().put(4, 0, whiteChessMen.getCharactersByType(ChessMen.KING).get(0));


        board.getBoard().put(4, 7, blackChessMen.getCharactersByType(ChessMen.QUEEN).get(0));
        board.getBoard().put(3, 7, blackChessMen.getCharactersByType(ChessMen.KING).get(0));

    }


    public static Board getBoard(List<List<String>> grid) {
        Board cBoard = new Board();
        Table<Integer, Integer, Character> board = HashBasedTable.create();
        cBoard.setBoard(board);
        int id = 0;
        for(int y = 0; y <= 7; y++) {
            List<String> row = grid.get(y);
            for(int x = 0; x <= 7; x++) {
                String chessMen = row.get(x);
                if(chessMen != null && !chessMen.isEmpty()) {
                    Character character = CharacterHelper.repToCharacterMap.get(chessMen);
                    character = new Character(character.getType(), character.getTeam(), id++);
                    board.put(x, y, character);
                }
            }
        }
        System.out.println(board);
        return cBoard;
    }



}