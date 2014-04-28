package formes;


import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Cercle extends ObjetGeometrique {

	protected double rayon;
	
	public Cercle(Point2D.Double centre, double rayon) {
		super("Cercle", centre, Color.black, Color.red);
		this.setRayon(rayon);
		this.generateShape();
	}
	
	public Cercle(Cercle c){
		super(c);
		this.rayon = c.rayon;
	}
	
	@Override
	public Cercle clone() {
		return new Cercle(this);
	}
	
	public Point2D getCoord() {
		return new Point2D.Double(this.centre.getX() - this.rayon, this.centre.getY() - this. rayon);
	}
	
	public void generateShape() {
		Point2D p = this.getCoord();
		this.forme = new Ellipse2D.Double(p.getX(), p.getY(), 2*rayon, 2*rayon);
	}

	
	public void setRayon(double rayon) {
		if(rayon > 0)
			this.rayon = rayon;
		else
			this.rayon = 0;
		this.generateShape();
	}
	
	public double getRayon() {
		return this.rayon;
	}

	public Element toXml(Document domDocument) {
		Element elem = domDocument.createElement("Cercle");
		elem.setAttribute("Rayon", String.valueOf(this.rayon));
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
