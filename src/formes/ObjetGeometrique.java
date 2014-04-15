package formes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;

public abstract class ObjetGeometrique {

	protected String nom;
	protected Point2D centre;
	protected Dimension size;//obsolète pas utilie finalement
	protected Color fillColor;
	protected Color strokeColor;
	protected Shape forme;
	protected AffineTransform trans;
	
	/**
	 * constructeur ObjetGeometrique
	 * 
	 * @param nom
	 * @param centre
	 * @param fillColor
	 * @param strokeColor
	 */
	protected ObjetGeometrique(String nom,Point2D centre,Color fillColor,Color strokeColor){
		this.nom = nom;
		this.centre = centre;
		this.fillColor = fillColor;
		this.strokeColor = strokeColor;
		this.forme = null;
		this.trans = null;
	}
	
	/**
	 * Calcule le point du coin en haut à gauche(pour le dessin)
	 * @return Point
	 */
	public abstract Point2D getCoord();
	
	/**
	 * Dessine le graphique dans l'objet dessin
	 */
	public abstract void dessineGraphics(Graphics2D g);
	
	/**
	 * Genere la forme de l'objet
	 */
	public abstract void generateShape();
	
	/**
	 * @param listePoints : Liste des points a translater
	 * @param easingFunction
	 * @param tDepart : Temps de debut de la transformation
	 * @param tFin : Temps de fin de la transformation
	 * @param tCourant : Instant courant pour récupérer l'état de l'objet
	 * @throws Exception
	 */
	public abstract void transTranslation(List<Point2D> listePoints, int easingFunction, int tDepart,
			int tFin, int tCourant) throws Exception; //TODO: Creer une exception
	
	/**
	 * @param sens : Le sens de la rotation
	 * @param easingFunction
	 * @param tDepart : Temps de debut de la transformation
	 * @param tFin : Temps de fin de la transformation
	 * @param tCourant : Instant courant pour récupérer l'état de l'objet
	 */
	public abstract void transRotationCentrale(int sens, int easingFunction, 
			int tDepart, int tFin, int tCourant) throws Exception;
	
	/**
	 * @param centre : Le point autour duquel la rotation aura lieu
	 * @param sens : Le sens de la rotation
	 * @param easingFunction
	 * @param tDepart : Temps de debut de la transformation
	 * @param tFin : Temps de fin de la transformation
	 * @param tCourant : Instant courant pour récupérer l'état de l'objet
	 */
	public abstract void transRotationExt(Point2D centre, int sens, int easingFunction,
			int tDepart, int tFin, int tCourant) throws Exception;
	
	/**
	 * @param finalStroke : 
	 * @param easingFunction
	 * @param tDepart : Temps de debut de la transformation
	 * @param tFin : Temps de fin de la transformation
	 * @param tCourant : Instant courant pour récupérer l'état de l'objet
	 */
	public abstract void transStroke(BasicStroke finalStroke, int easingFunction, 
			int tDepart, int tFin, int tCourant) throws Exception;
	
	/*
	 * getters
	 */
	public String getNom() {
		return this.nom;
	}
	
	public Point2D getCentre() {
		return this.centre;
	}
	
	public Dimension getDimensions() {
		return this.size;
	}
	
	public int getWidth() {
		return this.size.width;
	}
	
	public int getHeight() {
		return this.size.height;
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
	
	/*
	 *  setters
	 */
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public void setCentre(Point2D centre) {
		this.centre = centre;
		this.generateShape();
	}
	
	public void setDimensions(Dimension size){
		this.size = size;
	}
	
	public void setDimensions(int x, int y) {
		this.size = new Dimension(x,y);
	}
	
	public void setWidth(int width) {
		this.size.width = width;
	}
	
	public void setHeight(int height) {
		this.size.height = height;
	}
	
	public void setFillColor(Color c) {
		this.fillColor = c;
	}
	
	public void setStrokeColor(Color c) {
		this.strokeColor = c;
	}
}
