package affichage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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

import formes.*;

import Animations.GestionAnimation;


public class Edition extends JFrame implements ActionListener {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	
	//Element global de alarmbox de configuration
	private JColorChooser StrokeChooser;
	private JColorChooser FillChooser;
	private JTextField Epaisseur;
	
	// Item du menu création d'objet
	JMenuItem mi_Cercle = new JMenuItem("Cercle");
	JMenuItem mi_Triangle = new JMenuItem("Triangle");
	JMenuItem mi_Rectangle = new JMenuItem("Rectangle");
	JMenuItem mi_Carre = new JMenuItem("Carré");
	JMenuItem mi_Ligne = new JMenuItem("Ligne");
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
    	mi_Cercle.addActionListener(this);menu_C.add(mi_Cercle);
    	mi_Triangle.addActionListener(this);menu_C.add(mi_Triangle);
    	mi_Rectangle.addActionListener(this);menu_C.add(mi_Rectangle);
    	mi_Carre.addActionListener(this);menu_C.add(mi_Carre);
    	mi_Ligne.addActionListener(this);menu_C.add(mi_Ligne);
    	mi_Etoile.addActionListener(this);menu_C.add(mi_Etoile);
    	mi_Hexagone.addActionListener(this);menu_C.add(mi_Hexagone);
    	mi_Croix.addActionListener(this);menu_C.add(mi_Croix);
    	menuBarEditionMode.add(menu_C);
    	 	
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
    	liste.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListModel<Item> model = liste.getModel();
				
				gestionnaire.dessinerToile(0.); //TODO:recup le temps courant
				
				for(int i = 0; i < model.getSize(); i++) {
					if(liste.isSelectedIndex(i)) {
						ObjetGeometrique geo = gestionnaire.getObject(model.getElementAt(i).getId(), 0.); //TODO: recup le temps courant
						toile.dessineSelectionOf(geo);
						System.out.println(geo.getStroke().getLineWidth());
					}
				}
			}
		});
    	
    	liste.addMouseListener(new MouseAdapter() {
    		
    		public void mouseClicked(MouseEvent e) {
    			//On est interessé par le double clic sur la liste
    			if(e.getClickCount() == 2) {
    				int index = liste.locationToIndex(e.getPoint());
    				ListModel<Item> lm = liste.getModel();
    				Item item = lm.getElementAt(index);
    				ObjetGeometrique geo = gestionnaire.getObject(item.getId(), 0.); //TODO: recup le temps courant
    				String s;
    				if(geo instanceof Cercle) {
    					s = "Cercle";
    				}
    				else if(geo instanceof Triangle) {
    					s = "Triangle";
    				}
    				else if(geo instanceof Carre) {
    					s = "Carre";
    				}
    				else if(geo instanceof Rectangle) {
    					s = "Rectangle";
    				}
    				else { //Segment de droite
    					s = "Ligne";
    				}
    				alarm_configuration_objet(s,geo,false);
    				
    				System.out.println("Double clic sur "+item+ "id : "+ item.getId());
    			}
    		}
    		
		});
    	
    	JPanel east = new JPanel();
    	BorderLayout grid = new BorderLayout();
    	east.setLayout(grid);
    	east.setBackground(Color.lightGray);
    	JLabel titre = new JLabel("Liste d'objets");
    	titre.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
    	liste.setBackground(Color.lightGray);
    	east.add(titre, BorderLayout.NORTH);
    	east.add(liste, BorderLayout.CENTER);
    	this.add(east, BorderLayout.EAST);
    	
    	//Visionneuse
    	VisionneuseAnimation va = new VisionneuseAnimation(this, this.getGestionAnimation(), 2000);
    	va.dessineAnimation();
    	this.add(va, BorderLayout.SOUTH);
    	
    	// le champ quitter du menu ferme tout
    	mi_exit.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			System.exit(0);
    		}
	    });
    	
    	
    	this.listeObjets();
    	this.initColorchooser();
    	this.Epaisseur = new JTextField();
	}
	
	/**
	 * listeObjets va mettre a jour la JList contenant les objets geometriques
	 */
	public void listeObjets() {
		HashMap<Integer, ObjetGeometrique> map = this.gestionnaire.getAllObjects();
		DefaultListModel<Item> lm = new DefaultListModel<Item>();
		for(Entry<Integer, ObjetGeometrique> entry : map.entrySet()) {
			lm.addElement(new Item(entry.getKey(), entry.getValue().getNom()));
		}
		liste.setModel(lm);
	}
	
	public JList<Item> getListe() {
		return this.liste;
	}
	
	public Toile getToile() {
		return this.toile;
	}
	
	public GestionAnimation getGestionAnimation() {
		return this.gestionnaire;
	}
	
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		if(source == this.mi_Cercle.getText()){
			alarm_configuration_objet("Cercle", null, true);
		}
		else if(source == this.mi_Triangle.getText()){
			alarm_configuration_objet("Triangle", null, true);
		}
		else if(source == this.mi_Carre.getText()){
			alarm_configuration_objet("Carre", null, true);
		}
		else if(source == this.mi_Rectangle.getText()){
			alarm_configuration_objet("Rectangle", null, true);
		}
		else if(source == this.mi_Ligne.getText()){
			alarm_configuration_objet("Ligne", null, true);
		}
		else if(source == this.mi_Etoile.getText()){
			alarm_configuration_objet("Etoile", null, true);
		}
		else if(source == this.mi_Hexagone.getText()){
			alarm_configuration_objet("Hexagone", null, true);
		}
		else if(source == this.mi_Croix.getText()){
			alarm_configuration_objet("Croix", null, true);
		}
	}
	
	public void initColorchooser(){
		StrokeChooser = new JColorChooser();
		FillChooser = new JColorChooser();
		JTextField Epaisseur = new JTextField();
		Epaisseur.setText("1");
		
		//changer l'apercu (qui est moche) :
	    StrokeChooser.setPreviewPanel(new MyPreviewPanel(StrokeChooser));
	    FillChooser.setPreviewPanel(new MyPreviewPanel(FillChooser));
		
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
	}
	
	public JPanel affiche_Colorchooser(boolean isfill){
		JPanel panel_Colorchoosers = new JPanel(new BorderLayout());
		JPanel panel_stroke = new JPanel(new BorderLayout());
		JPanel panel_fill = new JPanel(new BorderLayout());
		JLabel stroke = new JLabel("Couleur de trait :");
		JLabel fill = new JLabel("Couleur de fond :");
		panel_stroke.add(stroke, BorderLayout.NORTH);
		panel_stroke.add(StrokeChooser, BorderLayout.CENTER);
		panel_fill.add(fill, BorderLayout.NORTH);
		panel_fill.add(FillChooser, BorderLayout.CENTER);
		panel_Colorchoosers.add(panel_stroke, BorderLayout.WEST);
		
		
		if (isfill)
			panel_Colorchoosers.add(panel_fill, BorderLayout.EAST);
		return panel_Colorchoosers;
	}
	
	public boolean alarmbox_action(ObjetGeometrique o,JPanel champ_configuration, String status, boolean isFill){
		Object[] messageCercle = {
			    "", champ_configuration,
			    "Choix des couleur", this.affiche_Colorchooser(isFill)
			};
			
			int optionCercle = JOptionPane.showConfirmDialog(null, messageCercle, status, JOptionPane.OK_CANCEL_OPTION);
			if (optionCercle == JOptionPane.OK_OPTION) {
				try {
					o.setStrokeColor(StrokeChooser.getColor());
					o.setFillColor(FillChooser.getColor());
					o.setStrokeWidth(Float.parseFloat(Epaisseur.getText()));
					this.toile.setObjTemporaire(o);
				}
				catch(Exception exception) {
					System.out.println("Pas un entier");
				}
				return true;
			} 
			else {
			    System.out.println("Annulation");
			    return false;
			}
		
	}
	
	public JPanel configure_forme(JTextField t, String nom_champ, String defauld_value){
		JPanel config_global = new JPanel(new BorderLayout());
		t.setText(defauld_value);
		JLabel label_forme = new JLabel(nom_champ);
		config_global.add(label_forme, BorderLayout.NORTH);
		config_global.add(t, BorderLayout.CENTER);
		
		return config_global;
	}
	
	public void alarm_configuration_objet(String type, ObjetGeometrique o, boolean isCreation){
		JPanel config_forme = new JPanel(new BorderLayout());
		config_forme.add(this.configure_forme(this.Epaisseur, "Choix de la taille du trait :", "1"), BorderLayout.SOUTH);
		String default_value = "10";
		boolean reponse_alarmbox = isCreation;
		if (!isCreation){
			StrokeChooser.setColor(o.getStrokeColor());
			FillChooser.setColor(o.getFillColor());
		}
		
		
		switch(type) {
		case "Cercle":
			
			JTextField Rayon = new JTextField();
			double r = Double.parseDouble(default_value);
			Cercle c;
			if(isCreation){
				c = new Cercle(new Point2D.Double(0,0), r);
			}
			else {
				c = (Cercle) o;
				default_value = ""+c.getRayon();
				Epaisseur.setText(""+c.getStroke().getLineWidth());
			}
			config_forme.add(this.configure_forme(Rayon, "Rayon :", default_value), BorderLayout.CENTER);
			reponse_alarmbox = this.alarmbox_action(c, config_forme, "Création de cercle", true);
			if (reponse_alarmbox){
				r = Double.parseDouble(Rayon.getText());
				c.setRayon(r);
			}
			break;
			
		case "Rectangle":
			JTextField LargeurRectangle = new JTextField();
			JTextField HauteurRectangle = new JTextField();
			double l = Double.parseDouble(default_value);
			double h = Double.parseDouble(default_value);
			Rectangle rect = new Rectangle("Rectangle", new Point2D.Double(0,0), l, h);
			
			if(isCreation){
				rect = new Rectangle("Rectangle", new Point2D.Double(0,0), l, h);
			}
			else {
				rect = (Rectangle) o;
				l = rect.getWidth();
				h = rect.getHeight();
				Epaisseur.setText(""+rect.getStroke().getLineWidth());
			}
			
			JPanel config_rectangle = new JPanel(new BorderLayout());
			config_rectangle.add(this.configure_forme(LargeurRectangle, "Largeur :", ""+l), BorderLayout.NORTH);
			config_rectangle.add(this.configure_forme(HauteurRectangle, "Hauteur :", ""+h), BorderLayout.CENTER);
			
			config_forme.add(config_rectangle, BorderLayout.CENTER);
			reponse_alarmbox = this.alarmbox_action(rect, config_forme, "Création de Rectangle", true);
			if (reponse_alarmbox) {
				l = Double.parseDouble(LargeurRectangle.getText());
				h = Double.parseDouble(HauteurRectangle.getText());
				rect.setWidth(l);
				rect.setHeight(h);
			}
			break;
			
		case "Carre":
			
			JTextField cote_carre = new JTextField();
			double cc = Double.parseDouble(default_value);
			Carre carre;
			if(isCreation){
				carre = new Carre(new Point2D.Double(0,0), cc);
			}
			else {
				carre = (Carre) o;
				default_value = ""+carre.getcote();
				Epaisseur.setText(""+carre.getStroke().getLineWidth());
			}
			config_forme.add(this.configure_forme(cote_carre, "Coté :", default_value), BorderLayout.CENTER);
			reponse_alarmbox = this.alarmbox_action(carre, config_forme, "Création du Carré", true);
			if (reponse_alarmbox){
				cc = Double.parseDouble(cote_carre.getText());
				carre.setcote(cc);
			}
			
			break;
			
		case "Triangle":
			JTextField CoteTriangle = new JTextField();
			double ct = Double.parseDouble(default_value);
			Triangle triangle;
			if(isCreation){
				triangle = new Triangle(new Point2D.Double(0, 0), (int) ct);
			}
			else {
				triangle = (Triangle) o;
				default_value = ""+ triangle.getTaille();
				Epaisseur.setText(""+triangle.getStroke().getLineWidth());
			}
			
			config_forme.add(this.configure_forme(CoteTriangle, "Coté :", default_value), BorderLayout.CENTER);
			reponse_alarmbox = this.alarmbox_action(triangle, config_forme, "Création du Triangle", true);
			if (reponse_alarmbox){
				ct = Double.parseDouble(CoteTriangle.getText());
				triangle.setTaille(ct);
			}
			
			break;
			
		case "Ligne":
			SegmentDroite seg;
			if(isCreation){
				seg = new SegmentDroite(new Point2D.Double(0,0), new Point2D.Double(0,0));
			}
			else{
				seg = (SegmentDroite) o;
			}
			reponse_alarmbox = this.alarmbox_action(seg, config_forme, "Création de ligne", false);
			break;
			
		case "Etoile":
			
			JTextField Taille = new JTextField();
			double t = Double.parseDouble(default_value);
			Etoile e;
			if(isCreation){
				e = new Etoile(new Point2D.Double(0,0),(int) t);
			}
			else {
				e = (Etoile) o;
				default_value = ""+e.getTaille();
				Epaisseur.setText(""+e.getStroke().getLineWidth());
			}
			config_forme.add(this.configure_forme(Taille, "Taille :", default_value), BorderLayout.CENTER);
			reponse_alarmbox = this.alarmbox_action(e, config_forme, "Création de Etoile", true);
			if (reponse_alarmbox){
				t = Double.parseDouble(Taille.getText());
				e.setTaille(t);
			}
			break;
			
		case "Hexagone":
			
			JTextField Rayon_h = new JTextField();
			double r_h = Double.parseDouble(default_value);
			Hexagone hexa;
			if(isCreation){
				hexa = new Hexagone(new Point2D.Double(0,0),(int) r_h);
			}
			else {
				hexa = (Hexagone) o;
				default_value = ""+hexa.getTaille();
				Epaisseur.setText(""+hexa.getStroke().getLineWidth());
			}
			config_forme.add(this.configure_forme(Rayon_h, "Rayon :", default_value), BorderLayout.CENTER);
			reponse_alarmbox = this.alarmbox_action(hexa, config_forme, "Création de Hexagone", true);
			if (reponse_alarmbox){
				r_h = Double.parseDouble(Rayon_h.getText());
				hexa.setTaille(r_h);
			}
			break;
			
		case "Croix":
			
			JTextField Rayon_c = new JTextField();
			double r_c = Double.parseDouble(default_value);
			Croix croix;
			if(isCreation){
				croix = new Croix(new Point2D.Double(0,0),(int) r_c);
			}
			else {
				croix = (Croix) o;
				default_value = ""+croix.getTaille();
				Epaisseur.setText(""+croix.getStroke().getLineWidth());
			}
			config_forme.add(this.configure_forme(Rayon_c, "Rayon :", default_value), BorderLayout.CENTER);
			reponse_alarmbox = this.alarmbox_action(croix, config_forme, "Création de Croix", true);
			if (reponse_alarmbox){
				r_c = Double.parseDouble(Rayon_c.getText());
				croix.setTaille(r_c);
			}
			break;
		}
		if(reponse_alarmbox && isCreation){
			//Ajouter un mouse movement listener a la toile avec un cercle a dessiner
			this.toile.modeListener();
		}
	}
}


//code trouvé sur ce site : http://exampledepot.8waytrips.com/egs/javax.swing.colorchooser/CustPreview.html
//This preview panel simply displays the currently selected color.
class MyPreviewPanel extends JComponent {
 // The currently selected color
 Color curColor;

 public MyPreviewPanel(JColorChooser chooser) {
     // Initialize the currently selected color
     curColor = chooser.getColor();

     // Add listener on model to detect changes to selected color
     ColorSelectionModel model = chooser.getSelectionModel();
     model.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent evt) {
             ColorSelectionModel model = (ColorSelectionModel)evt.getSource();

             // Get the new color value
             curColor = model.getSelectedColor();
         }
     }) ;

     // Set a preferred size
     setPreferredSize(new Dimension(50, 50));
 }

 // Paint current color
 public void paint(Graphics g) {
     g.setColor(curColor);
     g.fillRect(0, 0, getWidth()-1, getHeight()-1);
 }
}
