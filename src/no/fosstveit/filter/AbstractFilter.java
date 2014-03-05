package no.fosstveit.filter;

import java.awt.image.BufferedImage;

import no.fosstveit.view.View;

/**
 * @version 19 okt 2009
 * @author Håvar Aambø Fosstveit
 */
public abstract class AbstractFilter {
	
	private BufferedImage image;
	private View parent;
	
	public AbstractFilter(BufferedImage image, View parent) {
		this.image = image;
		this.parent = parent;
	}
	
	public abstract BufferedImage filter();
	
	public BufferedImage getImage() {
		return image;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public View getParent() {
		return parent;
	}

	public void setParent(View parent) {
		this.parent = parent;
	}
}
