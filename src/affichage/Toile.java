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
import formes.SegmentDroite;


public class Toile extends JPanel implements MouseListener,MouseMotionListener {

	// Buffer utilisé pour le dessin des formes par le gestionnaire d'animation
	private Image backBuffer;
		
	private static final long serialVersionUID = 1L;
	
	//Position de la souris
	private int x, y, ox, oy;
	
	//Objet temporaire en cours de creation (= null si aucun objet n'est en train d'etre créer)
	private ObjetGeometrique objTemporaire;
	
	private Edition parent;
	
	//modeListener permet de detecter si on est en mode ajout d'objet ou non sur la toile
	private boolean modeListener;
	
	/**
	 * Ancien constructeur
	 * @param dim : La dimension de la toile
	 */
	public Toile(Dimension dim) {
		super();
		this.setDoubleBuffered(true);
		this.setPreferredSize(dim);
		this.setSize(dim);//TODO : probablement remplacer par setMinSize 
		this.initObjTemporaire();
	}
	
	/**
	 * @param dim La dimension de la toile
	 * @param parent Fenetre d'edition dans laquelle la toile est insérée
	 */
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
			this.backBuffer = null;
			this.initBuffer();
			if (parent != null)
				this.parent.getGestionAnimation().dessinerToile(0.); //TODO: recup temps courant //COMMENT clem : savoir si on garde ça ou non (ça ressemble à du teste pour moi)
		}
		
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage((BufferedImage)this.backBuffer, null, 0, 0);
	}
	
	/**
	 * modeListener passe la toile en mode listener
	 * C'est a dire en mode ajout d'objet geometrique
	 */
	public void modeListener() {
		if(!this.modeListener) {
			this.modeListener = true;
			this.addMouseMotionListener(this);
		}
	}
	
	public void mousePressed(MouseEvent m) {
		//Si on est en mode listener (=Ajout d'objet)
		if(this.modeListener) {
			//On recupere l'objet geometrique temporaire
			ObjetGeometrique geo = this.getObjGeometrique();
			
			//Cet evenement ne sert que pour le Segment de droite
			if(geo instanceof SegmentDroite) {				
				SegmentDroite seg = (SegmentDroite) geo;
				
				//Le premier point du segment est la position du mouse press dans la toile
				seg.setPoint(new Point2D.Double(m.getX(),m.getY()), 1);
				
				//On sauvegarde maintenant ce segment comme nouvel objet temporaire
				this.setObjTemporaire(seg);
			}
		}
    }
    
    public void mouseReleased(MouseEvent m) {
    	//Si on est en mode listener (=Ajout d'objet)
    	if(this.modeListener) {
    		
    		//On recupere l'objet geometrique temporaire
    		ObjetGeometrique geo = this.getObjGeometrique();
    		
    		//Cet evenement ne sert que pour le Segment de droite
    		if(geo instanceof SegmentDroite) {
    			//On va placer le deuxieme point du segment la ou la souris a relaché son clic
    			((SegmentDroite) geo).setPoint(new Point2D.Double(m.getX(),m.getY()), 2);
    			//On genere la nouvelle forme du segment pour le dessin
    			geo.generateShape();
    			
    			//On va maintenant enregistrer notre objet dans le gestionnaire d'animation
    			this.parent.getGestionAnimation().ajouterComportement(geo, null);
				this.parent.getGestionAnimation().dessinerToile(0.); //TODO: recup le temps courant
				
				//On a terminé la creation de la ligne, on ne reste pas en mode listener
				removeMouseMotionListener((MouseMotionListener) this);
				this.modeListener = false;
				this.parent.listeObjets();
    		}
    	}
    }
    
	public void mouseEntered(MouseEvent m) {}
    public void mouseExited(MouseEvent m) {}
    
    public void mouseClicked(MouseEvent m) {
    	//Si on est en mode listener (=Ajout d'objet)
    	if(this.modeListener) {
    		
    		//On commence par recupérer l'objet geometrique temporaire
	    	ObjetGeometrique geo = this.getObjGeometrique();
	    	
	    	//Cet evenement ne sera utilisé que pour les formes se plaçant avec un clic unique (= Pas un segment)
	    	//L'ajout de ces formes est combiné avec le mouseMoved
	    	if(!(geo instanceof SegmentDroite)) {
	    		
	    		//On enregistre l'objet geometrique dans le gestionnaire d'animation
		    	this.parent.getGestionAnimation().ajouterComportement(geo, null);
		    	
		    	//On a terminé notre ajout, on ne reste pas en mode listener
		    	this.initObjTemporaire();
				removeMouseMotionListener((MouseMotionListener) this);
				this.modeListener = false;
				
				//On oublie pas de mettre a jour la liste des objets (JList)
				this.parent.listeObjets();
	    	}
		}
    	else { //Si on est pas en mode listener, le clic signifie selection
    		
    		//Si l'objet temporaire n'est pas null (Cas du segment après le dessin
    		if(this.getObjGeometrique() != null) {
    			//On remet a zéro
    			this.initObjTemporaire();
    		}
    		else { //Sinon, on va essayer de detecter le clic sur les objets geometriques dessinés
    			
	    		//On va demander au gestion animation si un objet contient les coordonnées du clic
	    		Entry<Integer, ObjetGeometrique> entry = this.parent.getGestionAnimation().getObjectAt(m.getX(),m.getY(),0.);
	
	    		//On deselectionne tous les elements de la liste
	    		this.parent.getListe().clearSelection();
	    		
	    		//Si on a trouvé un objet geometrique, on selectionne dans la liste la ligne correspondante
	    		if(entry != null) {
	    			
	    			//On va maintenant dessiner un rectangle pour montrer a l'utilisateur que l'objet est selectionné
	    			this.dessineSelectionOf(entry.getValue());
	    			
	    			//Et finalement, va selectionner la ligne de l'objet dans la JList
	    			ListModel<Item> model = this.parent.getListe().getModel();
	    			for(int i = 0; i < model.getSize(); i++) {
	    				if(model.getElementAt(i).getId() == entry.getKey()) {
	    					this.parent.getListe().setSelectedIndex(i);
	    				}
	    			}
	    		}
    		}
    	}
    }
    
    public void mouseDragged(MouseEvent e) {
    	//Si on est en mode listener (Ajout d'objet)
    	if (this.modeListener){
    		ox = e.getX();
    		oy = e.getY();
    		
    		ObjetGeometrique geo = this.getObjGeometrique();
    		Point2D.Double point = new Point2D.Double(ox,oy);
    		//Cet evenement n'est utilisé que pour le segment de droite (deplacement de souris apres le clic)
    		if(geo instanceof SegmentDroite) {
				SegmentDroite seg = (SegmentDroite) geo;
				seg.setPoint(point, 2);
				
				//On genere la nouvelle forme du segment pour le dessin
				seg.generateShape();
				
				//On met a jour notre objet temporaire
				this.setObjTemporaire(seg);
				
				//Et on raffraichi le dessin des objets
				this.parent.getGestionAnimation().dessinerToile(0.); //TODO: recup le temps courant
    		}
    	}	
    }
    
    public void mouseMoved(MouseEvent e) {
    	//Si on est en mode listener (Ajout d'objet)
    	if (this.modeListener){
    		ox = e.getX();
    		oy = e.getY();
    		
    		ObjetGeometrique geo = this.getObjGeometrique();
    		Point2D.Double point = new Point2D.Double(ox,oy);
    		//Cet evenement sert pour les objets geometrique qui ne sont pas des Segments
			if(!(geo instanceof SegmentDroite)) {
				//On deplace le centre de l'objet sur le pointeur de la souris
				geo.setCentre(point);
				
				//On raffraichi le dessin des objets
				this.parent.getGestionAnimation().dessinerToile(0.); //TODO: recup le temps courant
			}
    	}
    }

    
    public void setObjTemporaire(ObjetGeometrique geo) {
    	this.objTemporaire = geo;
    }
    
        
    public ObjetGeometrique getObjGeometrique() {
    	return this.objTemporaire;
    }
    
    /**
     * Va remettre l'objet temporaire a null (utilisé apres avoir crée un objet completement)
     */
    public void initObjTemporaire() {
    	this.objTemporaire = null;
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
