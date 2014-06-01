package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import Animations.Comportement;
import affichage.Edition;

public class SuppressionComportementListener implements ActionListener {

	private Edition frame;
	public SuppressionComportementListener(Edition frame) {
		this.frame = frame;
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
