package formes;

import java.awt.geom.Point2D;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Carre extends Rectangle {

	public Carre(Point2D.Double centre, double width) {
		super("Carre", centre, width, width);
	}
	
	public Carre(Carre c) {
		super(c);
	}
	
	public Carre clone(){
		return new Carre(this);
	}
	
	public double getcote(){
		return this.height;
	}
	
	public void setcote(double cote){
		this.setHeight(cote);
		this.setWidth(cote);
		this.generateShape();
	}
	
	public Element toXml(Document domDocument) {
		Element elem = domDocument.createElement("Carre");
		elem.setAttribute("cote", String.valueOf(this.width));
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
