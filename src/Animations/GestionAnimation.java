package Animations;
import Animations.Comportement;
import java.util.ArrayList;
import formes.ObjetGeometrique;
import affichage.Toile;

public class GestionAnimation {
	private ArrayList<Comportement> Comportements;
	private Toile t;
	
	public GestionAnimation(Toile t) {
		this.Comportements = new ArrayList<Comportement>();
		this.setToile(t);
	}
	
	public void viderComportements() {
		this.Comportements.clear();
	}
	
	public void setToile(Toile t) {
		this.t = t;
	}
	
	public Toile getToile() {
		return this.t;
	}
	
	//TODO : (nom un peux long) mais pensé qu'il faut exclure les objetsGeo en double
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
		
		if(comp != null) { //s'il y avait deja un comportement pour l'objet
			//On va mettre a jour le comportement de l'objet
			//Pouvoir recup l'animation de comportement + lui demander d'ajouter l'animation
			//Si exception on le fera dans un try catch
			this.Comportements.set(i, comp); //On remplace l'ancien comportement par le nouveau
		}
		else {
			comp = new Comportement(geo,anim);
			//Opérations pour generer le comportement
			// ...
			this.Comportements.add(comp);
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
			t.dessineObjet(this.Comportements.get(i).getEtatObjGeo(t_courant));
		}
		
		//On demande le raffraichissement de la toile
		t.repaint();
	}
}
