package pt.iul.poo.games.entity.mob;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JOptionPane;

import pt.iul.poo.games.DemoGame;
import pt.iul.poo.games.entity.mob.assets.Tooltip;
import pt.iul.poo.games.systems.SoundLoader;
import pt.iul.poo.image.SimpleImage;

public class Tower extends Mob {

	private SimpleImage img;

	private int attack_dmg;
	private int type;
	private int price;

	private boolean firing = false, tooltipVisible = false;
	private Color fire_color;
	private Tooltip tooltip;

	public Tower(DemoGame game, int x_pos, int y_pos, int type) {
		super(game);
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		this.type = type;

		setEntity();
	}

	protected void setEntity() {
		try {
			img = new SimpleImage("resources/sprites/towers/tower" + type + ".png", x_pos, y_pos);
			tooltip = new Tooltip(this);
			setAttackDmg();
			loadMusic();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Tower - Erro ao criar a torre", "Tower - Create", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	protected void setAttackDmg() {
		if (type == 1) {
			price = 100;
			attack_dmg = 2;
			fire_color = Color.black;
		}
		if (type == 2) {
			price = 250;
			attack_dmg = 3;
			fire_color = new Color(0x60FFF7);
		}
		if (type == 3) {
			price = 400;
			attack_dmg = 4;
			fire_color = new Color(0xFF8E2B);
		}
	}

	public void loadMusic() {
		sound = new SoundLoader("/sounds/towers/tower_" + type + "_sound.wav");
	}

	public int getType() {
		return type;
	}

	public void destroy() {
		game.getWindow().getDisplayer().remove(img);
		game.getLevel().getTowers().remove(img);
		update(false);
	}

	public int getAtkDmg() {
		return attack_dmg;
	}

	public SimpleImage getImage() {
		return img;
	}

	public boolean isFiring() {
		return firing;
	}

	public void setFiring(boolean fire) {
		firing = fire;
	}

	public Color getColor() {
		return fire_color;
	}

	public int getPrice() {
		return price;
	}

	public void unmute() {
		sound.unmute();
	}

	public void mute() {
		sound.mute();
	}

	public SoundLoader getSound() {
		return sound;
	}

	public void update(boolean value) {
		tooltipVisible = value;
		
		if(tooltipVisible)
			game.getWindow().getDisplayer().add(tooltip);
		else
			game.getWindow().getDisplayer().remove(tooltip);
	}

	public boolean getTooltipCreated() {
		return tooltipVisible;
	}

}
