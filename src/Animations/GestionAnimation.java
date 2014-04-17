package Animations;
import Animations.Comportement;
import java.util.HashMap;

import formes.ObjetGeometrique;
import affichage.Toile;

public class GestionAnimation {
	private HashMap<Integer, Comportement> Comportements;
	private Toile t;
	private int idComportement; //un comportement par objet
	
	public GestionAnimation(Toile t) {
		this.Comportements = new HashMap<Integer, Comportement>();
		this.setToile(t);
		this.idComportement = 0;
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
	
	public HashMap<Integer, ObjetGeometrique> getAllObjects() {
		HashMap<Integer, ObjetGeometrique> objets = new HashMap<Integer, ObjetGeometrique>();
		//Ici on va extraire tous les objets des comportements
		return objets;
	}
	
	//TODO : (nom un peux long)
	public void ajouterComportement(ObjetGeometrique geo, Animation anim) {
		//On va rechercher dans la liste si l'objet est deja present
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
			comp = new Comportement(geo, anim);
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
	public void dessinerToile(Double t_courant) {
		//On va parcourir notre liste de comportements pour demander l'etat de l'objet a l'instant t
		//On demande ensuite a la toile de dessiner chaque objets dans son buffer puis on appelle repaint()
		
		
		//On vide le buffer
		this.t.initBuffer();
		
		//On dessine les objets dans le buffer
		for(int i = 0; i < this.Comportements.size(); i++) {
			//t.dessineObjet(this.Comportements.get(i).getEtatObjGeo(t_courant));
			System.out.println(this.Comportements.get(i));
			t.dessineObjet(this.Comportements.get(i).getEtatObjGeo(t_courant));
		}
		
		//On demande le raffraichissement de la toile
		t.repaint();
	}
}
