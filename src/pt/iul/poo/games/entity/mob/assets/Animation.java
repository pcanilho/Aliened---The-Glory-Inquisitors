package pt.iul.poo.games.entity.mob.assets;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;

import pt.iul.poo.games.DemoGame;
import pt.iul.poo.games.systems.CustomTimer;
import pt.iul.poo.games.systems.SoundLoader;
import pt.iul.poo.image.properties.DisplayableElement;
import pt.iul.poo.image.properties.Displayer;

public class Animation {

	private DisplayableElement destructedImg;
	private DemoGame game;
	private CustomTimer timer;

	private int time_delay;
	private int time;

	private Point point;
	private SoundLoader sound;

	public Animation(DemoGame game, int time_delay, int x, int y, SoundLoader sound) {
		this.game = game;
		this.time_delay = time_delay;
		this.sound = sound;

		point = new Point(x, y);

		loadDestructionImg(point.x, point.y);
		setTimer();
		
	}

	private void setTimer() {
		sound.start(false);
		timer = new CustomTimer(time_delay);
	}

	public void destroy() {

		game.getWindow().getDisplayer().remove(destructedImg);
		timer.cancel();
		sound.stop();
	}

	private void loadDestructionImg(final int xOffset, final int yOffset) {

		destructedImg = new DisplayableElement() {

			public int compareTo(DisplayableElement o) {
				return 0;
			}

			public boolean isValid() {
				return true;
			}

			public int getPriority() {
				return 1;
			}

			public void display(Graphics g, Displayer arg1) {
				g.drawImage(new ImageIcon(getClass().getResource("/sprites/invaders/assets/explosion.gif")).getImage(), xOffset, yOffset, 50, 50, null);
			}
		};

		game.getWindow().getDisplayer().add(destructedImg);

	}

	public void update() {
		time = timer.seconds;
	}

	public int getTime() {
		return time;
	}
}
