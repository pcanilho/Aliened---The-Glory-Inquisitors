package pt.iul.poo.games.RunGame;

import java.awt.Dimension;

import pt.iul.poo.games.MapConstructor;
import pt.iul.poo.games.launcher.Launcher;
import pt.iul.poo.games.systems.SoundLoader;
import pt.iul.poo.image.properties.GameWindowInterface;
import pt.iul.poo.imagewindow.GameWindow;

public class Editor {
	
	public static final Dimension SQUARE_DIMENSION = new Dimension(50, 50);
	public static final Dimension WINDOW_DIMENSION = new Dimension(1235, 800);

	public void start() {

		if (Launcher.frame != null)
			Launcher.frame.dispose();
		if (SoundLoader.menuClip.isRunning())
			SoundLoader.menuClip.stop();

		GameWindowInterface window = new GameWindow("Aliened Editor", WINDOW_DIMENSION, SQUARE_DIMENSION);
		
		MapConstructor mC = new MapConstructor(window);
		mC.run();
	}

}
