package affichage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import formes.Carre;
import formes.Cercle;
import formes.Rectangle;
import formes.Triangle;

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
	private MouseMotionToileListener mouseToileListener;
	
	public Edition() {
		super("Edition");
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Creation de la toile
		this.toile = new Toile(new Dimension(Edition.widthToile, Edition.heightToile), this);
		this.toile.setBackground(Color.white);
		this.toile.setOpaque(true);
		
		//Listeners de la toile
		//mouseToileListener = new MouseMotionToileListener(this);
		//MouseToileListener mouseClickToileListener = new MouseToileListener(this, mouseToileListener);
		//this.toile.addMouseListener(mouseClickToileListener);
		
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
    	JMenu menu_C = new JMenu("Création d'objet");
    	JMenuItem mi_Cercle = new JMenuItem("Cercle");
    	JMenuItem mi_Triangle = new JMenuItem("Triangle");
    	JMenuItem mi_Rectangle = new JMenuItem("Rectangle");
    	JMenuItem mi_Carre = new JMenuItem("Carré");
    	JMenuItem mi_Ligne = new JMenuItem("Ligne");
    	menu_C.add(mi_Cercle);
    	menu_C.add(mi_Triangle);
    	menu_C.add(mi_Rectangle);
    	menu_C.add(mi_Carre);
    	menu_C.add(mi_Ligne);
    	menuBarEditionMode.add(menu_C);
    	 	
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
    	
    	//Ajout des listeners de bouttons de forme
    	mi_Cercle.addMouseListener(new BoutonFormeListener(this.toile, mouseToileListener));
    	mi_Triangle.addMouseListener(new BoutonFormeListener(this.toile, mouseToileListener));
    	mi_Rectangle.addMouseListener(new BoutonFormeListener(this.toile, mouseToileListener));
    	mi_Ligne.addMouseListener(new BoutonFormeListener(this.toile, mouseToileListener));
    	mi_Carre.addMouseListener(new BoutonFormeListener(this.toile, mouseToileListener));
    	
    	this.getContentPane().setLayout(new BorderLayout());
    	this.add(toile, BorderLayout.CENTER);
    	this.add(menu_object_add,BorderLayout.SOUTH);
    	this.setJMenuBar(this.menuBarEditionMode);
    	
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
    	
    	// le champ quitter du menu ferme tout
    	mi_Cercle.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			alarm_configuration_objet("Cercle");
    		}
	    });
    	
    	// le champ quitter du menu ferme tout
    	mi_Triangle.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			alarm_configuration_objet("Triangle");
    		}
	    });
    	
    	// le champ quitter du menu ferme tout
    	mi_Carre.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			alarm_configuration_objet("Carre");
    		}
	    });
    	
    	// le champ quitter du menu ferme tout
    	mi_Rectangle.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			alarm_configuration_objet("Rectangle");
    		}
	    });
    	
    	// le champ quitter du menu ferme tout
    	mi_Ligne.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			alarm_configuration_objet("Ligne");
    		}
	    });
    	
	}
	
	public Toile getToile() {
		return this.toile;
	}
	
	public GestionAnimation getGestionAnimation() {
		return this.gestionnaire;
	}
	
	public void alarm_configuration_objet(String type){
		//JButton b = (JButton) e.getSource();
		//System.out.println(b.getText());
		JColorChooser StrokeChooser = new JColorChooser();
		JColorChooser FillChooser = new JColorChooser();
		JTextField Epaisseur = new JTextField();
		Epaisseur.setText("1");
		
		for(final AbstractColorChooserPanel accp : StrokeChooser.getChooserPanels()) {
			if(!accp.getDisplayName().equals("RVB")) {
				StrokeChooser.removeChooserPanel(accp);
			}
		}
		
		for(final AbstractColorChooserPanel accp : FillChooser.getChooserPanels()) {
			if(!accp.getDisplayName().equals("RVB")) {
				FillChooser.removeChooserPanel(accp);
			}
		}
		
		switch(type) {
		
		case "Cercle":
			JTextField Rayon = new JTextField();
			Object[] messageCercle = {
			    "Rayon:", Rayon,
			    "Couleur de trait", StrokeChooser,
			    "Couleur de fond", FillChooser,
			    "Epaisseur de trai", Epaisseur
			};
			
			int optionCercle = JOptionPane.showConfirmDialog(null, messageCercle, "Création de cercle", JOptionPane.OK_CANCEL_OPTION);
			if (optionCercle == JOptionPane.OK_OPTION) {
				try {
					double r = Double.parseDouble(Rayon.getText());
					System.out.println(r);
					Cercle c = new Cercle(new Point2D.Double(0,0), r);
					c.setStrokeColor(StrokeChooser.getColor());
					c.setFillColor(FillChooser.getColor());
					c.setStrokeWidth(Float.parseFloat(Epaisseur.getText()));
					this.toile.setObjTemporaire(c);
				}
				catch(Exception exception) {
					System.out.println("Pas un entier");
				}
				//Ajouter un mouse movement listener a la toile avec un cercle a dessiner
				this.toile.modeListener();
				//this.toile.addMouseMotionListener(motion);
			} 
			else {
			    System.out.println("Annulation");
			}
			
			break;
			
		case "Rectangle":
			JTextField LargeurRectangle = new JTextField();
			JTextField HauteurRectangle = new JTextField();
			Object[] messageRectangle = {
				"Largeur:", LargeurRectangle,
				"Hauteur:", HauteurRectangle,
				"Couleur de trait", StrokeChooser,
			    "Couleur de fond", FillChooser,
			    "Epaisseur de trai", Epaisseur
			};
			
			int optionRectangle = JOptionPane.showConfirmDialog(null, messageRectangle, "Login", JOptionPane.OK_CANCEL_OPTION);
			if (optionRectangle == JOptionPane.OK_OPTION) {
				try {
					double l = Double.parseDouble(LargeurRectangle.getText());
					double h = Double.parseDouble(HauteurRectangle.getText());
					Rectangle rect = new Rectangle("Rectangle", new Point2D.Double(0,0), l, h);
					rect.setStrokeColor(StrokeChooser.getColor());
					rect.setFillColor(FillChooser.getColor());
					rect.setStrokeWidth(Float.parseFloat(Epaisseur.getText()));
					this.toile.setObjTemporaire(rect);
				}
				catch(Exception exception) {
					System.out.println("Pas un entier");
				}
				//Ajouter un mouse movement listener a la toile avec un rectangle a dessiner
				this.toile.modeListener();
				//this.toile.addMouseMotionListener(motion);
			} 
			else {
			    System.out.println("Annulation");
			}
			
			break;
			
		case "Carré":
			JTextField CoteCarre = new JTextField();
			Object[] messageCarre = {
				"Coté:", CoteCarre,
				"Couleur de trait", StrokeChooser,
			    "Couleur de fond", FillChooser,
			    "Epaisseur de trai", Epaisseur
			};
			
			int optionCarre = JOptionPane.showConfirmDialog(null, messageCarre, "Login", JOptionPane.OK_CANCEL_OPTION);
			if (optionCarre == JOptionPane.OK_OPTION) {
				try {
					double cote = Double.parseDouble(CoteCarre.getText());
					Carre carre = new Carre(new Point2D.Double(0,0),cote);
					carre.setStrokeColor(StrokeChooser.getColor());
					carre.setFillColor(FillChooser.getColor());
					carre.setStrokeWidth(Float.parseFloat(Epaisseur.getText()));
					this.toile.setObjTemporaire(carre);
				}
				catch(Exception exception) {
					System.out.println("Pas un entier");
				}
				//Ajouter un mouse movement listener a la toile avec un carré a dessiner
				this.toile.modeListener();
				//this.toile.addMouseMotionListener(motion);
			} 
			else {
			    System.out.println("Annulation");
			}
			
			break;
			
		case "Triangle":
			JTextField CoteTriangle = new JTextField();
			Object[] messageTriangle = {
				"Coté:", CoteTriangle,
				"Couleur de trait", StrokeChooser,
			    "Couleur de fond", FillChooser,
			    "Epaisseur de trai", Epaisseur
			};
			
			int optionTriangle = JOptionPane.showConfirmDialog(null, messageTriangle, "Login", JOptionPane.OK_CANCEL_OPTION);
			if (optionTriangle == JOptionPane.OK_OPTION) {
				try {
					double cote = Double.parseDouble(CoteTriangle.getText());
					Triangle triangle = new Triangle(new Point2D.Double(0, 0), (int) cote);
					triangle.setStrokeColor(StrokeChooser.getColor());
					triangle.setFillColor(FillChooser.getColor());
					triangle.setStrokeWidth(Float.parseFloat(Epaisseur.getText()));
					this.toile.setObjTemporaire(triangle);
				}
				catch(Exception exception) {
					System.out.println("Pas un entier");
				}
				//Ajouter un mouse movement listener a la toile avec un triangle a dessiner
				this.toile.modeListener();
				//this.toile.addMouseMotionListener(motion);
			} 
			else {
			    System.out.println("Annulation");
			}
			break;
			
		case "Ligne":
			
			break;
		}
	}
}
