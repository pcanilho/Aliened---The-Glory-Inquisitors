package pt.iul.poo.games.entity.mob;

import java.awt.Point;
import java.io.IOException;

import javax.swing.JOptionPane;

import pt.iul.poo.games.DemoGame;
import pt.iul.poo.games.entity.mob.assets.Animation;
import pt.iul.poo.games.entity.mob.assets.LifeBar;
import pt.iul.poo.games.entity.mob.assets.Tooltip;
import pt.iul.poo.games.systems.SoundLoader;
import pt.iul.poo.image.MovingImage;

public class Invader extends Mob {

	private MovingImage img;

	// Tooltip do invasor
	private Tooltip tooltip;
	private boolean visibleTooltip = false;

	// Barra de vida
	private LifeBar lifeBar;

	private static final int map_scale = 50;
	private int dir;
	private int next_xPos, next_yPos, next_negxPos;
	private int life;
	private int moving_speed;
	private int type;
	private int dmg;

	private boolean moving_left = false, moving_right = false;
	private boolean turned_right = false, turned_left = false, turned_down = false, destroyed = false;

	// dir: 0 = cima, 1 = direita, 3 = baixo, 4 = esquerda

	public Invader(int x_pos, int y_pos, DemoGame game, int type) {
		super(game);
		this.type = type;

		switch (type) {

		case 0:
			life = 1000;
			moving_speed = 2;
			dmg = 10;
			break;

		case 1:
			life = 2000;
			moving_speed = 2;
			dmg = 15;
			break;

		case 2:
			life = 3000;
			moving_speed = 1;
			dmg = 30;
			break;
		}

		setEntity(x_pos, y_pos);
		loadMusic();
	}

	public void setEntity(int x_pos, int y_pos) {

		try {
			img = new MovingImage("resources/sprites/invaders/invader" + type + "_down.png", x_pos, y_pos, 1);

			tooltip = new Tooltip(this);
			lifeBar = new LifeBar(this);

			game.getWindow().getDisplayer().add(lifeBar);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Invader - Invasor não criado!", "Erro", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public int getLife() {
		return life;
	}

	public void loadMusic() {
		sound = new SoundLoader("/sounds/invaders/explosion.wav");
	}

	public SoundLoader getSound() {
		return sound;
	}

	public void setLife(int decay) {
		life -= decay;
	}

	public void move() {

		if (game.getTime() % 2 == 1) {
			// Move para a direita
			if (img.getPosition().x + img.getDimension().width <= game.getWindow().getWidth() - img.getDimension().width && !moving_left) {
				next_xPos = img.getPosition().x + img.getDimension().width;
				if (game.getLevel().map.getColor(next_xPos / map_scale, img.getPosition().y / map_scale) == 0xFFFF00
						&& game.getLevel().map.getColor(next_xPos / map_scale, (img.getPosition().y + img.getDimension().height - 1) / map_scale) == 0xFFFF00) {
					dir = 1;
					img.move(new Point(moving_speed, 0));

					moving_right = true;
				}
			}
		}
		// Move para a esquerda
		if (img.getPosition().x != 0 && !moving_right) {
			next_negxPos = img.getPosition().x - 1;
			if (game.getLevel().map.getColor(next_negxPos / map_scale, img.getPosition().y / map_scale) == 0xFFFF00
					&& game.getLevel().map.getColor(next_negxPos / map_scale, (img.getPosition().y + img.getDimension().height - 1) / map_scale) == 0xFFFF00) {
				dir = 3;

				img.move(new Point(-moving_speed, 0));
				moving_left = true;
			}
		}

		// Move para a baixo
		if (img.getPosition().y + img.getDimension().height < game.getWindow().getHeight()) {
			next_yPos = img.getPosition().y + img.getDimension().height;
			if (game.getLevel().map.getColor(img.getPosition().x / map_scale, next_yPos / map_scale) == 0xFFFF00
					&& game.getLevel().map.getColor((img.getPosition().x + img.getDimension().width - 1) / map_scale, next_yPos / map_scale) == 0xFFFF00) {
				dir = 2;

				img.move(new Point(0, moving_speed));
				moving_left = false;
				moving_right = false;
			}
		}

		// Verifica se chegou a algum dos destinos
		checkdestiny1();
		checkDestiny2();
		checkDestiny3();

		x_pos = img.getPosition().x;
		y_pos = img.getPosition().y;

	}

	private void checkDestiny2() {
		// Chega ao destino 3 virado para baixo
		next_yPos = img.getPosition().y + img.getDimension().height;
		if (game.getLevel().map.getColor(img.getPosition().x / map_scale, next_yPos / map_scale) == 0x7F3300
				&& game.getLevel().map.getColor((img.getPosition().x + img.getDimension().width - 1) / map_scale, next_yPos / map_scale) == 0x7F3300) {

			dir = 5;

			destroyed = true;
		}

		// Chega ao destino 3 virado para a direita
		next_xPos = img.getPosition().x + img.getDimension().width;
		if (game.getLevel().map.getColor(next_xPos / map_scale, img.getPosition().y / map_scale) == 0x7F3300
				&& game.getLevel().map.getColor(next_xPos / map_scale, (img.getPosition().y + img.getDimension().height - 1) / map_scale) == 0x7F3300) {
			dir = 5;

			destroyed = true;
		}

		// Chega ao destino 3 virado para a esquerda
		next_negxPos = img.getPosition().x - 1;
		if (game.getLevel().map.getColor(next_negxPos / map_scale, img.getPosition().y / map_scale) == 0x7F3300
				&& game.getLevel().map.getColor(next_negxPos / map_scale, (img.getPosition().y + img.getDimension().height - 1) / map_scale) == 0x7F3300) {
			dir = 5;

			destroyed = true;
		}

	}

	private void checkdestiny1() {

		// Chega ao destino 3 virado para baixo
		next_yPos = img.getPosition().y + img.getDimension().height;
		if (game.getLevel().map.getColor(img.getPosition().x / map_scale, next_yPos / map_scale) == 0x00FFFF
				&& game.getLevel().map.getColor((img.getPosition().x + img.getDimension().width - 1) / map_scale, next_yPos / map_scale) == 0x00FFFF) {

			dir = 5;

			destroyed = true;
		}

		// Chega ao destino 3 virado para a direita
		next_xPos = img.getPosition().x + img.getDimension().width;
		if (game.getLevel().map.getColor(next_xPos / map_scale, img.getPosition().y / map_scale) == 0x00FFFF
				&& game.getLevel().map.getColor(next_xPos / map_scale, (img.getPosition().y + img.getDimension().height - 1) / map_scale) == 0x00FFFF) {
			dir = 5;

			destroyed = true;
		}

		// Chega ao destino 3 virado para a esquerda
		next_negxPos = img.getPosition().x - 1;
		if (game.getLevel().map.getColor(next_negxPos / map_scale, img.getPosition().y / map_scale) == 0x00FFFF
				&& game.getLevel().map.getColor(next_negxPos / map_scale, (img.getPosition().y + img.getDimension().height - 1) / map_scale) == 0x00FFFF) {
			dir = 5;

			destroyed = true;
		}

	}

	private void checkDestiny3() {
		// Chega ao destino 3 virado para baixo
		next_yPos = img.getPosition().y + img.getDimension().height;
		if (game.getLevel().map.getColor(img.getPosition().x / map_scale, next_yPos / map_scale) == 0xFFB27F
				&& game.getLevel().map.getColor((img.getPosition().x + img.getDimension().width - 1) / map_scale, next_yPos / map_scale) == 0xFFB27F) {

			dir = 5;
			destroyed = true;
		}

		// Chega ao destino 3 virado para a direita
		next_xPos = img.getPosition().x + img.getDimension().width;
		if (game.getLevel().map.getColor(next_xPos / map_scale, img.getPosition().y / map_scale) == 0xFFB27F
				&& game.getLevel().map.getColor(next_xPos / map_scale, (img.getPosition().y + img.getDimension().height - 1) / map_scale) == 0xFFB27F) {
			dir = 5;
			destroyed = true;
		}

		// Chega ao destino 3 virado para a esquerda
		next_negxPos = img.getPosition().x - 1;
		if (game.getLevel().map.getColor(next_negxPos / map_scale, img.getPosition().y / map_scale) == 0xFFB27F
				&& game.getLevel().map.getColor(next_negxPos / map_scale, (img.getPosition().y + img.getDimension().height - 1) / map_scale) == 0xFFB27F) {
			dir = 5;

			destroyed = true;
		}

	}

	public void destroy() {
		game.getWindow().getDisplayer().remove(img);
		game.getLevel().getAnimations().add(new Animation(game, 2, img.getPosition().x, img.getPosition().y, sound));

		if (visibleTooltip)
			game.getWindow().getDisplayer().remove(tooltip);

		game.getWindow().getDisplayer().remove(lifeBar);
	}

	private void setImage(MovingImage img) {
		this.img = img;
		game.getWindow().getDisplayer().add(img);
	}

	private void updateImg() throws IOException {

		if (dir == 1 && !turned_right) {
			game.getWindow().getDisplayer().remove(img);
			setImage(new MovingImage("resources/sprites/invaders/invader" + type + "_right.png", x_pos, y_pos, 1));

			turned_right = true;
			turned_down = false;
		}
		if (dir == 2 && !turned_down) {
			game.getWindow().getDisplayer().remove(img);
			setImage(new MovingImage("resources/sprites/invaders/invader" + type + "_down.png", x_pos, y_pos, 1));
			turned_down = true;

			turned_left = false;
			turned_right = false;
		}
		if (dir == 3 && !turned_left) {
			game.getWindow().getDisplayer().remove(img);
			setImage(new MovingImage("resources/sprites/invaders/invader" + type + "_left.png", x_pos, y_pos, 1));

			turned_left = true;
			turned_down = false;
		}

		if (dir == 5) {
			game.getPlayer().setScore(dmg);
			game.getWindow().getDisplayer().remove(img);

			game.getWindow().getDisplayer().remove(lifeBar);

			if (tooltipCreated)
				game.getWindow().getDisplayer().remove(tooltip);
			game.getLevel().getInvaders().remove(this);
		}
	}

	private boolean tooltipCreated = false;

	public void update() {
		try {

			if (!destroyed) {
				move();
				updateImg();
				updateTooltip();

			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Invader - Update erro!", "Erro", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void updateTooltip() {
		if (visibleTooltip && !tooltipCreated) {
			game.getWindow().getDisplayer().add(tooltip);
			tooltipCreated = true;
		}

		if (visibleTooltip && tooltipCreated) {
			tooltip.update(x_pos, y_pos);
		}

		if (!visibleTooltip) {
			game.getWindow().getDisplayer().remove(tooltip);
			tooltipCreated = false;
		}

	}

	public boolean getTooltipVisiblity() {
		return visibleTooltip;
	}

	public MovingImage getImage() {
		return img;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void setVisibleTooltip(boolean visibility) {
		visibleTooltip = visibility;

	}

	public int getType() {
		return type;
	}

	public int getDmg() {
		return dmg;
	}

	public int getSpeed() {
		return moving_speed;
	}

	public void unmute() {
		sound.unmute();
	}

	public void mute() {
		sound.mute();
	}
}
