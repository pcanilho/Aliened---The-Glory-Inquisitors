package pt.iul.poo.games.entity.mob.assets;

import java.awt.Color;
import java.awt.Graphics;

import pt.iul.poo.games.entity.mob.Invader;
import pt.iul.poo.image.properties.DisplayableElement;
import pt.iul.poo.image.properties.Displayer;

public class LifeBar implements DisplayableElement {

	private Invader invader;

	private final int fullLife;

	public LifeBar(Invader invader) {
		this.invader = invader;

		fullLife = invader.getLife();
	}

	public int compareTo(DisplayableElement o) {
		return 0;
	}

	public void display(Graphics g, Displayer arg1) {
		g.setColor(Color.black);
		g.drawRect(invader.getImage().getPosition().x, invader.getImage().getPosition().y - 15 - 2, invader.getLife() / 70 + 2, 10 + 2);

		setBarColor(g);
		g.fillRect(invader.getImage().getPosition().x + 2, invader.getImage().getPosition().y - 15, invader.getLife() / 70, 10);
	}

	private void setBarColor(Graphics g) {

		if (invader.getLife() >= fullLife / 2) {
			g.setColor(Color.green);
		} else if (invader.getLife() <= fullLife / 2 && invader.getLife() >= fullLife / 3) {
			g.setColor(Color.yellow);
		} else if (invader.getLife() <= fullLife / 3) {
			g.setColor(Color.red);
		}
	}

	public int getPriority() {
		return 1;
	}

	public boolean isValid() {
		return false;
	}

}
