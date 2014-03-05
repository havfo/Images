package no.fosstveit.filter;

import java.awt.image.BufferedImage;

import no.fosstveit.view.View;

/**
 * @version 19 okt 2009
 * @author Håvar Aambø Fosstveit
 */
public class DifferenceFilter extends AbstractFilter {

	private BufferedImage diff;
	
	public DifferenceFilter(BufferedImage image, View parent) {
		super(image, parent);
	}
	
	public void setDiffImage(BufferedImage diff) {
		this.diff = diff;
	}
	
	@Override
	public BufferedImage filter() {
		BufferedImage ret = new BufferedImage(getImage().getWidth(),
				getImage().getHeight(), BufferedImage.TYPE_INT_RGB);

		int w = getImage().getWidth();
		int h = getImage().getHeight();
		
		int w1 = diff.getWidth();
		int h1 = diff.getHeight();
		
		if (w != w1 || h != h1) {
			return null;
		}

		int[] pixels1 = getImage().getRGB(0, 0, w, h, null, 0, w);
		int[] pixels2 = diff.getRGB(0, 0, w1, h1, null, 0, w);
		int[] retPix = new int[pixels1.length];

		for (int i = 0; i < pixels1.length; i++) {
			retPix[i] = Math.abs(pixels1[i] - pixels2[i]);
		}

		ret.setRGB(0, 0, w, h, retPix, 0, w);

		return ret;
	}
}
