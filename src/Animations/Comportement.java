package Animations;

import formes.ObjetGeometrique;

public class Comportement {

	private ObjetGeometrique objGeo;
	private Animation a;
	
	
	public Comportement() {
	}
	
	public Comportement(ObjetGeometrique geo, Animation a) {
		this.objGeo = geo;
		this.a = a;
	}
	
	public void setObjGeo(ObjetGeometrique geo) {
		this.objGeo = geo;
	}
	
	public ObjetGeometrique getObjGeo() {
		return objGeo;
	}
	
	public Animation getAnimation() {
		return this.a;
	}
	
	public ObjetGeometrique getEtatObjGeo(int instantT) {
		//TODO: appliquer Animation pour obtenir l'état a l'instant T de l'objet
		//retourne null si l'objet n'existait pas a l'instant T ???
		return objGeo;
	}

	
}
