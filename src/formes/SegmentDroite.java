package formes;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class SegmentDroite extends ObjetGeometrique {

	protected Point2D.Double p1;
	protected Point2D.Double p2;
	
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
	
	public Element toXml(Document domDocument) {
		Element elem = domDocument.createElement("Segment");
		elem.setAttribute("x1", String.valueOf(this.p1.getX()));
		elem.setAttribute("y1", String.valueOf(this.p1.getY()));
		elem.setAttribute("x2", String.valueOf(this.p2.getX()));
		elem.setAttribute("y2", String.valueOf(this.p2.getY()));
		
		//On va maintenant creer un fils par attribut spécial (couleur fond, Trait)
		Element stroke = domDocument.createElement("Trait");
		stroke.setAttribute("epaisseur", String.valueOf(this.getStroke().getLineWidth()));
		stroke.setAttribute("red", String.valueOf(this.getStrokeColor().getRed()));
		stroke.setAttribute("green", String.valueOf(this.getStrokeColor().getGreen()));
		stroke.setAttribute("blue", String.valueOf(this.getStrokeColor().getBlue()));
		
		elem.appendChild(stroke);
		
		return elem;
	}
}
