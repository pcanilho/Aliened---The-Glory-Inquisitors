package pt.iul.poo.games.systems;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class ImageSaver {

	private BufferedImage img;
	private int[] pixels;

	public ImageSaver() {
		init();
	}

	private void init() {
		img = new BufferedImage(25, 13, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
	}

	public void save(int[] p_made, String fileName) {
		try {
			generateMap(p_made);

			File out = new File("resources/levels/level_" + fileName + ".png");
			if (out.exists()) {
				int answer = JOptionPane.showConfirmDialog(null, "Ficheiro existente - O ficheiro: " + "level_" + fileName + " já existe \n->Deseja dar prioridade ao ficheiro criado?",
						"Criação de Mapa", JOptionPane.YES_NO_OPTION);
				if (answer == JOptionPane.YES_OPTION) {
					ImageIO.write(img, "png", out);
				} else if (answer == JOptionPane.NO_OPTION) {
					fileName = JOptionPane.showInputDialog(null, "Insira o nome do seu ficheiro: ", "Criação de novo ficheiro", JOptionPane.QUESTION_MESSAGE);
					if (fileName != null)
						save(pixels, fileName);
				}
			} else
				ImageIO.write(img, "png", out);

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "ImageSaver - Problema ao salvar imagem", "ImageSaver", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

	}

	private void generateMap(int[] p_made) {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = p_made[i];
		}
	}
}
