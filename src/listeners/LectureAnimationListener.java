package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import affichage.Edition;
import affichage.LecteurAnimation;

public class LectureAnimationListener implements ActionListener {

	private Edition frame;
	
	private Thread threadLecture;
	
	public LectureAnimationListener(Edition frame) {
		this.frame = frame;
		this.threadLecture = null;
	}
	
	public void actionPerformed(ActionEvent e) {
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
		
			this.threadLecture = new Thread(lecteur); //On lance notre lecteur
			this.threadLecture.start();
			break;
		case "arret_lecture":
			lecteur.stop();
			lecteur.setTempsLecture(0.); //On se remet au debut
		case "pause_lecture":
			lecteur.stop(); //On demande a notre lecteur de se terminer
		case "fin": //TODO: Redondance avec le arret_lecture, peut etre utilisr la même commande
			//On passe la frame en mode edition (va reactiver les menus qu'il faut)
			this.frame.modeEdition();

			
			try {
				this.threadLecture.join();
				this.threadLecture = null;
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			this.frame.getGestionAnimation().refreshDessin();
			break;
		}	
	}

}
