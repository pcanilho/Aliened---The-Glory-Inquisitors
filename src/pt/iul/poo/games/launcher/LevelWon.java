package pt.iul.poo.games.launcher;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.swing.JOptionPane;

import pt.iul.poo.games.DemoGame;
import pt.iul.poo.games.RunGame.Game;
import pt.iul.poo.games.systems.SaveScore;
import pt.iul.poo.image.SimpleImage;
import pt.iul.poo.image.properties.DisplayableElement;
import pt.iul.poo.image.properties.Displayer;

public class LevelWon implements Runnable {

	private Thread thread;
	private SimpleImage[] panels;
	private DisplayableElement el;
	private boolean running = false;

	// Nível onde o jogador se encontra
	private DemoGame game;
	private int lastLevel;
	private SaveScore save;

	public static final int NUMBER_OF_MAPS = new File("resources/levels/").listFiles().length - 1;

	public LevelWon(DemoGame game) {
		this.game = game;
		init();
	}

	private void init() {
		try {
			// Nível onde o jogador se encontrava anteriormente
			lastLevel = game.getPlayer().level;

			panels = new SimpleImage[1];
			panels[0] = new SimpleImage("resources/images/levelWon/levelWon(pane)_2.png", 0, 0);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Menu - A imagem não foi encontrada", "Menu - Erro", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public void start() {
		thread = new Thread(this, "Menu");
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

	private void clearPlayerInterface() {
		game.getWindow().getDisplayer().remove(game.getPlayerInterface());
	}

	public void stop() {
		try {
			game.getWindow().getDisplayer().remove(el);

			thread.join();
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		game.getWindow().getDisplayer().add(panels[0]);
		game.getWindow().getDisplayer().add(getScore());
		cancelTimers();

		while (running) {
			updatePanel();
		}
	}

	private void updatePanel() {
		Set<Integer> keyEvents = game.getWindow().getKeyEvents();
		Set<Point> mouseEvents = game.getWindow().getMouseEvents();

		if (!keyEvents.isEmpty() || !mouseEvents.isEmpty()) {

			for (Integer i : keyEvents) {
				if (i.intValue() == KeyEvent.VK_ENTER) {

					if (lastLevel == NUMBER_OF_MAPS) {
						// saveGame();
						saveScore();
						resetMusic();

						new GameWon(game).start();
						stop();
					} else {
						// saveGame();
						game.getWindow().end();
						saveScore();
						resetMusic();
						new Game().start(lastLevel + 1);
						stop();
					}
				}
			}
		}

		if (game.getWindow() != null)
			game.getWindow().getDisplayer().refresh();
	}

	private void resetMusic() {
		game.getTracks()[game.getMusicIndex()].stop();

	}

	private void saveScore() {
		save = new SaveScore();
		save.save(game.getPlayer());
	}

	private void cancelTimers() {
		for (int tm = 0; tm < game.getTimers().length; tm++)
			game.getTimers()[tm].cancel();

	}

	private DisplayableElement getScore() {
		el = new DisplayableElement() {

			public int compareTo(DisplayableElement arg0) {
				return 0;
			}

			public boolean isValid() {
				return true;
			}

			public int getPriority() {
				return 2;
			}

			public void display(Graphics g, Displayer d) {
				int xOffSet = 200;
				int yOffSet = 500;

				g.setFont(new Font("Verdana", 1, 18));
				g.setColor(new Color(0x004CFF));

				// Desenha o nome do jogador
				g.drawString("* Jogador: " + game.getPlayer().name, xOffSet, yOffSet);

				// Desenha a pontuação do jogador
				g.drawString("* Pontuação: " + game.getPlayer().score, xOffSet, yOffSet + 50);

				// Desenha os recursos restantes do jogador
				g.drawString("* Recursos: " + game.getPlayer().resources, xOffSet, yOffSet + 100);

				// Desenha os invasores totais mortos
				g.drawString("* Invasores mortos: " + game.getPlayer().invaders_killed, xOffSet, yOffSet + 150);

			}
		};

		return el;

	}

}
