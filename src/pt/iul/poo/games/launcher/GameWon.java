package pt.iul.poo.games.launcher;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import pt.iul.poo.games.DemoGame;
import pt.iul.poo.games.RunGame.Game;
import pt.iul.poo.games.player.Player;
import pt.iul.poo.games.systems.ScoreBoard;
import pt.iul.poo.image.SimpleImage;
import pt.iul.poo.image.properties.DisplayableElement;
import pt.iul.poo.image.properties.Displayer;

public class GameWon implements Runnable {

	private Thread thread;
	private SimpleImage[] panels;
	private boolean running = false;

	private int index = 0;

	private DemoGame game;
	private ScoreBoard score;
	private TreeSet<Player> classifications;

	public GameWon(DemoGame game) {
		this.game = game;
		init();
	}

	private void init() {
		try {
			panels = new SimpleImage[3];
			panels[0] = new SimpleImage("resources/images/gameWon/gameWon(menu).png", 0, 0);
			panels[1] = new SimpleImage("resources/images/gameWon/gameWon(play).png", 0, 0);
			panels[2] = new SimpleImage("resources/images/gameWon/gameWon(quit).png", 0, 0);

			index = 0;
			score = new ScoreBoard();
			classifications = score.getBest();
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

	private void resetMusic() {
		game.getTracks()[game.getMusicIndex()].stop();

	}

	private void clearPlayerInterface() {
		game.getWindow().getDisplayer().remove(game.getPlayerInterface());
	}

	public boolean isRunning() {
		return running;
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
		game.getWindow().getDisplayer().add(panels[0]);
		game.getWindow().getDisplayer().add(getScoreboard());

		while (running) {
			updatePanel();
		}
	}

	private void updatePanel() {
		Set<Integer> keyEvents = game.getWindow().getKeyEvents();
		Set<Point> mouseEvents = game.getWindow().getMouseEvents();

		if (!keyEvents.isEmpty() || !mouseEvents.isEmpty()) {
			int tmp = index;
			for (Integer i : keyEvents) {
				if (i.intValue() == KeyEvent.VK_DOWN) {
					if (index < 2) {
						game.getWindow().getDisplayer().remove(panels[tmp]);
						index++;
						game.getWindow().getDisplayer().add(panels[index]);
						game.getWindow().getDisplayer().add(getScoreboard());
					}
				}
				if (i.intValue() == KeyEvent.VK_UP) {
					if (index > 0) {
						game.getWindow().getDisplayer().remove(panels[tmp]);
						index--;
						game.getWindow().getDisplayer().add(panels[index]);
						game.getWindow().getDisplayer().add(getScoreboard());
					}
				}

				if (i.intValue() == KeyEvent.VK_ENTER) {
					if (index == 0) {
						resetMusic();
						game.getWindow().end();

						
						new Launcher();
						stop();
					}
					if (index == 1) {
						resetMusic();
						game.getWindow().end();
						
						new Game().start(0);
						stop();
					} else{
						System.exit(0);
					}

				}
			}
		}

		if (game.getWindow() != null)
			game.getWindow().getDisplayer().refresh();
	}

	private DisplayableElement getScoreboard() {
		DisplayableElement el = new DisplayableElement() {

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

				// Os tres melhores jogadores
				generateScoreboard(g);
			}
		};

		return el;

	}

	private void generateScoreboard(Graphics g) {
		int xOffSet = 130;
		int yOffSet = 500;

		g.setFont(new Font("Verdana", 1, 16));
		g.setColor(new Color(0x004CFF));

		int iterator = 0, position = 3;
		for (Player p : classifications) {
			g.drawString(position + "º " + p.toString(), xOffSet, yOffSet + iterator);
			iterator += 50;
			position--;
		}
	}

}
