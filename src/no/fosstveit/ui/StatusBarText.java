package no.fosstveit.ui;

import javax.swing.JLabel;

import no.fosstveit.util.Constants;


/**
 * @version 19 okt 2009
 * @author Håvar Aambø Fosstveit
 */

public class StatusBarText extends JLabel {

	private static final long serialVersionUID = -1436538670250152000L;
	
	private Thread t;

	public StatusBarText() {
	}

	public void setStatus(final String status) {
		if (t != null && t.isAlive()) {
			t.interrupt();
		}
		
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					setText("  " + status);
					Thread.sleep(Constants.STATUS_BAR_TIME);
					setText("");
				} catch (InterruptedException e) {
					// System.out.println("Sleep interrupted. Probably new status.");
				}
			}
		});
		t.start();
	}
}
