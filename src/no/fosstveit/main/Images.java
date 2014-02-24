package no.fosstveit.main;

import javax.swing.UIManager;

import no.fosstveit.controller.Controller;
import no.fosstveit.model.Model;
import no.fosstveit.view.View;




/**
 * @version 19 okt 2009
 * @author Håvar Aambø Fosstveit
 */
public class Images {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Model model = new Model();
		
		View view = new View(model);
		
		@SuppressWarnings("unused")
		Controller controller = new Controller(model, view);
	}
}
