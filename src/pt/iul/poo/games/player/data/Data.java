package pt.iul.poo.games.player.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import pt.iul.poo.games.player.Player;

public class Data {

	private Player player;
	private Properties properties = new Properties();

	public Data() {
		String path = "resources/Player_Data/player_data.xml";
		load(path);
	}

	// Salvar pontuação e nível do jogador
	public void save(String key, int value) {
		String path = "resources/Player_Data/player_data.xml";

		try {
			File file = new File(path);
			boolean exists = file.exists();

			if (!exists)
				file.createNewFile();

			// Escrever a pontuação de um jogador e o respectivo nome
			OutputStream write = new FileOutputStream(path);
			properties.setProperty(key, Integer.toString(value));
			properties.storeToXML(write, "Save Game");

			write.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//

	// Salvar nome jogador
	public void save(String key, String value) {
		String path = "resources/Player_Data/player_data.xml";

		try {
			File file = new File(path);
			boolean exists = file.exists();

			if (!exists)
				file.createNewFile();

			// Escrever o nome do jogador
			OutputStream write = new FileOutputStream(path);
			properties.setProperty(key, value);
			properties.storeToXML(write, "Player");

			write.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void load(String path) {
		path = "resources/Player_Data/player_data.xml";

		try {
			InputStream read = new FileInputStream(path);

			properties.loadFromXML(read);
			String Player_name = properties.getProperty("Player");
			String Player_score = properties.getProperty("Score");
			String Player_level = properties.getProperty("Player_level");
			String Player_id = properties.getProperty("Player_id");
			String Player_res = properties.getProperty("Resources");
			String Player_inv = properties.getProperty("Inv_Kia");

			setPlayer(Player_name, Integer.parseInt(Player_id), Integer.parseInt(Player_score), Integer.parseInt(Player_level), Integer.parseInt(Player_res), Integer.parseInt(Player_inv));

			read.close();
		} catch (FileNotFoundException e) {

			// Se não existe nenhum jogo anteriormente salvo, é criado um vazio.

			// Nome do jogador = Default
			save("Player", "Default");
			// Pontuação = 0
			save("Score", 100);
			// Primeiro nível = 0
			save("Player_level", 0);
			// Id
			save("Player_id", 0);
			// Recursos
			save("Resources", 1000);
			// Invasores mortos
			save("Inv_Kia", 0);
			

			load(path);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Player getPlayer() {
		return player;
	}

	private void setPlayer(String player_name, int id, int score, int level, int resources, int inv_kia) {
		player = new Player(player_name, id, score, level, resources);
		player.invaders_killed = inv_kia;
	}

}
