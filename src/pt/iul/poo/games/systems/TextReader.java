package pt.iul.poo.games.systems;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

public class TextReader {

	private String tmp, sep, line;
	private StringBuilder sb;
	private BufferedReader read;

	public String loadFile(String path) {

		try {
			read = new BufferedReader(new FileReader(path));
			line = null;

			sb = new StringBuilder();
			sep = System.getProperty("line.separator");

			while ((line = read.readLine()) != null) {
				sb.append(line);
				sb.append(sep);
			}

			read.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Reader - Ficheiro não encontrado: " + path, "Erro!", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Reader - Ficheiro não pode ser lido: " + path, "Erro!", JOptionPane.ERROR_MESSAGE);
		}
		
		tmp = sb.toString();

		return tmp;
	}

}
