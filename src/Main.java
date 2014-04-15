import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import formes.Cercle;
import formes.Triangle;
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
		
		//teste du triangle :
		Triangle tr = new Triangle(new Point2D.Double(150,150),100);//centre bidon
		//Triangle tr = new Triangle(new Point2D.Double(0,0),100);//centre bidon
		Cercle c = new Cercle("moncercle", new Point2D.Double((int)(Math.random()*301), (int)(Math.random()*301)), 30);
		t.addObjet(tr);
		t.addObjet(c);


		/*
		 //petite annimation random
		ActionListener actionListener = new ActionListener() {
		      public void actionPerformed(ActionEvent actionEvent) {
		    	  //Ici du code pour ajouter des objets a la toile
		    	  Cercle c = new Cercle("moncercle", new Point2D.Double((int)(Math.random()*301), (int)(Math.random()*301)), 30);
		    	  c.setFillColor(Color.blue);
		    	  t.addObjet(c);
		    	  t.repaint();
		    	  t.demanderViderListe();
		      }
		    };
		   
		Timer timer = new Timer(1000/10, actionListener); //10 images par seconde
		timer.start();
		*/
	}
}
