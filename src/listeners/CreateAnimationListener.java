package listeners;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
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
import affichage.CreateTrajectoire;
import affichage.Edition;
import affichage.Item;
import affichage.ModifAnimBox;
import affichage.MyColorChooser;
import affichage.VisionneuseAnimation;

/**
 * Listener de creation d'animations
 * @author Alain
 *
 */
public class CreateAnimationListener implements ActionListener {

	private JList<Item> liste;
	private VisionneuseAnimation visionneuse;
	private JComboBox<String> comboBoxAnimations;
	private JComboBox<String> comboBoxEasing;
	private Edition edition;
	private Comportement comp;
	
	private JTextField angle;
	private JTextField epaisseur;
	private JTextField t_debut;
	private JTextField t_fin;
	private MyColorChooser colorChooser;
	
	public CreateAnimationListener(JList<Item> liste, VisionneuseAnimation visionneuse,Edition edition) {//TODO changer le constructeur pour qu'il ne récupère que l'editions puis que toile contient liste et visioneuse
		this.liste = liste;
		this.comboBoxAnimations = null;
		this.visionneuse = visionneuse;
		this.colorChooser = new MyColorChooser();
		this.edition = edition;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String commande = e.getActionCommand(); //Dans le doute de si on a plusieurs commandes
		
		switch (commande) {
		case "creation":
			//On va recuperer une liste des comportements selectionnés dans la JList
			comp = (Comportement) liste.getSelectedValue().getValeur(); //Pour l'instant je prends qu'un seul
			
			//On va initialiser les combobox
			this.initComboBoxAnimation(comp);
			this.initComboBoxEasing();
			
			//On affiche maintenant notre OptionBox
			Object[] message = {"Animation :", this.comboBoxAnimations, "Easing :", this.comboBoxEasing};
			
			int reponseSelection = JOptionPane.showConfirmDialog(null, message, "Création ["+comp.toString()+"]", JOptionPane.OK_CANCEL_OPTION);
			if(reponseSelection == JOptionPane.OK_OPTION) {
				String selectionAnim = this.comboBoxAnimations.getSelectedItem().toString();
				String selectionEasing = this.comboBoxEasing.getSelectedItem().toString();
				int easingType = ModifAnimBox.getEasingType(selectionEasing);
				int reponseAnim = this.afficheAlertBoxOf(selectionAnim);
				if(reponseAnim == JOptionPane.OK_OPTION) {
					this.creerAnimation(comp, selectionAnim, easingType);
				}
			}
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
			message = m;
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
	private void initComboBoxAnimation(Comportement comp) {
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
	
	private void initComboBoxEasing() {
		this.comboBoxEasing = ModifAnimBox.generateComboBoxEasing();
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
	private void creerAnimation(Comportement comp, String selection, int easing) {
		Animation anim = null;
		double tempsDebut = Double.parseDouble(this.t_debut.getText());
		double tempsFin = Double.parseDouble(this.t_fin.getText());
		
		//On ne va créer que des animations valides (avec un temps de fin apres le temps debut)
		if(tempsFin > tempsDebut) {
			//On recupere l'objet geometrique
			ObjetGeometrique obj = comp.getEtatObjGeo(tempsDebut);
			
			switch(selection) {
			case "Rotation":
				double angleRad = Math.toRadians(Double.parseDouble(this.angle.getText()));
				anim = new Rotation(tempsDebut,tempsFin, easing, angleRad, obj.getCentre());
				break;
			case "Translation":
				Point p = new Point();
				p.setLocation(comp.getObjGeo().getCentre());
				new CreateTrajectoire(edition,comp,tempsDebut,tempsFin,easing);
				break;
			case "Epaisseur de trait":
				//On va calculer la difference entre l'epaisseur demandée et l'epaisseur de l'objet
				float epaisseurObj = obj.getStroke().getLineWidth();
				float epaisseurDemande = Float.parseFloat(this.epaisseur.getText());
				anim = new StrokeWidth(tempsDebut, tempsFin, easing, epaisseurDemande - epaisseurObj);
				break;
			case "Couleur de trait":
				//On va calculer la difference entre la couleur demandée et la couleur de l'objet
				Color cObjStroke = obj.getStrokeColor();
				Color cDemandeStroke = this.colorChooser.getColor();
				anim = new StrokeColor(tempsDebut, tempsFin, easing, 
							cDemandeStroke.getRed() - cObjStroke.getRed(), 
							cDemandeStroke.getGreen() - cObjStroke.getGreen(),
							cDemandeStroke.getBlue() - cObjStroke.getBlue());
				break;
			case "Couleur de fond":
				//On va calculer la difference entre la couleur demandée et la couleur de l'objet
				Color cObjFill = obj.getFillColor();
				Color cDemandeFill = this.colorChooser.getColor();
				anim = new FillColor(tempsDebut, tempsFin, easing,
							cDemandeFill.getRed() - cObjFill.getRed(), 
							cDemandeFill.getGreen() - cObjFill.getGreen(),
							cDemandeFill.getBlue() - cObjFill.getBlue());
				break;
			}
			
			//On ajoute maintenant l'animation au comportement
			if(anim !=null){
				CompositeAnimation ca = (CompositeAnimation) comp.getAnimation();
				ca.add(anim);
			}
			
			//Et on met a jour le dessin de notre visionneuse d'animation
			this.visionneuse.dessineAnimation();
		}
	}
}
