package affichage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import listeners.MouseToileListener;

import formes.ObjetGeometrique;


public class Toile extends JPanel {

	// Buffer utilisé pour le dessin des formes par le gestionnaire d'animation
	private Image backBuffer;
		
	private static final long serialVersionUID = 1L;
	
	private Edition parent;
	
	private String mode;
	
	private MouseToileListener listener;
	
	/**
	 * Ancien constructeur
	 * @param dim : La dimension de la toile
	 */
	public Toile(Dimension dim) {
		super();
		this.setDoubleBuffered(true);
		this.setPreferredSize(dim);
		this.setSize(dim);//TODO : probablement remplacer par setMinSize 
	}
	
	/**
	 * @param dim La dimension de la toile
	 * @param parent Fenetre d'edition dans laquelle la toile est insérée
	 */
	public Toile(Dimension dim, Edition frame, MouseToileListener listener) {
		this(dim);
		this.addMouseListener(listener);
		this.parent = frame;
		this.listener = listener;
		this.modeSelection(); //par defaut on est en mode selection
	}
	
	
	/**
	 * @return un string representant le mode de la toile
	 */
	public String getMode() {
		return this.mode;
	}
	
	public void modeAjout() {
		this.mode = "ajoutObj";
		this.addMouseMotionListener((MouseMotionListener)this.listener);
	}
	
	public void modeSelection() {
		this.mode = "selection";
		this.removeMouseMotionListener((MouseMotionListener)this.listener);
	}
	
	public void paintComponent(Graphics g) {
		//Le buffer n'existe pas encore au moment du dessin
		if(this.backBuffer == null)
			this.initBuffer();
		
		//La dimension de la toile a changé, il faut changer le buffer
		if(this.backBuffer.getWidth(null) != this.getWidth() || this.backBuffer.getHeight(null) != this.getHeight()) {
			this.backBuffer = null;
			this.initBuffer();
			if (parent != null)
				this.parent.getGestionAnimation().dessinerToile(0.); //TODO: recup temps courant //COMMENT clem : savoir si on garde ça ou non (ça ressemble à du teste pour moi)
		}
		
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage((BufferedImage)this.backBuffer, null, 0, 0);
	}
    
    /**
     * dessineObjet va dessiner un objetGeometrique dans le backBuffer de la toile
     * @param geo l'objet geometrique à dessiner
     */
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
	
	/**
	 * @param geo l'objet geometrique autour duquel on va dessiner un rectangle de selection
	 */
	public void dessineSelectionOf(ObjetGeometrique geo) {
		//On cherche le shape qui contient l'objet geometrique (rectangle entourant)
		BasicStroke shapeStroke = geo.getStroke();
		
		//On doit prendre en compte l'epaisseur du trait pour dessiner autour
		Shape shape = shapeStroke.createStrokedShape(geo.getShape()).getBounds2D();
		
		//On va dessiner le shape obtenu dans le buffer
		Graphics2D g = (Graphics2D) this.backBuffer.getGraphics();
		
		float []pointilles = { 2.0f, 3.0f };
		BasicStroke stroke = new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND,1.0f,pointilles,0.0f);
		g.setStroke(stroke);
		g.setColor(Color.blue);
		g.draw(shape);
		
		this.repaint();
	}
	
	/**
	 * initBuffer, va creer le buffer de la taille de la toile
	 */
	public void initBuffer() {

		if(this.backBuffer == null) {
			this.backBuffer = this.createImage(this.getWidth(), this.getHeight());
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
