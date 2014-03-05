package no.fosstveit.filter;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import no.fosstveit.view.View;

/**
 * @version 19 okt 2009
 * @author Håvar Aambø Fosstveit
 */
public class ConvolutionFilter extends AbstractFilter {

	private Kernel kernel;
	
	public ConvolutionFilter(BufferedImage image, View parent) {
		super(image, parent);
	}

	@Override
	public BufferedImage filter() {
		return filter(kernel);
	}
	
	public void setKernel(float[] kernel, int w, int h) {
		this.kernel = new Kernel(w, h, kernel);
	}
	
	public void setKernel(Kernel kernel, int w, int h) {
		this.kernel = kernel;
	}

	public BufferedImage filter(Kernel kernel) {
		return new ConvolveOp(kernel).filter(getImage(), null);
	}
}
