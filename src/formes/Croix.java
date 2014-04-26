
package formes;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Croix extends ObjetGeometrique{
	
	private int taille; //taille de notre etoile
	private Point2D.Double point_centrale; //point centrale de notre objet
	
	public Croix (Point2D.Double centre,int taille) {
		super("Croix", null, Color.yellow, Color.black);
		this.point_centrale = centre;
		this.taille = taille;
		this.generateShape();
	}
	
	public Croix(Croix t) {
		super(t);
		this.taille = t.taille;
		this.point_centrale = t.point_centrale;
	}
	
	@Override
	public Croix clone() {
		return new Croix(this);
	}
	
	
	@Override
	public Point2D getCoord() {
		return null;
	}

	
	@Override
	public void generateShape(){
		int cote = this.taille; //taille du centre vers un sommet le plus eloigne de l'etoile
		int cote_2 = cote/2; // la moitié d'un cote
		int cote_d = cote*2; // double du cote
		int cote_3 = cote+cote_2; // cote plus la moitier
	
		//coordonées des points :
		int coords_x[] = {0, cote_2, cote_2, cote_3, cote_3, cote_d, cote_d, cote_3, cote_3, cote_2,cote_2, 0};
		int coords_y[] = {cote_2, cote_2, 0, 0, cote_2, cote_2, cote_3, cote_3, cote_d, cote_d, cote_3, cote_3};
		
		//forme du triangle généré :
		Polygon Croix = new Polygon(coords_x,coords_y,12);
		
		//on a besoin de translaté la forme pour qu'elle 
		//soit centré aux coordonées passé au constructeur :
		AffineTransform tx = new AffineTransform();
		tx.translate( point_centrale.getX()-this.taille, point_centrale.getY()-this.taille);
		this.forme = tx.createTransformedShape(Croix);
		this.centre =  point_centrale;;
		
			
	}

	public void setCentre(Point2D.Double centre) {
		this.point_centrale = centre;
		this.generateShape();
	}
	
	public void setTaille(double t){
		this.taille = (int) t;
		this.generateShape();
	}
	
	public double getTaille(){
		return this.taille;
	}
	
}
