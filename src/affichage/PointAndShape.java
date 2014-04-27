package affichage;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * class qui permet de réunir un point et sa forme visuel
 * @author clement
 *
 */
public class PointAndShape{
	
	private Point point;
	private Rectangle2D rect;
	
	public PointAndShape(Point point) {
		this.point = point;
		rect = new Rectangle2D.Double(point.x-5,point.y-5,10,10);//rectangle de taille 10x10 le centre correspond au point
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
		this.rect = new Rectangle2D.Double(point.x-5,point.y-5,10,10);//regénère le dessin 
	}

	public Rectangle2D getRect() {
		return rect;
	}
	
	/*
	 * pour savoir si le rect contien le point passé en paramètre
	 */
	public boolean contains(Point point){
		return rect.contains(point);
	}
	
}
