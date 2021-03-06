package de.htwg.se.dog.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.htwg.se.dog.controller.impl.Movement;
import de.htwg.se.dog.models.FieldInterface;
import de.htwg.se.dog.models.PlayerInterface;
import de.htwg.se.dog.models.impl.GameField;
import de.htwg.se.dog.models.impl.Player;

public class MoveSwitchTest {
    Movement movement;
    GameField gamefield;
    PlayerInterface player1, player2;
    FieldInterface[] array;
    private final int JACK = 11;
    private final int PLAYERCOUNT = 2;
    private final int FIELDSTILLHOUSE = 2;
    private final int HOUSECOUNT = 2;
    private final int FIGCOUNT = 2;
    private final int PLAYERID1 = 1;
    private final int PLAYERID2 = 2;
    private final int ZERO = 0;
    private final int ONE = 1;
    private final int TWO = 2;

    private final int THREE = 3;
    private final int FIVE = 5;
    private final int SIX = 6;
    private final int PLAYER1HOUSEFIELD = 3;
    private final int PLAYER2HOUSEFIELD = 6;

    /* Gamefield from setUp
     * 0 1 2 3 4 5 6 7
     * - - 1 1 - - 2 2
     */
    /* Generate empty gamefield with 2 players */
    @Before
    public void setUp() throws Exception {

        gamefield = new GameField(FIELDSTILLHOUSE, PLAYERCOUNT, HOUSECOUNT);
        movement = new Movement(gamefield);
        movement.setMoveStrategie(JACK);
        player1 = new Player(PLAYERID1, FIGCOUNT, gamefield.calculatePlayerStart(PLAYERID1));
        player2 = new Player(PLAYERID2, FIGCOUNT, gamefield.calculatePlayerStart(PLAYERID1));
        array = gamefield.getGameArray();

    } // Voll Geil

    @Test
    public void testMove() {
        array[ZERO].putFigure(player1.removeFigure());
        array[FIVE].putFigure(player2.removeFigure());
        array[PLAYER1HOUSEFIELD].putFigure(player1.removeFigure());
        array[PLAYER2HOUSEFIELD].putFigure(player2.removeFigure());
        assertTrue(movement.move(FIVE, ZERO));
        assertFalse(movement.move(ONE, ZERO));
        assertFalse(movement.move(ZERO, ONE));
        assertFalse(movement.move(PLAYER1HOUSEFIELD, ONE));
        assertFalse(movement.move(ONE, PLAYER1HOUSEFIELD));
        assertFalse(movement.move(THREE, SIX));
    }

    @Test
    public void testAnySwitchMove() {
        array[ZERO].putFigure(player1.removeFigure());
        array[TWO].putFigure(player1.removeFigure());
        array[TWO].setBlocked(true);
        array[FIVE].putFigure(player2.removeFigure());
        assertTrue(movement.anySwitchMove(ZERO));
        assertFalse(movement.anySwitchMove(TWO));
    }

    @Test
    public void testvalidMove() {
        array[ZERO].putFigure(player1.removeFigure());
        array[FIVE].putFigure(player2.removeFigure());
        array[PLAYER1HOUSEFIELD].putFigure(player1.removeFigure());
        array[PLAYER2HOUSEFIELD].putFigure(player2.removeFigure());
        assertTrue(movement.validMove(FIVE, ZERO));
        assertFalse(movement.validMove(ONE, ZERO));
    }

}
