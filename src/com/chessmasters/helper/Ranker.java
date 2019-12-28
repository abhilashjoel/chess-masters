package com.chessmasters.helper;

import com.chessmasters.characters.Character;
import com.chessmasters.model.Board;
import com.chessmasters.model.Constants;
import com.chessmasters.model.Move;

import java.util.List;

public class Ranker {
        /*
         * List all characeters Allies/Opponents
         * take values where not null && path
         * incerement count of possible values
         * identify characters, increment rank
         *
         */

    /**
     * @param board - State of the board
     * @param move - Move to be performed - Character, current position, dx, dy
     * @return score - A folating point value that represents the score of a move based on the following factors:
     *      > Whom am I capturing : Score increases by the baseValue of the victim piece
     *      > ScoreAtRest(AfterMove) - scoreAtRest(BeforeMove)
     *      > TODO Is this move causing a check to the King
     *      >
     */
    public static float getScoreOfAMove(Board board, Move move){
        float score = 0f;
        Character character = CharacterHelper.getCharacterById(board, move.getCharacterId());
        if(CharacterHelper.isBoardUnderCheck(board, character.getTeam())) {
            return Float.MIN_VALUE;
        }

        float scoreBeforeMove = getScoreOfCharacterAtPosition(board, character);

        Board clonedBoard = new Board(board);
        CharacterHelper.makeAMove(clonedBoard, character, move);
        CharacterHelper.enrichPositionInfoForCharacter(clonedBoard, character);
        float scoreAfterMove = getScoreOfCharacterAtPosition(clonedBoard, character);

        Character victim = board.getBoard().get(character.getX() + move.getDx(), character.getY() + move.getDy());
        if(victim != null) {
            score += victim.getType().getCharcterValue();
        }

        score += (scoreAfterMove - scoreBeforeMove);
        return score;
    }


    /**
     * @param board - State of the board
     * @param character - Chessmen whose score needs to be computed
     * @return score - A folating point value that represents the score of a chess piece based on the following factors
     *      > Who is protecting me : Each protector increases the score by `1`
     *      > Whom am I protecting : Score increases by a fraction of the baseValue of each protectee (baseValue defined in ChessMen.java).
     *      > Who is attacking me  : Score reduced by the baseValue of the `character`.
     *              If there is a guardian, then we take diff between min. baseValue of attacker and baseValue of `character`
     *      > TODO What number of tiles are accessible by my team.
     *      > TODO VulnerabilityScore of other pieces.
     *      > TODO Score of the opponent pieces that my team is now targetting
     *
     * NOTE: This method only returns the scoreAtRest for a chess piece.
     */
    public static float getScoreOfCharacterAtPosition(Board board, Character character){
        int baseScore = character.getType().getCharcterValue();
        List<Character> guardians = CharacterHelper.whoIsProtectingMe(board, character);
        List<Character> protectees = CharacterHelper.whomAmIProtecting(board, character); // = 1
        float defensiveScore = protectees.stream()
                                .map(protectee -> protectee.getType().getCharcterValue() * Constants.DEFENSIVE_FACTOR)
                                .reduce(Float::sum).get();
        List<Character> attackers = CharacterHelper.whoCanAttackMe(board, character, CharacterHelper.getOpponent(character.getTeam()));
        int vulnerabilityScore = 0;
        if(attackers.size() > 0) {
            vulnerabilityScore = baseScore;
            if(guardians.size() > 0){
                int minScoreOfAttacker = attackers.stream()
                                            .map(attacker -> attacker.getType().getCharcterValue())
                                            .reduce(Math::min).get();
                vulnerabilityScore = baseScore - minScoreOfAttacker;
            }
         } else {
            vulnerabilityScore = guardians.size() * (-1);
        }
        float score = defensiveScore - vulnerabilityScore;
        return score;
    }

}