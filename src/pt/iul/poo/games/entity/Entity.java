package pt.iul.poo.games.entity;

import pt.iul.poo.games.DemoGame;
import pt.iul.poo.games.systems.SoundLoader;

public abstract class Entity {

	protected int x_pos, y_pos;
	protected DemoGame game;
	protected SoundLoader sound;
	

	protected Entity(DemoGame game) {
		this.game = game;
		

		generate();
	}

	protected void generate() {

	}
	
	protected void loadMusic(){
		
	}
}
