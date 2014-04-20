package Animations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import javax.swing.JFrame;
import affichage.Toile;//juste pour teste
import formes.Rectangle;
import formes.SegmentDroite;

/**
 * 
 * @author clement
 *Animation simple : Rotation
 */
public class Rotation extends Animation{

	private Double angle;//sens de rotation en radian
	private Point2D.Double centre;//centre de rotation
	private Double a_courrant = Math.toRadians(9999999.);//mis en variable de classe juste pour la méthode AngleInfo()
	Double ttt = 0.;//debug
	
	public Rotation(Double t_debut, Double t_fin, int easing,Double angle,Point2D.Double centre) {
		super(t_debut, t_fin, easing, "rotation");
		this.angle = angle;
		this.centre = centre;
	}
	
	public Rotation(Rotation r){
		super(r.getT_debut(), r.getT_fin(), 0, "rotation");
		this.angle = r.getAngle();
		this.centre = r.getCentre();
	}
	
	@Override
	public AffineTransform getAffineTransform(double t_courant) {
		ttt = t_courant; 
		Double pu = this.getPourun(t_courant);
		//si pu est négatif c'est que notre temps courant n'est pas bon
		if (pu <0.0)
			return null;
		a_courrant = this.angle * pu;//TODO : plustard utiliser les easing functions( ajouter : *easing)
		//System.out.println("angle vaux ="+Math.round(Math.toDegrees(a_courrant))+"°  \tid="+getId());//info dev pour le teste
		AffineTransform at = new AffineTransform();
		at.setToRotation(a_courrant,centre.x,centre.y);
		//at.setToRotation(a_courrant);
		return at;
	}
	
	/**
	 * affiche les informations concernant l'angle
	 */
	public void AngleInfo(){
		System.out.print("angle vaux ="+Math.round(Math.toDegrees(a_courrant))+"°  \tid="+getId()+"\tpourcent="+this.getPourun(ttt)*100+"%");//info dev pour le teste
		System.out.println("\t\tt_deb ="+getT_debut()+"  t_fin="+getT_fin()+"  t_courant="+ttt);
	}
	
	/**
	 * Getteurs et Setteurs
	 */
	
	public Double getAngle() {
		return angle;
	}

	public Point2D.Double getCentre() {
		return centre;
	}

	public void setCentre(Point2D.Double centre) {
		this.centre = centre;
	}
	

	
}











/**
 * pour tester la rotation
 */


class testeRotation{

	//main de teste local
	public static void main(String[] args) {
		

		
		CompositeAnimation ca = new CompositeAnimation(0., 30., 0);
		CompositeAnimation ca2 = new CompositeAnimation(2., 5., 0);
		Rotation r1 = new Rotation(0., 30., 0, Math.toRadians(30.0),new Point2D.Double(0,0));
		Rotation r2 = new Rotation(3., 25., 0, Math.toRadians(30.0),new Point2D.Double(0,0));
		//teste affichage
		System.out.println("ca :"+ca+"\nr1"+r1+"\nr2"+r2+"\n\n\n");
		
		//teste add (la version actuelle peux boucler add de soit même ou d'un parent)
		ca.add(r1);
		ca.add(r2);
		
		ca2.add(ca);
		System.out.println("r1.getMyLevel()="+r1.getMyLevel());
		System.out.println("r2.getMyLevel()="+r2.getMyLevel());
		System.out.println("ca.getMyLevel() ="+ca.getMyLevel());
		System.out.println("ca2.getMyLevel()="+ca2.getMyLevel());
		
		//teste remove :
		System.out.println("\n1---r1 parent="+r1.getParent()+"\nca.remove(r1)="+ca.remove(r1));
		System.out.println("2---r1 parent="+r1.getParent()+"\nca.remove(r1)="+ca.remove(r1));
		
		//teste d'une rotation toute seule :
		Rotation r3 = new Rotation(0., 100., 0, Math.toRadians(180.0),new Point2D.Double(0,0));//temps_debut = 0% temps_fin = 100%
		//teste d'un affine transforme qui marche pas :
		r3.getAffineTransform(101.);
		//teste d'un affine transforme qui marche ça doit afficher 45.0°
		r3.getAffineTransform(25.);//45°
		r3.getAffineTransform(50.);//90°
		
		
		
		
		
		
		//teste visuel de la rotation :
		System.out.println("\n\n\nconsole :\n");
		JFrame frame = new JFrame("Visionneuse");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toile t = new Toile(new Dimension(300,300));
		frame.getContentPane().add(t);
		frame.pack();
		frame.setVisible(true);
		GestionAnimation gest = new GestionAnimation(t);
		
		
		//ajout d'un triangle
		/*
		Triangle tr = new Triangle(new Point2D.Double(150,150),60);
		Rotation rtr = new Rotation(0., 100., 0, Math.toRadians(360),tr.getCentre());
		gest.ajouterComportement(tr, rtr);
		*/
		//ajout d'un rectangle :
		Rectangle rect = new Rectangle("monrectangle", new Point2D.Double(100,200), 70, 40);
		rect.setStrokeWidth(2);
		rect.setStrokeColor(Color.green);
		rect.setFillColor(Color.cyan);
		
		CompositeAnimation ca3 = new CompositeAnimation(0., 200., 0);
		Rotation rr1 = new Rotation(0., 100., 0, Math.toRadians(-150),rect.getCentre());
		//Rotation rr2 = new Rotation(75., 200., 0, Math.toRadians(+100),rect.getCentre());
		Rotation rr2 = new Rotation(150., 250., 0, Math.toRadians(+100),rect.getCentre());
		ca3.add(rr1);
		ca3.add(rr2);	
		gest.ajouterComportement(rect, ca3);
		
		//ajout de segment :
		SegmentDroite seg1 = new SegmentDroite("une Droite",new Point2D.Double(150,100),new Point2D.Double(250,100));
		Rotation rs1 = new Rotation(rr1);
		rs1.setCentre(seg1.getCentre());//repositionner le centre de rotation
		gest.ajouterComportement(seg1,rs1);
		
		SegmentDroite seg2 = new SegmentDroite("une Droite",new Point2D.Double(10,100),new Point2D.Double(110,100));
		Rotation rs2 = new Rotation(rr2);
		rs2.setCentre(seg2.getCentre());//repositionner le centre de rotation
		gest.ajouterComportement(seg2,rs2);
		
		for(int j=0;j<10;j++)
		{
			for(double i=0.;i<500.;i+=5.){
				gest.dessinerToile(i);
				rr2.AngleInfo();
				
				try {
					//Thread.sleep((long) (1000));
					Thread.sleep((long) (100));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
