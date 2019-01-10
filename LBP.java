import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class bmpToH {
	public static void main(String[] args) throws IOException {
		File bmpFile = new File("D:\\Eclipde workspace\\IC\\src\\cow.png");//your image file address
		BufferedImage image = ImageIO.read(bmpFile);

		FileWriter pattern = new FileWriter("pattern3.dat");
		FileWriter golden = new FileWriter("golden3.dat");

		int[][] array2D = new int[image.getWidth()][image.getHeight()];
		int pixel = 0;

		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

		
		File f = null;
		Graphics g;
		Raster raster = image.getData();
		for (int j = 0; j < image.getWidth(); j++) {
			for (int k = 0; k < image.getHeight(); k++) {
				Color c = new Color(image.getRGB(j, k));
				int red = (int) (c.getRed() * 0.299);
				int green = (int) (c.getGreen() * 0.587);
				int blue = (int) (c.getBlue() * 0.114);
				Color newColor = new Color(red + green + blue,

						red + green + blue, red + green + blue);

				int grayLevelPixel = raster.getSample(j, k, 0);

				array2D[j][k] = grayLevelPixel;
				Color Color = new Color(grayLevelPixel, grayLevelPixel, grayLevelPixel);

				String pp = "%02X      //Pixel %d: %03d\n";
				String ppp = String.format(pp, grayLevelPixel, pixel++, grayLevelPixel);
				

				pattern.write(ppp);
			}
		}
		pattern.flush();
		pattern.close();

		int cx = 0, cy = 0;
		int temp[] = new int[9];
		int result[] = new int[image.getWidth() * image.getHeight()];
		pixel = 0;

		int zero = 0;
		for (int times = 0; times < image.getWidth() * image.getHeight(); times++) {
			if (cx == 0 || cx == image.getWidth() - 1 || cy == 0 || cy == image.getHeight() - 1) {
				result[pixel++] = 0;
				++zero;
				// System.out.println(result[pixel-1]);
			} else {
				temp[0] = array2D[cx - 1][cy - 1];
				temp[1] = array2D[cx][cy - 1];
				temp[2] = array2D[cx + 1][cy - 1];
				temp[3] = array2D[cx - 1][cy];
				temp[4] = array2D[cx][cy];
				temp[5] = array2D[cx + 1][cy];
				temp[6] = array2D[cx - 1][cy + 1];
				temp[7] = array2D[cx][cy + 1];
				temp[8] = array2D[cx + 1][cy + 1];
			

				temp[0] = (temp[0] < temp[4]) ? 0 : 1;
				temp[1] = (temp[1] < temp[4]) ? 0 : 1;
				temp[2] = (temp[2] < temp[4]) ? 0 : 1;
				temp[3] = (temp[3] < temp[4]) ? 0 : 1;
				temp[5] = (temp[5] < temp[4]) ? 0 : 1;
				temp[6] = (temp[6] < temp[4]) ? 0 : 1;
				temp[7] = (temp[7] < temp[4]) ? 0 : 1;
				temp[8] = (temp[8] < temp[4]) ? 0 : 1;

				
				temp[0] = temp[0];
				temp[1] = temp[1] * 2;
				temp[2] = temp[2] * 4;
				temp[3] = temp[3] * 8;
				temp[5] = temp[5] * 16;
				temp[6] = temp[6] * 32;
				temp[7] = temp[7] * 64;
				temp[8] = temp[8] * 128;
				
				result[pixel++] = temp[0] + temp[1] + temp[2] + temp[3] + temp[5] + temp[6] + temp[7] + temp[8];
				
			}


			if (result[pixel - 1] == 0)
				++zero;

			if (cx == image.getWidth() - 1) {
				cx = 0;
				cy = cy + 1;
			} else {
				cx = cx + 1;

			}

		}

		for (int x = 0; x < image.getWidth() * image.getHeight(); x++) {

			Color newColor = new Color(result[x], result[x], result[x]);
			img.setRGB(x % image.getWidth(), x / image.getHeight(), newColor.getRGB());
			String pp = "%02X      //Pixel %d: %03d\n";
			String ppp = String.format(pp, result[x], x, result[x]);
			golden.write(ppp);

		}

		golden.flush();
		golden.close();
		f = new File("D:\\Eclipde workspace\\IC\\src\\out.bmp");
		ImageIO.write(img, "bmp", f);
		
	}
}
