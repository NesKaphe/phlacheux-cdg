package affichage;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JPanel;

public class Toile extends JPanel {

	// Buffers utilisés pour le page flipping
	private Image backBuffer;
	private Image primarySurface;
	
	/**
	 * @param dim : La dimension de la toile
	 */
	public Toile(Dimension dim) {
		super();
		this.setPreferredSize(dim);	
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
		
		g2dImage.setColor(getForeground());
		//Ici le dessin sur le backBuffer
		
		//On echange nos deux buffers
		this.switchBuffers();
		g2dImage.dispose();
	}
}