package Listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import formes.ObjetGeometrique;

import affichage.Edition;

public class MouseMotionToileListener implements MouseMotionListener{

	Edition frame;
	
	public MouseMotionToileListener(Edition frame) {
		this.frame = frame;
	}
	
	
	public void mouseDragged(MouseEvent e) {
		
	}

	public void mouseMoved(MouseEvent e) {
		int posX = e.getX();
		int posY = e.getY();
		
		ObjetGeometrique geo = this.frame.getToile().getObjGeometrique();
		
		Point2D.Double centre = new Point2D.Double(posX,posY);
		geo.setCentre(centre);
		this.frame.getToile().setObjTemporaire(geo);
		
		System.out.println(this.frame.getToile().getObjGeometrique());
		this.frame.getGestionAnimation().dessinerToile(0.);
	}

}
