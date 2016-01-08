package pt.iul.poo.games.RunGame;

import pt.iul.poo.games.launcher.Launcher;
import pt.iul.poo.games.systems.SaveScore;
import pt.iul.poo.games.systems.ScoreBoard;

public class Main {

	public static SaveScore save;
	public static ScoreBoard score;

	public static void main(String[] args) {
		new Launcher();
	}
}
