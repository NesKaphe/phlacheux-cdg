package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import formes.SegmentDroite;

import Animations.Comportement;
import Animations.CompositeAnimation;
import affichage.Item;

/**
 * Listener de creation d'animations
 * @author Alain
 *
 */
public class CreateAnimationListener implements ActionListener{

	JList<Item> liste;

	JComboBox<String> comboBoxAnimations;
	
	//Frame pour l'alertBox
	JFrame alertFrame;
	
	//Champs
	JPanel champsDynamiques;
	JTextField angle;
	JTextField epaisseur;
	
	JOptionPane optionPane;
	
	public CreateAnimationListener(JList<Item> liste) {
		this.liste = liste;
		this.comboBoxAnimations = null;
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
			
			int reponseSelection = JOptionPane.showConfirmDialog(null, message, "Création d'animation", JOptionPane.OK_CANCEL_OPTION);
			if(reponseSelection == JOptionPane.OK_OPTION) {
				String selection = this.comboBoxAnimations.getSelectedItem().toString();
				int reponseAnim = this.afficheAlertBoxOf(selection);
				
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
			//A faire color chooser
		}
		else if (selection.equals("Couleur de fond")) {
			//A faire color chooser
		}
		
		Object[] messageFinal = new Object[message.length+4];
		for(int i = 0; i < message.length; i++) {
			messageFinal[i] = message[i];
		}
		
		messageFinal[messageFinal.length-4] = "Temps debut";
		messageFinal[messageFinal.length-3] = new JTextField("0");
		messageFinal[messageFinal.length-2] = "Temps fin";
		messageFinal[messageFinal.length-1] = new JTextField("0");
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
	
	private void creerAnimation(Comportement comp, String selection) {
		
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
