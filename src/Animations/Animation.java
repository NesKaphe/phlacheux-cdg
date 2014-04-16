package Animations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JFrame;

import formes.Rectangle;
import formes.Triangle;

import affichage.Toile;//juste pour le teste


//une Animation simple est une feuille
public abstract class Animation {
	//id de l'animation :
	private static int cpt_id = 0;
	private int id;
	private String type;//type d'animation
	
	protected Animation parent;//pour connaitre sont parent
	private Double t_debut;
	private Double t_fin;
	private int easing;//TODO : pour l'instant c'est un int plus tard on fera une vrai classe 
	public Animation(Double t_debut,Double t_fin,int easing,String type) {
		this.t_debut = t_debut;
		this.t_fin = t_fin;
		this.easing = easing;
		this.parent = null;
		this.type = type;
		this.id = this.cpt_id++;

	}
	
	/**
	 * AffineTransform getAffineTransform(Double t_courant) :
	 * ------------------------------------------------------
	 * prendre en paramètre le temps courant est retourne la transformation
	 * affine à cette instant si il n'y a aucune transformation retourne null
	 * @param t_courant
	 * @return
	 */
	public abstract AffineTransform getAffineTransform(Double t_courant);
	
	/**
	 * boolean tmpOk(Double tmp):
	 * -------------------------
	 * retourne vrai si le temps tmp est dans l'intervalle 
	 * de temps de l'animation
	 * 
	 * @param tmp
	 * @return
	 */
	public boolean tmpOk(Double tmp){
		return (tmp>=this.t_debut)&&(tmp<=this.t_fin);
	}
	
	/**
	 * retourne la propositon de temps que l'animation
	 * à parcouru "pour un". Autrement dit c'est équivalent
	 * à "pourcent" sauf que c'est "pourun".
	 * Si le temps "t_courant" est en dehord des limites une valeur négative est renvoyé.
	 *
	 * @param tmp
	 * @return
	 */
	public Double getPourun(Double t_courant){
		if (!tmpOk(t_courant))
			return -1.;
		return (t_courant / ( t_fin - t_debut));
	}
	
	/**
	 * retourne notre niveau dans l'arbre
	 * 0 = nous somme la racine
	 * @return
	 */
	public int getMyLevel(){
		Animation a_parent = this.getParent();//a_parent = animation parent
		//premier cas particulier nous sommes la racine :
		if (a_parent == null)
			return 0;
		int i=0;
		//boucle jusqu'à la racine
		for (i=1;;i++){
			if(a_parent.getParent() == null)
				break;
			else
				a_parent = a_parent.getParent();//passé a l'aïeux suivant
		}
		return i;
	}
	
	/**
	 * 
	 * Getteur et Setter et toString
	 * 
	 */
	
	public Animation getParent() {
		return parent;
	}
	
	public String getType() {
		return type;
	}

	protected void setParent(Animation parent) {
		this.parent = parent;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Animation [id=" + id + ", type=" + type + ", parent=" + parent
				+ ", t_debut=" + t_debut + ", t_fin=" + t_fin + ", easing="
				+ easing + "]";
	}
	
}


class CompositeAnimation extends Animation{
	
	private ArrayList<Animation> ChildAnimations;//list des animations enfants
	
	
	public CompositeAnimation(Double t_debut,Double t_fin,int easing){
		super(t_debut,t_fin,easing,"composite");
		this.ChildAnimations = new ArrayList<Animation>();
		//par défaut notre parent est à null car nous ne somme relier à personne
	}
	
	
	public boolean add(Animation a){
		//TODO : pour l'instant on ajoute sans discrimination à faire plus tard!!!
		//TODO : PROBLEME :la version actuelle peux boucler add de soit même ou d'un parent a exclure
		//TODO : solution faire une méthode isMyparent pour empècher qu'il y ait des boucles avec les parents
		//TODO : tester isMyParent(a)
		
		a.setParent(this);//modification du parent
		return this.ChildAnimations.add(a);
	}
	
	public boolean remove(Animation a){
		//TODO : éventuellement faire un message contenant tout ce qui à été supprimé
		a.setParent(null);//on lui détruit sa référence sur le parent
		return this.ChildAnimations.remove(a);
	}



	@Override
	public AffineTransform getAffineTransform(Double t_courant) {
		//si le temps demandé n'est pas dans notre intervalle 
		//retourne null imédiatement
		if(!tmpOk(t_courant))
			return null;
		
		AffineTransform at_retour = new AffineTransform();//va contenir les transformations
		//parcourir la liste des enfants pour connaitre toute leurs transformations :
		for(Animation a : this.ChildAnimations){
			//si notre enfant est conserné :
			if (a.tmpOk(t_courant)){
				AffineTransform atmp = a.getAffineTransform(t_courant);
				if (atmp != null)
					at_retour.concatenate(atmp);
			}
		}
		return at_retour;
	}
}

/**
 * 
 * @author clement
 *Animation simple : Rotation
 */
class Rotation extends Animation{

	private Double angle;//sens de rotation en radian
	private Point2D.Double centre;//centre de rotation

	public Rotation(Double t_debut, Double t_fin, int easing,Double angle,Point2D.Double centre) {
		super(t_debut, t_fin, easing, "rotation");
		this.angle = angle;
		this.centre = centre;
	}

	@Override
	public AffineTransform getAffineTransform(Double t_courant) {
		Double pu = this.getPourun(t_courant);
		//si pu est négatif c'est que notre temps courant n'est pas bon
		if (pu <0.0)
			return null;
		Double a_courrant = this.angle * pu;//TODO : plustard utiliser les easing functions( ajouter : *easing)
		System.out.println("angle vaux ="+Math.toDegrees(a_courrant));//info dev pour le teste
		AffineTransform at = new AffineTransform();
		at.setToRotation(a_courrant,centre.x,centre.y);
		//at.setToRotation(a_courrant);
		return at;
	}
	
	public Double getAngle() {
		return angle;
	}


	
}

/**
 * 
 */


class teste{

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
		Triangle tr = new Triangle(new Point2D.Double(150,150),60);
		Rotation rtr = new Rotation(0., 100., 0, Math.toRadians(360),tr.getCentre());
		gest.ajouterComportement(tr, rtr);

		//ajout d'un rectangle :
		Rectangle rect = new Rectangle("monrectangle", new Point2D.Double(100,200), 70, 40);
		rect.setStrokeWidth(2);
		rect.setStrokeColor(Color.green);
		rect.setFillColor(Color.cyan);
		Rotation rr = new Rotation(0., 100., 0, Math.toRadians(-110),rect.getCentre());
		gest.ajouterComportement(rect, rr);
		
		for(int j=0;j<10;j++)
		{
			for(double i=0.;i<100.;i+=0.01)
				gest.dessinerToile(i);
		}
	}
}
