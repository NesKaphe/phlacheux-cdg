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
			//On desactive le bouton de lecture et active le bouton d'arret
			this.frame.getMenuLectureDebut().setEnabled(false);
			this.frame.getMenuArretLecture().setEnabled(true);
			lecteur.setTempsLecture(0.); //On se place au debut avant de commencer la lecture
			this.threadLecture = new Thread(lecteur); //On lance notre lecteur
			this.threadLecture.start();
			break;
		case "arret_lecture":
			//On desactive le bouton d'arret et active le bouton de lecture
			this.frame.getMenuArretLecture().setEnabled(false);
			this.frame.getMenuLectureDebut().setEnabled(true);
			lecteur.stop(); //On demande a notre lecteur de se terminer
			
			try {
				this.threadLecture.join();
				this.threadLecture = null;
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			break;
		case "fin":
			//On desactuve le bouton d'arret et active le bouton de lecture
			this.frame.getMenuArretLecture().setEnabled(false);
			this.frame.getMenuLectureDebut().setEnabled(true);
			try {
				this.threadLecture.join();
				this.threadLecture = null;
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			break;
		}
	}

}
