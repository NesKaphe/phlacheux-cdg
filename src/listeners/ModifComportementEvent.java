package listeners;

import java.awt.event.ActionEvent;

import Animations.Comportement;

/**
 * C'est un action event contenant directement l'objet geometrique
 * @author Alain
 *
 */
@SuppressWarnings("serial")
public class ModifComportementEvent extends ActionEvent {

	private Comportement compAModif;
	
	public ModifComportementEvent(Object source, int id, String command,Comportement compAModif) {
		super(source, id, command);
		this.compAModif = compAModif;
	}
		
	public Comportement compAModif() {
		return this.compAModif;
	}

}
