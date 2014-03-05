package no.fosstveit.util;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.SwingWorker;
import no.fosstveit.model.Model;
import no.fosstveit.ui.ImagePanel;

public class ImageLoadWorker extends SwingWorker<BufferedImage, String> {
	private String url;
	private File file;
	private Model model;
	private BufferedImage loadedImage;
	private ImagePanel imagePanel;
	private boolean fileLoad = false;

	public ImageLoadWorker(Model model, String url, ImagePanel imagePanel) {
		this.model = model;
		this.url = url;
		this.imagePanel = imagePanel;
	}

	public ImageLoadWorker(Model model, File file, ImagePanel imagePanel) {
		this.model = model;
		this.file = file;
		this.imagePanel = imagePanel;
		fileLoad = true;
	}

	@Override
	protected void done() {
		try {
			imagePanel.stopLoader();
			model.setStatus("");
			model.setImage(loadedImage);
//			model.scaleImage(imagePanel.getWidth(), imagePanel.getHeight());
		} catch (Exception e) {
		}
	}

	@Override
	public BufferedImage doInBackground() {
		imagePanel.showLoader();
		model.setStatus("Loading image...");
		if (fileLoad) {
			loadedImage = Utilities.loadImage(file);
		} else {
			loadedImage = Utilities.loadImage(url);
		}
		return loadedImage;
	}
}
