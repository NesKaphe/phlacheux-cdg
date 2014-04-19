package formes;


import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

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
	}



}
