package pt.iul.poo.games.launcher;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import pt.iul.poo.games.RunGame.Editor;
import pt.iul.poo.games.RunGame.Game;
import pt.iul.poo.games.launcher.info.Information_window;
import pt.iul.poo.games.player.data.Data;
import pt.iul.poo.games.systems.SoundLoader;

public class Launcher {

	// Tamanho da janela
	private final int width = 960;
	private final int height = 720;

	private final int Button_sizeX = 100;
	private final int Button_sizeY = 40;

	// Sistema de carregamento
	private Data data;
	private ImageIcon frameIcon, muteIcon, unmuteIcon;
	private JLabel label;
	private boolean muted = false;

	// Janela
	public static JFrame frame;
	private Dimension dim;
	private String name;

	// Copyrights
	private JTextField footer;

	// Rectângulos
	private Rectangle loadButREC;
	private Rectangle newGameREC;
	private Rectangle mapEditorRec;
	private Rectangle quitButREC;
	private Rectangle footerREC;
	private Rectangle infoREC;
	private Rectangle muteREC;

	// Carregar jogo
	private JButton loadBut, newGame, quitBut, infoBut, muteBut, mapEditor;

	public Launcher() {

		// Fecha a janela de informações se esta estiver aberta
		clearInfo();

		// Lê as imagens
		loadImages();

		// Inicializa a janela
		init();

		// Inicializa butões
		setButtons();

		// Inicializa o resto da janela
		setLayout();

		// Inicia a música de fundo
		startMusic();

		frame.repaint();

	}

	private void clearInfo() {
		if (Information_window.frame != null)
			Information_window.frame.dispose();
	}

	private void loadImages() {

		frameIcon = new ImageIcon(getClass().getResource("/images/launcher.icon.png"));
		muteIcon = new ImageIcon(getClass().getResource("/images/buttons/muted.png"));
		unmuteIcon = new ImageIcon(getClass().getResource("/images/buttons/unmuted.png"));

		label = new JLabel(new ImageIcon(getClass().getResource("/images/launcher_cover_r.png")));
	}

	private void init() {

		frame = new JFrame("Aliened - The Glory Inquisitors");
		dim = new Dimension(width, height);
		data = new Data();

		frame.setIconImage(frameIcon.getImage());
		frame.setSize(dim);
		frame.setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);

		footer = new JTextField();
		footerREC = new Rectangle(0, height - 100, 250, 50);

		footer.setEditable(false);
		footer.setBounds(footerREC);
		footer.setBackground(Color.black);
		footer.setForeground(Color.white);
		footer.setFont(new Font("Verdana", 1, 10));
		footer.setText("Paulo Canilho © \\ Poo - Maria Albuquerque");

		frame.add(footer);
	}

	private void setButtons() {

		Cursor hand = new Cursor(12);

		// Carregar jogo
		loadBut = new JButton("Carregar Jogo");
		loadButREC = new Rectangle(Button_sizeX * 2, Button_sizeY * 5, Button_sizeX + 50, Button_sizeY);

		loadBut.setBounds(loadButREC);
		loadBut.setFont(new Font("Verdana", 3, 15));
		loadBut.setBackground(Color.black);
		loadBut.setForeground(Color.red);
		loadBut.setCursor(hand);
		loadBut.setToolTipText("Carregar um jogo previamente guardado");

		// Sentinela (interface) que carrega o jogador e inicia o jogo
		loadBut.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				SoundLoader.butClip.start(false);
				data.load("resources/Player_Data/player_data.xml");
				JOptionPane.showMessageDialog(frame, "Jogador: " + data.getPlayer().name + " carregado com sucesso! \n ->Pontuação: " + data.getPlayer().score + "\n -> Nível: "
						+ data.getPlayer().level, "Criação de utilizador com sucesso!", JOptionPane.INFORMATION_MESSAGE);
				SoundLoader.butClip.stop();

				startGame();
			}
		});

		frame.add(loadBut);

		// Iniciar a janela de editor de mapas
		mapEditor = new JButton("Editor de Mapas");
		mapEditorRec = new Rectangle(Button_sizeX * 7, Button_sizeY * 2, Button_sizeX + 80, Button_sizeY);

		mapEditor.setBounds(mapEditorRec);
		mapEditor.setFont(new Font("Verdana", 3, 15));
		mapEditor.setBackground(Color.black);
		mapEditor.setForeground(Color.red);
		mapEditor.setCursor(hand);
		mapEditor.setToolTipText("Carregar um jogo previamente guardado");

		// Sentinela (interface) que carrega o jogador e inicia o jogo
		mapEditor.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				SoundLoader.butClip.start(false);

				new Thread(new Runnable() {
					public void run() {
						new Editor().start();
					}
				}).start();

				SoundLoader.butClip.stop();
			}
		});

		frame.add(mapEditor);

		// Iniciar novo jogo
		newGame = new JButton("Novo Jogo");
		newGameREC = new Rectangle(Button_sizeX * 2, Button_sizeY * 2, Button_sizeX + 30, Button_sizeY);

		newGame.setBounds(newGameREC);
		newGame.setFont(new Font("Verdana", 3, 15));
		newGame.setBackground(Color.black);
		newGame.setForeground(Color.red);
		newGame.setCursor(hand);
		newGame.setToolTipText("Novo jogo.");

		newGame.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				SoundLoader.butClip.start(false);

				name = JOptionPane.showInputDialog(frame, "Crie o seu utilizador:", "Criação de Utilizador", JOptionPane.QUESTION_MESSAGE);

				if (name != null) {
					data.save("Player", name);
					data.save("Score", 200);
					data.save("Player_id", 0);
					data.save("Player_level", 0);
					data.save("Resources", 1500);
					data.save("Inv_Kia", 0);

					startNewGame();
				} else {
					JOptionPane.showMessageDialog(frame, "O seu jogador foi criado com o nome: \"Default\"", "Criação bem sucedida!", JOptionPane.INFORMATION_MESSAGE);
					name = "Default";

					data.save("Player", name);
					data.save("Score", 200);
					data.save("Player_id", 0);
					data.save("Player_level", 0);
					data.save("Resources", 1000);
					data.save("Inv_Kia", 0);

					startNewGame();
				}

				// Data.players.add(new Player(name, Data.players.size(), 100,
				// 0));
				// data.savePlayers(Data.players);

				SoundLoader.butClip.stop();
			}
		});

		frame.add(newGame);

		// Sair da aplicação
		quitBut = new JButton("Sair");
		quitButREC = new Rectangle(width - Button_sizeX * 2, height - Button_sizeY * 3, Button_sizeX, Button_sizeY);

		quitBut.setBounds(quitButREC);
		quitBut.setFont(new Font("Verdana", 3, 15));
		quitBut.setBackground(Color.black);
		quitBut.setForeground(Color.red);
		quitBut.setCursor(hand);
		quitBut.setToolTipText("Sair do jogo.");

		quitBut.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				SoundLoader.butClip.start(false);
				System.exit(0);
			}
		});

		frame.add(quitBut);

		// Menu informativo
		infoBut = new JButton("Informações");
		infoREC = new Rectangle(Button_sizeX * 2, Button_sizeY * 8, Button_sizeX + 50, Button_sizeY);

		infoBut.setBounds(infoREC);
		infoBut.setFont(new Font("Verdana", 3, 15));
		infoBut.setBackground(Color.black);
		infoBut.setForeground(Color.red);
		infoBut.setCursor(hand);
		infoBut.setToolTipText("Menu informativo do jogo.");

		infoBut.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				SoundLoader.butClip.start(false);
				new Information_window();
				SoundLoader.butClip.stop();
			}
		});

		frame.add(infoBut);

		// Silenciar sons
		muteBut = new JButton();
		muteREC = new Rectangle(Button_sizeX - 80, height - Button_sizeY * 4, Button_sizeX, Button_sizeY);

		muteBut.setBounds(muteREC);
		muteBut.setOpaque(false);
		muteBut.setContentAreaFilled(false);
		muteBut.setBorderPainted(false);
		muteBut.setCursor(hand);
		muteBut.setIcon(unmuteIcon);

		muteBut.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (!muted) {
					muteBut.setIcon(muteIcon);
					SoundLoader.menuClip.mute();
					SoundLoader.butClip.mute();
					muted = true;
				} else if (muted) {
					muteBut.setIcon(unmuteIcon);
					SoundLoader.menuClip.unmute();
					SoundLoader.butClip.unmute();
					muted = false;
				}

			}
		});

		frame.add(muteBut);
	}

	private void startNewGame() {
		new Thread(new Runnable() {

			public void run() {
				new Game().start(0);
			}
		}).start();
	}

	private void startGame() {
		new Thread(new Runnable() {

			public void run() {
				new Game().start(data.getPlayer().level);
			}
		}).start();
	}

	private void setLayout() {

		label.setIcon(new ImageIcon(getClass().getResource("/images/launcher_cover_r.png")));
		label.setBounds(new Rectangle(0, 0, width, height));

		frame.add(label);

	}

	public void startMusic() {
		SoundLoader.menuClip.start(true);
	}
}
