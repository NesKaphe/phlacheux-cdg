package formes;

import java.awt.geom.Point2D;

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
}
