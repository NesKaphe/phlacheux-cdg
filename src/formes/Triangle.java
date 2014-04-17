package formes;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.Polygon;

/**
 * 
 * @author clement
 *	triangle équilatérale
 */
public class Triangle extends ObjetGeometrique {

	private int taille;//taille d'un coté
	private Point2D.Double translation_centre;//pour sauvegarder position finale ou va se situer le dessin
										//TODO : voir si c'est l'endroit approprié
	
	
	//TODO faire une taille minimum de 2 ou 3 sinon probable bug (raison : division entière de 1 par 2 = 0)
	public Triangle(Point2D.Double centre,int taille) {
		super("triangle", null, Color.yellow, Color.black);
		this.translation_centre = centre;//impossible de positionner le centre directement
										 //cette opération est faite dans generateShape()
		this.taille = taille;
		this.generateShape();
	}
	
	public Triangle(Triangle t) {
		super(t);
		this.taille = t.taille;
		this.translation_centre = t.translation_centre;
	}
	
	@Override
	public Triangle clone() {
		return new Triangle(this);
	}
	
	
	@Override
	public Point2D getCoord() {
		return null;//pas de coin haut gauche
	}

	
	@Override
	public void generateShape() {
		
		//"hauteur" du 3 eme point par rapport aux 2 autres points:
		int h = (int)Math.round((this.taille*Math.sqrt(3.0))/2.0);
		
		//ici on donne le vrai centre (isobarycentre) de la figure :
		Point2D.Double recentre = new Point2D.Double(-((h*2)/3),-(this.taille/2));
		//Point2D recentre = new Point2D.Double(0,0);
		this.setCentre(recentre);
		
		//coordonées des points :
		int coords_x[] = {0,this.taille,(int)this.taille/2};
		int coords_y[] = {h,h,0};
		//forme du triangle généré :
		Polygon triangle = new Polygon(coords_x,coords_y,3);
		
		//on a besoin de translaté la forme pour qu'elle 
		//soit centré aux coordonées passé au constructeur :
		AffineTransform tx = new AffineTransform();
		tx.translate(centre.getX()+translation_centre.getX(),centre.getY()+translation_centre.getY());
		this.forme = tx.createTransformedShape(triangle);
		this.setCentre(translation_centre);
			
	}


	

}
