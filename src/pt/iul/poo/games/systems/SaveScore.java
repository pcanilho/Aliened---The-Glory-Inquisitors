package pt.iul.poo.games.systems;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import pt.iul.poo.games.player.Player;

public class SaveScore {

	private TreeSet<Player> classifications;
	private String path;
	private ScoreBoard scoreboard;

	public SaveScore() {
		init();
	}

	private void init() {
		path = "resources/Player_Data/scoreboard.dat";
		classifications = new TreeSet<Player>();
	}

	public void save(Player player) {

		try {

			scoreboard = new ScoreBoard();

			FileOutputStream file = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(file);

			// scoreboard.add(player);

			classifications = scoreboard.getPlayers();
			classifications.add(player);

			out.writeObject(classifications);
			out.close();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "SaveScore - Erro ao criar ficheiro.", "SaveScore - save()", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

	}

	public void flush() {
		FileOutputStream file;
		
		try {
			file = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(file);
			
			
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public TreeSet<Player> load() {
		TreeSet<Player> tmp = new TreeSet<Player>();

		try {
			FileInputStream file = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(file);

			Object object = in.readObject();
			tmp = (TreeSet<Player>) object;
			in.close();

		} catch (IOException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "SaveScore - Erro ao lêr ficheiro: " + path, "SaveScore - getPlayerList()", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return tmp;
	}
}
