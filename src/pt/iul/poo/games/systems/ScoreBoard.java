package pt.iul.poo.games.systems;

import java.util.TreeSet;

import pt.iul.poo.games.player.Player;

public class ScoreBoard {

	private SaveScore saveScore;
	private TreeSet<Player> scoreboard;

	public ScoreBoard() {
		init();
	}

	private void init() {

		saveScore = new SaveScore();
		scoreboard = saveScore.load();
	}

	public TreeSet<Player> getPlayers() {
		return scoreboard;
	}

	// if (p1.level.compareTo(p2.level) == 0) {
	// if (p1.resources.compareTo(p2.resources) == 1) {
	// return 1;
	// }
	// if (p1.resources.compareTo(p2.resources) == -1) {
	// return -1;
	// } else {
	// if (p1.score.compareTo(p2.score) == -1) {
	// return -1;
	// }
	// if (p1.score.compareTo(p2.score) == 1) {
	// return 1;
	// } else
	// return 0;
	// }
	//
	// }
	// if (p1.level.compareTo(p2.level) == -1)
	// return -1;
	// else
	// return 1;

	public TreeSet<Player> getBest() {

		TreeSet<Player> scoreList = new TreeSet<Player>();
		for (int i = 0; i < 3; i++)
			scoreList.add(scoreboard.pollLast());

		return scoreList;
	}
}
