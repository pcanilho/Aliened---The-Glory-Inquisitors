package pt.iul.poo.games.player;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {

	private Player player1, player2, player3, player4;

	@Before
	public void setUp() throws Exception {
		player1 = new Player("Default1", 0, 100, 1, 1000);
		player2 = new Player("Default2", 1, 150, 1, 1300);
		player3 = new Player("Default3", 2, 200, 1, 1200);
		player4 = new Player("Default4", 3, 250, 1, 1400);

		Assert.assertNotNull(player1);
		Assert.assertNotNull(player2);
		Assert.assertNotNull(player3);
		Assert.assertNotNull(player4);
	}

	@Test
	public final void testPlayerStringIntIntIntInt() {
		Player tmp = new Player("Default", 0, 100, 1, 1000);

		Assert.assertNotNull(tmp);
	}

	@Test
	public final void testSetResources() {
		player1.setResources(100);
		player2.setResources(100);
		player3.setResources(100);
		player4.setResources(100);

		Assert.assertEquals(new Integer(900), player1.resources);
		Assert.assertEquals(new Integer(1200), player2.resources);
		Assert.assertEquals(new Integer(1100), player3.resources);
		Assert.assertEquals(new Integer(1300), player4.resources);
	}

	@Test
	public final void testSetScore() {
		player1.setScore(100);
		player2.setScore(100);
		player3.setScore(100);
		player4.setScore(100);

		Assert.assertEquals(new Integer(0), player1.score);
		Assert.assertEquals(new Integer(50), player2.score);
		Assert.assertEquals(new Integer(100), player3.score);
		Assert.assertEquals(new Integer(150), player4.score);
	}

	@Test
	public final void testToString() {
		String expected1 = "Jogador: Default1, nível: 1, recursos: 1000 ,Invasores: 0";
		String expected2 = "Jogador: Default2, nível: 1, recursos: 1300 ,Invasores: 0";
		String expected3 = "Jogador: Default3, nível: 1, recursos: 1200 ,Invasores: 0";
		String expected4 = "Jogador: Default4, nível: 1, recursos: 1400 ,Invasores: 0";

		Assert.assertEquals(expected1, player1.toString());
		Assert.assertEquals(expected2, player2.toString());
		Assert.assertEquals(expected3, player3.toString());
		Assert.assertEquals(expected4, player4.toString());

	}

	@Test
	public final void testCompareTo() {
		Player equal = new Player("Default1", 0, 100, 1, 1000);
		Player lesser = new Player("Default1", 0, 100, 2, 1000);
		Player greater = new Player("Default1", 0, 100, 0, 1000);

		Assert.assertEquals(new Integer(0), new Integer(player1.compareTo(equal)));
		Assert.assertEquals(new Integer(1), new Integer(player1.compareTo(greater)));
		Assert.assertEquals(new Integer(-1), new Integer(player1.compareTo(lesser)));
	}

}
