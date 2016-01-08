package pt.iul.poo.games.launcher;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Set;

import javax.swing.JOptionPane;

import pt.iul.poo.games.DemoGame;
import pt.iul.poo.games.RunGame.Game;
import pt.iul.poo.games.player.Player;
import pt.iul.poo.games.player.data.Data;
import pt.iul.poo.image.SimpleImage;

public class Menu implements Runnable {

	private Thread thread;
	private SimpleImage[] panels;
	private int index;
	private boolean running = false;

	private DemoGame game;

	public Menu(DemoGame game) {
		this.game = game;
		init();
	}

	private void init() {
		try {
			index = 0;

			panels = new SimpleImage[5];

			panels[0] = new SimpleImage("resources/images/endgame/menu(resume).png", 640 - 606 / 2, 400 - 700 / 2, 2);
			panels[1] = new SimpleImage("resources/images/endgame/menu(menu).png", 640 - 606 / 2, 400 - 700 / 2, 2);
			panels[2] = new SimpleImage("resources/images/endgame/menu(save).png", 640 - 606 / 2, 400 - 700 / 2, 2);
			panels[3] = new SimpleImage("resources/images/endgame/menu(load).png", 640 - 606 / 2, 400 - 700 / 2, 2);
			panels[4] = new SimpleImage("resources/images/endgame/menu(quittry).png", 640 - 606 / 2, 400 - 700 / 2, 2);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Menu - A imagem não foi encontrada", "Menu - Erro", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public void start() {
		thread = new Thread(this, "Menu");
		thread.start();
		running = true;
		game.setMenuSel(true);
	}

	public void stop() {
		try {

			game.setMenuSel(false);
			thread.join();
			running = false;

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		game.getWindow().getDisplayer().add(panels[index]);
		while (running) {
			if (!game.gameOver)
				updateMenu();
			else
				stop();
		}
	}

	public boolean isRunning() {
		return running;
	}

	public SimpleImage getImage() {
		return panels[index];
	}

	private void updateMenu() {
		Set<Integer> keyEvents = game.getWindow().getKeyEvents();
		Set<Point> mouseEvents = game.getWindow().getMouseEvents();

		int tmp = index;

		if (!keyEvents.isEmpty() || !mouseEvents.isEmpty()) {

			for (Integer i : keyEvents) {
				if (i.intValue() == KeyEvent.VK_UP) {
					if (index != 0) {
						game.getWindow().getDisplayer().remove(panels[tmp]);
						index--;
						game.getWindow().getDisplayer().add(panels[index]);
					}
				}
				if (i.intValue() == KeyEvent.VK_DOWN) {
					if (index != 4) {
						game.getWindow().getDisplayer().remove(panels[tmp]);
						index++;
						game.getWindow().getDisplayer().add(panels[index]);
					}
				}
				if (i.intValue() == KeyEvent.VK_ENTER) {

					if (index == 0) {
						game.getWindow().getDisplayer().remove(panels[tmp]);
						stop();

					}
					if (index == 1) {
						resetMusic();

						game.setRunning(false);
						game.getWindow().end();
						cancelTimers();

						int final_answer = JOptionPane.showConfirmDialog(null, "Deseja gravar antes de sair?", "Sair", JOptionPane.YES_NO_OPTION);
						if (final_answer == JOptionPane.YES_OPTION) {
							saveGame();
						}
						new Launcher();
						stop();
					}
					if (index == 2) {
						saveGame();
					}
					if (index == 3) {
						resetMusic();

						loadGame();
					}
					if (index == 4) {
						int answer = JOptionPane.showConfirmDialog(null, "Tem a certeza que pretende sair?", "Sair", JOptionPane.YES_NO_OPTION);

						if (answer == JOptionPane.YES_OPTION) {
							int final_answer = JOptionPane.showConfirmDialog(null, "Deseja gravar antes de sair?", "Sair", JOptionPane.YES_NO_OPTION);
							if (final_answer == JOptionPane.YES_OPTION) {
								saveGame();
								System.exit(0);
							} else
								System.exit(0);
						}
					}
				}
				if (i.intValue() == KeyEvent.VK_ESCAPE) {
					game.inMenu = false;
					game.getWindow().getDisplayer().remove(panels[tmp]);
					stop();
				}
			}
		}

		if (game.getWindow() != null)
			game.getWindow().getDisplayer().refresh();
	}

	private void resetMusic() {
		game.getTracks()[game.getMusicIndex()].stop();

	}

	private void cancelTimers() {
		for (int tm = 0; tm < game.getTimers().length; tm++)
			game.getTimers()[tm].cancel();

	}

	private void saveGame() {
		game.getData().save("Player", game.getPlayer().name);
		game.getData().save("Score", game.getPlayer().score);
		game.getData().save("Player_id", game.getPlayer().id);
		game.getData().save("Player_level", game.getPlayer().level);
		game.getData().save("Resources", 1500);

		JOptionPane.showMessageDialog(null, "Jogador - " + game.getPlayer().name + " salvo com sucesso!", "Jogo salvo", JOptionPane.INFORMATION_MESSAGE);
	}

	private void loadGame() {
		JOptionPane.showMessageDialog(null, "Jogador: " + game.getPlayer().name + " carregado com sucesso!", "Carregamento", JOptionPane.INFORMATION_MESSAGE);

		cancelTimers();
		game.getWindow().end();

		Data data = new Data();
		Player newPlayer = data.getPlayer();

		new Game().start(newPlayer.level);

		stop();
	}

}
