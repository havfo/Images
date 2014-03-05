package no.fosstveit.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;

import no.fosstveit.view.View;

/**
 * @version 19 okt 2009
 * @author Håvar Aambø Fosstveit
 */
public class PseudoColorFilter extends AbstractFilter {

	/**
	 * @param image
	 */
	public PseudoColorFilter(BufferedImage image, View parent) {
		super(image, parent);
	}

	@Override
	public BufferedImage filter() {
		BufferedImage ret = new BufferedImage(getImage().getWidth(), getImage().getHeight(),
				BufferedImage.TYPE_INT_RGB);

		int[] retPix = ret.getRGB(0, 0, ret.getWidth(), ret.getHeight(), null,
				0, ret.getWidth());

		int[] pix = new GreyScaleFilter(getImage(), getParent()).filter().getRGB(0, 0, getImage().getWidth(), getImage().getHeight(), null, 0,
				getImage().getWidth());

		for (int i = 0; i < pix.length; i++) {
			int val = (pix[i] >> 16) & 0xff;

			if (val >= 1 && val < 43) { // R
				Color pse = new Color(255, val * 6, 0);
				retPix[i] = pse.getRGB();
			} else if (val >= 43 && val < 85) { // R + G
				Color pse = new Color((255 - (val - 42) * 6), 255, 0);
				retPix[i] = pse.getRGB();
			} else if (val >= 86 && val < 128) { // G
				Color pse = new Color(0, 255, (val - 85) * 6);
				retPix[i] = pse.getRGB();
			} else if (val >= 128 && val < 170) { // G + B
				Color pse = new Color(0, (255 - (val - 127) * 6), 255);
				retPix[i] = pse.getRGB();
			} else if (val >= 171 && val < 212) { // B
				Color pse = new Color((val - 170) * 6, 0, 255);
				retPix[i] = pse.getRGB();
			} else if (val >= 212 && val < 254) { // B + R
				Color pse = new Color(255, 0, (255 - (val - 211) * 6));
				retPix[i] = pse.getRGB();
			} else if (val == 0 || val >= 254) { // R
				Color pse = new Color(255, 0, 0);
				retPix[i] = pse.getRGB();
			} else if (val == 85) { // G
				Color pse = new Color(0, 255, 0);
				retPix[i] = pse.getRGB();
			} else if (val == 170) { // B
				Color pse = new Color(0, 0, 255);
				retPix[i] = pse.getRGB();
			}
		}

		ret.setRGB(0, 0, ret.getWidth(), ret.getHeight(), retPix, 0, ret
				.getWidth());

		return ret;
	}

}
