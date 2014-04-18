package Listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import formes.ObjetGeometrique;

import affichage.Edition;

public class MouseToileListener implements MouseListener{

	private Edition frame;
	private MouseMotionToileListener motion; //Pour pouvoir desactiver le listener apres un clic
	
	public MouseToileListener(Edition frame, MouseMotionToileListener motion) {
		this.frame = frame;
		this.motion = motion;
	}
	
	public void mouseClicked(MouseEvent e) {
		ObjetGeometrique geo = this.frame.getToile().getObjGeometrique();
		this.frame.getGestionAnimation().ajouterComportement(geo, null);
		this.frame.getToile().removeMouseMotionListener(this.motion);
		
		this.frame.getToile().initObjTemporaire();
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}

}
