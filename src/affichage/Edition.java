package affichage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import listeners.CreateAnimationListener;

import listeners.CreateObjGeoListener;
import listeners.ListeObjGeoSelectListener;


import formes.*;

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
	
	//Menus
	private JMenuBar menuBarEditionMode; //le menu du mode edition
	private JMenuBar menuBarVisionneuseMode; //le menu du mode visionneuse
	private JToolBar menu_object_add;

	
	//Liste des objets dessinés
	private JList<Item> liste;
	
	private ListeObjGeoSelectListener listenerListeObjet;
	
	//Bouton d'ajout d'animation
	JButton boutonAjoutAnimation;
	
	CreateAnimationListener listenerAnimations;
	
	//Element global de alarmbox de configuration
	private JColorChooser StrokeChooser;
	private JColorChooser FillChooser;
	private JTextField Epaisseur;
	
	// menu création d'objet
	JMenu menu_C = new JMenu("Création d'objet");
	
	// Item du menu création d'objet
	JMenuItem mi_Cercle = new JMenuItem("Cercle");
	JMenuItem mi_Triangle = new JMenuItem("Triangle");
	JMenuItem mi_Rectangle = new JMenuItem("Rectangle");
	JMenuItem mi_Carre = new JMenuItem("Carré");
	JMenuItem mi_Segment = new JMenuItem("Segment");
	JMenuItem mi_Etoile = new JMenuItem("Etoile");
	JMenuItem mi_Hexagone = new JMenuItem("Hexagone");
	JMenuItem mi_Croix = new JMenuItem("Croix");
	
	
	public Edition() {
		super("Edition");
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Creation de la toile
		this.toile = new Toile(new Dimension(Edition.widthToile, Edition.heightToile), this);
		this.toile.setBackground(Color.white);
		this.toile.setOpaque(true);
		
		//Creation du gestionnaire d'animation
		this.gestionnaire = new GestionAnimation(this.toile);
		
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
    	
    	//création et ajout du listener des menuItem :
    	//ce listener nous crée une alerte box pour créer un objGeométrique:
    	final CreateObjGeoListener create_obj_listener = new CreateObjGeoListener(this.toile);
    	mi_Cercle.addActionListener(create_obj_listener);
    	mi_Triangle.addActionListener(create_obj_listener);
    	mi_Rectangle.addActionListener(create_obj_listener);
    	mi_Carre.addActionListener(create_obj_listener);
    	mi_Segment.addActionListener(create_obj_listener);
    	mi_Etoile.addActionListener(create_obj_listener);
    	
    	//ajoute de la commande assosier:
    	mi_Cercle.setActionCommand("create_Cercle");
    	mi_Triangle.setActionCommand("create_Triangle");
    	mi_Rectangle.setActionCommand("create_Rectangle");
    	mi_Carre.setActionCommand("create_Carre");
    	mi_Segment.setActionCommand("create_Segment");
    	mi_Etoile.setActionCommand("create_Etoile");
    	 	
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
    	
    	//Liste
    	liste = new JList<Item>();

    	liste.addMouseListener(new MouseAdapter() {//TODO : implémenter une classe si code trop grand
    		//TODO : EN FAIT ÇA MARCHE PAS !!!!!!!!!!!!!!!!!!!

    		public void mouseClicked(MouseEvent e) {
    			//On est interessé par le double clic sur la liste
    			if(e.getClickCount() == 2) {
    				int index = liste.locationToIndex(e.getPoint());
    				ListModel<Item> lm = liste.getModel();
    				Item item = lm.getElementAt(index);
    				ObjetGeometrique geo = gestionnaire.getObject(item.getId(), 0.); //TODO: recup le temps courant
    				
    				create_obj_listener.actionPerformed("modif_"+geo.getNom());
    				
    				System.out.println("Double clic sur "+item+ "id : "+ item.getId());
    			}
    		}
    		
		});
    	
    	liste.setBackground(Color.lightGray);
    	
    	listenerAnimations = new CreateAnimationListener(liste);
    	
    	boutonAjoutAnimation = new JButton("Creer animation");
    	boutonAjoutAnimation.setEnabled(false);
    	boutonAjoutAnimation.addActionListener(listenerAnimations);
    	boutonAjoutAnimation.setActionCommand("creation");
    	listenerListeObjet = new ListeObjGeoSelectListener(this.gestionnaire, this.toile, this.boutonAjoutAnimation);
    	liste.addListSelectionListener(listenerListeObjet);
    	
    	JPanel east = new JPanel();
    	BorderLayout grid = new BorderLayout();
    	east.setLayout(grid);
    	east.setBackground(Color.lightGray);
    	
    	JLabel titre = new JLabel("Liste d'objets");
    	titre.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
    	
    	//On met la JList dans un scrollPane dans le cas ou il y a trop d'elements
    	JScrollPane scrollPane = new JScrollPane(liste);
    	scrollPane.setPreferredSize(new Dimension(0,0));
    	
    	east.add(titre, BorderLayout.NORTH);
    	east.add(scrollPane, BorderLayout.CENTER);
    	east.add(boutonAjoutAnimation, BorderLayout.SOUTH);
    	this.add(east, BorderLayout.EAST);
    	
    	//Visionneuse
    	VisionneuseAnimation va = new VisionneuseAnimation(this, gestionnaire, 2000);
    	va.dessineAnimation();
    	this.add(va, BorderLayout.SOUTH);
    	
    	
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

}
