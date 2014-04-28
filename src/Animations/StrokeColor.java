package Animations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;

import javax.swing.JFrame;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import affichage.Toile;
import formes.Rectangle;

public class StrokeColor extends ColorAnimation {

	public StrokeColor(Double t_debut, Double t_fin, int easing, int incrR, int incrG, int incrB) {
		super(t_debut, t_fin, easing, "StrokeColor", incrR, incrG, incrB);
	}

	public StrokeColor(StrokeColor anim) {
		super(anim);
	}

	@Override
	public int[] getStrokeColor(Double t_courant) {
		return this.getColor(t_courant);
	}


	public Element toXml(Document domDocument) {
		Element elem = domDocument.createElement("StrokeColor");
		
		elem.setAttribute("debut", this.getT_debut().toString());
		elem.setAttribute("fin", this.getT_fin().toString());
		elem.setAttribute("easing", String.valueOf(this.getEasing()));
		elem.setAttribute("incrR", String.valueOf(this.r));
		elem.setAttribute("incrG", String.valueOf(this.g));
		elem.setAttribute("incrB", String.valueOf(this.b));
		
		return elem;
	}
}

class testeStrokeColor{

	//main de teste local
	public static void main(String[] args) {
			
		//teste visuel de la rotation :
		System.out.println("\n\n\nconsole :\n");
		JFrame frame = new JFrame("Visionneuse");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toile t = new Toile(new Dimension(300,300));
		frame.getContentPane().add(t);
		frame.pack();
		frame.setVisible(true);
		GestionAnimation gest = new GestionAnimation(t);
		
		
		//ajout d'un rectangle :
		Rectangle rect = new Rectangle("monrectangle", new Point2D.Double(100,200), 70, 40);
		rect.setStrokeWidth(10);
		rect.setStrokeColor(Color.green);
		rect.setFillColor(Color.cyan);
		
		CompositeAnimation ca = new CompositeAnimation(0., 150., 0);
		StrokeColor fr = new StrokeColor(0., 30., 0, 90, -25, -70);
		StrokeColor fr2 = new StrokeColor(45., 90., 0, -40, -100, -80);
		ca.add(fr);
		ca.add(fr2);
		gest.ajouterComportement(rect, ca);
		
		for(int j=0;j<10;j++)
		{
			for(double i=0.;i<200.;i+=1.){
				gest.dessinerToile(i);
				//rr2.AngleInfo();
				
				try {
					//Thread.sleep((long) (1000));
					Thread.sleep((long) (100));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("fin Boucle 1");
			try {
				Thread.sleep((long) (5000));
			} catch (InterruptedException e) {
				// TODO Bloc catch généré automatiquement
				e.printStackTrace();
			}
		}
		
	}
}