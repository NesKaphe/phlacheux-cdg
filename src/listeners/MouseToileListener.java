package listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.Map.Entry;

import javax.swing.ListModel;

import affichage.Edition;
import affichage.Item;
import affichage.Toile;

import formes.ObjetGeometrique;
import formes.SegmentDroite;

public class MouseToileListener implements MouseListener, MouseMotionListener {

	private Edition frame;
	
	private int ox, oy;
	
	public MouseToileListener(Edition frame) {
		this.frame = frame;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		//Si on est en mode listener (Ajout d'objet)
		Toile toile = (Toile) e.getSource();
    	if (toile.getMode().equals("ajoutObj")){
    		ox = e.getX();
    		oy = e.getY();
    		
    		ObjetGeometrique geo = this.frame.getGestionAnimation().getObjGeoEnCreation();
    		Point2D.Double point = new Point2D.Double(ox,oy);
    		//Cet evenement n'est utilisé que pour le segment de droite (deplacement de souris apres le clic)
    		if(geo instanceof SegmentDroite) {
				SegmentDroite seg = (SegmentDroite) geo;
				seg.setPoint(point, 2);
				
				//On genere la nouvelle forme du segment pour le dessin
				seg.generateShape();
				
				//On met a jour notre objet temporaire
				this.frame.getGestionAnimation().setObjGeoEnCreation(seg);
				
				//Et on raffraichi le dessin des objets
				this.frame.getGestionAnimation().refreshDessin(); //TODO: recup le temps courant
    		}
    	}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//Si on est en mode listener (Ajout d'objet)
		Toile toile = (Toile) e.getSource();
    	if (toile.getMode().equals("ajoutObj")){
    		ox = e.getX();
    		oy = e.getY();
    		
    		ObjetGeometrique geo = this.frame.getGestionAnimation().getObjGeoEnCreation();
    		Point2D.Double point = new Point2D.Double(ox,oy);
    		//Cet evenement sert pour les objets geometrique qui ne sont pas des Segments
			if(!(geo instanceof SegmentDroite)) {
				//On deplace le centre de l'objet sur le pointeur de la souris
				geo.setCentre(point);
				
				//On raffraichi le dessin des objets
				this.frame.getGestionAnimation().refreshDessin(); //TODO: recup le temps courant
			}
    	}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//Si on est en mode listener (=Ajout d'objet)
		Toile toile = (Toile) e.getSource();
    	if(toile.getMode().equals("ajoutObj")) {
    		
    		//On commence par recupérer l'objet geometrique temporaire
	    	ObjetGeometrique geo = this.frame.getGestionAnimation().getObjGeoEnCreation();
	    	
	    	//Cet evenement ne sera utilisé que pour les formes se plaçant avec un clic unique (= Pas un segment)
	    	//L'ajout de ces formes est combiné avec le mouseMoved
	    	if(!(geo instanceof SegmentDroite)) {
	    		
	    		//On enregistre l'objet geometrique dans le gestionnaire d'animation
		    	this.frame.getGestionAnimation().ajouterComportement(geo, null);
		    	
		    	//On a terminé notre ajout, on ne reste pas en mode listener
		    	this.frame.getGestionAnimation().resetObjGeoEnCreation();
				//toile.removeMouseMotionListener((MouseMotionListener) this);
				toile.modeSelection();
				
				//On oublie pas de mettre a jour la liste des objets (JList)
				this.frame.MAJListeObjGeo();
	    	}
		}
    	else if(toile.getMode().equals("selection")) { //Si on est pas en mode listener, on regarde si on est en mode selection
    		
    		//Si l'objet temporaire n'est pas null (Cas du segment après le dessin
    		if(this.frame.getGestionAnimation().getObjGeoEnCreation() != null) {
    			//On remet a zéro
    			this.frame.getGestionAnimation().resetObjGeoEnCreation();
    		}
    		else { //Sinon, on va essayer de detecter le clic sur les objets geometriques dessinés
    			
	    		//On va demander au gestion animation si un objet contient les coordonnées du clic
	    		Entry<Integer, ObjetGeometrique> entry = this.frame.getGestionAnimation().getObjectAt(e.getX(), e.getY(),
	    				this.frame.getGestionAnimation().getLecteurAnimation().getTempsCourant());
	
	    		//On deselectionne tous les elements de la liste
	    		this.frame.getListe().clearSelection();
	    		
	    		//Si on a trouvé un objet geometrique, on selectionne dans la liste la ligne correspondante
	    		if(entry != null) {
	    			
	    			//On va maintenant dessiner un rectangle pour montrer a l'utilisateur que l'objet est selectionné
	    			toile.dessineSelectionOf(entry.getValue());
	    			
	    			//Et finalement, va selectionner la ligne de l'objet dans la JList
	    			ListModel<Item> model = this.frame.getListe().getModel();
	    			for(int i = 0; i < model.getSize(); i++) {
	    				if(model.getElementAt(i).getId() == entry.getKey()) {
	    					this.frame.getListe().setSelectedIndex(i);
	    				}
	    			}
	    		}
    		}
    	}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Stub de la méthode généré automatiquement
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Stub de la méthode généré automatiquement
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//Si on est en mode listener (=Ajout d'objet)
		Toile toile = (Toile) e.getSource();
		if(toile.getMode().equals("ajoutObj")) {
			//On recupere l'objet geometrique temporaire aupres du gestionnaire d'animation
			ObjetGeometrique geo = this.frame.getGestionAnimation().getObjGeoEnCreation();
			
			//Cet evenement ne sert que pour le Segment de droite
			if(geo instanceof SegmentDroite) {				
				SegmentDroite seg = (SegmentDroite) geo;
				
				//Le premier point du segment est la position du mouse press dans la toile
				seg.setPoint(new Point2D.Double(e.getX(),e.getY()), 1);
				
				//On sauvegarde maintenant ce segment comme nouvel objet temporaire
				this.frame.getGestionAnimation().setObjGeoEnCreation(seg);
			}
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//Si on est en mode listener (=Ajout d'objet)
		Toile toile = (Toile) e.getSource();
    	if(toile.getMode().equals("ajoutObj")) {
    		
    		//On recupere l'objet geometrique temporaire
    		ObjetGeometrique geo = this.frame.getGestionAnimation().getObjGeoEnCreation();
    		
    		//Cet evenement ne sert que pour le Segment de droite
    		if(geo instanceof SegmentDroite) {
    			//On va placer le deuxieme point du segment la ou la souris a relaché son clic
    			((SegmentDroite) geo).setPoint(new Point2D.Double(e.getX(), e.getY()), 2);
    			//On genere la nouvelle forme du segment pour le dessin
    			geo.generateShape();
    			
    			//On va maintenant enregistrer notre objet dans le gestionnaire d'animation
    			this.frame.getGestionAnimation().ajouterComportement(geo, null);
				this.frame.getGestionAnimation().refreshDessin(); //TODO: recup le temps courant
				
				//On a terminé la creation de la ligne, on ne reste pas en mode listener
				//toile.removeMouseMotionListener((MouseMotionListener) this);
				toile.modeSelection();
				this.frame.MAJListeObjGeo();
    		}
    	}
		
	}
}
