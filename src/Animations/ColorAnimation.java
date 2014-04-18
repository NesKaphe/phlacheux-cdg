package Animations;

import java.awt.geom.AffineTransform;

public abstract class ColorAnimation extends Animation {

	int r,g,b;
	
	public ColorAnimation(Double t_debut, Double t_fin, int easing, String type, int incrR, int incrG, int incrB) {
		super(t_debut, t_fin, easing, type);
		this.r = incrR;
		this.g = incrG;
		this.b = incrB;
	}

	@Override
	public AffineTransform getAffineTransform(Double t_courant) {
		return null;
	}

	@Override
	public Float getWidthStroke(double t_courant) {
		return null;
	}

	protected int[] getColor(Double t_courant) {
		Double pu = this.getPourun(t_courant);
		//si pu est négatif c'est que notre temps courant n'est pas bon
		if (pu < 0.0)
			return null;
		
		int r = (int) Math.round((this.r * pu));
		int g = (int) Math.round((this.g * pu));
		int b = (int) Math.round((this.b * pu));
		
		int[] c = new int[3];
		c[0] = r;
		c[1] = g;
		c[2] = b;
		return c;
	}
}
