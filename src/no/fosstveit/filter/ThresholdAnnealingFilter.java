package no.fosstveit.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import no.fosstveit.view.View;

/**
 * @version 19 okt 2009
 * @author Håvar Aambø Fosstveit
 */
public class ThresholdAnnealingFilter extends AbstractFilter {

	// Constants
	private int maxIterationTries = 500; // In case we are in a local minima
	private int maxIterations = 1000; // Number of iterations allowed in a run
	private double endTemperature = 0.001;
	private double startTemperature = 200.0;
	private double temperatureDecrease = 0.98; // How much the temperature
												// decreases pr iteration
	private double defaultValue = 0.4; // Found through trial and error
	private int colorDepth = 256; // Colordepth of image, 256 in this case
	private double maxA = 0;
	private double maxB = 0;
	private double maxC = 0; // Current best values for a, b and c start with 0
	// End of constants

	private double minEnergy; // Lowest energy so far
	private Random r;
	private double cumulativeScaling; // Graph scaling to get true values
	
	private double[] cumulativeValues = new double[colorDepth];;

	public ThresholdAnnealingFilter(BufferedImage image, View parent) {
		super(image, parent);
		r = new Random();
	}

	public void run() {
		makeImageMaximum(); // Set image to its brightest color components
									// pr pixel
		cumulativeScaling = makeCumulativeDistribution();

		runAnnealingSimulation(startTemperature, endTemperature);
	}

	private void setParameters(double a, double b, double c) {
		double energy = getEnergy(a, b, c);

		if (energy < minEnergy) {
			maxA = a;
			maxB = b;
			maxC = c;

			minEnergy = energy;
		}
	}

	private void runAnnealingSimulation(double startTemp, double endTemp) {
		double currentA = 0.0;
		double currentB = 0.0;
		double currentC = 0.0;

		// Initialize value
		double currentEnergy = getEnergy(maxA, maxB, maxC);
		minEnergy = currentEnergy;
		double nextEnergy;
		double currentTemp;
		double parameterMutation = 0.001;
		int tries = 0;
		int iterationTries = 0;
		boolean stillTrying;

		for (currentTemp = startTemp; currentTemp > endTemp; currentTemp *= temperatureDecrease) {
			iterationTries = 0;
			stillTrying = true;

			while (stillTrying) {
				iterationTries++;

				if (tries > maxIterations) {
					System.out
							.println("Oops, too many tries, I can't do it! :(");

					return;
				}

				if (iterationTries > maxIterationTries) { // We are in a local
															// minima
					System.out
							.println("This looks like a cool place, but I think we can get cooler someplace else! ;)");
					currentTemp = (1.10 * currentTemp <= startTemp * 1.15) ? 1.10 * currentTemp
							: startTemp * 1.15; // Getting out of local minima
					iterationTries = 0;
				}

				currentA = parameterMutation * currentTemp
						* (r.nextDouble() - 0.5);
				currentB = parameterMutation * currentTemp
						* (r.nextDouble() - 0.5);
				currentC = parameterMutation * currentTemp
						* (r.nextDouble() - 0.5);

				nextEnergy = getEnergy(currentA + maxA, currentB + maxB,
						currentC + maxC);

				if (nextEnergy <= currentEnergy) {
					setParameters(currentA + maxA, currentB + maxB, currentC
							+ maxC);
					stillTrying = false;
				}

				tries++;
			}

		}
	}

	private int getThresholdValue() {
		double[] values = new double[colorDepth];
		double[] derivatives = new double[colorDepth];
		int bestX = 1;
		double smallestDelta = 1.0;
		for (int x = 0; x < colorDepth; x++) {
			values[x] = maxA + maxB * Math.atan(maxC * (double) x);
		}

		for (int x = 1; x < colorDepth - 1; x++) {
			derivatives[x] = colorDepth * (values[x + 1] - values[x - 1]) / 2; // Simple
																				// numerical
																				// derivation

			if ((derivatives[x] - 1) < smallestDelta
					&& (derivatives[x] - 1) > 0) {
				smallestDelta = (derivatives[x] - 1);
				bestX = x; // Shoulder of graph
			}
		}

		return bestX + (int) Math.abs(defaultValue / maxC); // To the left of
															// the shoulder of
															// the graph
	}

	private double getEnergy(double a, double b, double c) {
		double sum = 0;
		double delta;
		double[] cumulativeValues = getCumulativeDistribution();

		for (int i = 0; i < colorDepth; i++) {
			delta = cumulativeValues[i] - (a + (b * Math.atan(c * i)));
			sum += delta * delta;
		}

		return sum;
	}
	
	public void makeImageMaximum() {

		int w = getImage().getWidth();
		int h = getImage().getHeight();

		int[] pixels = getImage().getRGB(0, 0, w, h, null, 0, w);

		int[] retPix = new int[pixels.length];

		int r;
		int g;
		int b;

		int max;

		for (int i = 0; i < pixels.length; i++) {
			r = (pixels[i] >>> 16) & 0xff;
			g = (pixels[i] >>> 8) & 0xff;
			b = pixels[i] & 0xff;

			max = max(r, g, b);

			retPix[i] = new Color(max, max, max).getRGB();
		}

		getImage().setRGB(0, 0, w, h, retPix, 0, w);
	}

	private int max(int a, int b, int c) {
		if (b > a) {
			a = b;
		}
		if (c > a) {
			a = c;
		}
		return a;
	}
	
	public double makeCumulativeDistribution() {
		int w = getImage().getWidth();
		int h = getImage().getHeight();
		double max;
		int[] frequency = new int[colorDepth];
		
		for (int i = 0; i < colorDepth; i++) {
			cumulativeValues[i] = 0.0;
			frequency[i] = 0;
		}
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				frequency[getImage().getRGB(j, i) & 0xff]++;
			}
		}
		
		cumulativeValues[0] = (double) frequency[0];
		
		for (int i = 1; i < colorDepth; i++) {
			cumulativeValues[i] += (double) frequency[i] + cumulativeValues[i - 1];
		}
		
		max = cumulativeValues[colorDepth - 1];
		
		for (int i = 0; i < colorDepth; i++) {
			cumulativeValues[i] /= max;
		}
		
		return max;
	}
	
	public BufferedImage threshold(int threshold) {
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
	
	public double[] getCumulativeDistribution() {
		return cumulativeValues;
	}

	@Override
	public BufferedImage filter() {
		run();
		
		return threshold(getThresholdValue());
	}

}
