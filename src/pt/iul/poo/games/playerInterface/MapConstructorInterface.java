package pt.iul.poo.games.playerInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import pt.iul.poo.games.MapConstructor;
import pt.iul.poo.games.launcher.Launcher;
import pt.iul.poo.games.systems.CustomTimer;
import pt.iul.poo.games.systems.ImageSaver;
import pt.iul.poo.image.SimpleImage;
import pt.iul.poo.image.properties.DisplayableElement;
import pt.iul.poo.image.properties.Displayer;
import pt.iul.poo.image.properties.GameWindowInterface;

public class MapConstructorInterface implements DisplayableElement {

	private boolean menu_sel = false;
	private CustomTimer mapTimer;

	private ImageSaver writter;

	private Rectangle interRec;
	private int tile_selected;

	private ImageIcon[] menu_button, musicBut, soundsBut, quitButton;
	private Rectangle but_Rec, quit_Rec;

	private ImageIcon road, end1, end2, end3, empty, arrow, tilesBackground;
	private Point[] positions;

	private MapConstructor mc;

	public MapConstructorInterface(MapConstructor mc) {
		this.mc = mc;

		init();
		setRectangles();
	}

	private void init() {
		writter = new ImageSaver();
		mapTimer = new CustomTimer(this, 1);

		initButtons();
		loadImages();

	}

	private void loadImages() throws NullPointerException {

		empty = new ImageIcon(getClass().getResource("/sprites/map/m_text.png"));
		end1 = new ImageIcon(getClass().getResource("/sprites/map/ending1.png"));
		end2 = new ImageIcon(getClass().getResource("/sprites/map/ending2.png"));
		end3 = new ImageIcon(getClass().getResource("/sprites/map/ending3.png"));
		road = new ImageIcon(getClass().getResource("/sprites/map/road.png"));
		arrow = new ImageIcon(getClass().getResource("/playerInterface/cursor.gif"));
		tilesBackground = new ImageIcon(getClass().getResource("/mapInterface/tilesBackground.png"));
	}

	private void initButtons() throws NullPointerException {

		// Botao do menu
		ImageIcon buttonP = new ImageIcon(getClass().getResource("/mapInterface/savepressed.png"));
		ImageIcon buttonU = new ImageIcon(getClass().getResource("/mapInterface/saveunpressed.png"));

		menu_button = new ImageIcon[2];
		menu_button[0] = buttonP;
		menu_button[1] = buttonU;

		// Botao de saida para o menu
		ImageIcon quitP = new ImageIcon(getClass().getResource("/mapInterface/quitpressed.png"));
		ImageIcon quitU = new ImageIcon(getClass().getResource("/mapInterface/quitunpressed.png"));

		quitButton = new ImageIcon[2];
		quitButton[0] = quitP;
		quitButton[1] = quitU;

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

		interRec = new Rectangle(0, 650, 1250, 140);
	}

	public int compareTo(DisplayableElement arg0) {
		return 0;
	}

	public void display(Graphics g, Displayer arg1) {

		try {

			g.drawImage(getInterfaceImage(), interRec.x, interRec.y, interRec.width, interRec.height, null);

			g.drawImage(tilesBackground.getImage(), 850 - 71, 682, 462, 89, null);
			drawButtons(g);
			drawSelectedTile(g);

		} catch (NullPointerException e) {
			System.err.println("MapConstructor - Imagem(ns) não encontrada(s)\nSaída abrupta do sistema");
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

	public void update(int index) {
		this.tile_selected = index;
	}

	private void drawSelectedTile(Graphics g) {

		int xOffSet = 825;
		int yOffSet = 700;

		Image[] tiles = { empty.getImage(), end1.getImage(), end2.getImage(), end3.getImage(), road.getImage() };
		positions = new Point[tiles.length];

		for (int i = 0; i < tiles.length; i++) {
			positions[i] = new Point(xOffSet, yOffSet);

			g.drawImage(tiles[i], xOffSet, yOffSet, 50, 50, null);
			xOffSet += 80;
		}

		g.setColor(new Color(0x1BA472));
		g.drawRect(positions[tile_selected - 1].x, positions[tile_selected - 1].y, 50, 50);

		g.drawImage(arrow.getImage(), positions[tile_selected - 1].x + 15, positions[tile_selected - 1].y + 10, 95, 95, null);

	}

	private void drawButtons(Graphics g) {

		but_Rec = new Rectangle(400, 700, 238, 76);
		quit_Rec = new Rectangle(132, 700, 238, 76);

		if (menu_sel) {
			g.drawImage(menu_button[0].getImage(), but_Rec.x, but_Rec.y, but_Rec.width, but_Rec.height, null);
		} else {
			g.drawImage(menu_button[1].getImage(), but_Rec.x, but_Rec.y, but_Rec.width, but_Rec.height, null);
		}

		g.drawImage(quitButton[1].getImage(), quit_Rec.x, quit_Rec.y, quit_Rec.width, quit_Rec.height, null);
	}

	public Rectangle getSaveBounds() {
		return but_Rec;
	}

	public Rectangle getQuitBounds() {
		return quit_Rec;
	}

	public void save() {

		String fileName = JOptionPane.showInputDialog(null, "Insira qual a posição do seu mapa: \n->Níveis previamente criados(" + new File("resources/levels/").listFiles().length
				+ ")\n->Última posição criada: " + (new File("resources/levels/").listFiles().length - 1), "Criação de Mapa", JOptionPane.QUESTION_MESSAGE);

		if (fileName != null) {

			if (fileName.equals(""))
				fileName = "level " + (new File("resources/levels/").listFiles().length + 1);

			int width = MapConstructor.MAP_SIZE[0];
			int height = MapConstructor.MAP_SIZE[1];

			int[] pixels = new int[width * height];

			// Desenha tiles vazias
			for (SimpleImage empty : mc.getVoidTiles()) {
				int xPos = empty.getPosition().x / 50;
				int yPos = empty.getPosition().y / 50;
				pixels[xPos + yPos * width] = 0xFFFF00FF;
			}

			// Desenha o caminho
			for (SimpleImage road : mc.getRoadTiles()) {
				int xPos = road.getPosition().x / 50;
				int yPos = road.getPosition().y / 50;
				pixels[xPos + yPos * width] = 0xFFFFFF00;
			}

			// Desenha end1
			for (SimpleImage end1 : mc.getEnd1Tiles()) {
				int xPos = end1.getPosition().x / 50;
				int yPos = end1.getPosition().y / 50;
				pixels[xPos + yPos * width] = 0xFF00FFFF;
			}

			// Desenha end2
			for (SimpleImage end2 : mc.getEnd2Tiles()) {
				int xPos = end2.getPosition().x / 50;
				int yPos = end2.getPosition().y / 50;
				pixels[xPos + yPos * width] = 0xFF7F3300;
			}

			// Desenha end3
			for (SimpleImage end3 : mc.getEnd3Tiles()) {
				int xPos = end3.getPosition().x / 50;
				int yPos = end3.getPosition().y / 50;
				pixels[xPos + yPos * width] = 0xFFFFB27F;
			}

			writter.save(pixels, fileName);
		}

	}

	public void quit() {
		mc.getTracks()[mc.getMusicIndex()].stop();
		mc.setRunning(false);
		mc.getWindow().end();
		mapTimer.cancel();

		new Launcher();
	}

	public GameWindowInterface getMapConsWindow() {
		return mc.getWindow();
	}

}
