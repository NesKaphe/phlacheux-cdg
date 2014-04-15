package formes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.List;

public class Cercle extends ObjetGeometrique {

	protected double rayon;
	
	public Cercle(String nom, Point2D centre, double rayon) {
		this.setNom(nom);
		this.setCentre(centre);
		this.setRayon(rayon);
		this.setDimensions((int)Math.floor(rayon), (int)Math.floor(rayon));
		this.size = new Dimension((int)Math.floor(rayon), (int)Math.floor(rayon)); //Utilité ?
		
		this.strokeColor = Color.black;
		this.fillColor = null; //Couleur de remplissage par defaut ?
		
		this.generateShape();
		this.trans = new AffineTransform();
	}
	
	public Point2D getCoord() {
		return new Point2D.Double(this.centre.getX() - this.rayon, this.centre.getY() - this. rayon);
	}
	
	public void dessineGraphics(Graphics2D g) {
		//Ca ne devrait pas etre a la forme de se dessiner mais a la toile de demander la forme de l'objet
	}
	
	public void generateShape() {
		Point2D p = this.getCoord();
		this.forme = new Ellipse2D.Double(p.getX(), p.getY(), rayon, rayon);
	}

	public void transTranslation(List<Point2D> listePoints, int easingFunction,
			int tDepart, int tFin, int tCourant) throws Exception {
		
	}

	public void transRotationCentrale(int sens, int easingFunction,
			int tDepart, int tFin, int tCourant) throws Exception {
		
	}

	public void transRotationExt(Point2D centre, int sens, int easingFunction,
			int tDepart, int tFin, int tCourant) throws Exception {
		// TODO Stub de la méthode généré automatiquement
		
	}

	public void transStroke(BasicStroke finalStroke, int easingFunction,
			int tDepart, int tFin, int tCourant) throws Exception {
		
	}
	
	public void setRayon(double rayon) {
		if(rayon > 0)
			this.rayon = rayon;
		else
			this.rayon = 0;
	}

}
