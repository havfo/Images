package no.fosstveit.model;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Observable;

import no.fosstveit.filter.AbstractFilter;
import no.fosstveit.util.Utilities;

/**
 * @version 19 okt 2009
 * @author Håvar Aambø Fosstveit
 */
public class Model extends Observable {

	private BufferedImage image;

	private int width, height;

	private boolean imageLoaded = false;

	private String status = "";

	double scale;

	private BufferedImage scaledImage;

	public Model() {
	}

	public void filter(AbstractFilter filter) {
		if (imageLoaded) {
			image = filter.filter();
			// scaleImage(width, height);

			setChanged();
			notifyObservers();
		}
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		imageLoaded = true;
		
		this.width = image.getWidth();
		this.height = image.getHeight();
		
		setScale(1.0);

		setChanged();
		notifyObservers();
	}

	public void setScale(double s) {
		scale = s;

		if (imageLoaded) {
			scaledImage = Utilities.scaleImage(image, (int) (scale * width), (int) (scale * height),
					RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);

			setChanged();
			notifyObservers();
		}
	}

	public double getScale() {
		return scale;
	}

	public BufferedImage getImage() {
		return image;
	}

	public BufferedImage getScaledImage() {
		return scaledImage;
	}

	public boolean isImageLoaded() {
		return imageLoaded;
	}

	public void setImageLoaded(boolean imageLoaded) {
		this.imageLoaded = imageLoaded;
	}

	public void setStatus(String status) {
		this.status = status;

		setChanged();
		notifyObservers();
	}

	public String getStatus() {
		return status;
	}

	public void reset() {
		// image = original;
		// scaleImage(width, height);

		setChanged();
		notifyObservers();
	}
}
