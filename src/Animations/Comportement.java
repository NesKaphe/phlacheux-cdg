package Animations;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.AffineTransform;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import formes.ObjetGeometrique;

public class Comportement {

	private ObjetGeometrique objGeo;
	private Animation a;//sera toujours un composite animation
	private int id;
	
	public Comportement() {
	}
	
	public Comportement(ObjetGeometrique geo, Animation a,int id) {
		this.objGeo = geo;
		//on "cast" l'animation en composite si s'en est pas une :
		if (a.getType().equals("composite")){
			this.a = a;
		}
		else{
			CompositeAnimation ca = new CompositeAnimation(a.getT_debut(),a.getT_fin(),a.getEasing());
			ca.add(a);
			this.a = ca;
		}
		this.id = id;
	}
	
	public Comportement(ObjetGeometrique geo, int id) {
		this(geo,new CompositeAnimation(0., 0., 0), id);
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
	
	public int getId() {
		return id;
	}

	public ObjetGeometrique getEtatObjGeo(Double t_courant) {
		//TODO: appliquer Animation pour obtenir l'état a l'instant T de l'objet
		//retourne null si l'objet n'existait pas a l'instant T ???
		
		
		//cas ou il n'y a pas d'animation relier à l'objet
		if (this.a == null)
			//TODO : revoyer null plus tard quand isVisible sera implémenté
			return this.objGeo;//cas particulier (attention il ne faut pas modifer l'objet)
		
		//ICI : récupération de toutes les animations
		//AffineTransform
		AffineTransform at = a.getAffineTransform(t_courant);
		
		//Stroke Transform
		BasicStroke stroke = null;
		Float width = a.getWidthStroke(t_courant);
		if(width != null) {
			width = this.objGeo.getStroke().getLineWidth() + width;
			stroke = new BasicStroke((width>0)?width:0);
		}
		
		//Colors Transform
		int r = 0;
		int g = 0;
		int b = 0;
		
		//Stroke Color
		Color colorStroke = null;
		int[] cStroke = a.getStrokeColor(t_courant);
		if(cStroke != null) {
			Color objColor = this.objGeo.getStrokeColor();
			r = objColor.getRed() + cStroke[0];
			g = objColor.getGreen() + cStroke[1];
			b = objColor.getBlue() + cStroke[2];
			
			//On doit limiter rgb entre 0 et 255
			r = r > 255 ? 255 : r;
			g = g > 255 ? 255 : g;
			b = b > 255 ? 255 : b;
			
			r = r < 0 ? 0 : r;
			g = g < 0 ? 0 : g;
			b = b < 0 ? 0 : b;
			
			colorStroke = new Color(r, g, b);
		}
		
		//Fill Color
		Color FillColor = null;
		int[] fColor = a.getFillColor(t_courant);
		if(fColor != null) {
			Color objColor = this.objGeo.getFillColor();
			if(objColor != null) { //Tous les objets ne sont pas remplis
				r = objColor.getRed() + fColor[0];
				g = objColor.getGreen() + fColor[1];
				b = objColor.getBlue() + fColor[2];
				
				//On doit limiter rgb entre 0 et 255
				r = r > 255 ? 255 : r;
				g = g > 255 ? 255 : g;
				b = b > 255 ? 255 : b;
				
				r = r < 0 ? 0 : r;
				g = g < 0 ? 0 : g;
				b = b < 0 ? 0 : b;
				
				FillColor = new Color(r, g, b);
			}
		}
		//TODO : récupération couleur stroke ,couleur fill,stroke ...

		return objGeo.AppliqueAnimation(at, FillColor, colorStroke, stroke);//TODO : null parceque pas encore implémenté les autre animations
	}

	public String toString() {
		return this.objGeo.getNom()+" - "+this.getId();
	}
	
	
	public Element toXml(Document domDocument) {
		Element elem = domDocument.createElement("Comportement");
		elem.setAttribute("id", String.valueOf(this.id));
		
		Element objGeo = domDocument.createElement("objGeo");
		Element filsObjGeo = this.objGeo.toXml(domDocument);
		objGeo.appendChild(filsObjGeo);
		
		Element filsAnimation = this.a.toXml(domDocument);
		
		elem.appendChild(objGeo);
		elem.appendChild(filsAnimation);
		return elem;
	}
}
