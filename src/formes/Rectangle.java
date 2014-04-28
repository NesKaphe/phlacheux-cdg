package formes;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class Rectangle extends ObjetGeometrique {

	protected double width;
	protected double height;
	
	public Rectangle(String nom, Point2D.Double centre, double width, double height) {
		super(nom, centre, null, Color.black);
		this.setWidth(width);
		this.setHeight(height);
		this.generateShape();
	}
	
	public Rectangle(Rectangle r){
		super(r);
		this.width = r.width;
		this.height = r.height;
	}
	
	@Override
	public Rectangle clone() {
		return new Rectangle(this);
	}
	
	public void setWidth(double width) {
		if(width > 0) {
			this.width = width;
		}
		this.generateShape();
	}
	
	public void setHeight(double height) {
		if(height > 0) {
			this.height = height;
		}
		this.generateShape();		
	}
	
	public double getWidth() {
		return this.width;
	}
	
	public double getHeight() {
		return this.height;
	}

	public Point2D getCoord() {
		Point2D p = new Point2D.Double(this.centre.getX() - this.width/2, this.centre.getY() - this.height/2);
		return p;
	}

	public void generateShape() {
		Point2D p = this.getCoord();
		this.forme = new Rectangle2D.Double(p.getX(),p.getY(), this.width, this.height);
	}

	public Element toXml(Document domDocument) {
		Element elem = domDocument.createElement("Rectangle");
		elem.setAttribute("Width", String.valueOf(this.width));
		elem.setAttribute("Height", String.valueOf(this.height));
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
