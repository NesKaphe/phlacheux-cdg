package affichage;

public class Item {

	private int id;
	private Object valeur;
	
	public Item(int id, Object valeur) {
		this.id = id;
		this.valeur = valeur;
	}
	
	public int getId() {
		return this.id;
	}
	
	public Object getValeur() {
		return this.valeur;
	}
	
	public String toString() {
		return this.valeur.toString();
	}
}
