package pt.iul.poo.games.entity.mob.assets;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import pt.iul.poo.games.entity.mob.Invader;
import pt.iul.poo.games.entity.mob.Tower;
import pt.iul.poo.image.properties.DisplayableElement;
import pt.iul.poo.image.properties.Displayer;

public class Tooltip implements DisplayableElement {

	private Invader invader;
	private Tower tower;

	private int xPos, yPos;
	private int id;
	private ImageIcon faction;

	public Tooltip(Invader invader) {
		this.invader = invader;

		id = 0;
		faction = new ImageIcon(getClass().getResource("/playerInterface/gdi.f.gif"));
	}

	public Tooltip(Tower tower) {
		this.tower = tower;

		id = 1;
		faction = new ImageIcon(getClass().getResource("/playerInterface/nod.r.gif"));
	}

	public int compareTo(DisplayableElement arg0) {
		return 0;
	}

	private Image getImage(int id) throws NullPointerException {
		ImageIcon image = null;
		if (id == 0)
			image = new ImageIcon(getClass().getResource("/sprites/invaders/assets/test2.png"));
		else
			image = new ImageIcon(getClass().getResource("/sprites/towers/assets/tooltip.png"));

		return image.getImage();
	}

	public void display(Graphics g, Displayer arg1) {
		int xOffset = 70;
		int yOffSet = 20;
		int rectX = 88, rectY = 80;

		if (id == 0) {
			xPos = invader.getImage().getPosition().x + xOffset;
			yPos = invader.getImage().getPosition().y;
		} else if (id == 1) {
			xPos = tower.getImage().getPosition().x + xOffset;
			yPos = tower.getImage().getPosition().y;
		}

		try {
			g.drawImage(getImage(id), xPos - xOffset / 3, yPos - yOffSet * 2 - 15, rectX, rectY, null);
		} catch (NullPointerException e) {
			System.err.println("Tooltip - Imagem não encontrada");
			e.printStackTrace();

			System.err.println("Saída abrupta do sistema (1)");
			System.exit(1);
		}

		g.setFont(new Font("Verdana", 1, 12));
		g.setColor(new Color(0x1DFFCA));
		for (int i = 0; i < getMessages(id).length; i++) {
			g.drawString(getMessages(id)[i], xPos - xOffset / 4, yPos - yOffSet);
			yOffSet -= 10;
		}

		drawFaction(g);

	}

	private void drawFaction(Graphics g) {
		g.drawImage(faction.getImage(), xPos + 36, yPos - 5, 30, 30, null);
	}

	private String[] getMessages(int id) {

		if (id == 0) {
			String[] messages = { "Vida: " + invader.getLife(), "Tipo: " + invader.getType(), "Dano: " + invader.getDmg(), "Vel.: " + invader.getSpeed() };
			return messages;
		} else if (id == 1) {
			String[] messages = { "Dano: " + tower.getAtkDmg(), "Custo: " + tower.getPrice(), "Tipo: " + tower.getType() };
			return messages;
		}

		String[] messages = { "Tooltip - ", "ID mal formulado" };

		return messages;
	}

	public int getPriority() {
		return 1;
	}

	public boolean isValid() {
		return true;
	}

	public void update(int x, int y) {
		this.xPos = x;
		this.yPos = y;
	}

}
