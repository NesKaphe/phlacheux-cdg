package Animations;

import java.awt.geom.Point2D;


/**
 * Enumération pour connaitre la direction vers laquelle on va.
 * Utile pour savoir le prochain pixel à observer (utiliser par la classe Translation)
 * (attention : c'est un abus de language Direction = sens du point de vu de l'espace vectoriel)
 * @author clement
 *
 */
public enum Direction {
	
	NO_DIR(0,"NO Direction"),
	S_O(1,"Sud Ouest"),
	S(2,"Sud"),
	S_E(3,"Sud Est"),
	E(4,"Est"),
	N_E(5,"Nord Est"),
	N(6,"Nord"),
	N_O(7,"Nord Ouest"),
	O(8,"Ouest");
	
	private final int chiffreDir;
	private final String nomDir;
	
	Direction(int chiffreDir,String nomDir){
		this.chiffreDir = chiffreDir;
		this.nomDir = nomDir;
	}
	
	/*
	Direction (Direction d){
		this.chiffreDir = d.chiffreDir;
		this.nomDir = d.nomDir;
	}
	*/
	/**
	 * retourne la direction suivante dans le sens horaire
	 * @return
	 */
	public Direction nextDir(){
		//COMENT : "swicth case" impossible je ne comprend pas l'erreur
		if 		 (this.chiffreDir == 0){
			System.err.println("NO_DIR : pas de point suivant");
			return NO_DIR;
		}else if (this.chiffreDir == 1){
			return S;
		}else if (this.chiffreDir == 2){
			return S_E;
		}else if (this.chiffreDir == 3){
			return E;
		}else if (this.chiffreDir == 4){
			return N_E;
		}else if (this.chiffreDir == 5){
			return N;
		}else if (this.chiffreDir == 6){
			return N_O;
		}else if (this.chiffreDir == 7){
			return O;
		}else if (this.chiffreDir == 8){
			return S_O;
		}else{
			System.err.println("Bravo vous avez craqué le système car ce cas est impossible");
			return null;//problème
		}
	}
	
	/**
	 * retourne la direction suivante dans le sens anti-horaire
	 * @return
	 */
	public Direction prevDir(){
		//COMENT : "swicth case" impossible je ne comprend pas l'erreur
		if 		 (this.chiffreDir == 0){
			System.err.println("NO_DIR : pas de point précédent");
			return NO_DIR;
		}else if (this.chiffreDir == 8){
			return N_O;
		}else if (this.chiffreDir == 7){
			return N;
		}else if (this.chiffreDir == 6){
			return N_E;
		}else if (this.chiffreDir == 5){
			return E;
		}else if (this.chiffreDir == 4){
			return S_E;
		}else if (this.chiffreDir == 3){
			return S;
		}else if (this.chiffreDir == 2){
			return S_O;
		}else if (this.chiffreDir == 1){
			return O;
		}else{
			System.err.println("Bravo vous avez craqué le système car ce cas est impossible");
			return null;//problème
		}
	}
	
	private int getChiffreDir(){
		return this.chiffreDir;
	}

	public String getNomDir() {
		return nomDir;
	}
	
	
	/**
	 * retourne retourne le point correspondant à la Direction (this).
	 * Par rapport au point passé en paramètre.
	 * @param p_org = point d'origine 
	 * @return
	 */
	public Point2D.Double CorespondPoint(Point2D.Double p_org){
		//COMMENT : "swicth case" impossible je ne comprend pas l'erreur
		if 		 (this.chiffreDir == 0){
			System.err.println("NO_DIR!!!!");
			return p_org;
		}else if (this.chiffreDir == 1){
			return new Point2D.Double(p_org.x-1,p_org.y+1);
		}else if (this.chiffreDir == 2){
			return new Point2D.Double(p_org.x,p_org.y+1);
		}else if (this.chiffreDir == 3){
			return new Point2D.Double(p_org.x+1,p_org.y+1);
		}else if (this.chiffreDir == 4){
			return new Point2D.Double(p_org.x+1,p_org.y);
		}else if (this.chiffreDir == 5){
			return new Point2D.Double(p_org.x+1,p_org.y-1);
		}else if (this.chiffreDir == 6){
			return new Point2D.Double(p_org.x,p_org.y-1);
		}else if (this.chiffreDir == 7){
			return new Point2D.Double(p_org.x-1,p_org.y-1);
		}else if (this.chiffreDir == 8){
			return new Point2D.Double(p_org.x-1,p_org.y);
		}else{
			System.err.println("ce cas est impossible");
			return null;//problème
		}
		

	}
	
	
};
