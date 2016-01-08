package pt.iul.poo.games.level;

import java.awt.Point;
import java.util.LinkedList;

import pt.iul.poo.games.DemoGame;
import pt.iul.poo.games.entity.mob.Invader;
import pt.iul.poo.games.entity.mob.Tower;
import pt.iul.poo.games.entity.mob.assets.Animation;
import pt.iul.poo.games.systems.ImageLoader;
import pt.iul.poo.image.SimpleImage;
import pt.iul.poo.image.properties.GameWindowInterface;

public abstract class Level {

	public LinkedList<SimpleImage> void_tiles;
	public LinkedList<SimpleImage> ending;
	public LinkedList<SimpleImage> path_1;
	protected LinkedList<Tower> towers;
	protected LinkedList<Invader> invaders;
	protected LinkedList<Animation> animations;

	public String log;

	protected boolean canCreate;
	protected String name;
	protected int numberOfMaps;
	protected int num_invaders;
	protected GameWindowInterface window;
	protected int id;
	protected int start_x, start_y;

	public ImageLoader map;

	public Level(String path, String name, GameWindowInterface window, int id) {
		this.name = name;
		this.window = window;
		this.id = id;

		loadLevel(path);
		generateLevel();
		generateInvaders();
		generateMap();
	}

	protected void addInvader() {

	}

	protected void loadLevel(String path) {
	}

	protected void generateLevel() {
	}

	protected void generateLevels() {
	}

	protected void generateInvaders() {
	}

	protected void finishLog() {
	}

	public Point getStartLocation() {
		return null;
	}

	public int getNumInvaders() {
		return 0;
	}

	public Point getVoidTile(int x_pos, int y_pos, DemoGame game) {
		return null;
	}

	public boolean isTowerInside(int x_pos, int y_pos) {
		return false;
	}

	public Tower getTowerInside(int x_pos, int y_pos, int type) {
		return null;
	}

	public void selectInvader(Point p) {

	}

	public LinkedList<Tower> getTowers() {
		return towers;
	}

	public LinkedList<Invader> getInvaders() {
		return invaders;
	}

	protected void generateMap() {

	}

	public GameWindowInterface getWindow() {
		return window;
	}

	public void update() {

	}
	
	public LinkedList<Animation> getAnimations(){
		return animations;
	}
}
