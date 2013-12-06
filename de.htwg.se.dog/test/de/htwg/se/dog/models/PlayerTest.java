package de.htwg.se.dog.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.htwg.se.dog.models.impl.Card;
import de.htwg.se.dog.models.impl.Figure;
import de.htwg.se.dog.models.impl.Player;

public class PlayerTest {

    Player p1;
    Card card2;
    int playerNr = 1;
    int figCount = 4;
    Figure temp;

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUp() {
        card2 = new Card(2);
        p1 = new Player(playerNr, figCount);
    }

    @Test
    public void testAddCard() {
        assertEquals(false, p1.getCardList().contains(card2));
        p1.addCard(card2);
        assertEquals(true, p1.getCardList().contains(card2));
    }

    @Test
    public void testRemoveCard() {
        p1.addCard(card2);
        assertEquals(true, p1.getCardList().contains(card2));
        assertEquals(true, p1.removeCard(card2));
    }

    @Test
    public void testRemoveFigure() {
        temp = p1.removeFigure();
        assertEquals(p1.getFigureList().size(), 3);
        p1.getFigureList().clear();
        assertNull(p1.removeFigure());
    }

    @Test
    public void testAddFigure() {
        p1.addFigure(temp);
        assertEquals(p1.getFigureList().size(), 5);
    }

    @Test
    public void testGetPlayerId() {
        assertEquals(playerNr, p1.getPlayerID());
    }

    @Test
    public void testUpdateFigurePos() {
        p1.updateFigurePos(1, 2);
        p1.updateFigurePos(15, 12);
        p1.updateFigurePos(1, -1);
        assertEquals((Integer) 12, p1.getFigureRegister().get(0));
    }

    @Test
    public void testtoString() {
        assertEquals("PlayerId: 1", p1.toString());
    }
}
