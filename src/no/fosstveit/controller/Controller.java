package no.fosstveit.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import no.fosstveit.model.Model;
import no.fosstveit.ui.ImageFileFilter;
import no.fosstveit.ui.PreviewPanel;
import no.fosstveit.util.ImageLoadWorker;
import no.fosstveit.util.Utilities;
import no.fosstveit.view.View;

/**
 * @version 19 okt 2009
 * @author Håvar Aambø Fosstveit
 */

public class Controller {
	private Model model;
	private View view;
	private boolean hasChanged = false;
	private ImageLoadWorker ilw;

	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;

		view.addNewImagesListener(new NewImagesListener());
		view.addNewUrlListener(new NewUrlImagesListener());
		view.addExitEntryListener(new ExitEntryListener());
		view.addWindowListener(new ViewListener());
		view.addSaveChangesListener(new SaveChangesListener());
		view.addAboutListener(new AboutListener());
		view.addImagePanelListener(new ImagePanelListener());
		view.addImagePanelMouseListener(new ImagePanelMouseListener());
	}

	private void exitImages() {
		int n = JOptionPane.YES_OPTION;
		if (hasChanged) {
			n = JOptionPane.showConfirmDialog(view,
					"You have unsaved changes, exit anyway?", "Warning",
					JOptionPane.YES_NO_OPTION);
		}

		if (n == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	class ViewListener implements WindowListener {
		@Override
		public void windowActivated(WindowEvent arg) {
		}

		@Override
		public void windowClosed(WindowEvent arg) {
		}

		@Override
		public void windowClosing(WindowEvent arg) {
			exitImages();
		}

		@Override
		public void windowDeactivated(WindowEvent arg) {
		}

		@Override
		public void windowDeiconified(WindowEvent arg) {
		}

		@Override
		public void windowIconified(WindowEvent arg) {
		}

		@Override
		public void windowOpened(WindowEvent arg) {
		}
	}

	class ImagePanelListener implements ComponentListener {
		@Override
		public void componentResized(ComponentEvent e) {
			model.scaleImage(view.getImagePanel().getWidth(), view
					.getImagePanel().getHeight());
		}

		@Override
		public void componentShown(ComponentEvent e) {
		}

		@Override
		public void componentHidden(ComponentEvent e) {
		}

		@Override
		public void componentMoved(ComponentEvent e) {
		}
	}

	class ImagePanelMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			model.reset();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}
	}

	class NewImagesListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg) {
			String filename = File.separator + "~";
			JFileChooser newImageChooser = new JFileChooser(new File(filename));
			newImageChooser.setFileFilter(new ImageFileFilter());

			PreviewPanel preview = new PreviewPanel();
			newImageChooser.setAccessory(preview);
			newImageChooser.addPropertyChangeListener(preview);

			if (newImageChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
				File tmp = newImageChooser.getSelectedFile();
				model.setImage(Utilities.loadJPEGFile(tmp));
				model.scaleImage(view.getImagePanel().getWidth(), view
						.getImagePanel().getHeight());
			}
		}
	}

	class NewUrlImagesListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg) {
			String url = JOptionPane.showInputDialog(view, null, "Url input",
					JOptionPane.QUESTION_MESSAGE);
			
			ilw = new ImageLoadWorker(model, url, view.getImagePanel());
			ilw.execute();
		}
	}

	class ExitEntryListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg) {
			exitImages();
		}
	}

	class SaveChangesListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg) {

			int n = JOptionPane.showConfirmDialog(view,
					"Are you sure you want to save changes?", "Warning",
					JOptionPane.YES_NO_OPTION);

			if (n == JOptionPane.YES_OPTION) {

			} else {
				view.setStatus("Unable to save... :(");
			}

		}
	}

	class AboutListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg) {
			// About dialog
		}
	}
}