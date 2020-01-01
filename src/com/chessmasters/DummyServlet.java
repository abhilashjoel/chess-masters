package com.chessmasters;

import com.chessmasters.characters.Character;
import com.chessmasters.helper.BoardHelper;
import com.chessmasters.helper.CharacterHelper;
import com.chessmasters.helper.Ranker;
import com.chessmasters.model.Board;
import com.chessmasters.model.Move;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

@WebServlet(urlPatterns = "/app")
public class DummyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("doPost...");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletInputStream inputStream = req.getInputStream();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String json = "";
        StringBuffer strBuffer = new StringBuffer();
        while((line = bufferedReader.readLine()) != null) {
            strBuffer.append(line);
        }
        json = strBuffer.toString();
        System.out.println(json);
        String teamString = req.getParameter("team");
        Character.Team team;
        if("W".equals(teamString)){
            team = Character.Team.WHITE;
        }else{
            team = Character.Team.BLACK;
        }


        List<List<String>> grid = new Gson().fromJson(json, new TypeToken<List<List<String>>>() {}.getType());

        Board board = BoardHelper.getBoard(grid);


        List<Character> allPlayers = CharacterHelper.getAllCharactersByTeam(board, team);
        float maxScore = Float.NEGATIVE_INFINITY;
        List<Move> bestMoves = new ArrayList();
        PriorityQueue<Move> prioritisedMoves = new PriorityQueue<>((move1, move2) -> Main.comp(move1.getScore(), move2.getScore()));
        for(Character player : allPlayers) {
            List<Move> moves = CharacterHelper.getAllValidMovesByCharacter(board, player);
            for(Move move : moves) {
                float score = 0;
                try {
                    score = Ranker.getScoreOfAMove(board, move, false);
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
        float topScore = Float.NEGATIVE_INFINITY;
        while(prioritisedMoves.peek() != null) {
            Move move = prioritisedMoves.poll();
            if(move.getScore() < topScore) {
                break;
            }
            topMoves.add(move);
            topScore = move.getScore();
        }

        System.out.println("TopMoves: " + topMoves);
        Move theMove = topMoves.get(new Random().nextInt(topMoves.size()));
        Character characterById = CharacterHelper.getCharacterById(board, theMove.getCharacterId());
        CharacterHelper.makeAMove(board, characterById, theMove);

        String response = board.getJson();
        resp.getWriter().write(response);

    }
}
