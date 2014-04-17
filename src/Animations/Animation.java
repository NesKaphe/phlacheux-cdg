package Animations;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;


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
	private AffineTransform trans;//sera la mémoire de la position de l'objet (seule une racine peux le modifier)
	
	public Animation(Double t_debut,Double t_fin,int easing,String type) {
		this.t_debut = t_debut;
		this.t_fin = t_fin;
		this.easing = easing;
		this.parent = null;
		this.type = type;
		this.id = Animation.cpt_id++;
		this.trans = null;

	}
	//TODO : il serait utile de faire un constructeur par recopie
	
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
		return ((t_courant- t_debut) / ( t_fin - t_debut));
	}
	
	/**
	 * retourne notre niveau dans l'arbre
	 * 0 = nous somme la racine
	 * @return
	 */
	public int getMyLevel(){
		Animation a_parent = this.getParent();//a_parent = animation parent
		//premier cas particulier nous sommes la racine :
		if (a_parent == null)
			return 0;
		int i=0;
		//boucle jusqu'à la racine
		for (i=1;;i++){
			if(a_parent.getParent() == null)
				break;
			else
				a_parent = a_parent.getParent();//passé a l'aïeux suivant
		}
		return i;
	}
	
	/**
	 * void resetTrans():
	 * 
	 * pour remettre l'obejet dans l'état ou on l'a trouvé
	 */
	protected void resetTrans(){
		this.trans = new AffineTransform();
	}
	
	
	/**
	 * void ChangeTminTmax(double t_min,double t_max):
	 * 
	 * pour changer le temps debut et fin
	 * Si t_min < this.t_debut alors t_debut = t_min
	 * Si t_max > this.t_fin alors t_debut = t_max
	 * cette méthode est récursive et change tout les parents
	 */
	public void ChangeTminTmax(double t_min,double t_max){
		this.setT_debut(Math.min(this.getT_debut(), t_min));//affectation du temps min
		this.setT_fin(Math.max(this.getT_fin(), t_max));//affectation du temps max
		if(parent != null)
			parent.ChangeTminTmax(t_min, t_max);
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
	
	public Double getT_debut() {
		return t_debut;
	}

	public Double getT_fin() {
		return t_fin;
	}

	protected AffineTransform getTrans() {
		return trans;
	}

	/**
	 * On ne peux changer trans (AffineTransform)
	 * que si on est une racine.
	 * renvoi faux si la modification n'est pas effectué (car this n'est pas la racine)
	 * 
	 * @param trans 
	 * @return
	 */
	protected boolean setTrans(AffineTransform trans) {
		if (this.getMyLevel()==0){
			//l'alocation ce fait ici
			if(trans == null){
				trans = new AffineTransform();
			}
			this.trans = trans;
			return true;
		}
		return false;
	}
	
	/**
	 * met à null l'objet trans.
	 * à utiliser dans le cas ou l'animation devien l'enfant d'une autre
	 */
	protected void setNullTrans(){
		this.trans =null;
	}
	
	/**
	 * attention à utiliser que si vraiment nécéssaire
	 * @param t_debut
	 */
	protected void setT_debut(Double t_debut) {
		this.t_debut = t_debut;
	}
	
	/**
	 * attention à utiliser que si vraiment nécéssaire
	 * @param t_fin
	 */
	protected void setT_fin(Double t_fin) {
		this.t_fin = t_fin;
	}

	@Override
	public String toString() {//TODO : faire une noivelle version
		return "Animation [id=" + id + ", type=" + type + ", parent=" + parent
				+ ", t_debut=" + t_debut + ", t_fin=" + t_fin + ", easing="
				+ easing + "]";
	}
	
}


class CompositeAnimation extends Animation{
	
	private ArrayList<Animation> ChildAnimations;//list des animations enfants
	
	
	public CompositeAnimation(Double t_debut,Double t_fin,int easing){
		super(t_debut,t_fin,easing,"composite");
		this.ChildAnimations = new ArrayList<Animation>();
		//par défaut notre parent est à null car nous ne somme relier à personne
	}
	
	
	public boolean add(Animation a){
		//TODO : pour l'instant on ajoute sans discrimination à faire plus tard!!!
		//TODO : PROBLEME :la version actuelle peux boucler add de soit même ou d'un parent a exclure
		//TODO : solution faire une méthode isMyparent pour empècher qu'il y ait des boucles avec les parents
		//TODO : tester isMyParent(a)
		
		//modifications de références pour l'enfant :
		a.setParent(this);//modification du parent
		a.setNullTrans();//comme "a" n'est plus racine on supprime l'utilisation de trans
		//modification des temps du parent qui est calé sur le temps min et temps max de ses enfants:
		this.ChangeTminTmax(a.getT_debut(), a.getT_fin());
		
		return this.ChildAnimations.add(a);
	}
	
	public boolean remove(Animation a){
		//TODO : éventuellement faire un message contenant tout ce qui à été supprimé
		a.setParent(null);//on lui détruit sa référence sur le parent
		return this.ChildAnimations.remove(a);
	}


	@Override
	public AffineTransform getAffineTransform(Double t_courant) {
		//si le temps demandé n'est pas dans notre intervalle 
		//retourne null imédiatement
		if(!tmpOk(t_courant))
			return getTrans();
		
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
		
		setTrans(at_retour);
		return at_retour;
	}
}

