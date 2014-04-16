package affichage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;
import formes.ObjetGeometrique;


public class Toile extends JPanel implements MouseListener,MouseMotionListener {

	// Buffer utilisé pour le dessin des formes par le gestionnaire d'animation
	private Image backBuffer;
	
	//private Image primarySurface;//obsolète //TODO:virer ce buffer
	
	/**
	 * ??
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<ObjetGeometrique> liste;//liste d'objets géometrique à dessiner
	private boolean flag;//obsolète
	
	//Position de la souris
	private int x, y, ox, oy;
	
	/**
	 * @param dim : La dimension de la toile
	 */
	public Toile(Dimension dim) {
		super();
		this.setDoubleBuffered(true);
		this.setPreferredSize(dim);
		this.setSize(dim);
		this.liste = new ArrayList<ObjetGeometrique>();//TODO : (down)
		/*modifier en conteneur Set (ou autre) pour pouvoir empècher 
		 * les doublons et retirer certaines valeurs
		 */
		this.flag = false;
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage((BufferedImage)this.backBuffer, null, 0, 0);
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
	
	public void mousePressed(MouseEvent m) {
    	ox = m.getX();
    	oy = m.getY();
    	addMouseMotionListener((MouseMotionListener) this);
    }
    
    public void mouseReleased(MouseEvent m) {
    	removeMouseMotionListener((MouseMotionListener) this);
    }
	public void mouseEntered(MouseEvent m) { }
    public void mouseExited(MouseEvent m) { }
    public void mouseClicked(MouseEvent m) { }
    
    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) { }

	public void dessineObjet(ObjetGeometrique geo) {		
		Graphics2D g = (Graphics2D) this.backBuffer.getGraphics();
				
		g.setStroke(geo.getStroke());
		g.setColor(geo.getStrokeColor());
		g.draw(geo.getShape());
		if(geo.getFillColor() != null) {
			g.setColor(geo.getFillColor());
			g.fill(geo.getShape());
		}
		
	}
	
	public void initBuffer() {
		
		if(this.backBuffer == null) {
			this.backBuffer = this.createImage(this.getWidth(), this.getHeight());
			this.initBuffer();
		}
		
		Graphics2D g = (Graphics2D) this.backBuffer.getGraphics();
		
		g.setColor(this.getBackground());
		g.fillRect(0, 0, this.getSize().width, this.getSize().height);
	}
	
	/*
	 ancienne version et pas correct avec les buffered Images 
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
			System.out.println("draw!!"+i+"  parent :"+this.getParent()+"\nshape ="+liste.get(i).getShape());//DEBUG
			
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
	 //Echange les deux buffers, pour permettre d'alterner le dessin et l'affichage
	 
	protected void switchBuffers() {
		Image temp = this.backBuffer;
		
		this.backBuffer = this.primarySurface;
		this.primarySurface = temp;
	}
	*/
}