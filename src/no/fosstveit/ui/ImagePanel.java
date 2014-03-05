package no.fosstveit.ui;

import javax.swing.*;

import no.fosstveit.model.Model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

/**
 * @version 19 okt 2009
 * @author Håvar Aambø Fosstveit
 */

public class ImagePanel extends JPanel implements Observer {
	private static final long serialVersionUID = 325690927060724988L;

	private Model model;
	private Color bg;

	private JLabel imageLoadingLabel;
	private ImageIcon imageLoading;

	public ImagePanel() {
		super();
		bg = getBackground();
		imageLoading = new ImageIcon("loading.gif");
		imageLoadingLabel = new JLabel(imageLoading);
	}

	public ImagePanel(Model model) {
		super();
		this.model = model;
		this.model.addObserver(this);
		bg = getBackground();
		imageLoading = new ImageIcon("loading.gif");
		imageLoadingLabel = new JLabel(imageLoading);
	}

	public void setModel(Model model) {
		this.model = model;
		this.model.addObserver(this);
		revalidate();
		repaint();
	}

	public void showLoader() {
		add(imageLoadingLabel, BorderLayout.CENTER);
		revalidate();
		repaint();
	}

	public void stopLoader() {
		remove(imageLoadingLabel);
		revalidate();
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (model.isImageLoaded()) {
			g.setColor(bg);

			g.fillRect(0, 0, getWidth(), getHeight());
			g.drawImage(model.getScaledImage(), 0, 0, this);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		int w = 0;
		int h = 0;
		if (model.isImageLoaded()) {
			w = (int) (model.getScale() * model.getImage().getWidth());
			h = (int) (model.getScale() * model.getImage().getHeight());
		}
		return new Dimension(w, h);
	}

	@Override
	public void update(Observable o, Object arg) {
		revalidate();
		repaint();
	}
}
