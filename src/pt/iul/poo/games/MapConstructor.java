package pt.iul.poo.games;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import pt.iul.poo.games.playerInterface.MapConstructorInterface;
import pt.iul.poo.games.systems.SoundLoader;
import pt.iul.poo.image.SimpleImage;
import pt.iul.poo.image.properties.GameWindowInterface;

public class MapConstructor {

	private GameWindowInterface window;

	private MapConstructorInterface map_interface;
	private int tile_index = 1;

	// Sistema sonoro
	private SoundLoader[] tracks;
	private int music_index;

	private LinkedList<SimpleImage> void_tiles, road, ending1, ending2, ending3;

	public static final int[] MAP_SIZE = { 25, 13 };
	private boolean running = false;

	public MapConstructor(GameWindowInterface window) {
		this.window = window;
	}

	private void init() {

		map_interface = new MapConstructorInterface(this);
		window.getDisplayer().add(map_interface);

		void_tiles = new LinkedList<SimpleImage>();
		road = new LinkedList<SimpleImage>();
		ending1 = new LinkedList<SimpleImage>();
		ending2 = new LinkedList<SimpleImage>();
		ending3 = new LinkedList<SimpleImage>();

		load_StartMusic();
		loadInterface();
		fakeInit();

		running = true;
	}

	private void loadInterface() {
		window.getDisplayer().add(map_interface);

	}

	private void load_StartMusic() {

		tracks = new SoundLoader[6];

		tracks[0] = SoundLoader.track1;
		tracks[1] = SoundLoader.track2;
		tracks[2] = SoundLoader.track3;
		tracks[3] = SoundLoader.track4;
		tracks[4] = SoundLoader.track5;
		tracks[5] = SoundLoader.track6;

		Random r = new Random();

		music_index = r.nextInt(tracks.length);
		tracks[music_index].start(false);
	}

	private void fakeInit() {

		// Desenha todos os "Displayable objects"
		drawImages();

	}

	private void drawImages() {
		for (int y = 0; y < MAP_SIZE[1]; y++) {
			for (int x = 0; x < MAP_SIZE[0]; x++) {
				try {
					SimpleImage tmp = new SimpleImage("resources/sprites/map/m_text.png", x * window.getDefaultElementWidth(), y * window.getDefaultElementHeight());

					void_tiles.add(tmp);
					window.getDisplayer().add(tmp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void run() {

		init();
		window.start();
		while (running) {

			update();

			Set<Integer> keyEvents = window.getKeyEvents();
			Set<Point> mouseEvents = window.getMouseEvents();

			if (!keyEvents.isEmpty() || !mouseEvents.isEmpty()) {

				for (Integer i : keyEvents) {
					interactInterface(i.intValue());
				}

			}
			for (Point p : mouseEvents) {
				try {
					addTile(p);
					saveMap(p);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (running)
				window.getDisplayer().refresh();
		}
	}

	private void addTile(Point p) throws IOException {
		Point toDeploy = getTilePos(p);
		if (toDeploy != null) {

			if (tile_index == 1) {

				// Se ja existia um bloco de estrada no ponto seleccionado, este
				// e removido
				for (SimpleImage path : road) {
					if (path.getPosition().equals(toDeploy)) {
						road.remove(path);
						window.getDisplayer().remove(path);
						break;
					}
				}

				SimpleImage tmp = new SimpleImage("resources/sprites/map/m_text.png", toDeploy.x, toDeploy.y);
				window.getDisplayer().add(tmp);
				void_tiles.add(tmp);
			}
			if (tile_index == 2) {
				SimpleImage tmp = new SimpleImage("resources/sprites/map/ending1.png", toDeploy.x, toDeploy.y);
				window.getDisplayer().add(tmp);
				ending1.add(tmp);
			}
			if (tile_index == 3) {
				SimpleImage tmp = new SimpleImage("resources/sprites/map/ending2.png", toDeploy.x, toDeploy.y);
				window.getDisplayer().add(tmp);
				ending2.add(tmp);
			}
			if (tile_index == 4) {
				SimpleImage tmp = new SimpleImage("resources/sprites/map/ending3.png", toDeploy.x, toDeploy.y);
				window.getDisplayer().add(tmp);
				ending3.add(tmp);
			}
			if (tile_index == 5) {
				SimpleImage tmp = new SimpleImage("resources/sprites/map/road.png", toDeploy.x, toDeploy.y);
				window.getDisplayer().add(tmp);
				road.add(tmp);
			}
		}

	}

	private Point getTilePos(Point p) {
		Point tmp = null;

		for (SimpleImage v : void_tiles) {
			if (p.x >= v.getPosition().x && p.x <= v.getPosition().x + v.getDimension().width && p.y >= v.getPosition().y && p.y <= v.getPosition().y + v.getDimension().height) {
				tmp = new Point(v.getPosition().x, v.getPosition().y);
			}
		}

		return tmp;
	}

	private void saveMap(Point p) {
		if (p.x >= map_interface.getSaveBounds().x && p.x <= map_interface.getSaveBounds().x + map_interface.getSaveBounds().width && p.y >= map_interface.getSaveBounds().y
				&& p.y <= map_interface.getSaveBounds().y + map_interface.getSaveBounds().height) {
			map_interface.save();
		}

		if (p.x >= map_interface.getQuitBounds().x && p.x <= map_interface.getQuitBounds().x + map_interface.getQuitBounds().width && p.y >= map_interface.getQuitBounds().y
				&& p.y <= map_interface.getQuitBounds().y + map_interface.getQuitBounds().height) {
			map_interface.quit();
		}
	}

	private void interactInterface(int value) {
		if (value == KeyEvent.VK_1) {
			tile_index = 1;
		}
		if (value == KeyEvent.VK_2) {
			tile_index = 2;
		}
		if (value == KeyEvent.VK_3) {
			tile_index = 3;
		}
		if (value == KeyEvent.VK_4) {
			tile_index = 4;
		}
		if (value == KeyEvent.VK_5) {
			tile_index = 5;
		}

	}

	private void update() {
		map_interface.update(tile_index);
	}

	public GameWindowInterface getWindow() {
		return window;
	}

	public int getMusicIndex() {
		return music_index;
	}

	public SoundLoader[] getTracks() {
		return tracks;
	}

	public LinkedList<SimpleImage> getVoidTiles() {
		return void_tiles;
	}

	public LinkedList<SimpleImage> getEnd1Tiles() {
		return ending1;
	}

	public LinkedList<SimpleImage> getEnd2Tiles() {
		return ending2;
	}

	public LinkedList<SimpleImage> getEnd3Tiles() {
		return ending3;
	}

	public LinkedList<SimpleImage> getRoadTiles() {
		return road;
	}

	public void setRunning(boolean value) {
		this.running = value;
	}

}
