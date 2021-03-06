package Animations;
import Animations.Comportement;

import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map.Entry;

import listeners.LectureAnimationListener;

import formes.ObjetGeometrique;
import affichage.LecteurAnimation;
import affichage.Toile;
import affichage.VisionneuseAnimation;

public class GestionAnimation {
	private HashMap<Integer, Comportement> Comportements;
	private Toile t;
	private LecteurAnimation lecteur; //Pour recuperer le temps courant du dessin
	private int idComportement; //un comportement par objet
	private VisionneuseAnimation visionneuse;
	private ObjetGeometrique objEnCreation;
	private boolean isDraw;
	
	public GestionAnimation(Toile t) {
		this.Comportements = new HashMap<Integer, Comportement>();
		this.setToile(t);
		this.idComportement = 0;//identifiant/clé associer aux comportements ajouté dans le HashMap "Comportements"
		this.lecteur = null;
		this.isDraw = false;//pour savoir si le dessin est complement fini
	}
	
	public void viderComportements() {
		this.Comportements.clear();
		this.idComportement = 0;
	}
	
	public void setToile(Toile t) {
		this.t = t;
	}
	
	public void setLecteurAnimation(LecteurAnimation lecteur) {
		this.lecteur = lecteur;
	}
	
	public void setVisionneuse(VisionneuseAnimation visionneuse) {
		this.visionneuse = visionneuse;
	}
	
	public LecteurAnimation getLecteurAnimation() {
		return this.lecteur;
	}
	
	public Toile getToile() {
		return this.t;
	}
	
	public HashMap<Integer, Comportement> getListComportements() {
		return Comportements;
	}

	public HashMap<Integer, ObjetGeometrique> getAllObjects() {
		HashMap<Integer, ObjetGeometrique> objets = new HashMap<Integer, ObjetGeometrique>();
		for(Entry<Integer, Comportement> entry : this.Comportements.entrySet()) {
			objets.put(entry.getKey(), entry.getValue().getObjGeo());
		}
		return objets;
	}
	
	public void ajouterComportement(ObjetGeometrique geo, Animation anim) {
		//On va rechercher dans la liste si l'objet est deja present
		System.out.println("ajoutComportement "+geo.getStroke().getLineWidth());
		Comportement comp = null;
		int i = 0;

		for(Entry<Integer, Comportement> entry : this.Comportements.entrySet()) {
			if(entry.getValue().getObjGeo().equals(geo)) {
				comp = entry.getValue();
			}
		}
		
		//Si on n'a pas trouvé l'objet geometrique, on l'ajoute
		if(comp == null) {
			if(anim != null)
				comp = new Comportement(geo, anim,this.idComportement);
			else
				comp = new Comportement(geo, this.idComportement);
			this.Comportements.put(this.idComportement, comp);
			this.idComportement++;
		}
	}
	
	public void supprimerComportement(int cle) {
		this.Comportements.remove(cle);
	}
	
	public void modifierComportement(Comportement comp) {
		this.Comportements.put(comp.getId(), comp);
	}
	
	public void modifierObjetComportement(int cle, ObjetGeometrique geo) {
		Comportement comp = this.Comportements.get(cle);
		if(comp != null) {
			System.out.println("MODIF");
			comp.setObjGeo(geo);
			//Decider de si on remet a 0 les animations ou non
		}
	}
	
	/**
	 * @param tDessin : L'instant t representant l'etat des objets pour le le dessin
	 */
	public void dessinerToile(double t_courant) {
		//On va parcourir notre liste de comportements pour demander l'etat de l'objet a l'instant t
		//On demande ensuite a la toile de dessiner chaque objets dans son buffer puis on appelle repaint()
		
		isDraw = false;
		//On vide le buffer
		this.t.initBuffer();
		
		//On dessine les objets dans le buffer
		for(Entry<Integer, Comportement> entry : this.Comportements.entrySet()) {
			t.dessineObjet(entry.getValue().getEtatObjGeo(t_courant));
		}
		
		//On dessine l'objet temporaire s'il existe
		ObjetGeometrique tmpGeo = this.getObjGeoEnCreation();
		if(tmpGeo != null) {
			t.dessineObjet(tmpGeo);
		}
		isDraw = true;
		//On demande le raffraichissement de la toile
		t.repaint();
	}
	
	/**
	 * Cette methode va demander le temps courant au lecteur puis va dessiner sur le buffer de la toile
	 * les objets a ce temps
	 */
	public void refreshDessin() {
		double t_courant = this.lecteur.getTempsCourant();
		this.dessinerToile(t_courant);
		this.visionneuse.changeCursorPosition((int) t_courant);
	}

	public Entry<Integer, ObjetGeometrique> getObjectAt(int x, int y, double t_courant) {
		AbstractMap.SimpleEntry<Integer, ObjetGeometrique> simpleEntry = null;
		
		//On va parcourir le hashmap des comportements et voir si le point x, y est contenu dans le shape
		for(Entry<Integer, Comportement> entry : this.Comportements.entrySet()) {
			Comportement c = entry.getValue();
			ObjetGeometrique geo = c.getEtatObjGeo(t_courant);
			Shape shape = geo.getStroke().createStrokedShape(geo.getShape());
			if(shape.contains(x,y) || geo.getShape().contains(x,y)) {
				if(simpleEntry == null) {
					simpleEntry = new AbstractMap.SimpleEntry<Integer, ObjetGeometrique>(entry.getKey(), geo);
				}
				else if (simpleEntry.getKey() < entry.getKey()) {
					simpleEntry = new AbstractMap.SimpleEntry<Integer, ObjetGeometrique>(entry.getKey(), geo);
				}
			}
		}
		return simpleEntry; //On renvoie null si on n'a pas trouvé d'objet ayant les coordonnées x,y a l'instant t_courant
	}
	
	public ObjetGeometrique getObject(int id, double t_courant) {
		return this.Comportements.get(id).getEtatObjGeo(t_courant);
	}
	
	public ObjetGeometrique getObjGeoEnCreation() {
		return this.objEnCreation;
	}
	
	public void setObjGeoEnCreation(ObjetGeometrique geo) {
		this.objEnCreation = geo;
	}
	
	public void resetObjGeoEnCreation() {
		this.objEnCreation = null;
	}
	
	/**
	 * Va parcourir tous les comportement pour trouver le temps final de toutes les animations
	 * @return un double representant le temps final le plus grand de tous les comportements
	 */
	public double getEndAnimations() {
		double t_fin = 0.;
		for(Entry<Integer, Comportement> entry : this.Comportements.entrySet()) {
			Animation anim = entry.getValue().getAnimation();
			if(anim.getT_fin() > t_fin)
				t_fin = anim.getT_fin();
		}
		return t_fin;
	}
	
	public double getTempsCourant() {
		return this.lecteur.getTempsCourant();
	}

	public void setTempCourant(double temps) {
		this.lecteur.setTempsLecture(temps);
	}
	
	/**
	 * retourne une copie visuel de la toile
	 * @return
	 */
	public BufferedImage getCopyBackBuffer() {
		if(isDraw)
			return this.t.getCopyBackBuffer();
		else{
			this.dessinerToile(this.getTempsCourant());
			return this.t.getCopyBackBuffer();
		}
	}
	
	

}
