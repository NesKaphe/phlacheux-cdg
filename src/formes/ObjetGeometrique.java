package formes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;


public abstract class ObjetGeometrique {

	protected String nom;
	protected Point2D.Double centre;
	protected BasicStroke stroke;
	protected Color fillColor;
	protected Color strokeColor;
	protected Shape forme;
	//TODO : rajouter un id qui sera donné par GestionAnimation
	
	/**
	 * constructeur ObjetGeometrique
	 * 
	 * @param nom
	 * @param centre
	 * @param fillColor
	 * @param strokeColor
	 */
	protected ObjetGeometrique(String nom,Point2D.Double centre,Color fillColor,Color strokeColor){
		this.nom = nom;
		this.centre = centre;
		this.stroke = new BasicStroke();
		this.fillColor = fillColor;
		this.strokeColor = strokeColor;
		this.forme = null;

	}
	
	/**
	 * constructeur par recopie
	 * @param obj
	 */
	protected ObjetGeometrique(ObjetGeometrique obj){
		this.nom = obj.nom;
		this.centre = obj.centre;
		this.stroke = obj.stroke;
		this.fillColor = obj.fillColor;
		this.strokeColor = obj.strokeColor;
		this.forme = obj.forme;

	}
	
	/**
	 * Clone()
	 * nous retourne une copie de this avec le bon typage
	 */
	public abstract ObjetGeometrique clone();
	
	/**
	 * Calcule le point du coin en haut à gauche(pour le dessin)
	 * @return Point
	 */
	public abstract Point2D getCoord();
	
	
	/**
	 * Genere la forme de l'objet
	 */
	public abstract void generateShape();
	
	
	/**
	 * 
	 * prend en paramètre les Animations a appliquer à l'objet
	 * et retourne une copie de l'ObjetGéométrique avec les transformations
	 * On peux changer la couleur principale, la couleur de bordure, la position,la taille de bordure.
	 * Si il n'y a pas de transformation spécifique pour un paramètre on peux mettre null
	 * @param at 
	 * @param fillColor
	 * @param strokeColor
	 * @param stroke
	 * @return
	 */
	public ObjetGeometrique AppliqueAnimation(AffineTransform at,Color fillColor, Color strokeColor, BasicStroke stroke){
		ObjetGeometrique objGeo = this.clone();//pour avoir concretement une copie de l'objet
		if (at != null)
			objGeo.forme = at.createTransformedShape(this.forme);
		if (fillColor != null)
			objGeo.fillColor = fillColor;
		if (strokeColor != null)
			objGeo.strokeColor = strokeColor;
		if (stroke != null)
			objGeo.stroke = new BasicStroke(this.stroke.getLineWidth()+stroke.getLineWidth());
		return objGeo;
	}
	
	
	/*
	 * getters
	 */
	public String getNom() {
		return this.nom;
	}
	
	public Point2D.Double getCentre() {
		return this.centre;
	}
	
	public BasicStroke getStroke() {
		return this.stroke;
	}
	
	public Color getFillColor() {
		return this.fillColor;
	}
	
	public Color getStrokeColor() {
		return this.strokeColor;
	}
	
	public Shape getShape() {
		return this.forme;
	}
	
	public String getInfos() {
		Class<?> enclosingClass = getClass().getEnclosingClass();
		String nomclass;
		if (enclosingClass != null) {
		  nomclass = enclosingClass.getSimpleName();
		} else {
		  nomclass = getClass().getSimpleName();
		}
		return ""+this.getNom()+"@"+nomclass;
	}
	
	/*
	 *  setters
	 */
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public void setCentre(Point2D.Double centre) {
		this.centre = centre;
	}
	
	public void setStroke(BasicStroke stroke) {
		this.stroke = stroke;
	}
	
	public void setStrokeWidth(float width) {
		if(width > 0)
			this.stroke = new BasicStroke(width);
	}
	
	public void setFillColor(Color c) {
		this.fillColor = c;
	}
	
	public void setStrokeColor(Color c) {
		this.strokeColor = c;
	}
}
