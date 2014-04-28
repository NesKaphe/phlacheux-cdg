package Animations;

import java.awt.geom.AffineTransform;


//une Animation simple est une feuille
public abstract class Animation {
	//id de l'animation :
	private static int cpt_id = 0;
	private int id;
	private final String type;//type d'animation
	protected Animation parent;//pour connaitre sont parent
	private Double t_debut;
	private Double t_fin;
	private int easing;
	private EasingFunction easingFunction;

	
	public Animation(Double t_debut,Double t_fin,int easing,String type) {
		this.t_debut = t_debut;
		this.t_fin = t_fin;
		this.easing = easing;
		this.parent = null;
		this.type = type;
		this.id = Animation.cpt_id++;
		this.easingFunction = new EasingFunction(easing);

	}
	//TODO : il serait utile de faire un constructeur par recopie
	
	public Animation(Animation anim) {
		this.t_debut = anim.t_debut;
		this.t_fin = anim.t_fin;
		this.easing = anim.easing;
		this.parent = anim.parent;
		this.type = anim.type;
		this.id = anim.id;
		this.easingFunction = anim.easingFunction;
	}
	/**
	 * AffineTransform getAffineTransform(Double t_courant) :
	 * ------------------------------------------------------
	 * Si notre animation conserne une translation il faut implémenter cette
	 * méthode.
	 * prendre en paramètre le temps courant est retourne la transformation
	 * affine à cette instant si il n'y a aucune transformation retourne null
	 * @param t_courant
	 * @return
	 */
	public AffineTransform getAffineTransform(double t_courant){
		return null;//null par défaut
	}
	
	/**
	 * Float getWidthStroke(Double t_courant) :
	 * -----------------------------------------------------
	 * @param t_courant le temps courant demandé pour l'animation
	 * @return un float representant une epaisseur de trait a incrementer et null
	 * si t_courant n'est pas dans l'intervalle de l'animation
	 */
	public Float getWidthStroke(double t_courant){
		return null;//null par défaut
	}
	
	/**
	 * Color getStrokeColor(Double t_courant) :
	 * -----------------------------------------------------
	 * @param t_courant
	 * @return un tableau contenant les valeurs RGB a incrementer à la forme geometrique
	 * et null si t_courant n'est pas dans l'intervalle de l'animation
	 */
	public int[] getStrokeColor(Double t_courant){
		return null;//null par défaut
	}
	
	/**
	 * Color getFillColor(Double t_courant) :
	 * -----------------------------------------------------
	 * @param t_courant
	 * @return un tableau contenant les valeurs RGB a incrementer à la forme geometrique
	 * et null si t_courant n'est pas dans l'intervalle de l'animation
	 */
	public int[] getFillColor(Double t_courant){
		return null;//null par défaut
	}
	
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
	 * Ajout récent : l'easing function agit sur le résultat
	 * @param tmp
	 * @return
	 */
	public Double getPourun(double t_courant){
		if (!tmpOk(t_courant)){
			return -1.;
		}else{
			double x = ((t_courant- t_debut) / ( t_fin - t_debut));
			return easingFunction.getEasing(x);
			//return ((t_courant- t_debut) / ( t_fin - t_debut));
		}
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
	
	public int getEasing() {
		return easing;
	}

	public void setEasing(int easing) {
		this.easing = easing;
		this.easingFunction.setType(easing);
	}
	
	/**
	 * attention à utiliser que si vraiment nécéssaire
	 * @param t_debut
	 */
	public void setT_debut(Double t_debut) {
		this.t_debut = t_debut;
		//ChangeTminTmax(t_debut , t_fin);//TODO : à faire fonctionner (bug quand on termine une translation)
	}
	
	/**
	 * attention à utiliser que si vraiment nécéssaire
	 * @param t_fin
	 */
	public void setT_fin(Double t_fin) {
		this.t_fin = t_fin;
		//ChangeTminTmax(t_debut , t_fin);//TODO : à faire fonctionner (bug quand on termine une translation)
	}
	
	/*
	 * pour changer t_debut et t_fin en même temps
	 * le paramètre déplacement correcpond au changement
	 * cette méthode ne decendre pas t_debut en dessous de zero
	 * on block tout au zero
	 */
	public void changePeriode(double déplacement){
		double test = this.t_debut + déplacement;
		if (test < 0){
			this.t_fin-=this.t_debut;
			this.t_debut= 0.;
		}else{
			this.t_debut+= déplacement;
			this.t_fin+= déplacement;
		}
		ChangeTminTmax(t_debut , t_fin);
	}
	

	@Override
	public String toString() {//TODO : faire une noivelle version
		return "Animation [id=" + id + ", type=" + type + ", parent=" + parent
				+ ", t_debut=" + t_debut + ", t_fin=" + t_fin + ", easing="
				+ easing + "]";
	}
	
}


