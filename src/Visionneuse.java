import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import affichage.Toile;


public class Visionneuse implements Runnable {
	
	private Toile toile;
	private JMenuBar menuBar;
	private JToolBar menu_object_add;
	private JToolBar menu_manage_ani;
	private JToolBar menu_3;
	
    public void run() {
    	JFrame frame = new JFrame("Visionneuse");
    	
    	// on crée notre toile
		this.toile = new Toile(new Dimension(300,300));
		
		// on crée un menuBar static avec juste un bouton pour quitter et recharger la page
		menuBar = new JMenuBar();
		JMenu menu = new JMenu("Fichier");
		JMenuItem mi1 = new JMenuItem("Recharger");
    	JMenuItem mi2 = new JMenuItem("Quitter");
    	menu.add(mi1);
    	menu.add(mi2);
    	menuBar.add(menu);
    	
    	//Les menu pouvant se déplacer
    	Container c = frame.getContentPane();
    	c.setLayout(new BorderLayout());
    	menu_object_add = new JToolBar(SwingConstants.HORIZONTAL);
    	menu_object_add.setFloatable(true);
    	menu_object_add.setRollover(true);
    	// Bouton objectGeometrique Cercle
    	JButton cercle = new JButton("Cercle");
	    cercle.setToolTipText("Aide pour "+cercle.getText());
	    menu_object_add.add(cercle);
	    // Bouton objectGeometrique Cercle
    	JButton triangle = new JButton("Triangle");
    	triangle.setToolTipText("Aide pour "+triangle.getText());
	    menu_object_add.add(triangle);
	    // Bouton objectGeometrique Cercle
    	JButton rectangle = new JButton("Rectangle");
    	rectangle.setToolTipText("Aide pour "+rectangle.getText());
	    menu_object_add.add(rectangle);
	    c.add(menu_object_add,BorderLayout.SOUTH);
	    c.add(toile,BorderLayout.CENTER);
	   
	    
	    

    	mi1.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			//TODO effectuer une recharge de la Toile
    		}
    	});
    	
    	// le champ quitter du menu ferme tout
    	mi2.addActionListener(new ActionListener() {
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
    	
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(this.menuBar);
		frame.pack();
		frame.setVisible(true);
    }
}
