package no.fosstveit.filter;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

import no.fosstveit.view.View;

/**
 * @version 19 okt 2009
 * @author Håvar Aambø Fosstveit
 */
public class GreyScaleFilter extends AbstractFilter {

	public GreyScaleFilter(BufferedImage image, View parent) {
		super(image, parent);
	}

	@Override
	public BufferedImage filter() {
		return new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null).filter(getImage(), null);
	}

}
