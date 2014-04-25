package affichage;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.event.EventListenerList;

import listeners.LectureAnimationListener;

import Animations.GestionAnimation;

public class LecteurAnimation implements Runnable {

	private GestionAnimation gestionnaire;
	private int fps;
	private long delay;
	private double step; //Pas de l'animation
	private double tempsLecture;
	private boolean lecture;
	
	protected EventListenerList listeners;
	
	public LecteurAnimation(GestionAnimation gestionnaire) {
		this.gestionnaire = gestionnaire;
		this.fps = 60; //Par defaut on va fixer les fps a 60
		this.step = 30./this.fps;
		this.delay = 1000/this.fps;
		this.tempsLecture = 0.;
		this.lecture = false;
		this.listeners = new EventListenerList(); 
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
		this.lecture = false;
	}
	
	public void run() {
		this.lecture = true;
		double tempsAnimation = this.gestionnaire.getEndAnimations();
		System.out.println(tempsAnimation);
		while(this.lecture) {
			long debut = System.nanoTime();
			
			this.gestionnaire.dessinerToile(this.tempsLecture);
			this.tempsLecture += this.step;
			
			if(this.tempsLecture > tempsAnimation) {
				this.stop(); //On va s'arreter au prochain tour car on est arrivé a la fin
				this.tempsLecture = tempsAnimation; //On se place a la vraie fin de l'animation
				
				//On va maintenant generer un action command pour signaler au listener qu'on a terminé la lecture
				ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "fin");
				this.fireEvent(event); //On declenche l'evenement sur nos listeners
			}
			
			long ecoule = System.nanoTime() - debut;
			
			long calcDelay = this.delay - ecoule/1000000;
			//System.out.println("le delais etait "+this.delay+ "et maintenant "+ calcDelay);
			if(calcDelay > 0) {
				try {
					Thread.sleep(calcDelay);
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
