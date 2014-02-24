package no.fosstveit.util;

import java.awt.image.BufferedImage;
import javax.swing.SwingWorker;
import no.fosstveit.model.Model;
import no.fosstveit.ui.ImagePanel;

public class ImageLoadWorker extends SwingWorker<BufferedImage, String> {
	private String url;
	private Model model;
	private BufferedImage loadedImage;
	private ImagePanel imagePanel;

	public ImageLoadWorker(Model model, String url, ImagePanel imagePanel) {
		this.model = model;
		this.url = url;
		this.imagePanel = imagePanel;
	}

	@Override
	protected void done() {
		try {
			imagePanel.stopLoader();
			model.setStatus("");
			model.setImage(loadedImage);
			model.scaleImage(imagePanel.getWidth(), imagePanel.getHeight());
		} catch (Exception e) {
		}
	}

	@Override
	public BufferedImage doInBackground() {
		imagePanel.showLoader();
		model.setStatus("Loading image...");
		loadedImage = Utilities.loadImage(url);
		return loadedImage;
	}
}
