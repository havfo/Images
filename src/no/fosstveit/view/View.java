package no.fosstveit.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import no.fosstveit.model.Model;
import no.fosstveit.ui.ImagePanel;
import no.fosstveit.ui.StatusBarText;

/**
 * @version 19 okt 2009
 * @author Håvar Aambø Fosstveit
 */

public class View extends JFrame implements Observer {
	private static final long serialVersionUID = -8246157902542015947L;

	private Model model;
	private ImagePanel imagePanel;
	private JMenuBar mainMenu;
	private JMenu fileMenu;
	private JMenuItem newEntryItem;
	private JMenuItem newUrlEntryItem;
	private JMenuItem saveChangesItem;
	private JMenuItem exitItem;
	private JMenu editMenu;
	private JMenu helpMenu;
	private JMenuItem aboutItem;
	private JPanel statusBar;
	private StatusBarText statusBarText;

	public View(Model model) {
		this.model = model;

		this.model.addObserver(this);
		initGUI();
	}

	private void initGUI() {
		setTitle("Images");

		statusBar = new JPanel();
		statusBar.setLayout(new BorderLayout());
		statusBar.setBackground(new Color(217, 217, 217));
		statusBar.setBorder(BorderFactory.createEtchedBorder());
		statusBarText = new StatusBarText();
		statusBarText.setPreferredSize(new Dimension(300, 15));
		statusBar.add(statusBarText, BorderLayout.WEST);

		mainMenu = new JMenuBar();

		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		newEntryItem = new JMenuItem("Open image");
		newEntryItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				KeyEvent.CTRL_MASK));
		fileMenu.add(newEntryItem);
		newUrlEntryItem = new JMenuItem("Image from URL");
		newUrlEntryItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_MASK));
		fileMenu.add(newUrlEntryItem);
		saveChangesItem = new JMenuItem("Save changes");
		saveChangesItem.setEnabled(false);
		saveChangesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				KeyEvent.CTRL_MASK));
		fileMenu.add(saveChangesItem);
		fileMenu.add(new JSeparator());
		exitItem = new JMenuItem("Exit");
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				KeyEvent.CTRL_MASK));
		fileMenu.add(exitItem);

		mainMenu.add(fileMenu);

		editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);

		mainMenu.add(editMenu);

		mainMenu.add(Box.createHorizontalGlue());

		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		aboutItem = new JMenuItem("About...");
		helpMenu.add(aboutItem);

		mainMenu.add(helpMenu);

		imagePanel = new ImagePanel(model);

		setLayout(new BorderLayout());
		add(mainMenu, BorderLayout.NORTH);

		add(imagePanel, BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);
		setSize(800, 600);
		
		setVisible(true);
	}
	
	public ImagePanel getImagePanel() {
		return imagePanel;
	}

	public void setStatus(String status) {
		statusBarText.setStatus(status);
	}

	public void addNewImagesListener(ActionListener nip) {
		newEntryItem.addActionListener(nip);
	}
	
	public void addNewUrlListener(ActionListener nul) {
		newUrlEntryItem.addActionListener(nul);
	}

	public void addExitEntryListener(ActionListener eel) {
		exitItem.addActionListener(eel);
	}

	public void addSaveChangesListener(ActionListener scl) {
		saveChangesItem.addActionListener(scl);
	}
	
	public void addAboutListener(ActionListener al) {
		aboutItem.addActionListener(al);
	}
	
	public void addImagePanelListener(ComponentListener cl) {
		imagePanel.addComponentListener(cl);
	}
	
	public void addImagePanelMouseListener(MouseListener ml) {
		imagePanel.addMouseListener(ml);
	}

	@Override
	public void update(Observable ob, Object o) {
		saveChangesItem.setEnabled(model.hasChanged());
		statusBarText.setStatus(model.getStatus());
	}
}