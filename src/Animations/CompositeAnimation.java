package Animations;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class CompositeAnimation extends Animation{
	
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
		boolean suppression = this.ChildAnimations.remove(a);
		
		//On va maintenant refresh le temps
		this.setT_debut(0.);
		this.setT_fin(0.);
		for(Animation anim : ChildAnimations) {
			this.ChangeTminTmax(anim.getT_debut(), anim.getT_fin());
		}
		
		return suppression;
	}
	
	/**
	 * ArrayList<Animation> getAllChilds() :
	 * ------------------------------------------------------------
	 * retourne tout les Animations simple (enfant) d'un CompositeAnnimation
	 * recherche de manière récursive
	 * @return
	 */
	public ArrayList<Animation> getAllChilds(){
		ArrayList<Animation> listA = new ArrayList<Animation>();
		for(Animation a : ChildAnimations){
			if(a.getType().equals("composite")){
				listA.addAll(((CompositeAnimation)a).getAllChilds());
			}else{
				listA.add(a);
			}
		}
		return listA;
	}

	@Override
	public AffineTransform getAffineTransform(double t_courant) {
		//si le temps demandé n'est pas dans notre intervalle 
		//retourne null imédiatement

		if(t_courant < this.getT_debut())
			return null;
		
		AffineTransform at_retour = new AffineTransform();//va contenir les transformations
		//parcourir la liste des enfants pour connaitre toute leurs transformations :
		for(Animation a : this.ChildAnimations){
			//si notre enfant est conserné :
			if (a.tmpOk(t_courant)){
				AffineTransform atmp = a.getAffineTransform(t_courant);
				if (atmp != null)
					at_retour.concatenate(atmp);
				
			}else if(a.getT_fin() < t_courant){
				AffineTransform atmp = a.getAffineTransform(a.getT_fin());
				if (atmp != null)
					at_retour.concatenate(atmp);
				//at_retour = a.getAffineTransform(a.getT_fin());
			}
		}
		
		//setTrans(at_retour);
		return at_retour;
	}
	
	public Float getWidthStroke(double t_courant) {
		if(t_courant < this.getT_debut())
			return null;
		
		float strokeWidthTotal = 0;
		//parcourir la liste des enfants pour connaitre toute leurs transformations :
		for(Animation a : this.ChildAnimations) {
			//si notre enfant est conserné :
			if (a.tmpOk(t_courant)){
				Float btmp = a.getWidthStroke(t_courant);
				if(btmp != null)
					strokeWidthTotal += btmp;
			}
			else if(a.getT_fin() < t_courant){
				Float btmp = a.getWidthStroke(a.getT_fin());
				if(btmp != null)
					strokeWidthTotal += btmp;
			}
		}
		
		if(strokeWidthTotal == 0) { //Si rien n'a changé, on avait pas d'animation de width dans nos fils
			return null;
		}
		return strokeWidthTotal;
	}


	@Override
	public int[] getStrokeColor(Double t_courant) {
		if(t_courant < this.getT_debut())
			return null;
		
		int r = 0;
		int g = 0;
		int b = 0;
		//parcourir la liste des enfants pour connaitre toute leurs transformations :
		for(Animation a : this.ChildAnimations) {
			//si notre enfant est conserné :
			int[] ctmp = null;
			if (a.tmpOk(t_courant)){
				ctmp = a.getStrokeColor(t_courant);
			}
			else if(a.getT_fin() < t_courant){
				ctmp = a.getStrokeColor(a.getT_fin());
			}

			if(ctmp != null) {
				r += ctmp[0];
				g += ctmp[1];
				b += ctmp[2];
			}
		}
		
		if(r == 0 && g == 0 && b == 0) {
			return null;
		}
		
		int[] c = new int[3];
		c[0] = r;
		c[1] = g;
		c[2] = b;
		
		return c;
	}


	@Override
	public int[] getFillColor(Double t_courant) {
		if(t_courant < this.getT_debut())
			return null;
		
		int r = 0;
		int g = 0;
		int b = 0;
		//parcourir la liste des enfants pour connaitre toute leurs transformations :
		for(Animation a : this.ChildAnimations) {
			//si notre enfant est conserné :
			int[] ctmp = null;
			if (a.tmpOk(t_courant)){
				ctmp = a.getFillColor(t_courant);
			}
			else if(a.getT_fin() < t_courant){
				ctmp = a.getFillColor(a.getT_fin());
			}
			
			if(ctmp != null) {
				r += ctmp[0];
				g += ctmp[1];
				b += ctmp[2];
			}
		}
		
		if(r == 0 && g == 0 && b == 0) {
			return null;
		}
		
		int[] c = new int[3];
		c[0] = r;
		c[1] = g;
		c[2] = b;
		
		return c;
	}
}
