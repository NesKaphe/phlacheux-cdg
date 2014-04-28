package formes;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Etoile extends ObjetGeometrique{
	
	private int taille; //taille de notre etoile
	private Point2D.Double point_centrale; //point centrale de notre objet
	
	public Etoile(Point2D.Double centre,int taille) {
		super("Etoile", null, Color.yellow, Color.black);
		this.point_centrale = centre;
		this.taille = taille;
		this.generateShape();
	}
	
	public Etoile(Etoile t) {
		super(t);
		this.taille = t.taille;
		this.point_centrale = t.point_centrale;
	}
	
	@Override
	public Etoile clone() {
		return new Etoile(this);
	}
	
	
	@Override
	public Point2D getCoord() {
		return null;
	}

	
	@Override
	public void generateShape(){
		int cote = this.taille; //taille du centre vers un sommet le plus eloigne de l'etoile
		int cote_d = cote*2; // double de la taille d'un cote
		int cote_1 = cote/3; // 2/3 d'un cote
		int cote_2 = cote_1*2; // 1/3 d'un cote
	
		//coordonées des points :
		int coords_x[] = {cote, cote+cote_1, cote_d, cote+cote_1, cote, cote_2, 0, cote_2};
		int coords_y[] = {cote_d, cote+cote_1, cote, cote_2, 0, cote_2, cote, cote+cote_1};
		
		//forme du triangle généré :
		Polygon etoile = new Polygon(coords_x,coords_y,8);
		
		//on a besoin de translaté la forme pour qu'elle 
		//soit centré aux coordonées passé au constructeur :
		AffineTransform tx = new AffineTransform();
		tx.translate( point_centrale.getX()-this.taille, point_centrale.getY()-this.taille);
		this.forme = tx.createTransformedShape(etoile);
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

	public Element toXml(Document domDocument) {
		Element elem = domDocument.createElement("Etoile");
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
