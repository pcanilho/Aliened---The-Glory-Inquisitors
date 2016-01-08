package pt.iul.poo.games.player;

import java.io.Serializable;

public class Player implements Serializable, Comparable<Player> {

	private static final long serialVersionUID = 1L;

	public int id;
	public String name;
	public Integer score;
	public Integer level;
	public Integer resources;
	public Integer invaders_killed = 0;

	/**
	 * Cria um jogador com apenas o nivel e a pontuacao como atributos
	 */

	public Player(int score, int level) {
		this.score = score;
		this.level = level;
	}

	/**
	 * Cria um jogador com os atributos: nome, id, pontuacao, nivel e recursos
	 */

	public Player(String name, int id, int score, int level, int resources) {
		this.name = name;
		this.id = id;
		this.score = score;
		this.level = level;
		this.resources = resources;
	}

	/**
	 * Recebe um valor como atributo e decrementa a variavel de recursos do
	 * jogador associado, se o valor dos recursos ja for igual a 0, o valor
	 * mantem-se
	 */

	public void setResources(int price) {
		resources -= price;
		if (resources < 0)
			resources = 0;
	}

	/**
	 * Recebe um valor como atributo e decrementa a variavel de pontuacao do
	 * jogador associado, se o valor da pontuacao ja for igual a 0, o valor
	 * mantem-se
	 */

	public void setScore(int value) {
		score -= value;
		if (score < 0)
			score = 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Jogador: " + name + ", nível: " + level + ", recursos: " + resources + " ,Invasores: " + invaders_killed;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */

	public int compareTo(Player p) {
		if (level.compareTo(p.level) == 0) {
			if (getAnswer(resources, p.resources) == 0) {
				return getAnswer(invaders_killed, p.invaders_killed);
			} else
				return getAnswer(resources, p.resources);
		}
		if (level.compareTo(p.level) == -1)
			return -1;
		else
			return 1;

	}

	/**
	 * Este metodo e utilizado de forma a simplificar o grau de comparacao na
	 * escrita de jogadores
	 * 
	 * @return 
	 * inteiros [-1, 1]
	 * 
	 */

	private int getAnswer(Integer p1, Integer p2) {
		return p1.compareTo(p2);
	}

}
