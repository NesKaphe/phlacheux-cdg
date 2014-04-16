package Animations;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.text.html.HTMLDocument.Iterator;

//une Animation simple est une feuille
public abstract class Animation {
	//id de l'animation :
	private static int cpt_id = 0;
	private int id;
	private String type;//type d'animation
	
	protected Animation parent;//pour connaitre sont parent
	private Double t_debut;
	private Double t_fin;
	private int easing;//TODO : pour l'instant c'est un int plus tard on fera une vrai classe 
	public Animation(Double t_debut,Double t_fin,int easing,String type) {
		this.t_debut = t_debut;
		this.t_fin = t_fin;
		this.easing = easing;
		this.parent = null;
		this.type = type;
		this.id = this.cpt_id++;

	}
	
	/**
	 * AffineTransform getAffineTransform(Double t_courant) :
	 * ------------------------------------------------------
	 * prendre en paramètre le temps courant est retourne la transformation
	 * affine à cette instant si il n'y a aucune transformation retourne null
	 * @param t_courant
	 * @return
	 */
	public abstract AffineTransform getAffineTransform(Double t_courant);
	
	/**
	 * boolean tmpOk(Double tmp):
	 * -------------------------
	 * retourne vrai si le temps tmp est dans l'intervalle 
	 * de temps de l'animation
	 * 
	 * @param tmp
	 * @return
	 */
	public boolean tmpOk(Double tmp){
		return (tmp>=this.t_debut)&&(tmp<=this.t_fin);
	}
	
	/**
	 * retourne la propositon de temps que l'animation
	 * à parcouru "pour un". Autrement dit c'est équivalent
	 * à "pourcent" sauf que c'est "pourun".
	 * Si le temps "t_courant" est en dehord des limites une valeur négative est renvoyé.
	 *
	 * @param tmp
	 * @return
	 */
	public Double getPourun(Double t_courant){
		if (!tmpOk(t_courant))
			return -1.;
		return (t_courant / ( t_fin - t_debut));
	}
	
	/**
	 * 
	 * Getteur et Setter et toString
	 * 
	 */
	
	public Animation getParent() {
		return parent;
	}
	
	public String getType() {
		return type;
	}

	protected void setParent(Animation parent) {
		this.parent = parent;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Animation [id=" + id + ", type=" + type + ", parent=" + parent
				+ ", t_debut=" + t_debut + ", t_fin=" + t_fin + ", easing="
				+ easing + "]";
	}
	
}






class CompositeAnimation extends Animation{
	
	private ArrayList<Animation> ChildAnimations;//list des animations enfants
	
	
	public CompositeAnimation(Double t_debut,Double t_fin,int easing){
		super(t_debut,t_fin,easing,"composite");
		//par défaut notre parent est à null car nous ne somme relier à personne
	}

	
	
	public boolean add(Animation a){//TODO :pour l'instant on ajoute sans discrimination à faire plus tard
		a.setParent(this);//modification du parent
		return this.ChildAnimations.add(a);
	}
	
	public boolean remove(Animation a){//TODO : éventuellement faire un message contenant tout ce qui à été supprimé
		return this.ChildAnimations.remove(a);
	}
	
	/**
	 * retourne notre niveau dans l'arbre
	 * 0 = nous somme la racine
	 * @return
	 */
	public int getMyLevel(){
		Animation a_parent = this.getParent();//a_parent = animation parent
		int i=0;
		//boucle jusqu'à la racine
		for (i=0;;i++){
			if(a_parent.getParent() == null)
				break;
			else
				a_parent = a_parent.getParent();//passé a l'aïeux suivant
		}
		return i;
	}



	@Override
	public AffineTransform getAffineTransform(Double t_courant) {
		//si le temps demandé n'est pas dans notre intervalle 
		//retourne null imédiatement
		if(!tmpOk(t_courant))
			return null;
		
		AffineTransform at_retour = new AffineTransform();//va contenir les transformations
		//parcourir la liste des enfants pour connaitre toute leurs transformations :
		for(Animation a : this.ChildAnimations){
			//si notre enfant est conserné :
			if (a.tmpOk(t_courant)){
				AffineTransform atmp = a.getAffineTransform(t_courant);
				if (atmp != null)
					at_retour.concatenate(atmp);
			}
		}
		return at_retour;
	}
}

/**
 * 
 * @author clement
 *Animation simple : Rotation
 */
class Rotation extends Animation{

	public Double angle;//sens de rotation en radian
	
	public Rotation(Double t_debut, Double t_fin, int easing,Double angle) {
		super(t_debut, t_fin, easing, "rotation");
		this.angle = angle;
		
	}

	@Override
	public AffineTransform getAffineTransform(Double t_courant) {
		Double pu = this.getPourun(t_courant);
		//si pu est négatif c'est que notre temps courant n'est pas bon
		if (pu <0.0)
			return null;
		Double a_courrant = this.angle * pu;//TODO : plustard utiliser les easing functions( ajouter : *easing)
		
		
		return null;
	}
	
}
