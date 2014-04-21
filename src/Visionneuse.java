import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import affichage.Toile;
import formes.*;


public class Visionneuse implements Runnable {
	
	private Toile toile;
	private ObjetGeometrique selection;
	private JMenuBar menuBar;
	private JToolBar menu_object_add;
	private JToolBar menu_manage_ani;
	private JToolBar menu_manage_object;
	
	private JColorChooser choicecolor;
	private JDialog dialog;
	
    public void run() {
    	JFrame frame = new JFrame("Visionneuse");
    	
    	// on crée notre toile
		this.toile = new Toile(new Dimension(300,300));
		this.toile.setBackground(Color.red);
		
		// on crée un menuBar 
		menuBar = new JMenuBar();
		
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
    	menuBar.add(menu_F);
    	
    	// menu création d'objet
    	JMenu menu_C = new JMenu("Création d'objet");
		JMenuItem mi_Cercle = new JMenuItem("Cercle");
		JMenuItem mi_Triangle = new JMenuItem("Triangle");
		JMenuItem mi_Rectangle = new JMenuItem("Rectangle");
    	JMenuItem mi_Ligne = new JMenuItem("Ligne");
    	menu_C.add(mi_Cercle);
    	menu_C.add(mi_Triangle);
    	menu_C.add(mi_Rectangle);
    	menu_C.add(mi_Ligne);
    	menuBar.add(menu_C);
    	
    	//Les menu pouvant se déplacer
    	Container c = frame.getContentPane();
    	c.setLayout(new BorderLayout());
    	
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
	    // Bouton objectGeometrique Cercle
    	JButton triangle = new JButton("Triangle");
    	triangle.setToolTipText("Aide pour "+triangle.getText());
    	list_form.add(triangle);
	    // Bouton objectGeometrique Cercle
    	JButton rectangle = new JButton("Rectangle");
    	rectangle.setToolTipText("Aide pour "+rectangle.getText());
    	list_form.add(rectangle);
	    JButton ligne = new JButton("Ligne");
    	ligne.setToolTipText("Aide pour "+ligne.getText());
    	list_form.add(ligne);
    	// Bouton objectGeometrique Cercle
    	JButton b4 = new JButton("b4");
	    list_form.add(b4);
	    // Bouton objectGeometrique Cercle
    	JButton b5= new JButton("b5");
    	list_form.add(b5);
	    // Bouton objectGeometrique Cercle
    	JButton b6 = new JButton("b6");
    	list_form.add(b6);
	    JButton b7 = new JButton("b7");
    	list_form.add(b7);
    	menu_object_add.add(list_form);
	    
	    // menu information ObjetGeometrique selection
	    menu_manage_object = new JToolBar(SwingConstants.HORIZONTAL);
	    menu_manage_object.setFloatable(true);
	    menu_manage_object.setRollover(true);
	    menu_manage_object.setName("Manage Object");
    	menu_manage_object.setVisible(false);
    	JButton l = new JButton("Ligne");
    	ligne.setToolTipText("Aide pour "+ligne.getText());
    	menu_manage_object.add(l);
	    
	    c.add(menu_object_add,BorderLayout.SOUTH);
	    c.add(menu_manage_object,BorderLayout.NORTH);
	    c.add(toile,BorderLayout.CENTER);

    	mi_new.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			//TODO effectuer une recharge de la Toile
    		}
    	});
    	
    	mi_refresh.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			//TODO effectuer une recharge de la Toile
    		}
    	});
    	
    	mi_add_object.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if (menu_object_add.isVisible()){
    				menu_object_add.setVisible(false);
    			}
    			else {
    				menu_object_add.setVisible(true);
    			}
    		}
    	});
    	
    	// le champ quitter du menu ferme tout
    	mi_exit.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			System.exit(0);
    		}
	    });
    	
    	cercle.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			//TODO choisir Structure Cercle et Menu Création Spécification de l'objet
    		}
    	});
    	
    	triangle.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			//TODO choisir Structure triangle et Menu Création Spécification de l'objet
    		}
    	});
    	
    	rectangle.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			//TODO choisir Structure rectangle et Menu Création Spécification de l'objet
    		}
    	});
    	
    	ligne.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			//TODO choisir Structure rectangle et Menu Création Spécification de l'objet
    		}
    	});
    	
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(this.menuBar);
		frame.pack();
		frame.setVisible(true);
    }
}
