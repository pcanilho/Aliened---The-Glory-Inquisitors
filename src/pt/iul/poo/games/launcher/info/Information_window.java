package pt.iul.poo.games.launcher.info;

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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import pt.iul.poo.games.launcher.Launcher;
import pt.iul.poo.games.player.Player;
import pt.iul.poo.games.systems.ScoreBoard;
import pt.iul.poo.games.systems.SoundLoader;
import pt.iul.poo.games.systems.TextReader;

public class Information_window {

	// Sistema de leitura de texto
	private TextReader text_reader = new TextReader();

	private String text_general_path;
	private String information_general;

	private String text_lvls_path;
	private String information_lvls;

	// Dimensão da janela
	private final int width = 960, height = 720;
	private Dimension dim;
	public static JFrame frame;

	// Áreas de texto
	private JTextArea info_general;
	private JScrollPane pane_general;

	private JTextArea info_lvls;
	private JScrollPane pane_lvls;

	private JTextArea scoreboard;
	private JScrollPane pane_score;

	// Título
	private JLabel title;
	private String title_text = "Informações";

	// Sentinela de menu popup
	private PopUp pop_menu;

	// Botão de volta para o launcher
	public JButton rulesBut, lvlsBut, backBut, scoreBut;

	// Dimensão da área de texto, botão, título
	private final int Button_sizeX = 100;
	private final int Button_sizeY = 40;
	private Rectangle info_gen_REC, backREC, titleREC, rulesButREC, lvlsButREC, scoreRec;

	// Imagem de fundo e icon da janela
	private ImageIcon background, icon;
	private JLabel label;

	public Information_window() {

		// Carrega a imagem
		loadImages();

		// Fecha a janela do launcher
		clearLauncher();

		// Inicia o texto dos níveis
		getLevels();

		// Inicializa a janela das informações relativas ao jogo
		init();

		// Inicializa o botão
		setButton();

		// Inicializa a áerea de texto
		setTextField();

		// Inicializa o resto da página
		setImages();

		frame.repaint();

	}

	private void loadImages() {
		String bg_path = "/images/info_cover_f.png";
		String ico_path = "/images/information_icon.png";

		background = new ImageIcon(getClass().getResource(bg_path));
		icon = new ImageIcon(getClass().getResource(ico_path));

	}

	private void clearLauncher() {
		if (Launcher.frame != null)
			Launcher.frame.dispose();
	}

	private void init() {
		frame = new JFrame();
		dim = new Dimension(width, height);

		frame.setSize(dim);
		frame.setLayout(null);
		frame.setIconImage(icon.getImage());
		frame.setResizable(false);
		frame.setTitle("Informações");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		pop_menu = new PopUp(this);
	}

	private void getLevels() {
		text_general_path = "resources/text/info.txt";
		information_general = text_reader.loadFile(text_general_path);

		text_lvls_path = "resources/text/info_lvls.txt";
		information_lvls = text_reader.loadFile(text_lvls_path);

	}

	private void setButton() {

		int hOffSet = 15;
		int yOffSet = 30;

		backBut = new JButton("Voltar");
		backREC = new Rectangle(width - Button_sizeX * 2, height - Button_sizeY * 3, Button_sizeX, Button_sizeY);

		backBut.setBounds(backREC);
		backBut.setFont(new Font("Verdana", 1, 15));
		backBut.setBackground(Color.black);
		backBut.setCursor(new Cursor(12));
		backBut.setForeground(Color.red);

		backBut.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				SoundLoader.butClip.start(false);
				new Launcher();
				SoundLoader.butClip.stop();

			}
		});

		frame.add(backBut);

		// Botão que mostra as regras
		int xOffSet = 70;
		Cursor hand = new Cursor(12);

		rulesBut = new JButton("Informações");
		rulesButREC = new Rectangle(width - Button_sizeX * 4 - xOffSet, height - Button_sizeY * 16 + yOffSet, Button_sizeX + 10, Button_sizeY - hOffSet);

		rulesBut.setBounds(rulesButREC);
		rulesBut.setFont(new Font("Verdana", 1, 10));
		rulesBut.setBackground(Color.black);
		rulesBut.setForeground(Color.red);
		rulesBut.setCursor(hand);

		rulesBut.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				pane_lvls.setVisible(true);
				pane_general.setVisible(false);
				pane_score.setVisible(false);

				rulesBut.setEnabled(false);

				scoreBut.setEnabled(true);
				lvlsBut.setEnabled(true);

			}
		});

		frame.add(rulesBut);

		// Botão que mostra as informações gerais do jogo
		lvlsBut = new JButton("Níveis");
		lvlsButREC = new Rectangle(width - Button_sizeX * 4 - xOffSet, height - Button_sizeY * 15 + yOffSet, Button_sizeX + 10, Button_sizeY - hOffSet);

		lvlsBut.setBounds(lvlsButREC);
		lvlsBut.setFont(new Font("Verdana", 1, 10));
		lvlsBut.setBackground(Color.black);
		lvlsBut.setForeground(Color.red);
		lvlsBut.setCursor(hand);

		lvlsBut.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				pane_general.setVisible(true);
				pane_lvls.setVisible(false);
				pane_score.setVisible(false);

				rulesBut.setEnabled(true);
				scoreBut.setEnabled(true);

				lvlsBut.setEnabled(false);

			}
		});

		// Botao relativo ao painel da lista de pontuacoes
		scoreBut = new JButton("Pontuações");
		scoreRec = new Rectangle(width - Button_sizeX * 4 - xOffSet, height - Button_sizeY * 14 + yOffSet, Button_sizeX + 10, Button_sizeY - hOffSet);

		scoreBut.setBounds(scoreRec);
		scoreBut.setFont(new Font("Verdana", 1, 10));
		scoreBut.setBackground(Color.black);
		scoreBut.setForeground(Color.red);
		scoreBut.setCursor(hand);

		scoreBut.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				pane_general.setVisible(false);
				pane_lvls.setVisible(false);
				pane_score.setVisible(true);

				scoreBut.setEnabled(false);

				rulesBut.setEnabled(true);
				lvlsBut.setEnabled(true);
			}
		});

		// O painel relativo aos niveis encontra-se previamente seleccionado
		lvlsBut.setEnabled(false);
		UIManager.getDefaults().put("Button.disabledText", new Color(0x29FCCA));

		frame.add(lvlsBut);
		frame.add(scoreBut);

	}

	private void setTextField() {

		// Título da área de texto
		title = new JLabel();
		titleREC = new Rectangle(11, 45, 628, 60);

		title.setFont(new Font("Verdana", 3, 30));
		title.setForeground(new Color(0x4CCFA3));
		title.setOpaque(true);
		title.setBackground(Color.black);
		title.setBounds(titleREC);
		title.setText("  " + title_text);

		frame.add(title);

		// Área de texto de informações gerais
		info_general = new JTextArea();
		info_general.setFont(new Font("Verdana", 1, 12));
		info_general.setText(information_general);
		info_general.setForeground(Color.yellow);
		info_general.setToolTipText("Clique com o botão do rato para o menu");
		info_general.setOpaque(false);
		info_general.setEditable(false);
		info_general.addMouseListener(pop_menu);

		pane_general = new JScrollPane(info_general);
		info_gen_REC = new Rectangle(10, 23, 645, 660);

		pane_general.setBounds(info_gen_REC);
		pane_general.setOpaque(false);
		pane_general.getViewport().setOpaque(false);
		pane_general.setWheelScrollingEnabled(true);

		frame.add(pane_general);

		// Área de texto de informações sobre os níveis
		info_lvls = new JTextArea();
		info_lvls.setFont(new Font("Verdana", 1, 12));
		info_lvls.setText(information_lvls);
		info_lvls.setForeground(Color.yellow);
		info_lvls.setToolTipText("Clique com o botão do rato para o menu");
		info_lvls.setOpaque(false);
		info_lvls.setEditable(false);
		info_lvls.addMouseListener(pop_menu);

		info_lvls.setEditable(false);

		pane_lvls = new JScrollPane(info_lvls);

		pane_lvls.setBounds(info_gen_REC);

		pane_lvls.setOpaque(false);
		pane_lvls.getViewport().setOpaque(false);
		pane_lvls.setWheelScrollingEnabled(true);

		// Area de texto que contem a lista de pontuacoes
		scoreboard = new JTextArea();
		scoreboard.setFont(new Font("Verdana", 1, 12));
		scoreboard.setForeground(Color.yellow);
		scoreboard.setToolTipText("Clique com o botão do rato para o menu");
		scoreboard.setOpaque(false);
		scoreboard.setEditable(false);
		scoreboard.addMouseListener(pop_menu);

		ScoreBoard score = new ScoreBoard();

		scoreboard.append("\n\n\n\n\n\n\n\n\n\n");
		for (Player player : score.getPlayers()) {
			scoreboard.append(player.toString() + "\n\n");
		}

		pane_score = new JScrollPane(scoreboard);
		pane_score.setBounds(info_gen_REC);
		pane_score.setOpaque(false);
		pane_score.getViewport().setOpaque(false);
		pane_score.setWheelScrollingEnabled(true);

		// Inicia invisível
		pane_lvls.setVisible(false);
		pane_score.setVisible(false);

		frame.add(pane_score);
		frame.add(pane_lvls);

	}

	private void setImages() {
		label = new JLabel(background);
		label.setSize(dim);

		frame.add(label);
	}

}
