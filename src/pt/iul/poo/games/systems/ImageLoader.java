package pt.iul.poo.games.systems;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class ImageLoader {

	private BufferedImage image;
	private int[] pixels;
	private int w, h;

	public ImageLoader(String path) {
		loadImage(path);
	}

	public void loadImage(String path) {

		try {
			image = ImageIO.read(getClass().getResource(path));
			w = image.getWidth();
			h = image.getHeight();

			pixels = new int[w * h];

			image.getRGB(0, 0, w, h, pixels, 0, w);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "ImageLoader - Imagem não encontrada: " + path);
			e.printStackTrace();
		}

	}

	public int[] getPixels() {
		return pixels;
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	// Retorna a cor presente na posicao (x,y)
	
	public int getColor(int next_xPos, int next_yPos) {

		if (image == null)
			return 0;

		if (next_xPos < 0)
			next_xPos = 0;
		if (next_yPos < 0)
			next_yPos = 0;

		if (next_xPos > getWidth() - 1)
			next_xPos = getWidth() - 1;

		if (next_yPos > getHeight() - 1)
			next_yPos = getHeight() - 1;

		int k = image.getRGB(next_xPos, next_yPos);

		int red = (k & 0xff0000) >> 16;
		int green = (k & 0x00ff00) >> 8;
		int blue = (k & 0x0000ff);

		int color = red << 16 | green << 8 | blue;

		return color;

	}
}
