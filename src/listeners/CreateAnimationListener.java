package listeners;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import formes.ObjetGeometrique;
import formes.SegmentDroite;

import Animations.Animation;
import Animations.Comportement;
import Animations.CompositeAnimation;
import Animations.FillColor;
import Animations.Rotation;
import Animations.StrokeColor;
import Animations.StrokeWidth;
import affichage.Item;
import affichage.MyColorChooser;
import affichage.VisionneuseAnimation;

/**
 * Listener de creation d'animations
 * @author Alain
 *
 */
public class CreateAnimationListener implements ActionListener {

	JList<Item> liste;
	VisionneuseAnimation visionneuse;

	JComboBox<String> comboBoxAnimations;
	
	
	//Champs
	JPanel champsDynamiques;
	JTextField angle;
	JTextField epaisseur;
	JTextField t_debut;
	JTextField t_fin;
	MyColorChooser colorChooser;
	
	JOptionPane optionPane;
	
	public CreateAnimationListener(JList<Item> liste, VisionneuseAnimation visionneuse) {
		this.liste = liste;
		this.comboBoxAnimations = null;
		this.visionneuse = visionneuse;
		this.colorChooser = new MyColorChooser();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String commande = e.getActionCommand(); //Dans le doute de si on a plusieurs commandes
		
		switch (commande) {
		case "creation":
			//On va recuperer une liste des comportements selectionnés dans la JList
			Comportement comp = (Comportement) liste.getSelectedValue().getValeur(); //Pour l'instant je prends qu'un seul
			
			//On va initialiser la combobox
			this.initComboBox(comp);
			
			
			//On affiche maintenant notre OptionBox
			Object[] message = {"Animation :", this.comboBoxAnimations};
			
			int reponseSelection = JOptionPane.showConfirmDialog(null, message, "Création ["+comp.toString()+"]", JOptionPane.OK_CANCEL_OPTION);
			if(reponseSelection == JOptionPane.OK_OPTION) {
				String selection = this.comboBoxAnimations.getSelectedItem().toString();
				int reponseAnim = this.afficheAlertBoxOf(selection);
				if(reponseAnim == JOptionPane.OK_OPTION) {
					this.creerAnimation(comp, selection);
				}
			}
			break;
		case "modification": //Pas sur mais je prefere le prevoir
			
			break;
		}
	}
	
	private int afficheAlertBoxOf(String selection) {
		Object[] message = {};
		if(selection.equals("Rotation")) {
			this.angle = new JTextField("0");
			Object[] m = {"Angle", angle};
			message = m;
		}
		else if (selection.equals("Translation")) {
			Object[] m = {"Veuillez dessiner le chemin"};
			message = m;
		}
		else if (selection.equals("Epaisseur de trait")) {
			this.epaisseur = new JTextField("0");
			Object[] m = {"Nouvelle epaisseur de trait",epaisseur};
			message = m;
		}
		else if (selection.equals("Couleur de trait")) {
			Object[] m = {"Nouvelle couleur de trait", this.colorChooser};
			message = m;
		}
		else if (selection.equals("Couleur de fond")) {
			Object[] m = {"Nouvelle conleur de fond", this.colorChooser};
		}
		
		Object[] messageFinal = new Object[message.length+4];
		for(int i = 0; i < message.length; i++) {
			messageFinal[i] = message[i];
		}
		
		//Temps debut et fin
		this.t_debut = new JTextField("0");
		this.t_fin = new JTextField("0");
		
		messageFinal[messageFinal.length-4] = "Temps debut";
		messageFinal[messageFinal.length-3] = this.t_debut;
		messageFinal[messageFinal.length-2] = "Temps fin";
		messageFinal[messageFinal.length-1] = this.t_fin;
		return JOptionPane.showConfirmDialog(null, messageFinal, "Paramètres "+selection, JOptionPane.OK_CANCEL_OPTION);
	}


	/**
	 * Va initialiser une JComboBox avec toutes les animations possibles que 
	 * l'utilisateur pourra creer
	 * @param un Comportement pour lequel on devra proposer des animations
	 */
	private void initComboBox(Comportement comp) {
		//On crée un vector qui servira a initialiser le JComboBox
		Vector<String> animations = new Vector<String>();
		
		//Ajout de toutes les animations dans le vecteur
		animations.add("Rotation");
		animations.add("Translation");
		animations.add("Epaisseur de trait");
		animations.add("Couleur de trait");
		//Cas particulier, les segment n'ont pas de couleur de fond
		if(!(comp.getObjGeo() instanceof SegmentDroite)) {
			animations.add("Couleur de fond");
		}
		
		comboBoxAnimations = new JComboBox<String>(animations);
	}
	
	/**
	 * Va regarder parmis tous les Comportement pour creer une JComboBox adaptée aux 
	 * animations disponibles pour l'utilisateur avec les Comportements selectionnés
	 * @param Une liste de comportements pour lesquels on devra proposer des animations
	 * @return une JComboBox contenant toutes les animations possibles
	 */
	private void initComboBox(List<Comportement> lComp) {
		//A implementer plus tard
	}
	
	//TODO: peut etre le rendre public ou non pour la translation (=non si on peut envoyer un action command depuis le clavier)
	private void creerAnimation(Comportement comp, String selection) {
		Animation anim = null;
		double tempsDebut = Double.parseDouble(this.t_debut.getText());
		double tempsFin = Double.parseDouble(this.t_fin.getText());
		
		//On ne va créer que des animations valides (avec un temps de fin apres le temps debut)
		if(tempsFin > tempsDebut) {
			//On recupere l'objet geometrique
			ObjetGeometrique obj = comp.getEtatObjGeo(0.); //TODO: recup le temps courant de l'edition
			
			switch(selection) {
			case "Rotation":
				double angleRad = Math.toRadians(Double.parseDouble(this.angle.getText()));
				anim = new Rotation(tempsDebut,tempsFin, 0, angleRad, obj.getCentre());
				break;
			case "Translation":
				//TODO: je sais pas comment le faire
				break;
			case "Epaisseur de trait":
				//On va calculer la difference entre l'epaisseur demandée et l'epaisseur de l'objet
				float epaisseurObj = obj.getStroke().getLineWidth();
				float epaisseurDemande = Float.parseFloat(this.epaisseur.getText());
				anim = new StrokeWidth(tempsDebut, tempsFin, 0, epaisseurDemande - epaisseurObj);
				break;
			case "Couleur de trait":
				//On va calculer la difference entre la couleur demandée et la couleur de l'objet
				Color cObjStroke = obj.getStrokeColor();
				Color cDemandeStroke = this.colorChooser.getColor();
				anim = new StrokeColor(tempsDebut, tempsFin, 0, 
							cDemandeStroke.getRed() - cObjStroke.getRed(), 
							cDemandeStroke.getGreen() - cObjStroke.getGreen(),
							cDemandeStroke.getBlue() - cObjStroke.getBlue());
				break;
			case "Couleur de fond":
				//On va calculer la difference entre la couleur demandée et la couleur de l'objet
				Color cObjFill = obj.getFillColor();
				Color cDemandeFill = this.colorChooser.getColor();
				anim = new FillColor(tempsDebut, tempsFin, 0,
							cDemandeFill.getRed() - cObjFill.getRed(), 
							cDemandeFill.getGreen() - cObjFill.getGreen(),
							cDemandeFill.getBlue() - cObjFill.getBlue());
				break;
			}
			
			//On ajoute maintenant l'animation au comportement
			CompositeAnimation ca = (CompositeAnimation) comp.getAnimation();
			ca.add(anim);
			
			//Et on met a jour le dessin de notre visionneuse d'animation
			this.visionneuse.dessineAnimation();
		}
	}
	
	/*
	private void createChamps() {
		this.champsDynamiques = new JPanel();
		this.champsDynamiques.setLayout(new FlowLayout());
		this.angle = new JTextField("0");
		this.strokeWidth = new JTextField("0");
	}
	
	/**
	 * Va creer et initialiser l'optionPane afin d'afficher une option box 
	 * pour que l'utilisateur puisse faire son choix d'animation et valider
	 * /!\ Cette methode doit être appelée apres avoir crée la JcomboBox des animations
	 */
	/*
	private void createOptionPane() {
		this.optionPane = new JOptionPane(this.comboBoxAnimations, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
	}
	*/
}
