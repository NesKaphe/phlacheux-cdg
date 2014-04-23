package Animations;
import Animations.Comportement;

import java.awt.Shape;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map.Entry;

import formes.ObjetGeometrique;
import affichage.Toile;

public class GestionAnimation {
	private HashMap<Integer, Comportement> Comportements;
	private Toile t;
	private int idComportement; //un comportement par objet
	
	public GestionAnimation(Toile t) {
		this.Comportements = new HashMap<Integer, Comportement>();
		this.setToile(t);
		this.idComportement = 0;//identifiant/clé associer aux comportements ajouté dans le HashMap "Comportements"
	}
	
	public void viderComportements() {
		this.Comportements.clear();
		this.idComportement = 0;
	}
	
	public void setToile(Toile t) {
		this.t = t;
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
	
	public HashMap<Integer, Animation> getAllAnimationsOf(int id) {
		HashMap<Integer, Animation> animations = new HashMap<Integer, Animation>();
		Animation anim = this.Comportements.get(id).getAnimation();
		//Ici les opérations pour recup toutes les animations du comportement "id"
		return animations;
	}
		
	//TODO : (nom un peux long)
	public void ajouterComportement(ObjetGeometrique geo, Animation anim) {
		//On va rechercher dans la liste si l'objet est deja present
		System.out.println(geo.getInfos());
		Comportement comp = null;
		int i = 0;
		while(i < this.Comportements.size()) {
			if(this.Comportements.get(i).getObjGeo().equals(geo)) {
				comp = this.Comportements.get(i);
				break;
			}
			i++;
		}
		
		//Si on n'a pas trouvé l'objet geometrique, on l'ajoute
		if(comp == null) {
			comp = new Comportement(geo, anim,this.idComportement);
			this.Comportements.put(this.idComportement, comp);
			this.idComportement++;
		}
	}
	
	public void supprimerComportement(int cle) {
		this.Comportements.remove(cle);
	}
	
	public void modifierObjetComportement(int cle, ObjetGeometrique geo) {
		Comportement comp = this.Comportements.get(cle);
		if(comp != null) {
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
		
		
		//On vide le buffer
		this.t.initBuffer();
		
		//On dessine les objets dans le buffer
		for(int i = 0; i < this.Comportements.size(); i++) {
			//t.dessineObjet(this.Comportements.get(i).getEtatObjGeo(t_courant));
			//System.out.println(this.Comportements.get(i));
			t.dessineObjet(this.Comportements.get(i).getEtatObjGeo(t_courant));
		}
		
		//On dessine l'objet temporaire s'il existe
		ObjetGeometrique tmpGeo = this.t.getObjGeometrique();
		if(tmpGeo != null) {
			System.out.println("\t"+tmpGeo.getCentre());
			t.dessineObjet(tmpGeo);
		}
		
		//On demande le raffraichissement de la toile
		t.repaint();
	}

	//TODO : renommer en getObjetAt en getObjetIdAt
	public Entry<Integer, ObjetGeometrique> getObjectAt(int x, int y, double t_courant) {
		//On va parcourir le hashmap des comportements et voir si le point x, y est contenu dans le shape
		for(Entry<Integer, Comportement> entry : this.Comportements.entrySet()) {
			Comportement c = entry.getValue();
			ObjetGeometrique geo = c.getEtatObjGeo(t_courant);
			Shape shape = geo.getStroke().createStrokedShape(geo.getShape());
			if(shape.contains(x,y) || geo.getShape().contains(x,y))
				return new AbstractMap.SimpleEntry<Integer, ObjetGeometrique>(entry.getKey(), geo);
		}
		return null; //On renvoie null si on n'a pas trouvé d'objet ayant les coordonnées x,y a l'instant t_courant
	}
	
	public ObjetGeometrique getObject(int id, double t_courant) {
		return this.Comportements.get(id).getEtatObjGeo(t_courant);
	}
}
