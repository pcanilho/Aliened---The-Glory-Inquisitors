package pt.iul.poo.games;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JOptionPane;

import pt.iul.poo.games.entity.mob.Invader;
import pt.iul.poo.games.entity.mob.Mob;
import pt.iul.poo.games.entity.mob.Tower;
import pt.iul.poo.games.launcher.GameOver;
import pt.iul.poo.games.launcher.LevelWon;
import pt.iul.poo.games.launcher.Menu;
import pt.iul.poo.games.level.CustomLevel;
import pt.iul.poo.games.level.Level;
import pt.iul.poo.games.player.Player;
import pt.iul.poo.games.player.data.Data;
import pt.iul.poo.games.playerInterface.PlayerInterface;
import pt.iul.poo.games.systems.CustomTimer;
import pt.iul.poo.games.systems.SoundLoader;
import pt.iul.poo.image.MovingImage;
import pt.iul.poo.image.properties.GameWindowInterface;
import pt.iul.poo.shapes.SimpleShape;

public class DemoGame {

	private static final double RANGE = 100;
	private GameWindowInterface window;
	private boolean running = false;

	private int time;

	// Tipo de torre seleccionada
	private int tower_sel = 1;

	// Sistema de carregamento das informacoes do jogador
	private Data player_data;
	private Player player;

	// Sistema sonoro
	private SoundLoader[] tracks;
	private int music_index;

	// Sistemas baseados em tempo
	private CustomTimer inv_timer, time_timer, msg_timer;

	// Menu
	private Menu in_menu;
	public boolean inMenu = false;

	// Interface do jogador
	private PlayerInterface p_interface;
	private boolean menu_sel, music_sel, sounds_sel;

	// Game Over
	public boolean gameOver = false;
	private GameOver g_over;

	// Nível ultrapassado
	public boolean creepsFlushed = false;
	private boolean levelWon = false;
	private LevelWon l_won;

	// Listas dos "displayable objects"
	private List<SimpleShape> shot;
	

	// Nível onde se encontra o jogador
	private Level level;
	private int l_index;

	public DemoGame(GameWindowInterface window, int l_index) {
		this.window = window;
		this.l_index = l_index;
	}

	private void init() {
		shot = new LinkedList<SimpleShape>();
		

		player_data = new Data();
		player = player_data.getPlayer();
		player.level = l_index;

		p_interface = new PlayerInterface(player);

		load_StartMusic();
		fakeInit();
	}

	private void load_StartMusic() {
		tracks = new SoundLoader[6];

		tracks[0] = SoundLoader.track1;
		tracks[1] = SoundLoader.track2;
		tracks[2] = SoundLoader.track3;
		tracks[3] = SoundLoader.track4;
		tracks[4] = SoundLoader.track5;
		tracks[5] = SoundLoader.track6;

		loadRandomMusic();
	}

	private void loadRandomMusic() {
		Random r = new Random();

		// Parar a musica que está a decorrer
		if (tracks[music_index].isRunning())
			tracks[music_index].stop();

		// Assegura que a proxima musica nao seja repetida
		int tmp = music_index;
		music_index = r.nextInt(tracks.length);
		if (music_index == tmp)
			loadRandomMusic();

		new CustomTimer(10, "Música: " + tracks[music_index].getTitle() + " carregada", window);

		tracks[music_index].start(false);
	}

	private void fakeInit() {
		// Cria o nível em que o jogador se encontra
		createLevel();

		// Desenha a interface do jogador
		drawInterface();

		// Inicia o relógio
		startClock();
	}

	private void createLevel() {
		level = new CustomLevel("/levels/level_" + l_index + ".png", "Nível" + player.level, window, player.level);
		JOptionPane.showMessageDialog(null, "Nível: " + player.level + " ,Invasores: " + level.getNumInvaders(), "Nível " + player.level, JOptionPane.INFORMATION_MESSAGE);
	}

	private void drawInterface() {
		window.getDisplayer().add(p_interface);
	}

	private void startClock() {

		// Temporizador dos invasores
		inv_timer = new CustomTimer(level, this, 4);

		// Temporizador usado para a manutenção do tempo
		time_timer = new CustomTimer(this);

		// Temporizador usado para fazer "refresh" a mensagem
		msg_timer = new CustomTimer(this, 2);

		// Tempo ao inicio
		time = 0;
	}

	public void run() {
		init();
		window.start();
		running = true;

		while (!gameOver) {
			update();

			if (running) {
				Set<Integer> keyEvents = window.getKeyEvents();
				Set<Point> mouseEvents = window.getMouseEvents();

				if (!keyEvents.isEmpty() || !mouseEvents.isEmpty()) {

					for (Integer i : keyEvents) {
						selectTower(i);

						if (i.intValue() == KeyEvent.VK_Q) {
							exitPane();
						}
						if (i.intValue() == KeyEvent.VK_ESCAPE) {
							startMenu();
						}
					}
					for (Point p : mouseEvents) {
						level.selectInvader(p);
						interactiveInterface(p);

						if (!level.isTowerInside(p.x, p.y)) {
							addTower(p.x, p.y);
						} else {
							removeTower(p.x, p.y);
						}
					}
				}
				window.getDisplayer().refresh();
			}

			checkIfGameOver();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean music_clicked = false, sounds_clicked;

	private void interactiveInterface(Point p) {

		// Interage com o botao do menu
		if (p.x >= p_interface.getMenuBounds().x && p.x <= p_interface.getMenuBounds().x + p_interface.getMenuBounds().width && p.y >= p_interface.getMenuBounds().y
				&& p.y <= p_interface.getMenuBounds().y + p_interface.getMenuBounds().height) {
			menu_sel = true;
			startMenu();
		} else {
			menu_sel = false;
		}

		// Interage com o botao da musica
		if (p.x >= p_interface.getMusicBounds().x && p.x <= p_interface.getMusicBounds().x + p_interface.getMusicBounds().width && p.y >= p_interface.getMusicBounds().y
				&& p.y <= p_interface.getMusicBounds().y + p_interface.getMusicBounds().height) {
			// music_sel = true;
			if (tracks[music_index].isRunning() && !music_clicked) {
				tracks[music_index].mute();
				music_sel = true;
				music_clicked = true;
			} else {
				tracks[music_index].unmute();
				music_sel = false;
				music_clicked = false;
			}
		}
		// Interage com o botao dos sons
		if (p.x >= p_interface.getSoundBounds().x && p.x <= p_interface.getSoundBounds().x + p_interface.getSoundBounds().width && p.y >= p_interface.getSoundBounds().y
				&& p.y <= p_interface.getSoundBounds().y + p_interface.getSoundBounds().height) {
			if (tracks[music_index].isRunning() && !sounds_clicked) {
				muteTowers();
				muteInvaders();
				sounds_sel = true;
				sounds_clicked = true;
			} else {
				unmuteTowers();
				unmuteInvaders();
				sounds_sel = false;
				sounds_clicked = false;
			}
		}

		// Interage com o botao do radar
		if (p.x >= p_interface.getRadarBounds().x && p.x <= p_interface.getRadarBounds().x + p_interface.getRadarBounds().width && p.y >= p_interface.getRadarBounds().y
				&& p.y <= p_interface.getRadarBounds().y + p_interface.getRadarBounds().height) {

		}

		// Interage com o botao da proxima musica
		if (p.x >= p_interface.getNextMusicBounds().x && p.x <= p_interface.getNextMusicBounds().x + p_interface.getNextMusicBounds().width && p.y >= p_interface.getNextMusicBounds().y
				&& p.y <= p_interface.getNextMusicBounds().y + p_interface.getNextMusicBounds().height) {
			loadRandomMusic();
		}
	}

	private void unmuteInvaders() {
		for (Invader inv : level.getInvaders())
			inv.unmute();
	}

	private void unmuteTowers() {
		for (Tower tw : level.getTowers())
			tw.unmute();
	}

	private void muteInvaders() {
		for (Invader inv : level.getInvaders())
			inv.mute();
	}

	private void muteTowers() {
		for (Tower tw : level.getTowers())
			tw.mute();
	}

	private void update() {
		checkIfWon();

		moveCreeps();
		towerShots();
		refreshAnimations();
		refreshPlayerInterface();
	}

	private void refreshAnimations() {
		level.update();
		
	}

	private void refreshPlayerInterface() {
		p_interface.update(tower_sel, menu_sel, music_sel, sounds_sel);
	}

	private void exitPane() {
		int answer = JOptionPane.showConfirmDialog(null, "Deseja sair e guardar?", "Sair", JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			player_data.save("Score", player.score);
			player_data.save("Player_level", player.level);
			System.exit(0);
		} else
			System.exit(0);
	}

	private void startMenu() {
		inMenu = true;
		in_menu = new Menu(this);
		in_menu.start();
	}

	private void checkIfWon() {
		if (level.getInvaders().size() == 0 && creepsFlushed) {
			levelWon = true;
			gameOver = true;
		}

		if (levelWon) {
			l_won = new LevelWon(this);
			l_won.start();
		}

	}

	private void checkIfGameOver() {
		if (player.score == 0)
			gameOver = true;

		if (gameOver && !levelWon) {
			g_over = new GameOver(this);
			g_over.start();
		}
	}

	private void selectTower(Integer i) {
		if (i.intValue() == KeyEvent.VK_1) {
			tower_sel = 1;
			window.setMessage("Torre tipo 1 seleccionada , custo: " + 100);
		}

		if (i.intValue() == KeyEvent.VK_2) {
			tower_sel = 2;
			window.setMessage("Torre tipo 2 seleccionada, custo: " + 250);
		}
		if (i.intValue() == KeyEvent.VK_3) {
			tower_sel = 3;
			window.setMessage("Torre tipo 3 seleccionada custo: " + 400);
		}
	}

	public void addTower(int x_pos, int y_pos) {
		Point tmp = level.getVoidTile(x_pos, y_pos, this);

		if (tmp != null) {
			Tower newTower = new Tower(this, tmp.x, tmp.y, tower_sel);

			if (player.resources >= newTower.getPrice()) {

				level.getTowers().add(newTower);
				window.getDisplayer().add(newTower.getImage());
				window.setMessage("Torre criada com sucesso na posição - x: " + newTower.getImage().getPosition().x + " ,y: " + newTower.getImage().getPosition().y);

				player.setResources(newTower.getPrice());
			} else
				window.setMessage("Recursos insuficientes para contruir a torre tipo: " + newTower.getType() + " - [" + player.resources + "/" + newTower.getPrice() + "]");
		}
	}

	public void removeTower(int x_pos, int y_pos) {
		if (level.getTowerInside(x_pos, y_pos, tower_sel) != null && !level.getTowerInside(x_pos, y_pos, tower_sel).isFiring()) {

			player.setResources(-level.getTowerInside(x_pos, y_pos, tower_sel).getPrice() / 2);

			level.getTowerInside(x_pos, y_pos, tower_sel).destroy();
			level.getTowers().remove(level.getTowerInside(x_pos, y_pos, tower_sel));
		} else {
			window.setMessage("O tipo de torre existente não é o mesmo que o seleccionado");
		}
	}

	public void addInvader() {
		Random r = new Random();
		Mob newInvader = new Invader(level.getStartLocation().x, level.getStartLocation().y, this, r.nextInt(3));

		level.getInvaders().add((Invader) newInvader);
		window.getDisplayer().add(((Invader) newInvader).getImage());
	}

	private synchronized void towerShots() {
		for (SimpleShape s : shot) {
			if (shot != null)
				window.getDisplayer().remove(s);
		}

		// Se a vida do invasor for menor que 0, este e removido da lista e do
		// ecran
		checkStatus();
		resetShot();

		if (!inMenu) {
			for (Tower tower : level.getTowers()) {
				for (Invader i : level.getInvaders()) {
					if (Math.abs(tower.getImage().getPosition().distance(i.getImage().getPosition())) < RANGE) {
						drawTowerShot(tower, i.getImage());
						soundSystem(tower);
						if (!level.getInvaders().isEmpty()) {
							i.setLife(tower.getAtkDmg());
						}
					}
				}
			}
		}
	}

	private void soundSystem(Tower tower) {
		tower.getSound().start(false);

		if (!tower.getSound().isRunning()) {
			tower.getSound().stop();
			tower.getSound().start(false);
		}
	}

	private void checkStatus() {
		LinkedList<Invader> tmp = new LinkedList<Invader>();

		for (Invader inv : level.getInvaders()) {
			if (inv.getLife() <= 0) {
				tmp.add(inv);
				inv.destroy();
				player.invaders_killed++;
			}
		}
		level.getInvaders().removeAll(tmp);
	}

	private void resetShot() {
		for (Tower tower : level.getTowers()) {
			tower.setFiring(false);
		}
		shot.removeAll(shot);
	}

	private void drawTowerShot(Tower tower, MovingImage x) {

		// Necessaria a implementacao de diferentes blocos devido a diferenca de
		// posicoes nos disparos

		if (tower.getType() == 1) {
			tower.setFiring(true);
			SimpleShape s = new SimpleShape(new Line2D.Double(tower.getImage().getPosition().getX() + tower.getImage().getDimension().width / 2, tower.getImage().getPosition().getY()
					+ tower.getImage().getDimension().height / 2 - 17, x.getPosition().getX() + x.getDimension().width / 2, x.getPosition().getY() + x.getDimension().height / 2), tower.getColor(), 50);

			shot.add(s);
			window.getDisplayer().add(s);
		}

		if (tower.getType() == 2) {
			tower.setFiring(true);
			SimpleShape s = new SimpleShape(new Line2D.Double(tower.getImage().getPosition().getX() + tower.getImage().getDimension().width / 2, tower.getImage().getPosition().getY()
					+ tower.getImage().getDimension().height / 2 + 8, x.getPosition().getX() + x.getDimension().width / 2, x.getPosition().getY() + x.getDimension().height / 2), tower.getColor(), 50);

			SimpleShape s2 = new SimpleShape(new Line2D.Double(tower.getImage().getPosition().getX() + tower.getImage().getDimension().width / 2, tower.getImage().getPosition().getY()
					+ tower.getImage().getDimension().height / 2 - 17, x.getPosition().getX() + x.getDimension().width / 2, x.getPosition().getY() + x.getDimension().height / 2), tower.getColor(), 50);

			shot.add(s2);
			window.getDisplayer().add(s2);

			shot.add(s);
			window.getDisplayer().add(s);
		}

		if (tower.getType() == 3) {
			tower.setFiring(true);
			SimpleShape s = new SimpleShape(new Line2D.Double(tower.getImage().getPosition().getX() + tower.getImage().getDimension().width / 2 - 5, tower.getImage().getPosition().getY()
					+ tower.getImage().getDimension().height / 2, x.getPosition().getX() + x.getDimension().width / 2, x.getPosition().getY() + x.getDimension().height / 2), tower.getColor(), 50);
			SimpleShape s2 = new SimpleShape(new Line2D.Double(tower.getImage().getPosition().getX() + tower.getImage().getDimension().width / 2, tower.getImage().getPosition().getY()
					+ tower.getImage().getDimension().height / 2 - 15, x.getPosition().getX() + x.getDimension().width / 2, x.getPosition().getY() + x.getDimension().height / 2), tower.getColor(), 50);
			SimpleShape s3 = new SimpleShape(new Line2D.Double(tower.getImage().getPosition().getX() + tower.getImage().getDimension().width / 2 - 8, tower.getImage().getPosition().getY()
					+ tower.getImage().getDimension().height / 2, x.getPosition().getX() + x.getDimension().width / 2, x.getPosition().getY() + x.getDimension().height / 2), tower.getColor(), 50);

			shot.add(s3);
			window.getDisplayer().add(s3);

			shot.add(s2);
			window.getDisplayer().add(s2);

			shot.add(s);
			window.getDisplayer().add(s);
		}
	}

	

	private void moveCreeps() {
		if (!inMenu) {
			for (int i = 0; i < level.getInvaders().size(); i++) {
				level.getInvaders().get(i).update();
			}
		}
	}

	public Menu getMenu() {
		return in_menu;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int i) {
		time += i;
	}

	public CustomTimer[] getTimers() {
		CustomTimer[] timers = new CustomTimer[3];

		timers[0] = inv_timer;
		timers[1] = time_timer;
		timers[2] = msg_timer;

		return timers;
	}

	public Player getPlayer() {
		return player;
	}


	public Data getData() {
		return player_data;
	}

	public GameWindowInterface getWindow() {
		return window;
	}

	public Level getLevel() {
		return level;
	}

	public int getSelectedTower() {
		return tower_sel;
	}

	public void setMenuSel(boolean value) {
		this.menu_sel = value;
	}

	public PlayerInterface getPlayerInterface() {
		return p_interface;
	}

	public int getMusicIndex() {
		return music_index;
	}

	public SoundLoader[] getTracks() {
		return tracks;
	}

	public void setRunning(boolean value) {
		this.running = value;
	}
}
