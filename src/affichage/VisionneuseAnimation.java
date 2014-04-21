package affichage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import formes.Rectangle;


import Animations.GestionAnimation;
import Animations.Rotation;

/**
 * Visionneuse d'animation (il faut l'ajouter au sud) f.getContentPane().add("VisionneuseAnimation",BorderLayout.SOUTH);
 * @author clement
 *
 */
public class VisionneuseAnimation extends JScrollPane{

	private JFrame f;
	private JPanel p;
	
	public VisionneuseAnimation(JFrame f,GestionAnimation GA) {
		this.f = f;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		p = new JPanel();//le panel qui va contenir la représentation de l'animation
		p.setSize(new Dimension(1000,1000));
		p.setBackground(Color.gray);
		this.setPreferredSize(new Dimension(screenSize.width,screenSize.height/4));//va prendre 1/4  de la hauteur de l'écran
		this.setViewportView(p);
	}


	
}


class testeVisionAnim{
	public static void main(String[] args) {
		JFrame f= new JFrame("teste de visonneuse Animation");
		f.setPreferredSize(new Dimension(450,450));
		Toile t = new Toile(new Dimension(400,400));
		
		
		GestionAnimation GA = new GestionAnimation(t);
		VisionneuseAnimation vi = new VisionneuseAnimation(f,GA);
		

		
		
		f.getContentPane().add(t,BorderLayout.CENTER);
		f.getContentPane().add(vi,BorderLayout.SOUTH);
		
		
		//dessiner un rectangle dans le GA :
		Rectangle rect = new Rectangle("monrectangle", new Point2D.Double(100,200), 70, 40);
		rect.setStrokeWidth(2);
		rect.setStrokeColor(Color.green);
		rect.setFillColor(Color.cyan);
		Rotation rr1 = new Rotation(0., 125., 0, Math.toRadians(-120),rect.getCentre());
		GA.ajouterComportement(rect, rr1);
		GA.dessinerToile(0.);
		
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		f.pack();
	}
}
