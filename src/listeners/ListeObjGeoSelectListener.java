package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Animations.Comportement;
import Animations.GestionAnimation;
import affichage.Edition;
import affichage.Item;
import affichage.Toile;
import formes.ObjetGeometrique;

/**
 * Listener de JList des objets geometriques crées
 * @author Alain
 */
public class ListeObjGeoSelectListener extends MouseAdapter implements ListSelectionListener {

	private CreateObjGeoListener create_obj_listener;
	
	private Edition frame;
	
	/**
	 * 
	 * @param frame la fenetre d'edition
	 */
	public ListeObjGeoSelectListener(Edition frame, CreateObjGeoListener create_obj_listener) {
		this.frame = frame;
		this.create_obj_listener = create_obj_listener;
	}
	
	public void valueChanged(ListSelectionEvent e) {
		this.frame.getGestionAnimation().refreshDessin();
		if (e.getSource() instanceof JList<?>) {
			JList<?> liste = (JList<?>) e.getSource();
			List<?> listeItemSelected = liste.getSelectedValuesList();
			
			if(listeItemSelected.size()>0) { //Si on a au moins un element selectionné
				//On va rendre cliquable le bouton d'ajout d'animations
				this.frame.activerAjoutSuppression();
				
				//On va boucler sur tous les elements de la liste pour les selectionner sur la toile
				for(Object item : listeItemSelected) {
					ObjetGeometrique geo = this.frame.getGestionAnimation().getObject(((Item) item).getId(), this.frame.getGestionAnimation().getLecteurAnimation().getTempsCourant());
					this.frame.getToile().dessineSelectionOf(geo);
				}
			}
			else {
				//Sinon, on desactive le bouton car aucun objet n'est selectionné dans la liste
				this.frame.desactiverAjoutSuppression();
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		//On est interessé par le double clic sur la liste
		if (e.getSource() instanceof JList<?>) {
			JList<?> liste = (JList<?>) e.getSource();
			if(e.getClickCount() >= 2) {
				int index = liste.locationToIndex(e.getPoint());
				ListModel<?> lm = liste.getModel();
				Item item = (Item)lm.getElementAt(index);
				Comportement comp = (Comportement) item.getValeur();
				
				//On va creer un action event pour informer le listener de creation d'objets qu'on veut modifier un objet
				//Commande : modif_+nomObj
				ModifComportementEvent event = new ModifComportementEvent(this, ActionEvent.ACTION_PERFORMED, "modif_"+comp.getObjGeo().getNom(), comp);
				this.create_obj_listener.actionPerformed(event);
				//create_obj_listener.actionPerformed("modif_"+comp.getObjGeo().getNom(),comp);
				
				Edition frame = (Edition) liste.getRootPane().getParent();
				frame.MAJListeObjGeo();
				
				System.out.println("Double clic sur "+item+ "id : "+ item.getId());
			}
		}
	}
	
}
