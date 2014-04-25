package affichage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.event.EventListenerList;

import listeners.LectureAnimationListener;

import Animations.GestionAnimation;

public class LecteurAnimation implements ActionListener {

	private GestionAnimation gestionnaire;
	private int fps;
	private int delay;
	private double step; //Pas de l'animation
	private double tempsLecture;
	
	protected EventListenerList listeners;
	
	private Timer timer;
	
	public LecteurAnimation(GestionAnimation gestionnaire) {
		this.gestionnaire = gestionnaire;
		this.fps = 60; //Par defaut on va fixer les fps a 60
		this.step = 30./this.fps;
		this.delay = 1000/this.fps;
		this.tempsLecture = 0.;
		this.listeners = new EventListenerList();
		this.timer = new Timer(delay, this);
	}
	
	public void addLectureAnimationListener(LectureAnimationListener listener) {
		listeners.add(LectureAnimationListener.class, listener);
	}
	
	public void removeLectureAnimationListener(LectureAnimationListener listener) {
		listeners.remove(LectureAnimationListener.class, listener);
	}
	
	protected void fireEvent(ActionEvent e) {
		Object []l = listeners.getListenerList();
		for (int i=l.length-1; i>=0; i-=2) {
	      ((LectureAnimationListener)l[i]).actionPerformed(e);
	    }
	}
	
	public void setFps(int fps) {
		if(fps > 0) {
			this.fps = fps;
			this.delay = 1000/fps;
		}
	}
	
	public void setTempsLecture(double tempsLecture) {
		if(tempsLecture >= 0.)
			this.tempsLecture = tempsLecture;
	}
	
	public void stop() {
		this.timer.stop();
	}
	
	public void lire() {
		this.timer.start();
	}
	
	public double getTempsCourant() {
		return this.tempsLecture;
	}

	/**
	 * Evenements du timer
	 */
	public void actionPerformed(ActionEvent e) {
		double tempsAnimation = this.gestionnaire.getEndAnimations();
		this.gestionnaire.refreshDessin();
		this.tempsLecture += this.step;
		
		if(this.tempsLecture > tempsAnimation) {
			this.stop(); //On va s'arreter au prochain tour car on est arrivé a la fin
			this.tempsLecture = tempsAnimation; //On se place a la vraie fin de l'animation
			
			//On va maintenant generer un action command pour signaler au listener qu'on a terminé la lecture
			ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "fin");
			this.fireEvent(event); //On declenche l'evenement sur nos listeners
		}	
	}

}
