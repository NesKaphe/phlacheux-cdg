package formes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;
import java.awt.Polygon;
/**
 * 
 * @author clement
 *	triangle équilatérale
 */
public class Triangle extends ObjetGeometrique {

	private int taille;//taille d'un coté
	private Point2D translation_centre;//pour sauvegarder position finale ou va se situer le dessin
	
	
	//TODO faire une taille minimum de 2 ou 3 sinon probable bug (raison : division entière de 1 par 2 = 0)
	public Triangle(Point2D centre,int taille) {
		super("triangle", null, Color.yellow, Color.black);
		this.translation_centre = centre;//impossible de positionner le centre directement
										 //cette opération est faite dans generateShape()
		this.taille = taille;
		this.generateShape();
	}

	@Override
	public Point2D getCoord() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dessineGraphics(Graphics2D g) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void generateShape() {
		
		//"hauteur" du 3 eme point par rapport aux 2 autres points:
		int h = (int)Math.round((this.taille*Math.sqrt(3.0))/2.0);
		
		//ici on donne le vrai centre (isobarycentre) de la figure :
		Point2D recentre = new Point2D.Double(-((h*2)/3),-(this.taille/2));
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
			
	}
	
	
	@Override
	public void transTranslation(List<Point2D> listePoints, int easingFunction,
			int tDepart, int tFin, int tCourant) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void transRotationCentrale(int sens, int easingFunction,
			int tDepart, int tFin, int tCourant) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void transRotationExt(Point2D centre, int sens, int easingFunction,
			int tDepart, int tFin, int tCourant) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void transStroke(BasicStroke finalStroke, int easingFunction,
			int tDepart, int tFin, int tCourant) throws Exception {
		// TODO Auto-generated method stub

	}

}
