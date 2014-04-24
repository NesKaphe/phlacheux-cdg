package listeners;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Animations.GestionAnimation;
import affichage.Item;
import affichage.Toile;
import formes.ObjetGeometrique;

/**
 * Listener de JList des objets geometriques crées
 * @author Alain
 */
public class ListeObjGeoSelectListener implements ListSelectionListener {

	private GestionAnimation gestionnaire;
	private Toile toile;
	private JButton boutonEnableDisable;
	
	/**
	 * 
	 * @param frame la fenetre d'edition
	 */
	public ListeObjGeoSelectListener(GestionAnimation gest, Toile t, JButton boutonEnableDisable) {
		this.gestionnaire = gest;
		this.toile = t;
		this.boutonEnableDisable = boutonEnableDisable;
	}
	
	public void valueChanged(ListSelectionEvent e) {
		this.gestionnaire.dessinerToile(0.); //TODO:recup le temps courant
		if (e.getSource() instanceof JList<?>) {
			JList<?> liste = (JList<?>) e.getSource();
			List<?> listeItemSelected = liste.getSelectedValuesList();
			
			if(listeItemSelected.size()>0) { //Si on a au moins un element selectionné
				//On va rendre cliquable le bouton d'ajout d'animations
				this.boutonEnableDisable.setEnabled(true);
				
				//On va boucler sur tous les elements de la liste pour les selectionner sur la toile
				for(Object item : listeItemSelected) {
					ObjetGeometrique geo = this.gestionnaire.getObject(((Item) item).getId(), 0.); //TODO : Encore une fois recup le temps courant
					this.toile.dessineSelectionOf(geo);
				}
			}
			else {
				//Sinon, on desactive le bouton car aucun objet n'est selectionné dans la liste
				this.boutonEnableDisable.setEnabled(false);
			}
		}
	}

}
