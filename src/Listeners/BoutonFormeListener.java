package Listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import formes.Carre;
import formes.Cercle;
import formes.Rectangle;
import formes.Triangle;

import affichage.Toile;

public class BoutonFormeListener implements MouseListener {

	private Toile toile; //la fenetre fait le lien entre tous les elements
	private MouseMotionToileListener motion;
	
	public BoutonFormeListener(Toile toile, MouseMotionToileListener motion) {
		this.toile = toile;
		this.motion = motion;
	}
	
	public void mouseClicked(MouseEvent e) {
		JButton b = (JButton) e.getSource();
		System.out.println(b.getText());
		JColorChooser StrokeChooser = new JColorChooser();
		JColorChooser FillChooser = new JColorChooser();
		JTextField Epaisseur = new JTextField();
		
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
		
		switch(b.getText()) { //TODO: remplacer les noms en francais par une recupération dans un ressource bundle (tp1)
		
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
				this.toile.addMouseMotionListener(this.motion);
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
				this.toile.addMouseMotionListener(this.motion);
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
				this.toile.addMouseMotionListener(this.motion);
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
					Triangle triangle = new Triangle(new Point2D.Double(50, 50), (int) cote);
					triangle.setStrokeColor(StrokeChooser.getColor());
					triangle.setFillColor(FillChooser.getColor());
					triangle.setStrokeWidth(Float.parseFloat(Epaisseur.getText()));
					this.toile.setObjTemporaire(triangle);
				}
				catch(Exception exception) {
					System.out.println("Pas un entier");
				}
				//Ajouter un mouse movement listener a la toile avec un triangle a dessiner
				this.toile.addMouseMotionListener(this.motion);
			} 
			else {
			    System.out.println("Annulation");
			}
			break;
			
		case "Ligne":
			
			break;
		}
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent arg0) {
		
	}

}
