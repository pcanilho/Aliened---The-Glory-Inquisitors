package pt.iul.poo.games.launcher.info;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class PopUp extends MouseAdapter {

	// Janela utilizada
	Information_window info;

	// Menus
	private JPopupMenu main;
	private JMenu menu;
	private JMenuBar bar;

	private Font custom_font = new Font("Verdana", 1, 10);

	// Itens do menu
	private JMenuItem quit, lvls, rules, back, title, scoreboard;

	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger())
			drawPopupmenu(e);
	}

	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger())
			drawPopupmenu(e);
	}

	private void drawPopupmenu(MouseEvent e) {

		main.show(e.getComponent(), e.getX() - 20, e.getY() - 30);
	}

	public PopUp(Information_window info) {
		this.info = info;

		init();
		setItems();

		main.add(menu);
		main.add(quit);
	}

	private void init() {
		main = new JPopupMenu();
		menu = new JMenu("Informações");
	}

	private void setItems() {

		// Sair da aplicação
		quit = new JMenuItem("Sair");
		quit.setFont(custom_font);
		quit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		// Menu de regras
		rules = new JMenuItem("Informações");
		lvls = new JMenuItem("Níveis");
		scoreboard = new JMenuItem("Pontuações");
		back = new JMenuItem("Voltar");
		title = new JMenuItem("Menu");

		// Associar a font personalizada
		scoreboard.setFont(custom_font);
		rules.setFont(custom_font);
		lvls.setFont(custom_font);
		back.setFont(custom_font);
		title.setFont(custom_font);
		menu.setFont(custom_font);
		
		// Associar cor personalizada
		rules.setBackground(Color.black);
		rules.setForeground(Color.red);
		lvls.setBackground(Color.black);
		lvls.setForeground(Color.red);
		back.setBackground(Color.black);
		back.setForeground(new Color(0x29FCCA));
		title.setBackground(Color.black);
		title.setForeground(Color.red);
		menu.setBackground(Color.black);
		menu.setForeground(Color.red);
		scoreboard.setBackground(Color.black);
		scoreboard.setForeground(Color.red);
		
		rules.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				info.rulesBut.doClick();
			}
		});

		// Menu de níveis
		
		lvls.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				info.lvlsBut.doClick();
			}
		});

		// Voltar ao menu principal

		
		back.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				info.backBut.doClick();
			}
		});
		
		scoreboard.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				info.scoreBut.doClick();
				
			}
		});

		// Título do menu
		

		bar = new JMenuBar();
		bar.add(title);

		// Adicionar itens ao menu
		menu.add(rules);
		menu.add(lvls);
		menu.add(scoreboard);
		menu.add(back);
		

		// Adicionar título ao menu
		main.add(bar);
	}
}
