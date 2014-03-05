package no.fosstveit.filter;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import no.fosstveit.view.View;

/**
 * @version 19 okt 2009
 * @author Håvar Aambø Fosstveit
 */
public class NegativeFilter extends AbstractFilter {

	public NegativeFilter(BufferedImage image, View parent) {
		super(image, parent);
	}

	@Override
	public BufferedImage filter() {
		return new RescaleOp(-1.0f, 255f, null).filter(getImage(), null);
	}

}
