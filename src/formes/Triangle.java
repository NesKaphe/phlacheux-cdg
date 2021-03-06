package formes;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.Polygon;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author clement
 *	triangle équilatérale
 */
public class Triangle extends ObjetGeometrique {

	private int taille;//taille d'un coté
	private Point2D.Double translation_centre;//pour sauvegarder position finale ou va se situer le dessin
	
	
	public Triangle(Point2D.Double centre,int taille) {
		super("Triangle", null, Color.yellow, Color.black);
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
		this.centre = recentre;
		
		//coordonées des points :
		int coords_x[] = {0,this.taille,(int)this.taille/2};
		int coords_y[] = {h,h,0};
		//forme du triangle généré :
		Polygon triangle = new Polygon(coords_x,coords_y,3);
		
		//on a besoin de translaté la forme pour qu'elle 
		//soit centré aux coordonées passé au constructeur :
		AffineTransform tx = new AffineTransform();
		tx.translate(translation_centre.getX()-this.taille/2,translation_centre.getY() - h*2/3);
		this.forme = tx.createTransformedShape(triangle);
		this.centre = translation_centre;
			
	}

	public void setCentre(Point2D.Double centre) {
		this.translation_centre = centre;
		this.generateShape();
	}
	
	public void setTaille(double t){
		this.taille = (int) t;
		this.generateShape();
	}
	
	public double getTaille(){
		return this.taille;
	}
	
	public Element toXml(Document domDocument) {
		Element elem = domDocument.createElement("Triangle");
		elem.setAttribute("taille", String.valueOf(this.taille));
		elem.setAttribute("centreX", String.valueOf(this.centre.getX()));
		elem.setAttribute("centreY", String.valueOf(this.centre.getY()));
		
		//On va maintenant creer un fils par attribut spécial (couleur fond, Trait)
		Element stroke = domDocument.createElement("Trait");
		stroke.setAttribute("epaisseur", String.valueOf(this.getStroke().getLineWidth()));
		stroke.setAttribute("red", String.valueOf(this.getStrokeColor().getRed()));
		stroke.setAttribute("green", String.valueOf(this.getStrokeColor().getGreen()));
		stroke.setAttribute("blue", String.valueOf(this.getStrokeColor().getBlue()));
		
		Element fond = domDocument.createElement("Fond");
		fond.setAttribute("red", String.valueOf(this.getFillColor().getRed()));
		fond.setAttribute("green", String.valueOf(this.getFillColor().getGreen()));
		fond.setAttribute("blue", String.valueOf(this.getFillColor().getBlue()));
		
		elem.appendChild(stroke);
		elem.appendChild(fond);
		
		return elem;
	}
}
