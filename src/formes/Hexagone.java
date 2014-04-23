package formes;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Hexagone extends ObjetGeometrique{
	
	private int taille; //hauteur du sapin
	private Point2D.Double point_centrale; //point centrale de notre objet
	
	public Hexagone(Point2D.Double centre,int taille) {
		super("Hexagone", null, Color.yellow, Color.black);
		this.point_centrale = centre;
		this.taille = taille;
		this.generateShape();
	}
	
	public Hexagone(Hexagone t) {
		super(t);
		this.taille = t.taille;
		this.point_centrale = t.point_centrale;
	}
	
	@Override
	public Hexagone clone() {
		return new Hexagone(this);
	}
	
	
	@Override
	public Point2D getCoord() {
		return null;
	}

	
	@Override
	public void generateShape(){
		
		int cote = this.taille; //taille du centre vers un sommet le plus eloigne de l'etoile
		int cote_2 = cote/2; // 1/2 d'un cote
		int t = (int)Math.sqrt((double)(cote *cote) - (cote_2*cote_2));
	
		//coordonées des points :
		int coords_x[] = {0,cote_2,cote+cote_2,cote+cote,cote+cote_2,cote_2};
		int coords_y[] = {t,0,0,t,t+t,t+t};
		
		//forme du triangle généré :
		Polygon hexagone = new Polygon(coords_x,coords_y,6);
				
		//on a besoin de translaté la forme pour qu'elle 
		//soit centré aux coordonées passé au constructeur :
		AffineTransform tx = new AffineTransform();
		tx.translate( point_centrale.getX()-this.taille, point_centrale.getY()-this.taille);
		this.forme = tx.createTransformedShape(hexagone);
		this.centre =  point_centrale;;
		
			
	}

	public void setCentre(Point2D.Double centre) {
		this.point_centrale = centre;
		this.generateShape();
	}
	
	public void setTaille(double t){
		this.taille = (int) t;
	}
	
	public double getTaille(){
		return this.taille;
	}
	
}
