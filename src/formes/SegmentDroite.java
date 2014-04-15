package formes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

public class SegmentDroite extends ObjetGeometrique {

	protected Point2D p1;
	protected Point2D p2;
	
	public SegmentDroite(String nom, Point2D p1, Point2D p2) {
		super(nom, new Point2D.Double((p1.getX() + p2.getX())/2, (p1.getY() + p2.getY())/2), null, Color.black);
		this.setPoint(p1, 1);
		this.setPoint(p2, 2);
		this.generateShape();
	}

	private void setPoint(Point2D p, int i) {
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
	}

	public Point2D getCoord() {
		return null;
	}

	public void dessineGraphics(Graphics2D g) {

	}

	public void generateShape() {
		this.forme = new Line2D.Double(this.p1, this.p2);
	}

	@Override
	public void transTranslation(List<Point2D> listePoints, int easingFunction,
			int tDepart, int tFin, int tCourant) throws Exception {
		// TODO Stub de la méthode généré automatiquement

	}

	@Override
	public void transRotationCentrale(int sens, int easingFunction,
			int tDepart, int tFin, int tCourant) throws Exception {
		// TODO Stub de la méthode généré automatiquement

	}

	@Override
	public void transRotationExt(Point2D centre, int sens, int easingFunction,
			int tDepart, int tFin, int tCourant) throws Exception {
		// TODO Stub de la méthode généré automatiquement

	}

	@Override
	public void transStroke(BasicStroke finalStroke, int easingFunction,
			int tDepart, int tFin, int tCourant) throws Exception {
		// TODO Stub de la méthode généré automatiquement

	}

}
