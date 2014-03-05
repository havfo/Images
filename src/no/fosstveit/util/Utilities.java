package no.fosstveit.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

/**
 * @version 19 okt 2009
 * @author Håvar Aambø Fosstveit
 */

public class Utilities {

	public static BufferedImage loadImage(File file) {
		BufferedImage image;
		try {
			image = ImageIO.read(file);

			return image;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static BufferedImage loadImage(String url) {
		BufferedImage img = null;
		try {
			URL u = new URL(url);
			URLConnection uc = u.openConnection();
			uc.addRequestProperty(
					"User-Agent",
					"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.77 Safari/535.7");

			uc.connect();

			InputStream i = uc.getInputStream();
			img = ImageIO.read(i);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

	public static BufferedImage average(BufferedImage[] images) {

		int n = images.length;

		// Assuming that all images have the same dimensions
		int w = images[0].getWidth();
		int h = images[0].getHeight();

		BufferedImage average = new BufferedImage(w, h,
				BufferedImage.TYPE_BYTE_GRAY);

		WritableRaster raster = average.getRaster()
				.createCompatibleWritableRaster();

		for (int y = 0; y < h; ++y)
			for (int x = 0; x < w; ++x) {

				float sum = 0.0f;

				for (int i = 0; i < n; ++i)
					sum = sum + images[i].getRaster().getSample(x, y, 0);

				raster.setSample(x, y, 0, Math.round(sum / n));
			}

		average.setData(raster);

		return average;
	}

	public static BufferedImage scaleImage(BufferedImage img, int targetWidth,
			int targetHeight, Object hint, boolean progressiveBilinear) {
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) img;
		BufferedImage scratchImage = null;
		Graphics2D g2 = null;
		int w, h;
		int prevW = ret.getWidth();
		int prevH = ret.getHeight();
		if (progressiveBilinear && (img.getWidth() > targetWidth && img.getHeight() > targetHeight)) {
			w = img.getWidth();
			h = img.getHeight();
		} else {
			w = targetWidth;
			h = targetHeight;
		}

		do {
			if (progressiveBilinear && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}
			if (progressiveBilinear && h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}
			if (scratchImage == null) {
				scratchImage = new BufferedImage(w, h, type);
				g2 = scratchImage.createGraphics();
			}
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);
			prevW = w;
			prevH = h;
			ret = scratchImage;
		} while (w != targetWidth || h != targetHeight);
		if (g2 != null) {
			g2.dispose();
		}

		if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
			scratchImage = new BufferedImage(targetWidth, targetHeight, type);
			g2 = scratchImage.createGraphics();
			g2.drawImage(ret, 0, 0, null);
			g2.dispose();
			ret = scratchImage;
		}
		return ret;
	}
}
