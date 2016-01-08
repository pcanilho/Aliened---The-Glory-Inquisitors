package pt.iul.poo.games.playerInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import pt.iul.poo.image.properties.DisplayableElement;
import pt.iul.poo.image.properties.Displayer;

public class Warning implements DisplayableElement {

	private String message;

	private Point position;

	public Warning(String message) {
		this.message = message;
		init();
	}

	private void init() {
		position = new Point(500, 680);
	}

	public int compareTo(DisplayableElement arg0) {
		return 0;
	}

	public void display(Graphics g, Displayer arg1) {
		g.setColor(Color.red);
		g.setFont(new Font("Verdana", 1, 12));
		g.drawString(message, position.x, position.y);
	}

	public int getPriority() {
		return 1;
	}

	public boolean isValid() {
		return true;
	}

}
