package affichage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.JPanel;

import formes.ObjetGeometrique;

public class Toile extends JPanel {

	// Buffers utilisés pour le page flipping
	private Image backBuffer;
	private Image primarySurface;
	
	private ArrayList<ObjetGeometrique> liste;
	private boolean flag;
	
	/**
	 * @param dim : La dimension de la toile
	 */
	public Toile(Dimension dim) {
		super();
		this.setDoubleBuffered(true);
		this.setPreferredSize(dim);	
		this.liste = new ArrayList<ObjetGeometrique>();
		this.flag = false;
	}
	
	/**
	 * Echange les deux buffers, pour permettre d'alterner le dessin et l'affichage
	 */
	protected void switchBuffers() {
		Image temp = this.backBuffer;
		
		this.backBuffer = this.primarySurface;
		this.primarySurface = temp;
	}
	
	public void paintComponent(Graphics g) {
		//Initialisation des buffers
		if(this.backBuffer == null) {
			this.primarySurface = createImage(this.getWidth(), this.getHeight());
			this.backBuffer = createImage(this.getWidth(), this.getHeight());
		}
		
		//On recupère le graphics du backBuffer pour dessiner dessus
		Graphics2D g2dImage = (Graphics2D) this.backBuffer.getGraphics();
		Graphics2D g2d = (Graphics2D) g;
		
		//On affiche l'image principale
		g2d.drawImage(this.primarySurface,0,0,null);
		
		//On vide notre backBuffer
		g2dImage.setColor(getBackground());
		g2dImage.fillRect(0, 0, this.getSize().width, this.getSize().height);
		
		//Ici le dessin sur le backBuffer
		for(int i = 0; i < this.liste.size(); i++) {
			g2dImage.setStroke(liste.get(i).getStroke());
			g2dImage.setColor(liste.get(i).getStrokeColor());
			g2dImage.draw(liste.get(i).getShape());
			
			if(this.liste.get(i).getFillColor() != null) {
				g2dImage.setColor(this.liste.get(i).getFillColor());
				g2dImage.fill(this.liste.get(i).getShape());
			}
		}
		
		//On echange nos deux buffers
		this.switchBuffers();
		g2dImage.dispose();
		
		if(this.flag)
			this.liste.clear();
	}
	
	public void addObjet(ObjetGeometrique geo) {
		this.liste.add(geo);
	}
	
	public void demanderViderListe() {
		this.flag = true;
	}
	
	private void viderListe() {
		this.liste.clear();
	}
}