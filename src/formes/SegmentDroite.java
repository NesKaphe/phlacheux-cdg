package formes;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;


public class SegmentDroite extends ObjetGeometrique {

	protected Point2D.Double p1;        //TODO : modifier en Point2D.Double
	protected Point2D.Double p2; //TODO               //
	
	public SegmentDroite(Point2D.Double p1, Point2D.Double p2) {
		super("Segment", new Point2D.Double((p1.getX() + p2.getX())/2, (p1.getY() + p2.getY())/2), null, Color.black);
		this.setPoint(p1, 1);
		this.setPoint(p2, 2);
		this.generateShape();
	}
	
	public SegmentDroite(SegmentDroite sd){
		super(sd);
		this.p1 = sd.p1;
		this.p2 = sd.p2;
	}
	
	@Override
	public SegmentDroite clone() {
		return new SegmentDroite(this);
	}

	public void setPoint(Point2D.Double p, int i) {
		switch(i) {
		case 1:
			this.p1 = p;
			break;
		case 2:
			this.p2 = p;
			break;
		default: //On ne fait rien car un segment n'a que deux points
			break;
		}
		//On recalcule le centre du segment si les deux points sont initialisés
		if(p1 != null && p2 != null)
			this.setCentre(new Point2D.Double((p1.getX() + p2.getX())/2, (p1.getY() + p2.getY())/2));
	}

	public Point2D getCoord() {
		return null;
	}

	public void generateShape() {
		this.forme = new Line2D.Double(this.p1, this.p2);
	}

	public String getInfos() {
		return this.p1.toString()+this.p2.toString();
		
	}
}
