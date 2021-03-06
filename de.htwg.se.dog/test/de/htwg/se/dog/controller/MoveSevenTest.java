package de.htwg.se.dog.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.htwg.se.dog.controller.impl.Movement;
import de.htwg.se.dog.models.FieldInterface;
import de.htwg.se.dog.models.FigureInterface;
import de.htwg.se.dog.models.impl.GameField;
import de.htwg.se.dog.models.impl.Player;

public class MoveSevenTest {
    Movement movement;
    GameField gamefield;
    Player tp1, tp2;
    FieldInterface[] array;
    private final int PLAYERCOUNT = 2;
    private final int FIELDSTILLHOUSE = 4;
    private final int HOUSECOUNT = 2;
    private final int FIGCOUNT = 2;
    private final int PLAYERID1 = 1;
    private final int PLAYERID2 = 2;
    private final int SEVEN = 7;

    /*
     * Gamefield from setUp 0 1 2 3 4 5 6 7 8 9 10 11
     *                      - - - - 1 1 - - - - 2  2
     */
    /* Generate empty gamefield with 2 players */
    @Before
    public void setUp() throws Exception {

        gamefield = new GameField(FIELDSTILLHOUSE, PLAYERCOUNT, HOUSECOUNT);
        movement = new Movement(gamefield);
        movement.setMoveStrategie(SEVEN);
        tp1 = new Player(PLAYERID1, FIGCOUNT, gamefield.calculatePlayerStart(PLAYERID1));
        tp2 = new Player(PLAYERID2, FIGCOUNT, gamefield.calculatePlayerStart(PLAYERID1));
        array = gamefield.getGameArray();

    }

    @Test
    public void testMoveFigure() {
        // Move Figure from empty Field
        array[2].putFigure(tp1.removeFigure());
        array[3].putFigure(tp2.removeFigure());
        FigureInterface tempFig = array[2].getFigure();
        movement.move(2, 2);
        // Startfield is empty
        assertNull(array[2].getFigure());
        //Figure on the Way was kicked
        assertNull(array[3].getFigure());
        // Figure on targetfield == Figure which moved
        assertEquals(tempFig, array[6].getFigure());

        //Try to Move Figure over blocked field-------------------------
        array[3].putFigure(tp1.removeFigure());
        // Move over Blocked field is not possible
        assertFalse(movement.move(1, 2));
    }

    @Test
    public void testMoveOverEdgeOfGamefield() {
        array[9].putFigure(tp1.removeFigure(), 9);
        assertTrue(movement.move(7, 9));
    }

    @Test
    public void testAnyValidMove() {
        // Search AnyValidMove of Player without Figures on Field
        assertFalse(movement.anyValidMove(tp2));
        // One Figure moves 7
        array[0].putFigure(tp1.removeFigure(), 0);
        assertTrue(movement.anyValidMove(tp1));
        // two figures move
        array[7].putFigure(tp1.removeFigure(), 7);
        array[6].putFigure(tp2.removeFigure(), 6);
        array[8].putFigure(tp2.removeFigure(), 8);
        array[6].setBlocked(true);
        assertTrue(movement.anyValidMove(tp1));
        // No Move possible
        array[8].setBlocked(true);
        assertFalse(movement.anyValidMove(tp1));

    }

    @Test
    public void testAnyValidMove2() {

        // eigene fig killen
        array[9].putFigure(tp1.removeFigure(), 9);
        array[3].putFigure(tp1.removeFigure(), 3);
        array[6].putFigure(tp2.removeFigure(), 6);
        array[2].putFigure(tp2.removeFigure(), 2);
        array[6].setBlocked(true);
        array[2].setBlocked(true);
        assertFalse(movement.anyValidMove(tp1));

    }

    @Test
    public void testAnyValidMove3() {
        // eigene fig killen
        array[0].putFigure(tp1.removeFigure(), 0);
        array[1].putFigure(tp1.removeFigure(), 1);
        array[6].putFigure(tp2.removeFigure(), 6);
        array[3].putFigure(tp2.removeFigure(), 3);
        array[6].setBlocked(true);
        array[3].setBlocked(true);
        assertFalse(movement.anyValidMove(tp1));

    }
}
/*
 * Gamefield from setUp 0 1 2 3 4 5 6 7 8 9 10 11 - - - - 1 1 - - - - 2 2 1 !2 1
 */