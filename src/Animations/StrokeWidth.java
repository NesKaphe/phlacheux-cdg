package Animations;

import java.awt.geom.AffineTransform;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class StrokeWidth extends Animation {

	private float strokeWidthIncrement;
	
	public StrokeWidth(Double t_debut, Double t_fin, int easing, float strokeWidthIncrement) {
		super(t_debut, t_fin, easing, "StrokeWidth");
		this.strokeWidthIncrement = strokeWidthIncrement;
	}

	
	public StrokeWidth(StrokeWidth anim) {
		super(anim);
		this.strokeWidthIncrement = anim.strokeWidthIncrement;
	}


	public AffineTransform getAffineTransform(Double t_courant) {	
		return null;
	}
	
	public Float getWidthStroke(double t_courant) {
		Double pu = this.getPourun(t_courant);
		//si pu est négatif c'est que notre temps courant n'est pas bon
		if (pu < 0.0)
			return null;
		
		return (float) (pu * this.strokeWidthIncrement);
	}

	public void setStrokeWidthIncrement(float increment) {
		this.strokeWidthIncrement = increment;
	}


	public Element toXml(Document domDocument) {
		Element elem = domDocument.createElement("StrokeWidth");
		
		elem.setAttribute("debut", this.getT_debut().toString());
		elem.setAttribute("fin", this.getT_fin().toString());
		elem.setAttribute("easing", String.valueOf(this.getEasing()));
		elem.setAttribute("incrWidth", String.valueOf(this.strokeWidthIncrement));
		
		return elem;
	}
}
