package Animations;

import java.awt.geom.AffineTransform;

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
	
	public ObjetGeometrique getEtatObjGeo(Double t_courant) {
		//TODO: appliquer Animation pour obtenir l'état a l'instant T de l'objet
		//retourne null si l'objet n'existait pas a l'instant T ???
		
		
		//cas ou il n'y a pas d'animation relier à l'objet
		if (this.a == null)
			//TODO : revoyer null plus tard quand isVisible sera implémenté
			return this.objGeo;//cas particulier (attention il ne faut pas modifer l'objet)
		
		//ICI : récupération de toutes les animations
		AffineTransform at = a.getAffineTransform(t_courant);
		//TODO : récupération couleur stroke ,couleur fill,stroke ...

		return objGeo.AppliqueAnimation(at, null, null, null);//TODO : null parceque pas encore implémenté les autre animations
	}

	
}
