package formes;

import java.awt.geom.Point2D;

public class Carre extends Rectangle {

	public Carre(String nom, Point2D.Double centre, double width) {
		super(nom, centre, width, width);
	}
	
	public Carre(Carre c) {
		super(c);
	}
	
	public Carre clone(){
		return new Carre(this);
	}
}
