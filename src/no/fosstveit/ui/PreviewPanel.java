package no.fosstveit.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import no.fosstveit.util.Utilities;


/**
 * @version 19 okt 2009
 * @author Håvar Aambø Fosstveit
 */
public class PreviewPanel extends JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = 5799697847338075624L;
	private int width, height;
	private BufferedImage image;
	private static final int ACCSIZE = 155;
	private Color bg;

	public PreviewPanel() {
		setPreferredSize(new Dimension(ACCSIZE, -1));
		bg = getBackground();
	}

	public void propertyChange(PropertyChangeEvent e) {
		String propertyName = e.getPropertyName();

		// Make sure we are responding to the right event.
		if (propertyName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
			File selection = (File) e.getNewValue();
			String name;

			if (selection == null) {
				return;
			} else {
				name = selection.getAbsolutePath();
			}

			if ((name != null) && name.toLowerCase().endsWith(".jpg")
					|| name.toLowerCase().endsWith(".jpeg")
					|| name.toLowerCase().endsWith(".png")) {
				try {
					image = ImageIO.read(selection.getAbsoluteFile());

				} catch (IOException ex) {
				}
				scaleImage();
				repaint();
			}
		}
	}

	private void scaleImage() {
		width = image.getWidth(this);
		height = image.getHeight(this);
		double ratio = 1.0;
		if (width >= height) {
			ratio = (double) (ACCSIZE - 5) / width;
			width = ACCSIZE - 5;
			height = (int) (height * ratio);
		} else {
			if (getHeight() > 150) {
				ratio = (double) (ACCSIZE - 5) / height;
				height = ACCSIZE - 5;
				width = (int) (width * ratio);
			} else {
				ratio = (double) getHeight() / height;
				height = getHeight();
				width = (int) (width * ratio);
			}
		}

		image = Utilities.scaleImage(image, width, height,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
	}

	public void paintComponent(Graphics g) {
		g.setColor(bg);
		g.fillRect(0, 0, ACCSIZE, getHeight());
		if (image != null) {
			g.drawImage(image, getWidth() / 2 - width / 2 + 5, getHeight() / 2
					- height / 2, this);
		}
	}
}
