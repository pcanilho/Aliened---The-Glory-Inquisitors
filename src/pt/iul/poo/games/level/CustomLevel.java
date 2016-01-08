package pt.iul.poo.games.level;

import java.awt.Point;
import java.io.IOException;
import java.util.LinkedList;

import pt.iul.poo.games.DemoGame;
import pt.iul.poo.games.entity.mob.Invader;
import pt.iul.poo.games.entity.mob.Tower;
import pt.iul.poo.games.entity.mob.assets.Animation;
import pt.iul.poo.games.systems.ImageLoader;
import pt.iul.poo.image.SimpleImage;
import pt.iul.poo.image.properties.GameWindowInterface;

public class CustomLevel extends Level {

	
	public CustomLevel(String path, String name, GameWindowInterface window, int id) {
		super(path, name, window, id);

	}

	public void loadLevel(String path) {
		map = new ImageLoader(path);
		setAtributes();

		animations = new LinkedList<Animation>();

	}

	private void refreshAnimations() {
		for (int i = 0; i < animations.size(); i++) {
			if (animations.get(i).getTime() > 1) {
				animations.get(i).destroy();
				animations.remove(animations.get(i));
			} else
				animations.get(i).update();
		}
	}

	/*
	 * Branco = vazio , Vermelho = Torre, Amarelo = caminho, Azul = chegada
	 */

	private void setAtributes() {
		switch (id) {
		case 0:
			num_invaders = 10;
			break;
		case 1:
			num_invaders = 14;
			break;
		case 2:
			num_invaders = 18;
			break;
		case 3:
			num_invaders = 25;
			break;
		case 4:
			num_invaders = 30;
			break;
		case 5:
			num_invaders = 35;
			break;
		case 6:
			num_invaders = 40;
			break;
		case 7:
			num_invaders = 50;
			break;
		case 8:
			num_invaders = 55;
			break;
		case 9:
			num_invaders = 60;
			break;
		case 10:
			num_invaders = 65;
			break;
		}

	}

	public void generateMap() {
		// Desenha todos os outros objectos
		for (SimpleImage objects : ending) {
			window.getDisplayer().add(objects);
		}

		// Desenha espacos vazios
		for (SimpleImage voids : void_tiles) {
			window.getDisplayer().add(voids);
		}

		for (SimpleImage paths : path_1) {
			window.getDisplayer().add(paths);
		}
	}
	
	public LinkedList<Animation> getAnimations(){
		return animations;
	}

	// O mapa escala 50x (dimensao de cada imagem) segundo a imagem orginal

	public void generateLevel() {
		createLists();

		try {
			for (int y = 0; y < map.getHeight(); y++) {
				for (int x = 0; x < map.getWidth(); x++) {
					if (map.getPixels()[x + y * map.getWidth()] == 0xFFFF00FF)
						void_tiles.add(new SimpleImage("resources/sprites/map/m_text.png", x * window.getDefaultElementWidth(), y * window.getDefaultElementHeight()));
					if (map.getPixels()[x + y * map.getWidth()] == 0xFF00FFFF)
						ending.add(new SimpleImage("resources/sprites/map/ending1.png", x * window.getDefaultElementWidth(), y * window.getDefaultElementHeight()));
					if (map.getPixels()[x + y * map.getWidth()] == 0xFF7F3300)
						ending.add(new SimpleImage("resources/sprites/map/ending2.png", x * window.getDefaultElementWidth(), y * window.getDefaultElementHeight()));
					if (map.getPixels()[x + y * map.getWidth()] == 0xFFFFB27F)
						ending.add(new SimpleImage("resources/sprites/map/ending3.png", x * window.getDefaultElementWidth(), y * window.getDefaultElementHeight()));
					if (map.getPixels()[x + y * map.getWidth()] == 0xFFFFFF00)
						path_1.add(new SimpleImage("resources/sprites/map/road.png", x * window.getDefaultElementWidth(), y * window.getDefaultElementHeight()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void createLists() {
		towers = new LinkedList<Tower>();
		void_tiles = new LinkedList<SimpleImage>();
		ending = new LinkedList<SimpleImage>();
		invaders = new LinkedList<Invader>();
		path_1 = new LinkedList<SimpleImage>();
	}

	public void generateInvaders() {

		start_x = path_1.getFirst().getPosition().x;
		start_y = path_1.getFirst().getPosition().y;

	}

	public Point getVoidTile(int x_pos, int y_pos, DemoGame game) {
		Point tmp = null;
		for (SimpleImage v : void_tiles) {
			if (x_pos >= v.getPosition().x && x_pos <= (v.getPosition().x + v.getDimension().width) && y_pos >= v.getPosition().y && y_pos <= (v.getPosition().y + v.getDimension().height)) {
				tmp = new Point(v.getPosition().x, v.getPosition().y);
			}
		}

		if (x_pos >= game.getPlayerInterface().getRadarBounds().x && x_pos <= game.getPlayerInterface().getRadarBounds().x + game.getPlayerInterface().getRadarBounds().width
				&& y_pos >= game.getPlayerInterface().getRadarBounds().y && y_pos <= game.getPlayerInterface().getRadarBounds().y + game.getPlayerInterface().getRadarBounds().height) {
			showTowerTooltips(game);

			tmp = null;
		}

		return tmp;
	}

	private boolean clicked = false;

	private void showTowerTooltips(DemoGame game) {
		if (!clicked) {
			for (Tower tw : towers) {
				tw.update(true);
				clicked = true;
			}
		} else if (clicked) {
			for (Tower tw : towers) {
				tw.update(false);
				clicked = false;
			}
		}
	}

	public boolean isTowerInside(int x_pos, int y_pos) {
		for (Tower t : towers) {
			if (x_pos >= t.getImage().getPosition().x && x_pos <= (t.getImage().getPosition().x + t.getImage().getDimension().width) && y_pos >= t.getImage().getPosition().y
					&& y_pos <= (t.getImage().getPosition().y + t.getImage().getDimension().height)) {
				return true;
			}
		}
		return false;
	}

	public Tower getTowerInside(int x_pos, int y_pos, int type) {
		for (Tower t : towers) {
			if (x_pos >= t.getImage().getPosition().x && x_pos <= (t.getImage().getPosition().x + t.getImage().getDimension().width) && y_pos >= t.getImage().getPosition().y
					&& y_pos <= (t.getImage().getPosition().y + t.getImage().getDimension().height) && type == t.getType()) {
				return t;
			}
		}
		return null;
	}

	public void selectInvader(Point p) {
		for (Invader inv : invaders) {
			if (p.x >= inv.getImage().getPosition().x && p.x <= inv.getImage().getPosition().x + inv.getImage().getDimension().width && p.y >= inv.getImage().getPosition().y
					&& p.y <= inv.getImage().getPosition().y + inv.getImage().getDimension().height) {
				inv.setVisibleTooltip(true);
			} else
				inv.setVisibleTooltip(false);
		}

	}

	public Point getStartLocation() {
		Point tmp = new Point(start_x, start_y);
		return tmp;
	}

	public int getId() {
		return id;
	}

	public int getNumInvaders() {
		return num_invaders;
	}

	public LinkedList<Invader> getInvaders() {
		return invaders;
	}

	public LinkedList<Tower> getTowers() {
		return towers;
	}
	
	public void update(){
		refreshAnimations();
	}

}
