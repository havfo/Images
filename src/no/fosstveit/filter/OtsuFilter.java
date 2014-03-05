package no.fosstveit.filter;

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import no.fosstveit.view.View;

/**
 * @version 19 okt 2009
 * @author Håvar Aambø Fosstveit
 */
public class OtsuFilter extends AbstractFilter {

	private int[] histData;
	private int maxLevelValue;
	private int threshold;

	public OtsuFilter(BufferedImage image, View parent) {
		super(image, parent);
		histData = new int[256];
	}

	@Override
	public BufferedImage filter() {
		setImage(new GreyScaleFilter(getImage(), getParent()).filter());
		
		int width = getImage().getWidth();
		int height = getImage().getHeight();

		// Get raw image data
		Raster raster = getImage().getData();
		DataBuffer buffer = raster.getDataBuffer();

		int type = buffer.getDataType();
		if (type != DataBuffer.TYPE_BYTE) {
			System.err.println("Wrong image data type");
			System.exit(1);
		}
		if (buffer.getNumBanks() != 1) {
			System.err.println("Wrong image data format");
			System.exit(1);
		}

		DataBufferByte byteBuffer = (DataBufferByte) buffer;
		byte[] srcData = byteBuffer.getData(0);

		// Sanity check image
		if (width * height != srcData.length) {
			System.err
					.println("Unexpected image data size. Should be greyscale image");
			System.exit(1);
		}

		byte[] dstData = new byte[srcData.length];

		int threshold = doThreshold(srcData, dstData);

		System.out.printf("Threshold: %d\n", threshold);

		DataBufferByte dataBuffer = new DataBufferByte(dstData, dstData.length,
				0);

		PixelInterleavedSampleModel sampleModel = new PixelInterleavedSampleModel(
				DataBuffer.TYPE_BYTE, width, height, 1, width, new int[] { 0 });
		ColorSpace colourSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		ComponentColorModel colourModel = new ComponentColorModel(colourSpace,
				new int[] { 8 }, false, false, Transparency.OPAQUE,
				DataBuffer.TYPE_BYTE);

		WritableRaster retRaster = Raster.createWritableRaster(sampleModel,
				dataBuffer, null);

		BufferedImage image = new BufferedImage(colourModel, retRaster, false,
				null);

		return image;
	}

	private int doThreshold(byte[] srcData, byte[] monoData) {
		int ptr;

		// Clear histogram data
		// Set all values to zero
		ptr = 0;
		while (ptr < histData.length)
			histData[ptr++] = 0;

		// Calculate histogram and find the level with the max value
		// Note: the max level value isn't required by the Otsu method
		ptr = 0;
		maxLevelValue = 0;
		while (ptr < srcData.length) {
			int h = 0xFF & srcData[ptr];
			histData[h]++;
			if (histData[h] > maxLevelValue)
				maxLevelValue = histData[h];
			ptr++;
		}

		// Total number of pixels
		int total = srcData.length;

		float sum = 0;
		for (int t = 0; t < 256; t++)
			sum += t * histData[t];

		float sumB = 0;
		int wB = 0;
		int wF = 0;

		float varMax = 0;
		threshold = 0;

		for (int t = 0; t < 256; t++) {
			wB += histData[t]; // Weight Background
			if (wB == 0)
				continue;

			wF = total - wB; // Weight Foreground
			if (wF == 0)
				break;

			sumB += (float) (t * histData[t]);

			float mB = sumB / wB; // Mean Background
			float mF = (sum - sumB) / wF; // Mean Foreground

			// Calculate Between Class Variance
			float varBetween = (float) (wB * wF) * (mB - mF) * (mB - mF);

			// Check if new maximum found
			if (varBetween > varMax) {
				varMax = varBetween;
				threshold = t;
			}
		}

		// Apply threshold to create binary image
		if (monoData != null) {
			ptr = 0;
			while (ptr < srcData.length) {
				monoData[ptr] = ((0xFF & srcData[ptr]) >= threshold) ? (byte) 255
						: 0;
				ptr++;
			}
		}

		return threshold;
	}
}
