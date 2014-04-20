package Animations;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Translation extends Animation {
	
	private ArrayList<Point2D.Double> listPoint;//va contenir la liste des points 
	public GeneralPath gp;//chemin/tracé de notre translation //TODO : temporairement public pour les testes
	private Point2D.Double start_point;
	private Point2D.Double end_point;
	public Point2D.Double cur_point;//point courrant
	public Point2D.Double pre_point;//precedent point
	private Direction cur_dir ;//direction 0 à 9 (voir pavé num)
	private ArrayList<Point2D.Double> listToutPoint;//list d'absolument tout les points (voir si c'est pas trop lourd pour l'acces mémoire)
	
	
	public Translation(double t_debut, double t_fin, int easing,ArrayList<Point2D.Double> listPoint) {
		super(t_debut, t_fin, easing, "translation");
		if(listPoint.size() <2){
			//TODO : lancer une exception à la place du message d'erreur
			//throw new ListPointException()
			//il faut détruire l'objet translation et recommencer
			System.out.println("listPoint.size() = "+listPoint.size());//DEBUG
			System.err.println("\n\nERREUR : Animation-Translation construite mais invalide car la taille listPoint est <2\n\n");
			this.listPoint = null;
		}else{
			this.listPoint = new ArrayList<Point2D.Double>(listPoint);
		}
		this.start_point = listPoint.get(0);
		this.end_point = listPoint.get(listPoint.size()-1);
		this.cur_point = null;
		
	}
	
	@Override
	public AffineTransform getAffineTransform(double t_courant) {
		

		
		return null;
	}
	
	
	/**
	 * GeneralPath generatePath() :
	 * ----------------------------------------------------
	 * Génère le chemin "gp" à partir de la list de points "listPoint".
	 * Les points sont consiédés alternativement comme points de controle et points de passage.
	 * exemple :
	 * Le 1er point est un point de passage.
	 * Le 2 eme = point de controle.
	 * et le 3eme = point de passage.
	 * et ainsi de suite...
	 * Avec c'est 3 points nous générons un quadCurve "quad_to".
	 * 
	 * Si le nombre de points est paire le chemin ce terminera par une ligne droite "line_to".
	 * Sinon il ce termine en courbe quadCurve "quad_to".
	 * 
	 * Pour des raisons technique le Chemin "gp" est dessin avec le chemin de retour.
	 * Raison : le chemin "gp" est un GeneralPath (c'est un Shape).
	 * 			Pour parcourir le chemin nous utilisons la méthode intersec de "gp".
	 * 			Cette méthode retourne vrai si nous somme dans le Shape de "gp".
	 * 			Hors ce que nous voulons c'est la bordure du Shape "gp" et pas l'interieur.
	 * 			C'est pourquoi l'astuce est de re-parcourir le chemin à l'envers. Ainsi le Shape 
	 * 			"gp" ne se résume plus qu'à sa bordure.
	 * 
	 * @param listPoint
	 * @return
	 */
	public void generatePath(){
		
		this.gp = new GeneralPath();
		int limite = listPoint.size();
		boolean paire = true;
		//savoir si nous sommes paire ou impaire :
		if(listPoint.size()%2 == 1){
			paire=false;
		}
		//DESSIN CHEMIN :=====================
		//on place le premier point :
		gp.moveTo(this.start_point.x,this.start_point.y);
		System.out.println("DEBUG - CHEMIN  : moveTO p ("+start_point+")");
		//on fait des quadCurve pour les points (sauf le dernier si c'est paire):
		for(int i=1 ; limite!=2 && i < limite-1;i+=2){
			System.out.println("DEBUG - i = "+i);
			System.out.println("DEBUG - CHEMIN  :  quadTo ( p1("+listPoint.get(i).x+","+listPoint.get(i).y+")  p2 ("+listPoint.get(i+1).x+","+listPoint.get(i+1).y+") )");
			gp.quadTo(listPoint.get(i).x, listPoint.get(i).y, listPoint.get(i+1).x, listPoint.get(i+1).y);
		}
		//si nous sommes paire le dernier point est une ligne
		if (paire){
			gp.lineTo(listPoint.get(limite-1).x, listPoint.get(limite-1).y);
			System.out.println("DEBUG - CHEMIN  :lineTo p ("+listPoint.get(limite-1).x+","+listPoint.get(limite-1).y+")");
		}
		//DESSIN CHEMIN INVERSE:==============
		if (paire){
			limite -=1;
			System.out.println("DEBUG - CHEMIN-INVERSE : lineTo p ("+listPoint.get(limite-1).x+","+listPoint.get(limite-1).y+")");
			gp.lineTo(listPoint.get(limite-1).x, listPoint.get(limite-1).y);
		}
		
		limite-=2;
		
		for(int i=limite ; i >0 ;i-=2){
			System.out.println("DEBUG - CHEMIN-INVERSE : i ="+i);
			System.out.println("DEBUG - CHEMIN-INVERSE : quadTo ( p1("+listPoint.get(i).x+","+listPoint.get(i).y+"), p2 ("+listPoint.get(i-1).x+","+listPoint.get(i-1).y+") )");
			gp.quadTo(listPoint.get(i).x, listPoint.get(i).y, listPoint.get(i-1).x, listPoint.get(i-1).y);
		}
		gp.closePath();
		
	}
	
	
	/**
	 * 
	 *  Prend en paramètre une liste de points retourne le GeneralPath correspondant.
	 *  Intéret on peux sélectionner des segments de chemins (3 points) par exemples
	 *  TODO : renommer generateSegmentPath
	 * @param LP
	 * @return
	 */
	private GeneralPath generateSegmentPath(ArrayList<Point2D.Double> LP){
		
		int limite = LP.size();
		if((limite<2)&&(limite>3)){
			System.err.println("ERREUR - il y n'y a pas le nombre de point demandé");
			return null;
		}
			
		GeneralPath gp_seg = new GeneralPath();
		
		boolean line = true;
	
		//DESSIN CHEMIN :=====================
		//on place le premier point :
		gp_seg.moveTo(LP.get(0).x,LP.get(0).y);//LP.get(0) = point de départ
		System.out.println("DEBUG - CHEMIN  : moveTO p ("+LP.get(0).x+","+LP.get(0).y+")");
		//3 points = quadCurve:
		if(LP.size() == 3){
			System.out.println("DEBUG - CHEMIN  : quadTo");
			gp_seg.quadTo(LP.get(1).x, LP.get(1).y, LP.get(2).x, LP.get(2).y);
			gp_seg.quadTo(LP.get(1).x, LP.get(1).y, LP.get(0).x, LP.get(0).y);//chemin inverse
		}
		//2 points c'est une ligne :
		else{
			System.out.println("DEBUG - CHEMIN  :lineTo");
			gp_seg.lineTo(LP.get(1).x, LP.get(1).y);
			gp_seg.lineTo(LP.get(0).x, LP.get(0).y);//chemin inverse
		}
		gp_seg.closePath();

		return gp_seg;
	}
	
	/**
	 * initialise la direction courante grace au point passé en paramètre.
	 * 
	 * retourne faux si le point n'a 
	 * @param pt
	 * @return
	 */
	public boolean initCurDir(GeneralPath seg_gp,Point2D.Double pt){
		Point2D.Double nextPts ;
		Direction dir = Direction.S;//on attribut arbitrairement une direction

		//on explore tout les pixels environants sauf celui d'où l'on vien:
		for(int i=0;i<8;i++){
			//solution pour optimiser la recherche autour du point :
			switch(i){
			case 0:break;								//mm dir que la précédente
			case 1: dir = dir.nextDir();break;						//dir sens horaire +1
			case 2: dir = dir.prevDir();break;						//dir sens anti-horaire +1
			case 3: dir = dir.nextDir().nextDir();break;			//dir sens horaire +2
			case 4: dir = dir.prevDir().prevDir();break;			//dir sens anti-horaire +2
			case 5: dir = dir.nextDir().nextDir().nextDir();break;	//dir sens horaire +3
			case 6: dir = dir.prevDir().prevDir().prevDir();break;	//dir sens anti-horaire +3
			case 7: dir = dir.prevDir().prevDir().prevDir().prevDir();break;//cas particulier pour l'initialisation ce cas ne doit ce produire au maximum qu'une fois par semgent
			}
			nextPts = dir.CorespondPoint(pt);
			//System.out.println("DEBUG -nextPts ="+nextPts);
			if(seg_gp.intersects(nextPts.getX(),nextPts.getY(),1,1)){
				cur_dir = dir;
				return true;
			}
		}
		return false;//on a rien trouvé 
	}
	
	/**
	 * Point2D.Double nextPoint() :
	 * ----------------------------------------------------------------
	 * Parcours le GeneralPath "gp". 
	 * Cette méthode utilise "cur_point" comme point de départ et retourne le point voisin.
	 * Si elle trouve, "cur_point" est remplacé par le nouveau point.
	 * Si elle ne trouve pas de point suivant retourne null. Nous somme probablement au bout
	 * du chemin.
	 * Initialise "cur_point" et "cur_dir".
	 * @return
	 */
	//TODO : détection de croisement
	/**
	 * 
	 * version 2 pour les segments
	 * attention le "cur_seg" doit être initialisé
	 * @param cur_pt
	 * @return
	 */
	public Point2D.Double nextPoint(GeneralPath seg_gp){
		Point2D.Double nextPts ;
		Direction dir = Direction.NO_DIR;//copie de la direction courante
		//on explore tout les pixels environants sauf celui d'où l'on vien:
		for(int i=0;i<8;i++){
			//solution pour optimiser la recherche autour du point :
			switch(i){
			case 0: dir = cur_dir;break;								//mm dir que la précédente
			case 1: dir = cur_dir.nextDir();break;						//dir sens horaire +1
			case 2: dir = cur_dir.prevDir();break;						//dir sens anti-horaire +1
			case 3: dir = cur_dir.nextDir().nextDir();break;			//dir sens horaire +2
			case 4: dir = cur_dir.prevDir().prevDir();break;			//dir sens anti-horaire +2
			case 5: dir = cur_dir.nextDir().nextDir().nextDir();break;	//dir sens horaire +3
			case 6: dir = cur_dir.prevDir().prevDir().prevDir();break;	//dir sens anti-horaire +3
			//case 7: dir = cur_dir.prevDir().prevDir().prevDir().prevDir();break;//cas particulier pour l'initialisation ce cas ne doit ce produire au maximum qu'une fois par semgent
			}
			nextPts = dir.CorespondPoint(cur_point);
			//System.out.println("DEBUG -nextPts ="+nextPts);
			if(seg_gp.intersects(nextPts.getX(),nextPts.getY(),1,1)){
				this.cur_dir = dir;
				cur_point = nextPts;
				return nextPts;
			}
		}
		return null;//on a rien trouvé 
	}
	
	

	
	public int cur_seg = -1;//segment courant //TODO : mettre en privée
	private int nb_seg = 0;//nombres de segments qui sont des quadCurves
	public GeneralPath cur_seg_gp = null;//le generalPath du segment courant //TODO : mettre en privée
	private boolean nb_points_paire = false;//pour savoir si nous finissons par une line
	private Point2D.Double cur_start_pt = null;//TODO : pas forcément utile probablement retirer si non utilisé
	private Point2D.Double cur_end_pt = null;//point courant de fin du segment
	
	/**
	 * version avec les segments de chemins étudiers séparéments
	 * retourne le point suivant de
	 * @return
	 */
	public Point2D.Double nextPointSegment(){
		//INITIALISATIONS ============================
		//initialisation de cur_seg et de nb_seg et cur_point:
		if(cur_seg == -1){
			int nb_points = listPoint.size();
			if (nb_points%2 == 0){
				nb_points_paire = true;
				nb_seg += 1;
				nb_points-=1;
			}
			nb_seg += nb_points/2;//division entière
			cur_seg = 0;
			this.cur_point = listPoint.get(0);//le point courrant est mis sur le point de départ
		}
		
		//initialisation de cur_seg_gp ,et des point de départ et de fin:
		if (this.cur_seg_gp == null){
			System.out.println("DEBUG -------nouveau seg gp--------");
			ArrayList<Point2D.Double> LP = new ArrayList<Point2D.Double>();
			//cas pour le dernier segment si c'est une ligne :
			if((cur_seg == this.nb_seg-1) && nb_points_paire){
				System.out.println("DEBUG - new seg line ");
				cur_start_pt = listPoint.get(listPoint.size()-2);
				cur_end_pt = listPoint.get(listPoint.size()-1);
				//System.out.println("DEBUG dans new seg line - cur_point ="+cur_point+"  end_pt = "+cur_end_pt+"  cur_seg="+cur_seg+"  cur_dir = "+cur_dir.getNomDir());
				LP.add(listPoint.get(listPoint.size()-2));//avant dernier point
				LP.add(listPoint.get(listPoint.size()-1));//denier point
				cur_seg_gp = generateSegmentPath(LP);//généré le chemin
				cur_point = cur_start_pt;
				boolean re = initCurDir(cur_seg_gp, cur_start_pt);//initiliser le "cur_dir"
				System.out.println("initCurDir ="+re);
			}else{//quadCurve
				System.out.println("DEBUG - new seg curve ");
				cur_start_pt = listPoint.get((cur_seg*2)  );
				cur_end_pt = listPoint.get((cur_seg*2)+2);
				
				LP.add(  listPoint.get((cur_seg*2)  )  );
				LP.add(  listPoint.get((cur_seg*2)+1)  );
				LP.add(  listPoint.get((cur_seg*2)+2)  );
				
				System.out.println("DEBUG - p1 ="+cur_start_pt+", p2="+listPoint.get((cur_seg*2)+1)+", p3="+cur_end_pt);
				cur_seg_gp = generateSegmentPath(LP);
				cur_point = cur_start_pt;
				boolean re = initCurDir(cur_seg_gp, cur_start_pt);
				System.out.println("initCurDir ="+re);
			}
		}
		//============================================
		
		
		//on recupère le point suivant dans le segment:
		cur_point = nextPoint(cur_seg_gp);
		System.out.println("DEBUG - cur_point ="+cur_point+"  end_pt = "+cur_end_pt+"  cur_seg="+cur_seg+"  cur_dir = "+cur_dir.getNomDir());
		//on regarde si on est à la fin du segment :
		//si cur_point est à null c'est que le segment est fini
		if( cur_point == null){
			System.out.println("DEBUG - fin du segment ");
			//si nous somme au dernier segment retourne null
			if(cur_seg == nb_seg){
				System.out.println("DEBUG - dernier point!!!!!!!!");
				return null;
			}
			//pour passer au segment suivant on va mettre cur_seg_gp à null et incrementer
			//cur_seg pour que à la prochaine utilisation on initialise une nouveau segment
			cur_seg_gp = null;
			cur_seg ++;
		}
		
		return this.cur_point;
	}
	
}













//==========================================================================================================
//==========================================================================================================
//===================EN DESSOUS PLEIN DE TESTES=============================================================
//==========================================================================================================
//==========================================================================================================



class petiteToile extends JPanel{
	private static final long serialVersionUID = 1L;
	private static int compteur = 0;
	

	
	public petiteToile() {
		this.setPreferredSize(new Dimension(500,500));
	}
	
	@Override
	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(preferredSize);
		this.setBackground(Color.white);
	}
	
	
	public void foo(){
		return;
	}
	
	private void desine_point(Graphics2D g2,Point2D p,int taille){
		//vertical:
		g2.drawLine((int)p.getX(),(int)p.getY()+taille,(int)p.getX(),(int)p.getY()-taille);
		//horizontal:
		g2.drawLine((int)p.getX()+taille,(int)p.getY(),(int)p.getX()-taille,(int)p.getY());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		System.out.println("------------repaint-------------");
		Graphics2D g2 = (Graphics2D)g;
		super.paintComponent(g2);
		g2.setStroke(new BasicStroke(1));
		
		//===PREMIERS TESTES=================
		//Points :
		Point2D.Double p = new Point2D.Double(30,180);//points de test
		Point2D.Double p1 = new Point2D.Double(30,30);
		Point2D.Double p2 = new Point2D.Double(260,160);
		Point2D.Double p3 = new Point2D.Double(30,180);
		Point2D.Double p4 = new Point2D.Double(260,350);
		Point2D.Double p5 = new Point2D.Double(100,300);
		Point2D.Double p6 = new Point2D.Double(260,250);
		/*
		 * 
		//dessin des points :
		g2.setColor(Color.green);
		this.desine_point(g2, p1,5);
		this.desine_point(g2, p2,5);
		this.desine_point(g2, p3,5);
		//dessin point de teste :
		g2.setColor(Color.lightGray);
		this.desine_point(g2, p, 5);
		
		//General Path (les points sont mis à la main):
		GeneralPath gp7 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		gp7.moveTo(p1.x,p1.y);
		gp7.quadTo(p2.x, p2.y, p3.x, p3.y);
		gp7.lineTo(p4.x, p4.y);
		//chemin inverse
		gp7.lineTo(p3.x, p3.y);
		gp7.quadTo( p2.x, p2.y,p1.x,p1.x);
		
		gp7.closePath();

	
		//dessin path :
		g2.setColor(Color.red);
		g2.draw(gp7);
		//g2.fill(gp7);
		 												*
		 												*/
		//infos :
		//System.out.println("gp.contains(p) = "+gp7.intersects(p.getX(),p.getY(),1,1)+"  p =("+(int)p.getX()+","+(int)p.getY()+")");
		//===FIN PREMIERS TESTES===========================
		//===TESTES TRANSLATION============================
		//dessiner le "gp"
		ArrayList<Point2D.Double> LP = new ArrayList<Point2D.Double>();
		LP.add(p1);
		LP.add(p2);
		LP.add(p3);
		LP.add(p4);
		LP.add(p5);
		LP.add(p6);
		Translation tr = new Translation(0., 0., 0, LP);
		
		tr.generatePath();
		g2.setColor(Color.blue);
		g2.draw(tr.gp);
		//g2.fill(tr.gp);
		

		/*
		//avoir les n-ieme px de la courbe
		Point2D.Double p_nieme = null;
		for (int i = 0 ; i<compteur; i++){
			p_nieme = tr.nextPoint();
		}
		compteur++;
		g2.setColor(Color.green);
		this.desine_point(g2, p_nieme, 10);

		//avoir les n-ieme+10 px de la courbe
		Point2D.Double p_nieme2 = null;
		for (int i = 0 ; i<10; i++){
			p_nieme2 = tr.nextPoint();
		}
		g2.setColor(Color.red);
		this.desine_point(g2, p_nieme2, 10);
		*/
		//=================================================
		//TEST de la version avec les segments séparés

		Point2D.Double p_nieme = null;
		for (int i = 0 ; i<compteur; i++){//629 = maximum du chemin
			//System.out.println("boucle next ="+i);
			p_nieme = tr.nextPointSegment();
		}
		compteur ++;
		System.out.println("tr.cur_seg = "+tr.cur_seg+"   tr.cur_point ="+tr.cur_point+"  tr.cur_seg_gp="+tr.cur_seg_gp);
		
		g2.setColor(Color.green);
		if (p_nieme != null){
			this.desine_point(g2, p_nieme, 10);
			g2.draw(tr.cur_seg_gp);
		}
		else 
			System.out.println("NOUS SOMMES à la fin de notre trait");
		
		
		
		//System.out.println("gp.contains(p) = "+tr.gp.intersects(p.getX(),p.getY(),1,1)+"  p =("+(int)p.getX()+","+(int)p.getY()+")");
		
		
	}
}

class testeTranslation{

	public static void main(String[] args) {

		JFrame f = new JFrame("teste");
		petiteToile t = new petiteToile();
		f.setSize(500, 500);
		f.getContentPane().add(t);
		f.repaint();
		f.setVisible(true);
		//boucle pour voir l'animation de façon détourné :

		while(true){
			//t.foo();
			f.repaint();
			try {
				//Thread.sleep((long) (1000));
				Thread.sleep((long) (100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
	}

}


/**
 ===========================================
 VERSION QUI MARCHE PAS JUSQU'AU point final
 ============================================
 
 public class Translation extends Animation {
	
	private ArrayList<Point2D.Double> listPoint;//va contenir la liste des points 
	public GeneralPath gp;//chemin/tracé de notre translation //TODO : temporairement public pour les testes
	private Point2D.Double start_point;
	private Point2D.Double end_point;
	public Point2D.Double cur_point;//point courrant
	public Point2D.Double pre_point;//precedent point
	private Direction cur_dir ;//direction 0 à 9 (voir pavé num)
	private ArrayList<Point2D.Double> listToutPoint;//list d'absolument tout les points (voir si c'est pas trop lourd pour l'acces mémoire)
	
	
	public Translation(double t_debut, double t_fin, int easing,ArrayList<Point2D.Double> listPoint) {
		super(t_debut, t_fin, easing, "translation");
		if(listPoint.size() <2){
			//TODO : lancer une exception à la place du message d'erreur
			//throw new ListPointException()
			//il faut détruire l'objet translation et recommencer
			System.out.println("listPoint.size() = "+listPoint.size());//DEBUG
			System.err.println("\n\nERREUR : Animation-Translation construite mais invalide car la taille listPoint est <2\n\n");
			this.listPoint = null;
		}else{
			this.listPoint = new ArrayList<Point2D.Double>(listPoint);
		}
		this.start_point = listPoint.get(0);
		this.end_point = listPoint.get(listPoint.size()-1);
		this.cur_point = null;
		
	}
	
	@Override
	public AffineTransform getAffineTransform(double t_courant) {
		

		
		return null;
	}
	
	
	**
	 * GeneralPath generatePath() :
	 * ----------------------------------------------------
	 * Génère le chemin "gp" à partir de la list de points "listPoint".
	 * Les points sont consiédés alternativement comme points de controle et points de passage.
	 * exemple :
	 * Le 1er point est un point de passage.
	 * Le 2 eme = point de controle.
	 * et le 3eme = point de passage.
	 * et ainsi de suite...
	 * Avec c'est 3 points nous générons un quadCurve "quad_to".
	 * 
	 * Si le nombre de points est paire le chemin ce terminera par une ligne droite "line_to".
	 * Sinon il ce termine en courbe quadCurve "quad_to".
	 * 
	 * Pour des raisons technique le Chemin "gp" est dessin avec le chemin de retour.
	 * Raison : le chemin "gp" est un GeneralPath (c'est un Shape).
	 * 			Pour parcourir le chemin nous utilisons la méthode intersec de "gp".
	 * 			Cette méthode retourne vrai si nous somme dans le Shape de "gp".
	 * 			Hors ce que nous voulons c'est la bordure du Shape "gp" et pas l'interieur.
	 * 			C'est pourquoi l'astuce est de re-parcourir le chemin à l'envers. Ainsi le Shape 
	 * 			"gp" ne se résume plus qu'à sa bordure.
	 * 
	 * @param listPoint
	 * @return
	 *
	public void generatePath(){
		
		this.gp = new GeneralPath();
		int limite = listPoint.size();
		boolean paire = true;
		//savoir si nous sommes paire ou impaire :
		if(listPoint.size()%2 == 1){
			paire=false;
		}
		//DESSIN CHEMIN :=====================
		//on place le premier point :
		gp.moveTo(this.start_point.x,this.start_point.y);
		System.out.println("DEBUG - CHEMIN  : moveTO p ("+start_point+")");
		//on fait des quadCurve pour les points (sauf le dernier si c'est paire):
		for(int i=1 ; limite!=2 && i < limite-1;i+=2){
			System.out.println("DEBUG - i = "+i);
			System.out.println("DEBUG - CHEMIN  :  quadTo ( p1("+listPoint.get(i).x+","+listPoint.get(i).y+")  p2 ("+listPoint.get(i+1).x+","+listPoint.get(i+1).y+") )");
			gp.quadTo(listPoint.get(i).x, listPoint.get(i).y, listPoint.get(i+1).x, listPoint.get(i+1).y);
		}
		//si nous sommes paire le dernier point est une ligne
		if (paire){
			gp.lineTo(listPoint.get(limite-1).x, listPoint.get(limite-1).y);
			System.out.println("DEBUG - CHEMIN  :lineTo p ("+listPoint.get(limite-1).x+","+listPoint.get(limite-1).y+")");
		}
		//DESSIN CHEMIN INVERSE:==============
		if (paire){
			limite -=1;
			System.out.println("DEBUG - CHEMIN-INVERSE : lineTo p ("+listPoint.get(limite-1).x+","+listPoint.get(limite-1).y+")");
			gp.lineTo(listPoint.get(limite-1).x, listPoint.get(limite-1).y);
		}
		
		limite-=2;
		
		for(int i=limite ; i >0 ;i-=2){
			System.out.println("DEBUG - CHEMIN-INVERSE : i ="+i);
			System.out.println("DEBUG - CHEMIN-INVERSE : quadTo ( p1("+listPoint.get(i).x+","+listPoint.get(i).y+"), p2 ("+listPoint.get(i-1).x+","+listPoint.get(i-1).y+") )");
			gp.quadTo(listPoint.get(i).x, listPoint.get(i).y, listPoint.get(i-1).x, listPoint.get(i-1).y);
		}
		gp.closePath();
		
	}
	
	
	**
	 * 
	 *  Prend en paramètre une liste de points retourne le GeneralPath correspondant.
	 *  Intéret on peux sélectionner des segments de chemins (3 points) par exemples
	 * @param LP
	 * @return
	 *
	private GeneralPath generatePath(ArrayList<Point2D.Double> LP){
		
		GeneralPath gp_seg = new GeneralPath();
		int limite = LP.size();
		boolean paire = true;
		//savoir si nous sommes paire ou impaire :
		if(LP.size()%2 == 1){
			paire=false;
		}
		//DESSIN CHEMIN :=====================
		//on place le premier point :
		gp_seg.moveTo(LP.get(0).x,LP.get(0).y);//LP.get(0) = point de départ

		System.out.println("DEBUG - CHEMIN  : moveTO p ("+LP.get(0).x+","+LP.get(0).y+")");
		//on fait des quadCurve pour les points (sauf le dernier si c'est paire):
		for(int i=1 ; limite!=2 && i < limite-1;i+=2){
			System.out.println("DEBUG - i = "+i);
			System.out.println("DEBUG - CHEMIN  :  quadTo ( p1("+LP.get(i).x+","+LP.get(i).y+")  p2 ("+LP.get(i+1).x+","+LP.get(i+1).y+") )");
			gp_seg.quadTo(LP.get(i).x, LP.get(i).y, LP.get(i+1).x, LP.get(i+1).y);
		}
		//si nous sommes paire le dernier point est une ligne
		if (paire){
			gp_seg.lineTo(LP.get(limite-1).x, LP.get(limite-1).y);
			System.out.println("DEBUG - CHEMIN  :lineTo p ("+LP.get(limite-1).x+","+LP.get(limite-1).y+")");
		}

		//DESSIN CHEMIN INVERSE:==============
		if (paire){
			limite -=1;
			System.out.println("DEBUG - CHEMIN-INVERSE : lineTo p ("+LP.get(limite-1).x+","+LP.get(limite-1).y+")");
			gp_seg.lineTo(LP.get(limite-1).x, LP.get(limite-1).y);
		}
		
		limite-=2;
		
		for(int i=limite ; i >0 ;i-=2){
			System.out.println("DEBUG - CHEMIN-INVERSE : i ="+i);
			System.out.println("DEBUG - CHEMIN-INVERSE : quadTo ( p1("+LP.get(i).x+","+LP.get(i).y+"), p2 ("+LP.get(i-1).x+","+LP.get(i-1).y+") )");
			gp_seg.quadTo(LP.get(i).x, LP.get(i).y, LP.get(i-1).x, LP.get(i-1).y);
			
			//repositionnement du point de fin courrant :
			cur_end_pt = pointVoisin(gp_seg,cur_end_pt);
		}
		gp_seg.closePath();


		
		return gp_seg;
	}
	
	**
	 * Point2D.Double nextPoint() :
	 * ----------------------------------------------------------------
	 * Parcours le GeneralPath "gp". 
	 * Cette méthode utilise "cur_point" comme point de départ et retourne le point voisin.
	 * Si elle trouve, "cur_point" est remplacé par le nouveau point.
	 * Si elle ne trouve pas de point suivant retourne null. Nous somme probablement au bout
	 * du chemin.
	 * Initialise "cur_point" et "cur_dir".
	 * @return
	 *
	//TODO : pour éviter des bug de superposition ne redessiner que la portion de chemin qui nous intéresse
	//+ détection de croisement
	public Point2D.Double nextPoint(){
		Point2D.Double nextPts ;
		Direction dir = Direction.NO_DIR;//copie de la direction courante
		
		//cas ou c'est notre premier passage :
		if(cur_point == null){
			cur_point = start_point;
			//initialisation de cur_dir :
			//cas particulier pour initialiser cur_dir il nous faut une direction de plus
			cur_dir = Direction.O;
			nextPts = cur_dir.CorespondPoint(cur_point);
			//TODO : éventuellement faire une méthode pour éviter la duplication de code
			if(this.gp.intersects(nextPts.getX(),nextPts.getY(),1,1)){
				this.cur_dir = dir;
				this.cur_point = nextPts;
				return nextPts;
			}
			else{
				this.cur_dir = Direction.E;
			}
		}
		
		//on explore tout les pixels environants sauf celui d'où l'on vien:
		for(int i=0;i<7;i++){
			//solution pour optimiser la recherche autour du point :
			switch(i){
			case 0: dir = cur_dir;break;								//mm dir que la précédente
			case 1: dir = cur_dir.nextDir();break;						//dir sens horaire +1
			case 2: dir = cur_dir.prevDir();break;						//dir sens anti-horaire +1
			case 3: dir = cur_dir.nextDir().nextDir();break;			//dir sens horaire +2
			case 4: dir = cur_dir.prevDir().prevDir();break;			//dir sens anti-horaire +2
			case 5: dir = cur_dir.nextDir().nextDir().nextDir();break;	//dir sens horaire +3
			case 6: dir = cur_dir.prevDir().prevDir().prevDir();break;	//dir sens anti-horaire +3
			}
			nextPts = dir.CorespondPoint(cur_point);
			if(this.gp.intersects(nextPts.getX(),nextPts.getY(),1,1)){
				this.cur_dir = dir;
				this.cur_point = nextPts;
				return nextPts;
			}
		}
		return null;//on a rien trouvé
	}
	
	
	**
	 * 
	 * version 2 pour les segments
	 * attention il faut tester si le point est le point d'arrivé du segment 
	 * sinon on boucle à l'infinit
	 * @param cur_pt
	 * @return
	 *
	public Point2D.Double nextPoint(GeneralPath seg_gp){
		Point2D.Double nextPts ;
		Direction dir = Direction.NO_DIR;//copie de la direction courante
		//initialisation :
		if(cur_dir == null){
			cur_dir = Direction.S;//on choisit arbitrairement une direction
		}
		//on explore tout les pixels environants sauf celui d'où l'on vien:
		for(int i=0;i<8;i++){
			//solution pour optimiser la recherche autour du point :
			switch(i){
			case 0: dir = cur_dir;break;								//mm dir que la précédente
			case 1: dir = cur_dir.nextDir();break;						//dir sens horaire +1
			case 2: dir = cur_dir.prevDir();break;						//dir sens anti-horaire +1
			case 3: dir = cur_dir.nextDir().nextDir();break;			//dir sens horaire +2
			case 4: dir = cur_dir.prevDir().prevDir();break;			//dir sens anti-horaire +2
			case 5: dir = cur_dir.nextDir().nextDir().nextDir();break;	//dir sens horaire +3
			case 6: dir = cur_dir.prevDir().prevDir().prevDir();break;	//dir sens anti-horaire +3
			case 7: dir = cur_dir.prevDir().prevDir().prevDir().prevDir();break;//cas particulier pour l'initialisation ce cas ne doit ce produire au maximum qu'une fois par semgent
			}
			nextPts = dir.CorespondPoint(cur_point);
			//System.out.println("DEBUG -nextPts ="+nextPts);
			if(seg_gp.intersects(nextPts.getX(),nextPts.getY(),1,1)){
				this.cur_dir = dir;
				cur_point = nextPts;
				return nextPts;
			}
		}
		return null;//on a rien trouvé 
	}
	
	
	
	
	**
	 * méthode qui retourne un point voisin au point passer en paramètre
	 * (cette méthode ne sera utilisé que pour déterminer les points d'arrivés et de fin)
	 * Pourquoi cette méthode : 
	 * Parceque les points d'arrivée de fin ne sont pas pris en compte 
	 * dans le chemin "gp". Or j'ai besoin que se teste marche.
	 * @param seg_gp
	 * @return
	 *
	private Point2D.Double pointVoisin(GeneralPath seg_gp,Point2D.Double pt){
		Point2D.Double nextPts ;
		Direction dir = Direction.S;//copie de la direction courante

		//on explore tout les pixels environants sauf celui d'où l'on vien:
		for(int i=0;i<8;i++){
			//solution pour optimiser la recherche autour du point :
			switch(i){
			case 0:break;								//mm dir que la précédente
			case 1: dir = dir.nextDir();break;						//dir sens horaire +1
			case 2: dir = dir.prevDir();break;						//dir sens anti-horaire +1
			case 3: dir = dir.nextDir().nextDir();break;			//dir sens horaire +2
			case 4: dir = dir.prevDir().prevDir();break;			//dir sens anti-horaire +2
			case 5: dir = dir.nextDir().nextDir().nextDir();break;	//dir sens horaire +3
			case 6: dir = dir.prevDir().prevDir().prevDir();break;	//dir sens anti-horaire +3
			case 7: dir = dir.prevDir().prevDir().prevDir().prevDir();break;//cas particulier pour l'initialisation ce cas ne doit ce produire au maximum qu'une fois par semgent
			}
			nextPts = dir.CorespondPoint(pt);
			//System.out.println("DEBUG -nextPts ="+nextPts);
			if(seg_gp.intersects(nextPts.getX(),nextPts.getY(),1,1)){
				return nextPts;
			}
		}
		return null;//on a rien trouvé 
	}
	
	
	
	
	public int cur_seg = -1;//segment courant //TODO : mettre en privée
	private int nb_seg = 0;//nombres de segments qui sont des quadCurves
	public GeneralPath cur_seg_gp = null;//le generalPath du segment courant //TODO : mettre en privée
	private boolean nb_points_paire = false;//pour savoir si nous finissons par une line
	private Point2D.Double cur_start_pt = null;//TODO : pas forcément utile probablement retirer si non utilisé
	private Point2D.Double cur_end_pt = null;//point courant de fin du segment
	
	**
	 * version avec les segments de chemins étudiers séparéments
	 * retourne le point suivant de
	 * @return
	 *
	public Point2D.Double nextPointSegment(){
		//initialisation de cur_seg et de nb_seg et cur_point:
		if(cur_seg == -1){
			int nb_points = listPoint.size();
			if (nb_points%2 == 0){
				nb_points_paire = true;
				nb_seg += 1;
				nb_points-=1;
			}
			nb_seg += nb_points/2;//division entière
			cur_seg = 0;
			this.cur_point = listPoint.get(0);//le point courrant est mis sur le point de départ
		}
		
		//initialisation de cur_seg_gp ,et des point de départ et de fin:
		if (this.cur_seg_gp == null){
			System.out.println("DEBUG -------nouveau seg gp--------");
			ArrayList<Point2D.Double> LP = new ArrayList<Point2D.Double>();
			//cas pour le dernier segment si c'est une ligne :
			if((cur_seg == this.nb_seg-1) && nb_points_paire){
				System.out.println("DEBUG - new seg line ");
				cur_start_pt = listPoint.get(listPoint.size()-2);
				cur_end_pt = listPoint.get(listPoint.size()-1);
				//System.out.println("DEBUG dans new seg line - cur_point ="+cur_point+"  end_pt = "+cur_end_pt+"  cur_seg="+cur_seg+"  cur_dir = "+cur_dir.getNomDir());
				LP.add(listPoint.get(listPoint.size()-2));//avant dernier point
				LP.add(listPoint.get(listPoint.size()-1));//denier point
				cur_seg_gp = generatePath(LP);
			}else{//quadCurve
				System.out.println("DEBUG - new seg curve ");
				cur_start_pt = listPoint.get((cur_seg*2)  );
				cur_end_pt = listPoint.get((cur_seg*2)+2);
				
				LP.add(  listPoint.get((cur_seg*2)  )  );
				LP.add(  listPoint.get((cur_seg*2)+1)  );
				LP.add(  listPoint.get((cur_seg*2)+2)  );
				
				System.out.println("DEBUG - p1 ="+cur_start_pt+", p2="+listPoint.get((cur_seg*2)+1)+", p3="+cur_end_pt);
				cur_seg_gp = generatePath(LP);
			}
		}
		//on recupère le point suivant dans le segment:
		pre_point = cur_point;
		cur_point = nextPoint(cur_seg_gp);
		System.out.println("DEBUG - cur_point ="+cur_point+"  end_pt = "+cur_end_pt+"  cur_seg="+cur_seg+"  cur_dir = "+cur_dir.getNomDir());
		//on regarde si on est à la fin du segment :
		//on va passer au segment suivant
		if( (cur_point.x == cur_end_pt.x) && (cur_point.y == cur_end_pt.y) || (pre_point == cur_point)){
			System.out.println("DEBUG - 1  dernier point courant ");
			//si nous somme au dernier segment retourne null
			if(cur_seg == nb_seg){
				System.out.println("DEBUG - 2 dernier point!!!!!!!!");
				return null;
			}
			//pour passer au segment suivant on va mettre cur_seg_gp à null et incrementer
			//cur_seg pour que à la prochaine utilisation on initialise une nouveau segment
			cur_seg_gp = null;
			
			cur_seg ++;
		}
		
		return this.cur_point;
	}
	
}
 
 
 */

