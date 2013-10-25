package de.htwg.se.dog.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class FigureTest {
	Figure f;
	Figure fwrong;
	int playernr = 2;
	int fignr = 4;
	int wrongPlayernr = -1;
	int wrongFignr = 34;
	@Rule
	public ExpectedException expected = ExpectedException.none();
	@Before
	
	public void setUp() {
		f = new Figure(playernr, fignr);
	}
	
	@Test
	public void testGetOwner() {
		assertEquals(playernr, f.getOwner());
	}
	
	@Test
	public void testGetFignr() {
		assertEquals(fignr, f.getFignr());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testExpectedExceptionWrongFigNr() {
		fwrong = new Figure(playernr,wrongFignr);
		assertNotNull(fwrong);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testExpectedExceptionWrongOwnerNr() {
		fwrong = new Figure(wrongPlayernr,fignr);
		assertNotNull(fwrong);
	}
	

}
