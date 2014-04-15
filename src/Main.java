import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import affichage.Toile;


public class Main implements Runnable {

	private Toile t;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Main());
	}

	public void run() {
		JFrame frame = new JFrame("Visionneuse");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		t = new Toile(new Dimension(300,300));
		frame.getContentPane().add(t);
		frame.pack();
		frame.setVisible(true);
		
		ActionListener actionListener = new ActionListener() {
		      public void actionPerformed(ActionEvent actionEvent) {
		    	  //Ici du code pour ajouter des objets a la toile
		      }
		    };
		    
		Timer timer = new Timer(100/60, actionListener);
		timer.start();
	}
}
