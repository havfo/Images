/**
 * 
 */
package no.fosstveit.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;

import no.fosstveit.view.View;

/**
 * @version 19 okt 2009
 * @author Håvar Aambø Fosstveit
 */
public class ThresholdFilter extends AbstractFilter {

	private int threshold;
	
	public ThresholdFilter(BufferedImage image, View parent) {
		super(image, parent);
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
	
	@Override
	public BufferedImage filter() {
		return filter(threshold);
	}

	public BufferedImage filter(int threshold) {
		BufferedImage ret = new BufferedImage(getImage().getWidth(),
				getImage().getHeight(), BufferedImage.TYPE_INT_RGB);

		int w = getImage().getWidth();
		int h = getImage().getHeight();

		int thresh = new Color(threshold, threshold, threshold).getRGB();

		int[] pixels1 = getImage().getRGB(0, 0, w, h, null, 0, w);
		int[] retPix = new int[pixels1.length];

		for (int i = 0; i < pixels1.length; i++) {
			if (pixels1[i] < thresh) {
				retPix[i] = new Color(0, 0, 0).getRGB();
			} else {
				retPix[i] = new Color(255, 255, 255).getRGB();
			}
		}

		ret.setRGB(0, 0, w, h, retPix, 0, w);

		return ret;
	}
}
