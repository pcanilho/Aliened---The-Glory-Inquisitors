package pt.iul.poo.games.launcher;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Set;

import javax.swing.JOptionPane;

import pt.iul.poo.games.DemoGame;
import pt.iul.poo.games.RunGame.Game;
import pt.iul.poo.image.SimpleImage;

public class GameOver implements Runnable {

	private Thread thread;
	private SimpleImage[] panels;
	private int index;
	private boolean running = false;

	private DemoGame game;
	private int lastLevel;

	public GameOver(DemoGame game) {
		this.game = game;
		init();
	}

	private void init() {
		try {
			index = 0;
			lastLevel = game.getPlayer().level;

			panels = new SimpleImage[3];
			panels[0] = new SimpleImage("resources/images/endgame/endgame_pane(jogar).png", 0, 0, 2);
			panels[1] = new SimpleImage("resources/images/endgame/endgame_pane(menu).png", 0, 0, 2);
			panels[2] = new SimpleImage("resources/images/endgame/endgame_pane(sair).png", 0, 0, 2);

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "GameOver - A imagem não foi encontrada", "GameOver - Erro", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public void start() {
		thread = new Thread(this, "GameOver");
		thread.start();
		running = true;

		clearMenu();
		clearPlayerInterface();
	}

	private void clearMenu() {
		if (game.getMenu() != null) {
			if (game.getMenu().isRunning()) {
				game.getWindow().getDisplayer().remove(game.getMenu().getImage());
				game.getMenu().stop();
			}
		}
	}
	
	private void clearPlayerInterface(){
		game.getWindow().getDisplayer().remove(game.getPlayerInterface());
	}

	public void stop() {
		try {
			thread.join();
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		resetMusic();
		game.getWindow().getDisplayer().add(panels[index]);
		cancelTimers();

		while (running) {
			updatePanel();
		}
	}

	private void cancelTimers() {
		for (int tm = 0; tm < game.getTimers().length; tm++)
			game.getTimers()[tm].cancel();

	}
	
	private void resetMusic() {
		game.getTracks()[game.getMusicIndex()].stop();

	}

	private void updatePanel() {
		Set<Integer> keyEvents = game.getWindow().getKeyEvents();
		Set<Point> mouseEvents = game.getWindow().getMouseEvents();

		int tmp = index;

		if (!keyEvents.isEmpty() || !mouseEvents.isEmpty()) {

			for (Integer i : keyEvents) {
				if (i.intValue() == KeyEvent.VK_RIGHT) {
					if (index < 2) {
						game.getWindow().getDisplayer().remove(panels[tmp]);
						index++;
						game.getWindow().getDisplayer().add(panels[index]);
					}
				}
				if (i.intValue() == KeyEvent.VK_LEFT) {
					if (index > 0) {
						game.getWindow().getDisplayer().remove(panels[tmp]);
						index--;
						game.getWindow().getDisplayer().add(panels[index]);
					}
				}
				if (i.intValue() == KeyEvent.VK_ENTER) {
					if (index == 0) {
						game.getWindow().end();
						new Game().start(lastLevel);
						stop();

					}
					if (index == 1) {
						game.getWindow().end();
						new Launcher();
						stop();
					}
					if (index == 2) {
						saveGame();
						System.exit(0);
					}
				}
			}
		}

		if (game.getWindow() != null)
			game.getWindow().getDisplayer().refresh();
	}


	private void saveGame() {
		game.getData().save("Player", game.getPlayer().name);
		game.getData().save("Score", 100);
		game.getData().save("Player_id", game.getPlayer().id);
		game.getData().save("Player_level", game.getPlayer().level);
		game.getData().save("Resources", 1000);
	}

}
