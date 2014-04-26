package affichage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import listeners.CreateAnimationListener;
import listeners.CreateObjGeoListener;
import listeners.LectureAnimationListener;
import listeners.ListeObjGeoSelectListener;
import listeners.MouseToileListener;
import listeners.SuppressionComportementListener;


import Animations.Comportement;
import Animations.GestionAnimation;

@SuppressWarnings("serial")
public class Edition extends JFrame {

	//Gestionnaire d'animation
	private GestionAnimation gestionnaire;
	
	//Toile 
	private Toile toile;
	private static final int widthToile = 300;
	private static final int heightToile = 300;
	
	private MouseToileListener listenerToile;
	
	//Menus
	private JMenuBar menuBarEditionMode; //le menu du mode edition
	private JToolBar menu_object_add;

	
	//Liste des objets dessinés
	private JList<Item> liste;
	
	private ListeObjGeoSelectListener listenerListeObjet;
	
	//Bouton d'ajout d'animation
	private JButton boutonAjoutAnimation;
	private JButton boutonSuppressionComportement;
	
	private CreateAnimationListener listenerAnimations;
	
	private CreateObjGeoListener create_obj_listener;
	
	private SuppressionComportementListener suppressionComportementListener;
	
	private LectureAnimationListener lectureListener;
	
	private VisionneuseAnimation visionneuse;
	
	private LecteurAnimation lecteur;
	
	// menu création d'objet
	JMenu menu_C = new JMenu("Création d'objet");
	
	// menu lecture
	JMenu menu_L = new JMenu("Lecture");
	
	// Item du menu création d'objet
	JMenuItem mi_Cercle = new JMenuItem("Cercle");
	JMenuItem mi_Triangle = new JMenuItem("Triangle");
	JMenuItem mi_Rectangle = new JMenuItem("Rectangle");
	JMenuItem mi_Carre = new JMenuItem("Carré");
	JMenuItem mi_Segment = new JMenuItem("Segment");
	JMenuItem mi_Etoile = new JMenuItem("Etoile");
	JMenuItem mi_Hexagone = new JMenuItem("Hexagone");
	JMenuItem mi_Croix = new JMenuItem("Croix");
	
	JMenuItem mi_lecture_debut = new JMenuItem("Lecture depuis le debut");
	JMenuItem mi_arret_lecture = new JMenuItem("Arret lecture");
	JMenuItem mi_pause_lecture = new JMenuItem("Pause");
	JMenuItem mi_reprendre_lecture = new JMenuItem("Reprendre lecture");
	
	public Edition() {
		super("Edition");
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Creation de la toile
		this.listenerToile = new MouseToileListener(this);
		
		this.toile = new Toile(new Dimension(Edition.widthToile, Edition.heightToile), this, this.listenerToile);
		this.toile.setBackground(Color.white);
		this.toile.setOpaque(true);
		
		//Creation du gestionnaire d'animation
		this.gestionnaire = new GestionAnimation(this.toile);
		this.lecteur = new LecteurAnimation(this.gestionnaire);
		this.gestionnaire.setLecteurAnimation(this.lecteur);
		
		//Creation des menus
		this.menuBarEditionMode = new JMenuBar();
		
    	// menu fichier
    	JMenu menu_F = new JMenu("Fichier");
    	JMenuItem mi_new = new JMenuItem("Nouvelle Toile");
    	JMenuItem mi_refresh = new JMenuItem("Rafraichir");
    	JMenuItem mi_add_object = new JMenuItem("Ajouter un forme");
    	JMenuItem mi_exit = new JMenuItem("Quitter");
    	menu_F.add(mi_new);
    	menu_F.add(mi_refresh);
    	menu_F.add(mi_add_object);
    	menu_F.add(mi_exit);
    	menuBarEditionMode.add(menu_F);
    	  
    	// menu création d'objet
    	menu_C.add(mi_Cercle);
    	menu_C.add(mi_Triangle);
    	menu_C.add(mi_Rectangle);
    	menu_C.add(mi_Carre);
    	menu_C.add(mi_Segment);
    	menu_C.add(mi_Etoile);
    	menu_C.add(mi_Hexagone);
    	menu_C.add(mi_Croix);
    	menuBarEditionMode.add(menu_C);
    	
    	// menu lecture
    	menu_L.add(mi_lecture_debut);
    	menu_L.add(mi_arret_lecture);
    	menu_L.add(mi_pause_lecture);
    	menu_L.add(mi_reprendre_lecture);
    	menuBarEditionMode.add(menu_L);
    	
    	//création et ajout du listener des menuItem :
    	    	
    	lectureListener = new LectureAnimationListener(this);
    	    	
    	
    	//ajoute de la commande assosier:
    	mi_Cercle.setActionCommand("create_Cercle");
    	mi_Triangle.setActionCommand("create_Triangle");
    	mi_Rectangle.setActionCommand("create_Rectangle");
    	mi_Carre.setActionCommand("create_Carre");
    	mi_Segment.setActionCommand("create_Segment");
    	mi_Etoile.setActionCommand("create_Etoile");
    	mi_Croix.setActionCommand("create_Croix");
    	mi_Hexagone.setActionCommand("create_Hexagone");
    	
    	mi_lecture_debut.setActionCommand("lecture_debut");
    	mi_arret_lecture.setActionCommand("arret_lecture");
    	mi_pause_lecture.setActionCommand("pause_lecture");
    	mi_reprendre_lecture.setActionCommand("reprendre_lecture");
    	 	
    	//Formes
    	// menu d'ajout d'objet
    	menu_object_add = new JToolBar(SwingConstants.HORIZONTAL);
    	menu_object_add.setFloatable(true);
    	menu_object_add.setRollover(true);
    	menu_object_add.setName("Add object");
    	menu_object_add.setVisible(false);
    	JPanel list_form= new JPanel();
    	// Bouton objectGeometrique Cercle
    	JButton cercle = new JButton("Cercle");
	    cercle.setToolTipText("Aide pour "+cercle.getText());
	    list_form.add(cercle);
    	menu_object_add.add(list_form);
    	
    	this.getContentPane().setLayout(new BorderLayout());
    	this.add(toile, BorderLayout.CENTER);
    	this.add(menu_object_add,BorderLayout.SOUTH);
    	this.setJMenuBar(this.menuBarEditionMode);
    	
    	//Visionneuse
    	this.visionneuse = new VisionneuseAnimation(this, gestionnaire, 2000);
    	this.visionneuse.dessineAnimation();
    	this.gestionnaire.setVisionneuse(this.visionneuse);
    	this.add(this.visionneuse, BorderLayout.SOUTH);
    	
    	//Listener du lecteur d'animation
    	this.lecteur.addLectureAnimationListener(lectureListener);
    	
    	//Liste de comportements
    	liste = new JList<Item>();
    	
    	//ce listener nous crée une alerte box pour créer un objGeométrique:
    	create_obj_listener = new CreateObjGeoListener(this.toile, this.gestionnaire);
    	
    	suppressionComportementListener = new SuppressionComportementListener(this, create_obj_listener);
    	
    	liste.setBackground(Color.lightGray);
    	
    	listenerAnimations = new CreateAnimationListener(liste, this.visionneuse);
    	
    	boutonAjoutAnimation = new JButton("Creer animation");
    	boutonAjoutAnimation.setEnabled(false);
    	boutonAjoutAnimation.setActionCommand("creation");
    	
    	boutonSuppressionComportement = new JButton("Supprimer selection");
    	boutonSuppressionComportement.setEnabled(false);
    	boutonSuppressionComportement.setActionCommand("suppression");
    	
    	listenerListeObjet = new ListeObjGeoSelectListener(this, this.create_obj_listener);
    	
    	JPanel east = new JPanel();
    	BorderLayout grid = new BorderLayout();
    	east.setLayout(grid);
    	east.setBackground(Color.lightGray);
    	
    	JLabel titre = new JLabel("Liste d'objets");
    	titre.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
    	
    	//On met la JList dans un scrollPane dans le cas ou il y a trop d'elements
    	JScrollPane scrollPane = new JScrollPane(liste);
    	scrollPane.setPreferredSize(new Dimension(0,0));
    	
    	JPanel east_south = new JPanel();
    	BorderLayout grid_south = new BorderLayout();
    	east_south.setLayout(grid_south);
    	
    	east_south.add(boutonSuppressionComportement, BorderLayout.NORTH);
    	east_south.add(boutonAjoutAnimation, BorderLayout.SOUTH);
    	
    	east.add(titre, BorderLayout.NORTH);
    	east.add(scrollPane, BorderLayout.CENTER);
    	east.add(east_south, BorderLayout.SOUTH);
    	this.add(east, BorderLayout.EAST);
    	
    	//On est par defaut en mode edition
    	this.initListeners();
    	this.modeEdition();
    	
    	//TODO : on va voir si c'est vraiment utile ========================================
    	mi_new.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    		}
    	});
    	
    	mi_refresh.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    		}
    	});
    	
    	mi_add_object.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    		}
    	});
    	
    	// le champ quitter du menu ferme tout
    	mi_exit.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			System.exit(0);
    		}
	    });

    	this.MAJListeObjGeo();//mettre à jour la liste d'objets Géométriques
    	//===============================================================================
	}
	
	
	/**
	 * listeObjets va mettre a jour la JList contenant les objets geometriques
	 */
	public void MAJListeObjGeo() {
		HashMap<Integer, Comportement> map = this.gestionnaire.getListComportements();
		DefaultListModel<Item> lm = new DefaultListModel<Item>();
		for(Entry<Integer, Comportement> entry : map.entrySet()) {
			lm.addElement(new Item(entry.getValue().getId(), entry.getValue()));
		}
		liste.setModel(lm);
	}
	
	/**
	 * C'est cette methode qui va lier tous les elements de la frame a un listener
	 */
	public void initListeners() {
		mi_Cercle.addActionListener(create_obj_listener);
    	mi_Triangle.addActionListener(create_obj_listener);
    	mi_Rectangle.addActionListener(create_obj_listener);
    	mi_Carre.addActionListener(create_obj_listener);
    	mi_Segment.addActionListener(create_obj_listener);
    	mi_Etoile.addActionListener(create_obj_listener);
    	mi_Croix.addActionListener(create_obj_listener);
    	mi_Hexagone.addActionListener(create_obj_listener);
		
    	mi_lecture_debut.addActionListener(lectureListener);
    	mi_arret_lecture.addActionListener(lectureListener);
    	mi_pause_lecture.addActionListener(lectureListener);
    	mi_reprendre_lecture.addActionListener(lectureListener);
    	
    	boutonAjoutAnimation.addActionListener(listenerAnimations);
    	boutonSuppressionComportement.addActionListener(suppressionComportementListener);
    	
		liste.addListSelectionListener(listenerListeObjet);
		liste.addMouseListener(listenerListeObjet);
	}
	
	/**
	 * Va passer la fenetre en mode edition, c'est a dire activer tous les boutons d'edition
	 * sur les elements
	 */
	public void modeEdition() {
		
		mi_Cercle.setEnabled(true);
		mi_Triangle.setEnabled(true);
		mi_Rectangle.setEnabled(true);
		mi_Carre.setEnabled(true);
		mi_Segment.setEnabled(true);
		mi_Etoile.setEnabled(true);
		mi_Hexagone.setEnabled(true);
		mi_Croix.setEnabled(true);
		
		mi_lecture_debut.setEnabled(true);
    	mi_arret_lecture.setEnabled(false);
    	mi_pause_lecture.setEnabled(false);
    	if(this.lecteur.getTempsCourant()>0. && this.lecteur.getTempsCourant()<this.gestionnaire.getEndAnimations())
    		mi_reprendre_lecture.setEnabled(true);
    	else
    		mi_reprendre_lecture.setEnabled(false);
		    	
    	liste.addListSelectionListener(listenerListeObjet);
		this.toile.modeSelection();
	}
	
	/**
	 * Va passer la fenetre en mode lecture, c'est a dire desactiver tous les boutons d'edition
	 * et ne laisser que les listeners de lecture
	 */
	public void modeLecture() {
		
		mi_Cercle.setEnabled(false);
		mi_Triangle.setEnabled(false);
		mi_Rectangle.setEnabled(false);
		mi_Carre.setEnabled(false);
		mi_Segment.setEnabled(false);
		mi_Etoile.setEnabled(false);
		mi_Hexagone.setEnabled(false);
		mi_Croix.setEnabled(false);
		
		mi_lecture_debut.setEnabled(false);
    	mi_arret_lecture.setEnabled(true);
    	mi_pause_lecture.setEnabled(true);
    	
    	mi_reprendre_lecture.setEnabled(false);
		
    	//On deselectionne tous les elements de la liste et retire le listener
    	liste.removeListSelectionListener(listenerListeObjet);
    	liste.clearSelection();
		
		this.toile.modeLecture();
	}
	
	public void activerAjoutSuppression() {
		this.boutonAjoutAnimation.setEnabled(true);
		this.boutonSuppressionComportement.setEnabled(true);
	}
	
	public void desactiverAjoutSuppression() {
		this.boutonAjoutAnimation.setEnabled(false);
		this.boutonSuppressionComportement.setEnabled(false);
	}
	
	public JList<Item> getListe() {
		return this.liste;
	}
	
	public JButton getBoutonAjoutAnimation() {
		return this.boutonAjoutAnimation;
	}
	
	public Toile getToile() {
		return this.toile;
	}
	
	public GestionAnimation getGestionAnimation() {
		return this.gestionnaire;
	}
	
	public LecteurAnimation getLecteur() {
		return this.lecteur;
	}
	
	public JMenuItem getMenuLectureDebut() {
		return this.mi_lecture_debut;
	}
	
	public JMenuItem getMenuArretLecture() {
		return this.mi_arret_lecture;
	}

}
