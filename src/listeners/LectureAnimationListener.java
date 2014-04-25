package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import affichage.Edition;
import affichage.LecteurAnimation;

public class LectureAnimationListener implements ActionListener {

	private Edition frame;
		
	public LectureAnimationListener(Edition frame) {
		this.frame = frame;
	}
	
	public void actionPerformed(ActionEvent e) {
		frame.getGestionAnimation().resetObjGeoEnCreation(); //Si on avait un objet en création, on le supprime
		String commande = e.getActionCommand();
		LecteurAnimation lecteur = this.frame.getLecteur();
		switch(commande) {
		case "lecture_debut":
			lecteur.setTempsLecture(0.); //On se place au debut avant de commencer la lecture
		case "reprendre_lecture":
			//On passe la frame en mode lecture (va desactiver les menus qu'il faut)
			this.frame.modeLecture();
			
			//On desactive le bouton d'ajout d'animation
			this.frame.getBoutonAjoutAnimation().setEnabled(false);
		
			lecteur.lire(); //On lance notre lecteur
			break;
		case "arret_lecture":
			lecteur.stop();
			lecteur.setTempsLecture(0.); //On se remet au debut
		case "pause_lecture":
			lecteur.stop(); //On demande a notre lecteur de se terminer
		case "fin": //TODO: Redondance avec le arret_lecture, peut etre utilisr la même commande
			//On passe la frame en mode edition (va reactiver les menus qu'il faut)
			this.frame.modeEdition();
			this.frame.getGestionAnimation().refreshDessin();
			break;
		}	
	}

}
