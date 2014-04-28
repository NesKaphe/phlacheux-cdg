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

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import listeners.MouseToileListener;

import formes.ObjetGeometrique;


public class Toile extends JPanel {

	// Buffer utilisé pour le dessin des formes par le gestionnaire d'animation
	private Image backBuffer;
		
	private static final long serialVersionUID = 1L;
	
	private Edition parent;
	
	private String mode;
	
	private MouseToileListener listener;
	
	private Shape trajectoire;//pour dessiner la trajectoire sur la toile
	
	private JPopupMenu popupMenu; //Menu clic droit
	
	/**
	 * Ancien constructeur
	 * @param dim : La dimension de la toile
	 */
	public Toile(Dimension dim) {
		super();
		this.setDoubleBuffered(true);
		this.setPreferredSize(dim);
		this.setSize(dim);//TODO : probablement remplacer par setMinSize 
		this.trajectoire = null;
		
		this.popupMenu = new JPopupMenu();
		
		JMenuItem menuItem_modification = new JMenuItem("Modifier");
		menuItem_modification.setActionCommand("modification");
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
		this.trajectoire = null;
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
	
	public void modeLecture() {
		this.mode = "lecture";
		this.removeMouseMotionListener((MouseMotionListener)this.listener);
	}
	
	public void modeTrajectoire(){
		this.mode = "trajectoire";
		this.addMouseMotionListener((MouseMotionListener)this.listener);//TODO : modifier le listener de MouseToileListener
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
				this.parent.getGestionAnimation().refreshDessin();
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
	 * va dessiner aussi la trajecoire si on est en mode trajectoire
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

	/**
	 * cette méthode ne donne aucunne garantie que le dessin soit complet
	 * c'est pourquoi il faut passer par GestionAnimation
	 * @return
	 */
	public BufferedImage getCopyBackBuffer() {
		BufferedImage bi  = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) bi.getGraphics();
		g2.drawImage((BufferedImage) this.backBuffer, null, 0, 0);
		return bi;

	}
	
	
}
