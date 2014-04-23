package listeners;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import affichage.Edition;
import affichage.Item;
import formes.ObjetGeometrique;

/**
 * Listener de JList des objets geometriques crées
 * @author Alain
 */
public class ListeObjGeoSelectListener implements ListSelectionListener {

	//On garde une reference vers la fenetre, pour pouvoir interragir avec ses composants
	private Edition frame;
	
	/**
	 * 
	 * @param frame la fenetre d'edition
	 */
	public ListeObjGeoSelectListener(Edition frame) {
		this.frame = frame;
	}
	
	public void valueChanged(ListSelectionEvent e) {
		this.frame.getGestionAnimation().dessinerToile(0.); //TODO:recup le temps courant
		
		List<Item> listeItemSelected = this.frame.getListe().getSelectedValuesList();
		if(listeItemSelected.size()>0) { //Si on a au moins un element selectionné
			//On va rendre cliquable le bouton d'ajout d'animations
			this.frame.getBoutonAjoutAnimation().setEnabled(true);
			
			//On va boucler sur tous les elements de la liste pour les selectionner sur la toile
			for(Item item : listeItemSelected) {
				ObjetGeometrique geo = this.frame.getGestionAnimation().getObject(item.getId(), 0.); //TODO : Encore une fois recup le temps courant
				this.frame.getToile().dessineSelectionOf(geo);
			}
		}
		else {
			this.frame.getBoutonAjoutAnimation().setEnabled(false);
		}
		/*
		//On va boucler sur tous les elements de la liste pour voir s'ils sont sélectionnés
		for(int i = 0; i < model.getSize(); i++) {
			if(this.frame.getListe().isSelectedIndex(i)) {
				ObjetGeometrique geo = this.frame.getGestionAnimation().getObject(model.getElementAt(i).getId(), 0.); //TODO: recup le temps courant
				this.frame.getToile().dessineSelectionOf(geo);
				System.out.println(geo.getStroke().getLineWidth());
			}
		}
		*/
	}

}
