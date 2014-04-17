package Animations;

import java.awt.geom.AffineTransform;

public class StrokeWidth extends Animation {

	private float strokeWidthIncrement;
	
	public StrokeWidth(Double t_debut, Double t_fin, int easing, float strokeWidthIncrement) {
		super(t_debut, t_fin, easing, "StrokeWidth");
		this.strokeWidthIncrement = strokeWidthIncrement;
	}

	
	public AffineTransform getAffineTransform(Double t_courant) {	
		return null;
	}
	
	public Float getWidthStroke(double t_courant) {
		Double pu = this.getPourun(t_courant);
		//si pu est négatif c'est que notre temps courant n'est pas bon
		if (pu < 0.0)
			return null;
		System.out.println("\t"+pu * this.strokeWidthIncrement);
		return (float) (pu * this.strokeWidthIncrement);
	}
}
