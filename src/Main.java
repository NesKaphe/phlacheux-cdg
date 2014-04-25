import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import affichage.Edition;
import affichage.LecteurAnimation;


public class Main implements Runnable {
	
	public static void main(String[] args) {
		try {
		    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch(Exception e) {
		    e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Main());
	}

	public void run() {
		Edition edition = new Edition();
		edition.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		edition.setVisible(true);
		edition.pack();
	}
}
