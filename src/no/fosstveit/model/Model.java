package no.fosstveit.model;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Observable;

import no.fosstveit.util.Utilities;


/**
 * @version 19 okt 2009
 * @author Håvar Aambø Fosstveit
 */
public class Model extends Observable {

	private BufferedImage original;

	private BufferedImage image;

	private int width, height;
	
	private boolean hasChanged = false;

	private boolean imageLoaded = false;
	
	private String status = "";

	public Model() {
	}

	public void setImage(BufferedImage image) {
		this.original = image;
		this.image = image;

		imageLoaded = true;
		setChanged();
		notifyObservers();
	}

	public void scaleImage(int width, int height) {
		if (imageLoaded) {
			if ((width > 20 && height > 20)
					&& (width <= original.getWidth() || height <= original
							.getHeight())) {
				this.width = width;
				this.height = height;
				double ratio = (double) original.getWidth()
						/ (double) original.getHeight();
				double panelRatio = (double) width / (double) height;

				if (ratio >= panelRatio) {
					height = (int) (width / ratio);
				} else {
					width = (int) (height * ratio);
				}

				image = Utilities.scaleImage(original, width, height,
						RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);

				setChanged();
				notifyObservers();
			} else {
				setChanged();
				notifyObservers();
			}
		}
	}

	public BufferedImage getImage() {
		return image;
	}
	
	public BufferedImage getOriginalImage() {
		return original;
	}
	
	public boolean hasChanged() {
		return hasChanged;
	}

	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
		
		setChanged();
		notifyObservers();
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
		image = original;
		scaleImage(width, height);

		setChanged();
		notifyObservers();
	}
}
