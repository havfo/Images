package no.fosstveit.ui;

import javax.swing.*;

import no.fosstveit.model.Model;


import java.awt.*;
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
		bg = getBackground();
		imageLoading = new ImageIcon("loading.gif");
		imageLoadingLabel = new JLabel(imageLoading);
	}

	public ImagePanel(Model model) {
		this.model = model;
		this.model.addObserver(this);
		bg = getBackground();
		imageLoading = new ImageIcon("loading.gif");
		imageLoadingLabel = new JLabel(imageLoading);
	}

	public void setModel(Model model) {
		this.model = model;
		this.model.addObserver(this);
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
		g.setColor(bg);

		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(model.getImage(), 0, 0, this);
	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}
}
