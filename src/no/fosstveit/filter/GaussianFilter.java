package no.fosstveit.filter;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import no.fosstveit.view.View;

/**
 * @version 19 okt 2009
 * @author Håvar Aambø Fosstveit
 */
public class GaussianFilter extends AbstractFilter {

	private int radius;
	
	public GaussianFilter(BufferedImage image, View parent) {
		super(image, parent);
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
	}

	@Override
	public BufferedImage filter() {
		return filter(radius);
	}
	
	public BufferedImage filter(int radius) {
		if (radius < 1) {
			throw new IllegalArgumentException("Radius must be >= 1");
		}
		
		BufferedImage ret = new BufferedImage(getImage().getWidth(),
				getImage().getHeight(), BufferedImage.TYPE_INT_RGB);

		int size = radius * 2 + 1;
		float[] data = new float[size];

		float sigma = radius / 3.0f;
		float twoSigmaSquare = 2.0f * sigma * sigma;
		float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
		float total = 0.0f;

		for (int i = -radius; i <= radius; i++) {
			float distance = i * i;
			int index = i + radius;
			data[index] = (float) Math.exp(-distance / twoSigmaSquare)
					/ sigmaRoot;
			total += data[index];
		}

		for (int i = 0; i < data.length; i++) {
			data[i] /= total;
		}

		Kernel kernel = null;
		kernel = new Kernel(size, 1, data);
		
		ret = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null).filter(getImage(), null);
		
		kernel = new Kernel(1, size, data);
		
		ret = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null).filter(ret, null);
		
		return ret;
	}

}
