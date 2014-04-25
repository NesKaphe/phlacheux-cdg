package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;

import Animations.GestionAnimation;
import affichage.Toile;

import formes.ObjetGeometrique;
import affichage.*;

public class CreateObjGeoListener implements ActionListener{
	
	private Toile toile;
	private GestionAnimation gestionnaire;
	
	private ObjetGeometrique objGeoAModifier;
	
	public CreateObjGeoListener(Toile toile/*ref sur le itemSelect*/, GestionAnimation gestionnaire) {
		this.toile = toile;
		this.gestionnaire = gestionnaire;
		this.objGeoAModifier = null;
	}
	

	@Override
	//forme de la commande (create_+nomObj) (modif_+nomObj)
	public void actionPerformed(ActionEvent e) {
		String[] commands = getCommands(e.getActionCommand());
		__Action(commands);
	}
	
	public void actionPerformed(String Command){
		String[] commands = getCommands(Command);
		__Action(commands);
	}
	
	public void actionPerformed(String Command, ObjetGeometrique geoAModifier) {
		this.objGeoAModifier = geoAModifier;
		this.actionPerformed(Command);
	}
	
	/**
	 * String[] getCommands(String str):
	 * ---------------------------------
	 * pour décomposer la commande
	 * en 2 parties
	 * @param str
	 * @return
	 */
	private String[] getCommands(String str){
		return str.split("_");
	}


	
	private void __Action(String[] commands){//TODO : recevoir un String 
		CreateObjGeoBox alert_box = null;
		switch(commands[0]){
			case "create":
				System.out.println("Alert box create");
				alert_box = new CreateObjGeoBox(gestionnaire,commands[1]);
				break ;
			case "modif" :
				System.out.println("Alert box modif");
				alert_box = new CreateObjGeoBox(gestionnaire, this.objGeoAModifier);//TODO : a finir
				break;
			default:
				System.err.println("\n\ncommande\""+commands[0]+//TODO : une fois modifier changer ça
				"...\"non reconnu\nforme attendu :\"create_+nomObj\" ou \"modif_+nomObj\"\n\n");
		}
		ObjetGeometrique geo = alert_box.GenerateAndConfigureBox();
		//TODO : attention rajouter une suppresion si c'est une modification d'objet
		//si geo est a null c'est que l'action est annulée
		if (geo != null){
			if(this.objGeoAModifier == null) {
				this.toile.modeAjout();
				//toile.setObjTemporaire(geo);//la toile va se chager de dessiner l'objet
				this.gestionnaire.setObjGeoEnCreation(geo);
			}
			else {
				this.gestionnaire.refreshDessin();
				this.objGeoAModifier = null;
			}
		}
	}
}