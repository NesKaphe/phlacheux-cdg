package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;
import javax.swing.JOptionPane;

import Animations.Comportement;
import affichage.Edition;
import affichage.Item;

public class SuppressionComportementListener implements ActionListener {

	private Edition frame;
	private CreateObjGeoListener listener;
	
	public SuppressionComportementListener(Edition frame, CreateObjGeoListener listener) {
		this.frame = frame;
		this.listener = listener;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("suppression")) {
			int reponse = JOptionPane.showConfirmDialog(null, "Voulez vous vraiment supprimer cet objet ?", "Suppression", JOptionPane.OK_CANCEL_OPTION);
			if(reponse == JOptionPane.OK_OPTION) {
				Comportement comp = (Comportement) this.frame.getListe().getSelectedValue().getValeur();
				this.frame.getGestionAnimation().supprimerComportement(comp.getId());
				this.frame.MAJListeObjGeo();
				this.frame.getGestionAnimation().refreshDessin();
			}
		}
	}

}
