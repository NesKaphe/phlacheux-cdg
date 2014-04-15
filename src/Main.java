import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import formes.Carre;
import formes.Cercle;
import formes.Triangle;
import formes.SegmentDroite;
import formes.Rectangle;
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
		

		//Triangle :
		Triangle tr = new Triangle(new Point2D.Double(150,150),100);//centre bidon
		t.addObjet(tr);

		//Cercle :
		Cercle c = new Cercle("moncercle", new Point2D.Double((int)(Math.random()*301), (int)(Math.random()*301)), 30);
		c.setFillColor(Color.blue);
		c.setStrokeWidth(5);
		
		//Ligne
		Point2D p1 = new Point2D.Double((int)(Math.random()*301),(int)(Math.random()*301));
		Point2D p2 = new Point2D.Double((int)(Math.random()*301),(int)(Math.random()*301));
		SegmentDroite seg = new SegmentDroite("une Droite",p1,p2);
		seg.setStroke(new BasicStroke(7));
		seg.setStrokeColor(Color.red);
		
		//rectangle
		Point2D p3 = new Point2D.Double((int)(Math.random()*301),(int)(Math.random()*301));
		Rectangle rect = new Rectangle("monrectangle", p3, 10, 30);
		rect.setStrokeWidth(2);
		rect.setStrokeColor(Color.green);
		rect.setFillColor(Color.cyan);
		
		//Carre
		Point2D p4 = new Point2D.Double((int)(Math.random()*301),(int)(Math.random()*301));
		Carre carre = new Carre("moncarre", p4, 10);
		carre.setStrokeWidth(6);
		carre.setStrokeColor(Color.green);
		carre.setFillColor(Color.orange);
		
		
		t.addObjet(c);
		t.addObjet(seg);
		t.addObjet(rect);
		t.addObjet(carre);
		t.repaint();

	}
}
