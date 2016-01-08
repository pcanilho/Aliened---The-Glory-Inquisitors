package pt.iul.poo.games.systems;

import java.util.Timer;
import java.util.TimerTask;

import pt.iul.poo.games.DemoGame;
import pt.iul.poo.games.level.Level;
import pt.iul.poo.games.playerInterface.MapConstructorInterface;
import pt.iul.poo.games.playerInterface.Warning;
import pt.iul.poo.image.properties.GameWindowInterface;

public class CustomTimer {

	private Timer timer;

	private Level level;
	private DemoGame game;

	private int limit;
	private int time_delay = 0, interval = 0;
	public int seconds = 0;

	/*
	 * 
	 * Temporizador responsavel pela manutencao dos invasores
	 */

	public CustomTimer(Level level, DemoGame game, int time_delay) {
		this.level = level;
		this.game = game;
		this.time_delay = time_delay;
		init();
		setAlarm();

	}

	/*
	 * Temporizador responsavel pela actualizacao da mensagem ao jogador
	 */

	public CustomTimer(DemoGame game, int interval) {
		this.game = game;
		this.interval = interval;
		init();
		setPlayerMsg();

	}

	/*
	 * Temporizador responsavel pelo tempo de jogo
	 */

	public CustomTimer(DemoGame game) {
		this.game = game;
		init();
		setClock();
	}

	public CustomTimer(int delay) {
		init();
		setDelayTimer(delay);

	}

	public CustomTimer(int time, String message, GameWindowInterface window) {
		init();
		setWindowMessage(message, window);

	}

	public CustomTimer(final MapConstructorInterface mcI, int interval) {
		init();

		timer.schedule(new TimerTask() {
			public void run() {
				if (seconds % 3 == 0) {
					String msg = "Quando estiver pronto(a) clique gravar e jogue o seu novo mapa!";
					mcI.getMapConsWindow().setMessage(msg);
				} else if (seconds % 4 == 0) {
					String msg = "Seleccione uma textura e preencha o ecrân!";
					mcI.getMapConsWindow().setMessage(msg);
				}
				seconds++;
			}
		}, 0, interval * 1000);
	}

	private void setWindowMessage(final String message, final GameWindowInterface window) {
		final Warning warning = new Warning(message);

		window.getDisplayer().add(warning);

		timer.schedule(new TimerTask() {

			public void run() {
				if (seconds > interval) {
					window.getDisplayer().remove(warning);
					cancel();
				}
				seconds++;
			}
		}, 0, 1 * 1000);
	}

	private void setDelayTimer(int delay) {
		timer.schedule(new TimerTask() {

			public void run() {
				seconds++;
			}
		}, 0, delay * 1000);

	}

	private void setClock() {
		timer.schedule(new TimerTask() {

			public void run() {
				game.setTime(1);
			}
		}, 0, 1 * 10);
	}

	private void setPlayerMsg() {
		timer.schedule(new TimerTask() {

			public void run() {
				String msg = "Jogador: " + game.getPlayer().name + " ,recursos: " + game.getPlayer().resources + " ,Pontuação: " + game.getPlayer().score + ", Invasores destruidos["
						+ game.getPlayer().invaders_killed + "/" + game.getLevel().getNumInvaders() + "]";
				game.getWindow().setMessage(msg);
			}
		}, 0, interval * 1000);
	}

	public void cancel() {
		timer.cancel();
	}

	private void init() {
		timer = new Timer();

	}

	private void setAlarm() {
		limit = level.getNumInvaders();

		timer.schedule(new TimerTask() {

			public void run() {
				seconds++;
				if (seconds < time_delay)
					game.getWindow().setMessage("O jogo vai começar dentro de " + (time_delay - seconds) + " tempo de jogo.");
				if (limit > 0 && seconds >= time_delay && !game.inMenu) {
					if ((int) (Math.random() * 5) <= 1) {
						game.addInvader();
						limit--;
					}
				} else if (limit == 0) {
					game.creepsFlushed = true;
					timer.cancel();
				}
			}
		}, 0, 1 * 1000);
	}
}
