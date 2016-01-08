package pt.iul.poo.games.playerInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import pt.iul.poo.games.player.Player;
import pt.iul.poo.image.properties.DisplayableElement;
import pt.iul.poo.image.properties.Displayer;

public class PlayerInterface implements DisplayableElement {

	private Player player;
	private int sel_tower;
	private boolean menu_sel = false, musicBut_sel = false, soundsBut_sel;
	private Rectangle soundsRec, musicRec, interRec, radarRec;
	private Point[] tower_positions;

	// Imagens de torres
	private ImageIcon tower_1, tower1_price;
	private ImageIcon tower_2, tower2_price;
	private ImageIcon tower_3, tower3_price;

	// Imagens extra
	private ImageIcon text;
	private ImageIcon arrow;
	private ImageIcon inv_killed;
	private ImageIcon resources;
	private ImageIcon score;
	private ImageIcon name;
	private ImageIcon animation, radar_frame;
	private ImageIcon nextMusic;

	private ImageIcon[] menu_button, musicBut, soundsBut;
	private Rectangle but_Rec, nextMusic_Rec;

	public PlayerInterface(Player player) {
		this.player = player;

		init();
		setRectangles();
	}

	private void init() throws NullPointerException {

		tower_1 = new ImageIcon(getClass().getResource("/sprites/towers/tower1.png"));
		tower1_price = new ImageIcon(getClass().getResource("/playerInterface/prices/tower1_price.png"));

		tower_2 = new ImageIcon(getClass().getResource("/sprites/towers/tower2.png"));
		tower2_price = new ImageIcon(getClass().getResource("/playerInterface/prices/tower2_price.png"));

		tower_3 = new ImageIcon(getClass().getResource("/sprites/towers/tower3.png"));
		tower3_price = new ImageIcon(getClass().getResource("/playerInterface/prices/tower3_price.png"));

		arrow = new ImageIcon(getClass().getResource("/playerInterface/cursor.gif"));
		text = new ImageIcon(getClass().getResource("/playerInterface/selctedText.png"));
		inv_killed = new ImageIcon(getClass().getResource("/playerInterface/inv_killed.png"));
		resources = new ImageIcon(getClass().getResource("/playerInterface/resources.png"));
		score = new ImageIcon(getClass().getResource("/playerInterface/score.png"));
		name = new ImageIcon(getClass().getResource("/playerInterface/player.png"));

		animation = new ImageIcon(getClass().getResource("/playerInterface/nod(p).e.gif"));
		radar_frame = new ImageIcon(getClass().getResource("/playerInterface/radar_frame.png"));

		nextMusic = new ImageIcon(getClass().getResource("/playerInterface/nextMusic.png"));

		initButtons();

	}

	private void initButtons() throws NullPointerException {

		// Botao do menu
		ImageIcon buttonP = new ImageIcon(getClass().getResource("/playerInterface/menupressed.png"));
		ImageIcon buttonU = new ImageIcon(getClass().getResource("/playerInterface/menuUnpressed.png"));

		menu_button = new ImageIcon[2];
		menu_button[0] = buttonP;
		menu_button[1] = buttonU;

		// Botao da musica
		ImageIcon musicP = new ImageIcon(getClass().getResource("/playerInterface/mpressed.png"));
		ImageIcon musicU = new ImageIcon(getClass().getResource("/playerInterface/upressed.png"));

		musicBut = new ImageIcon[2];
		musicBut[0] = musicU;
		musicBut[1] = musicP;

		// Botao dos sons
		ImageIcon soundsP = new ImageIcon(getClass().getResource("/playerInterface/mpressed.png"));
		ImageIcon soundsU = new ImageIcon(getClass().getResource("/playerInterface/upressed.png"));

		soundsBut = new ImageIcon[2];
		soundsBut[0] = soundsU;
		soundsBut[1] = soundsP;

	}

	private void setRectangles() {

		but_Rec = new Rectangle(400, 700, 238, 76);
		soundsRec = new Rectangle(800, 660, 60, 59);
		interRec = new Rectangle(0, 650, 1250, 140);
		musicRec = new Rectangle(800, 720, 60, 59);
		nextMusic_Rec = new Rectangle(870, 720, 60, 59);
	}

	public int compareTo(DisplayableElement arg0) {
		return 0;
	}

	public void display(Graphics g, Displayer arg1) {

		try {
			g.drawImage(getInterfaceImage(), interRec.x, interRec.y, interRec.width, interRec.height, null);

			drawTowers(g);
			drawTextImages(g);
			drawSelectedTower(g, sel_tower);
			drawButtons(g);
			drawRadar(g);

		} catch (NullPointerException e) {
			System.err.println("PlayerInterface - Imagem(ns) não encontrada(s)\nSaída abrupta do sistema");
			e.printStackTrace();
			System.exit(1);
		}

	}

	public int getPriority() {
		return 0;
	}

	public boolean isValid() {
		return true;
	}

	private Image getInterfaceImage() throws NullPointerException {
		ImageIcon image = new ImageIcon(getClass().getResource("/playerInterface/interface.png"));

		return image.getImage();

	}

	public void update(int index, boolean menu_sel, boolean music_sel, boolean sounds_sel) {
		sel_tower = index;
		this.menu_sel = menu_sel;
		this.soundsBut_sel = sounds_sel;
		this.musicBut_sel = music_sel;
	}

	private void drawTowers(Graphics g) {
		int xOffSet = 950;
		int yOffSet = 700;

		Image[] images = { tower_1.getImage(), tower_2.getImage(), tower_3.getImage() };
		Image[] prices = { tower1_price.getImage(), tower2_price.getImage(), tower3_price.getImage() };

		tower_positions = new Point[images.length];

		for (int i = 0; i < images.length; i++) {
			tower_positions[i] = new Point(xOffSet, yOffSet);

			g.drawImage(images[i], xOffSet, yOffSet, 50, 50, null);
			g.drawImage(prices[i], xOffSet, yOffSet + 45, 50, 31, null);
			xOffSet += 80;
		}
	}

	private void drawTextImages(Graphics g) {
		int xOffSet = 880;
		int yOffSet = 660;

		g.setFont(new Font("Verdana", 1, 19));
		g.setColor(new Color(0xB9FDFF));

		g.drawImage(text.getImage(), xOffSet, yOffSet, 356, 40, null);

		g.drawImage(inv_killed.getImage(), xOffSet / 30, yOffSet, 328, 40, null);
		g.drawString("" + player.invaders_killed, xOffSet / 30 + 328 + 10, yOffSet + 25);

		g.drawImage(resources.getImage(), xOffSet / 30, yOffSet + 30, 170, 40, null);
		g.drawString("" + player.resources, xOffSet / 30 + 170 + 10, yOffSet + 55);

		g.drawImage(score.getImage(), xOffSet / 30, yOffSet + 60, 206, 40, null);
		g.drawString("" + player.score, xOffSet / 30 + 206 + 10, yOffSet + 85);

		g.drawImage(name.getImage(), xOffSet / 30, yOffSet + 90, 170, 40, null);
		g.drawString("" + player.name, xOffSet / 30 + 170 + 5, yOffSet + 115);

	}

	private void drawRadar(Graphics g) {
		radarRec = new Rectangle(880 / 32 - 21, 660 - 109, 89, 89);
		g.drawImage(radar_frame.getImage(), radarRec.x - 11, radarRec.y - 7, radarRec.width + 18, radarRec.height + 18, null);
		g.drawImage(animation.getImage(), radarRec.x, radarRec.y, radarRec.width, radarRec.height, null);
	}

	private void drawSelectedTower(Graphics g, int index) {
		g.setColor(new Color(0x1BA472));
		g.drawRect(tower_positions[index - 1].x, tower_positions[index - 1].y, 50, 50);
		g.drawImage(arrow.getImage(), tower_positions[index - 1].x + 15, tower_positions[index - 1].y + 10, 95, 95, null);
	}

	private void drawButtons(Graphics g) {

		if (menu_sel) {
			g.drawImage(menu_button[0].getImage(), but_Rec.x, but_Rec.y, but_Rec.width, but_Rec.height, null);
		} else {
			g.drawImage(menu_button[1].getImage(), but_Rec.x, but_Rec.y, but_Rec.width, but_Rec.height, null);
		}

		if (musicBut_sel) {
			g.drawImage(musicBut[0].getImage(), musicRec.x, musicRec.y, musicRec.width, musicRec.height, null);
		} else {
			g.drawImage(musicBut[1].getImage(), musicRec.x, musicRec.y, musicRec.width, musicRec.height, null);
		}

		if (soundsBut_sel) {
			g.drawImage(soundsBut[0].getImage(), soundsRec.x, soundsRec.y, soundsRec.width, soundsRec.height, null);
		} else {
			g.drawImage(soundsBut[1].getImage(), soundsRec.x, soundsRec.y, soundsRec.width, soundsRec.height, null);
		}

		g.drawImage(nextMusic.getImage(), nextMusic_Rec.x, nextMusic_Rec.y, nextMusic_Rec.width, nextMusic_Rec.height, null);
	}

	public Rectangle getMenuBounds() {
		return but_Rec;
	}

	public Rectangle getSoundBounds() {
		return soundsRec;
	}

	public Rectangle getMusicBounds() {
		return musicRec;
	}

	public Rectangle getRadarBounds() {
		return radarRec;
	}

	public Rectangle getNextMusicBounds() {
		return nextMusic_Rec;
	}

}
