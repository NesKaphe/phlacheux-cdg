package affichage;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Animations.*;

public class ModifAnimBox {

	private ModifAnimBox() {} //On ne construit pas
	
	public static Animation createBoxAnimation(String type) {
		return null;
	}
	
	public static Animation createBoxAndModify(Animation selected, Comportement comp, double t_courant, JFrame f) {
		Animation retour = null;
		
		Object[] message = ModifAnimBox.generateFields(selected);
		int reponse = JOptionPane.showConfirmDialog(null, message, "Modification "+selected.getType(), JOptionPane.OK_CANCEL_OPTION);
		if(reponse == JOptionPane.OK_OPTION) {
			retour = ModifAnimBox.modify(selected, comp, message, t_courant, f);
		}
		return retour;
	}
	
	
	public static JComboBox<String> generateComboBoxEasing() {
		//On crée un vector qui servira a initialiser le JComboBox
		Vector<String> easings = new Vector<String>();
		
		//Ajout de toutes les easing function dans le vecteur
		easings.add("Lineaire");
		easings.add("Smooth step");
		easings.add("Square");
		easings.add("Cube");
		easings.add("Invert");
		easings.add("Invert square");
		easings.add("Invert cube");
		easings.add("Power");
		easings.add("Power square");
		easings.add("Power cube");
		
		return new JComboBox<String>(easings);
				
	}
	
	public static int getEasingType(String selectionEasing) {
		switch(selectionEasing) {
		case "Leneaire":
			return EasingFunction.LINEAR;
		case "Smooth step":
			return EasingFunction.SMOOTHSTEP;
		case "Square":
			return EasingFunction.SQUARE;
		case "Cube":
			return EasingFunction.CUBE;
		case "Invert":
			return EasingFunction.INVERT;
		case "Invert square":
			return EasingFunction.INVERT_SQUARE;
		case "Invert cube":
			return EasingFunction.INVERT_CUBE;
		case "Power":
			return EasingFunction.POWER;
		case "Power square":
			return EasingFunction.POWER_SQUARE;
		case "Power cube":
			return EasingFunction.POWER_CUBE;
		default:
			return EasingFunction.LINEAR;
		}
	}
	
	
	private static Object[] generateFields(Animation anim) {
		MyColorChooser chooser = new MyColorChooser();
		Object[] message = {};
		if(anim.getType().equals("Rotation")) {
			Double angleDegree = Math.toDegrees(((Rotation) anim).getAngle());
			JTextField angle = new JTextField(angleDegree.toString());
			Object[] m = {"Angle", angle};
			message = m;
		}
		else if (anim.getType().equals("Translation")) {
			Object[] m = {"Veuillez dessiner le chemin"};
			message = m;
		}
		else if (anim.getType().equals("StrokeWidth")) {
			JTextField epaisseur = new JTextField(anim.getWidthStroke(anim.getT_fin()).toString());
			Object[] m = {"Nouvelle epaisseur de trait",epaisseur};
			message = m;
		}
		else if (anim.getType().equals("StrokeColor")) {
			chooser.setColor(anim.getStrokeColor(anim.getT_fin()));
			Object[] m = {"Nouvelle couleur de trait", chooser};
			message = m;
		}
		else if (anim.getType().equals("FillColor")) {
			chooser.setColor(anim.getFillColor(anim.getT_fin()));
			Object[] m = {"Nouvelle conleur de fond", chooser};
			message = m;
		}
		
		Object[] messageFinal = new Object[message.length+6];
		for(int i = 0; i < message.length; i++) {
			messageFinal[i] = message[i];
		}
		
		//Easing
		JComboBox<String> easing = ModifAnimBox.generateComboBoxEasing();
		easing.setSelectedIndex(anim.getEasing());
		
		//Temps debut et fin
		Double tempsDebut = anim.getT_debut() / LecteurAnimation.CONSTANTE_TEMPS;
		JTextField t_debut = new JTextField(tempsDebut.toString());
		Double dureeAnim = (anim.getT_fin() - anim.getT_debut()) / LecteurAnimation.CONSTANTE_TEMPS;
		JTextField t_fin = new JTextField(dureeAnim.toString());
		
		messageFinal[messageFinal.length-6] = "Easing";
		messageFinal[messageFinal.length-5] = easing;
		messageFinal[messageFinal.length-4] = "Temps debut";
		messageFinal[messageFinal.length-3] = t_debut;
		messageFinal[messageFinal.length-2] = "Durée";
		messageFinal[messageFinal.length-1] = t_fin;
				
		return messageFinal;
	}
	
	private static Animation modify(Animation selected, Comportement comp, Object[] message, double t_courant, JFrame f) {
		Animation modifie = null;
		String select = ((JComboBox<String>) message[message.length-5]).getSelectedItem().toString();
		int easingType = ModifAnimBox.getEasingType(select);
		System.out.println("C'est le easing "+ easingType+ "qui est selectionné");
		
		if(selected.getType().equals("Rotation")) {
			modifie = new Rotation((Rotation) selected);
			double angleRad = Math.toRadians(Double.parseDouble(((JTextField)message[1]).getText().toString()));
			((Rotation)modifie).setAngle(angleRad);
			modifie.setEasing(easingType);
		}
		else if (selected.getType().equals("StrokeWidth")) {
			modifie = new StrokeWidth((StrokeWidth) selected);
			
			float epaisseurObj = comp.getEtatObjGeo(selected.getT_debut()).getStroke().getLineWidth();
			float widthDemande = Float.parseFloat(((JTextField)message[1]).getText().toString());
			((StrokeWidth)modifie).setStrokeWidthIncrement(widthDemande - epaisseurObj);
			modifie.setEasing(easingType);
		}
		else if (selected.getType().equals("StrokeColor")) {
			modifie = new StrokeColor((StrokeColor) selected);
			
			Color cObjStroke = comp.getEtatObjGeo(selected.getT_debut()).getStrokeColor();
			Color cDemandeStroke = ((MyColorChooser)message[1]).getColor();
			((StrokeColor)modifie).setColor(
					cDemandeStroke.getRed() - cObjStroke.getRed(), 
					cDemandeStroke.getGreen() - cObjStroke.getGreen(),
					cDemandeStroke.getBlue() - cObjStroke.getBlue()
					);
			modifie.setEasing(easingType);
		}
		else if (selected.getType().equals("FillColor")) {
			modifie = new FillColor((FillColor) selected);
			
			Color cObjStroke = comp.getEtatObjGeo(selected.getT_debut()).getStrokeColor();
			Color cDemandeStroke = ((MyColorChooser)message[1]).getColor();
			((FillColor)modifie).setColor(
					cDemandeStroke.getRed() - cObjStroke.getRed(), 
					cDemandeStroke.getGreen() - cObjStroke.getGreen(),
					cDemandeStroke.getBlue() - cObjStroke.getBlue()
					);
			modifie.setEasing(easingType);
		}
		
		//cas particulier pour modifier une translation :
		Double debut = Double.parseDouble(((JTextField)message[message.length-3]).getText().toString());
		Double fin = (debut + Double.parseDouble(((JTextField)message[message.length-1]).getText().toString())) * LecteurAnimation.CONSTANTE_TEMPS;

		
		if (selected.getType().equals("Translation")) {
			((CompositeAnimation) comp.getAnimation()).remove(selected);
			Point p = new Point();
			p.setLocation(comp.getEtatObjGeo(t_courant).getCentre());
			new CreateTrajectoire((Edition) f,comp,debut,fin, easingType,(Translation) selected);
			modifie = null;
		}
		
		
		if (modifie != null){
			modifie.setT_debut(debut * LecteurAnimation.CONSTANTE_TEMPS);
			modifie.setT_fin(fin);
			modifie.setEasing(easingType);
		}
		
		return modifie;
	}
}
