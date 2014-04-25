package listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListModel;
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
public class ListeObjGeoSelectListener extends MouseAdapter implements ListSelectionListener {

	private GestionAnimation gestionnaire;
	private Toile toile;
	private JButton boutonEnableDisable;
	private CreateObjGeoListener create_obj_listener;
	
	/**
	 * 
	 * @param frame la fenetre d'edition
	 */
	public ListeObjGeoSelectListener(GestionAnimation gest, Toile t, JButton boutonEnableDisable, CreateObjGeoListener listener) {
		this.gestionnaire = gest;
		this.toile = t;
		this.boutonEnableDisable = boutonEnableDisable;
		this.create_obj_listener = listener;
	}
	
	public void valueChanged(ListSelectionEvent e) {
		this.gestionnaire.refreshDessin();
		if (e.getSource() instanceof JList<?>) {
			JList<?> liste = (JList<?>) e.getSource();
			List<?> listeItemSelected = liste.getSelectedValuesList();
			
			if(listeItemSelected.size()>0) { //Si on a au moins un element selectionné
				//On va rendre cliquable le bouton d'ajout d'animations
				this.boutonEnableDisable.setEnabled(true);
				
				//On va boucler sur tous les elements de la liste pour les selectionner sur la toile
				for(Object item : listeItemSelected) {
					ObjetGeometrique geo = this.gestionnaire.getObject(((Item) item).getId(), this.gestionnaire.getLecteurAnimation().getTempsCourant());
					this.toile.dessineSelectionOf(geo);
				}
			}
			else {
				//Sinon, on desactive le bouton car aucun objet n'est selectionné dans la liste
				this.boutonEnableDisable.setEnabled(false);
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		//On est interessé par le double clic sur la liste
		if (e.getSource() instanceof JList<?>) {
			JList<?> liste = (JList<?>) e.getSource();
			if(e.getClickCount() == 2) {
				int index = liste.locationToIndex(e.getPoint());
				ListModel<?> lm = liste.getModel();
				Item item = (Item)lm.getElementAt(index);
				ObjetGeometrique geo = gestionnaire.getObject(item.getId(), 0.); //TODO: recup le temps courant
				
				create_obj_listener.actionPerformed("modif_"+geo.getNom(),geo);
				
				System.out.println("Double clic sur "+item+ "id : "+ item.getId());
			}
		}
	}
	
}
