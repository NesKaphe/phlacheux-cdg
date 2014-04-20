package affichage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import Animations.GestionAnimation;
import Listeners.BoutonFormeListener;
import Listeners.MouseMotionToileListener;
import Listeners.MouseToileListener;

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
	
	public Edition() {
		super("Edition");
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Creation de la toile
		this.toile = new Toile(new Dimension(Edition.widthToile, Edition.heightToile));
		this.toile.setBackground(Color.white);
		this.toile.setOpaque(true);
		
		//Listeners de la toile
		MouseMotionToileListener mouseToileListener = new MouseMotionToileListener(this);
		MouseToileListener mouseClickToileListener = new MouseToileListener(this, mouseToileListener);
		this.toile.addMouseListener(mouseClickToileListener);
		
		//Creation du gestionnaire d'animation
		this.gestionnaire = new GestionAnimation(this.toile);
		
		//Creation des menus
		this.menuBarEditionMode = new JMenuBar();
		
		//Fichier
		JMenu menu = new JMenu("Fichier");
		JMenuItem mi_new = new JMenuItem("Nouvelle Toile");
		JMenuItem mi_refresh = new JMenuItem("Rafraichir");
		JMenuItem mi_add_object = new JMenuItem("Ajouter un forme");
    	JMenuItem mi_exit = new JMenuItem("Quitter");
    	menu.add(mi_new);
    	menu.add(mi_refresh);
    	menu.add(mi_add_object);
    	menu.add(mi_exit);
    	this.menuBarEditionMode.add(menu);
    	
    	//Formes
    	// menu d'ajout d'objet
    	menu_object_add = new JToolBar(SwingConstants.HORIZONTAL);
    	menu_object_add.setFloatable(true);
    	menu_object_add.setRollover(true);
    	menu_object_add.setName("Add object");
    	JPanel list_form= new JPanel();
    	// Bouton objectGeometrique Cercle
    	JButton cercle = new JButton("Cercle");
	    cercle.setToolTipText("Aide pour "+cercle.getText());
	    list_form.add(cercle);
	    // Bouton objectGeometrique Triangle
    	JButton triangle = new JButton("Triangle");
    	triangle.setToolTipText("Aide pour "+triangle.getText());
    	list_form.add(triangle);
	    // Bouton objectGeometrique Rectangle
    	JButton rectangle = new JButton("Rectangle");
    	rectangle.setToolTipText("Aide pour "+rectangle.getText());
    	list_form.add(rectangle);
	    JButton ligne = new JButton("Ligne");
    	ligne.setToolTipText("Aide pour "+ligne.getText());
    	list_form.add(ligne);
    	// Bouton objectGeometrique Carre
    	JButton carre = new JButton("Carré");
	    list_form.add(carre);
	    // Bouton objectGeometrique Cercle
    	JButton b5= new JButton("b5");
    	list_form.add(b5);
	    // Bouton objectGeometrique Cercle
    	JButton b6 = new JButton("b6");
    	list_form.add(b6);
	    JButton b7 = new JButton("b7");
    	list_form.add(b7);
    	menu_object_add.add(list_form);
    	
    	//Ajout des listeners de bouttons de forme
    	cercle.addMouseListener(new BoutonFormeListener(this.toile, mouseToileListener));
    	triangle.addMouseListener(new BoutonFormeListener(this.toile, mouseToileListener));
    	rectangle.addMouseListener(new BoutonFormeListener(this.toile, mouseToileListener));
    	ligne.addMouseListener(new BoutonFormeListener(this.toile, mouseToileListener));
    	carre.addMouseListener(new BoutonFormeListener(this.toile, mouseToileListener));
    	
    	this.getContentPane().setLayout(new BorderLayout());
    	this.add(toile, BorderLayout.CENTER);
    	this.add(menu_object_add,BorderLayout.SOUTH);
    	this.setJMenuBar(this.menuBarEditionMode);
	}
	
	public Toile getToile() {
		return this.toile;
	}
	
	public GestionAnimation getGestionAnimation() {
		return this.gestionnaire;
	}
}
