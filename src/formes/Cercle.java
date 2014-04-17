package formes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.List;

public class Cercle extends ObjetGeometrique {

	protected double rayon;
	
	public Cercle(String nom, Point2D.Double centre, double rayon) {
		super(nom, centre, Color.black, Color.red);
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
		this.forme = new Ellipse2D.Double(p.getX(), p.getY(), rayon, rayon);
	}

	
	public void setRayon(double rayon) {
		if(rayon > 0)
			this.rayon = rayon;
		else
			this.rayon = 0;
	}



}
