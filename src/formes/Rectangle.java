package formes;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


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
	
	private void setWidth(double width) {
		if(width > 0) {
			this.width = width;
		}
	}
	
	private void setHeight(double height) {
		if(height > 0) {
			this.height = height;
		}
		
	}

	public Point2D getCoord() {
		Point2D p = new Point2D.Double(this.centre.getX() - this.width/2, this.centre.getY() - this.height/2);
		return p;
	}

	public void generateShape() {
		Point2D p = this.getCoord();
		this.forme = new Rectangle2D.Double(p.getX(),p.getY(), this.width, this.height);
	}



}
