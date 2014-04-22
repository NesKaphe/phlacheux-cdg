package affichage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.ListModel;

import formes.ObjetGeometrique;


public class Toile extends JPanel implements MouseListener,MouseMotionListener {

	// Buffer utilisé pour le dessin des formes par le gestionnaire d'animation
	private Image backBuffer;
		
	private static final long serialVersionUID = 1L;
	
	//Position de la souris
	private int x, y, ox, oy;
	
	//Objet temporaire en cours de creation (= null si aucun objet n'est en train d'etre créer)
	private ObjetGeometrique objTemporaire;
	
	private Edition parent;
	
	private boolean modeListener;
	
	/**
	 * @param dim : La dimension de la toile
	 */
	public Toile(Dimension dim) {
		super();
		this.setDoubleBuffered(true);
		this.setPreferredSize(dim);
		this.setSize(dim);//TODO : probablement remplacer par setMinSize 
		this.initObjTemporaire();
	}
	
	public Toile(Dimension dim, Edition parent) {
		this(dim);
		this.parent = parent;
		this.modeListener = false;
		this.addMouseListener(this);
	}
	
	public void paintComponent(Graphics g) {
		//Le buffer n'existe pas encore au moment du dessin
		if(this.backBuffer == null)
			this.initBuffer();
		
		//La dimension de la toile a changé, il faut changer le buffer
		if(this.backBuffer.getWidth(null) != this.getWidth() || this.backBuffer.getHeight(null) != this.getHeight()) {
			Image newBuffer = this.createImage(this.getWidth(), this.getHeight());
			Image tmp = this.backBuffer;
			this.backBuffer = newBuffer;
			this.initBuffer();
			Graphics2D g2tmp = (Graphics2D) this.backBuffer.getGraphics();
			g2tmp.drawImage((BufferedImage)tmp, null, 0, 0);
		}
		
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage((BufferedImage)this.backBuffer, null, 0, 0);
	}
	

	public void modeListener() {
		if(!this.modeListener) {
			this.modeListener = true;
			this.addMouseMotionListener(this);
		}
	}
	
	public void mousePressed(MouseEvent m) {
    	/*ox = m.getX();
    	oy = m.getY();
    	addMouseMotionListener((MouseMotionListener) this);*/
    }
    
    public void mouseReleased(MouseEvent m) {}
	public void mouseEntered(MouseEvent m) {}
    public void mouseExited(MouseEvent m) {}
    public void mouseClicked(MouseEvent m) {
    	if(this.modeListener) {
	    	ObjetGeometrique geo = this.getObjGeometrique();
	    	//Voir si c'est une ligne ici et faire un truc
	    		//...
	    	//Sinon c'est une forme qui se place qu'avec un clic
			this.parent.getGestionAnimation().ajouterComportement(geo, null);
			
			this.initObjTemporaire();
			removeMouseMotionListener((MouseMotionListener) this);
			this.modeListener = false;
			this.parent.listeObjets();
		}
    	else {
    		//Si on est pas en mode listener, le clic signifie selection
    		//On va demander au gestion animation si un objet contient les coordonnées du clic
    		//this.parent.getGestionAnimation().dessinerToile(0.); //TODO: recup le temps courant
    		Entry<Integer, ObjetGeometrique> entry = this.parent.getGestionAnimation().getObjectAt(m.getX(),m.getY(),0.);
    		this.parent.getListe().clearSelection();
    		//Si oui, on selectionne dans la liste de david la ligne correspondante
    		if(entry != null) {
    			this.dessineSelectionOf(entry.getValue());
    			ListModel<JListItem> model = this.parent.getListe().getModel();
    			for(int i = 0; i < model.getSize(); i++) {
    				if(model.getElementAt(i).getId() == entry.getKey()) {
    					this.parent.getListe().setSelectedIndex(i);
    				}
    			}
    		}
    	}
    }
    public void mouseDragged(MouseEvent e) {}
    
    public void mouseMoved(MouseEvent e) {
    	if (this.modeListener){
    		ox = e.getX();
    		oy = e.getY();
    		
    		ObjetGeometrique geo = this.getObjGeometrique();
			
			Point2D.Double centre = new Point2D.Double(ox,oy);
			geo.setCentre(centre);
			
			System.out.println(this.getObjGeometrique());
			this.parent.getGestionAnimation().dessinerToile(0.); //TODO: recup le temps courant
    	}
    }

    public void setObjTemporaire(ObjetGeometrique geo) {
    	this.objTemporaire = geo;
    }
    
    public ObjetGeometrique getObjGeometrique() {
    	return this.objTemporaire;
    }
    
    public void initObjTemporaire() {
    	this.objTemporaire = null;
    }
    
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
	
	public void dessineSelectionOf(ObjetGeometrique geo) {
		//On cherche le shape qui contient l'objet geometrique
		Shape shape = geo.getShape().getBounds2D();
		//On va dessiner le shape dans le buffer
		Graphics2D g = (Graphics2D) this.backBuffer.getGraphics();
		float []pointilles = { 2.0f, 3.0f };
		BasicStroke stroke = new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND,1.0f,pointilles,0.0f);
		g.setStroke(stroke);
		g.setColor(Color.blue);
		g.draw(shape);
		this.repaint();
	}
	
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
