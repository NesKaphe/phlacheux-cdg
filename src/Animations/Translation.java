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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import formes.Rectangle;

import affichage.PointAndShape;
import affichage.Toile;

public class Translation extends Animation {
	
	private ArrayList<Point2D.Double> listPoint;//va contenir la liste des points 
	public GeneralPath chemin;//chemin/tracé de notre translation //TODO : temporairement public pour les testes
	private ArrayList<Point2D.Double> listToutPoint;//liste de tout les points sur le parcours (moins de calculs à faire mais prend de la mémoire)
	private int len = 0;//longeur totale en pixel du chemin
	private Point2D.Double start_pt;//point de départ de la translation
	
	//variables pour le calcul :=============================
	private Point2D.Double cur_point;//point courrant
	private int cur_point_num = -1;//numeros du point courant
	private Direction cur_dir ;//direction courante 
	private int cur_seg = -1;//segment courant 
	private int nb_seg = 0;//nombres de segments qui sont des quadCurves
	private GeneralPath cur_seg_gp = null;//le generalPath du segment courant
	private boolean nb_points_paire = false;//pour savoir si nous finissons par une line
	private Point2D.Double cur_start_pt = null;
	private Point2D.Double cur_end_pt = null;//point courant de fin du segment TODO : A supprimer
	private Set<Point2D.Double> cur_list_points;//pour savoir si nous avons des points en double dans un segment
	
	public Translation(double t_debut, double t_fin, int easing,ArrayList<Point2D.Double> listPoint) {
		super(t_debut, t_fin, easing, "Translation");
		if(listPoint.size() <2){
			//TODO : lancer une exception à la place du message d'erreur
			//throw new ListPointException()//il faut détruire l'objet translation et recommencer
			System.err.println("\n\nERREUR : Animation-Translation construite mais invalide car la taille listPoint est <2\n\n");
			this.listPoint = null;
			this.listToutPoint = null;
		}else{
			this.listPoint = new ArrayList<Point2D.Double>(listPoint);
		}
		this.start_pt = listPoint.get(0);
		this.cur_point = null;
		calculLen();
		generateListToutPoint();//prend de la mémoire
	}
	
	@Override
	/**
	 * Attention cette version utilise ListToutPoint qui prend de la mémoire
	 */
	public AffineTransform getAffineTransform(double t_courant) {
		double pu = this.getPourun(t_courant);
		//si pu est négatif c'est que notre temps courant n'est pas bon
		if (pu <0.)
			return null;
		double distance = (len-1)*pu;//distance que doit parcourir l'objetGeometrique à cette instant
		
		Point2D.Double pt = this.listToutPoint.get((int)distance);//position du point à cette instant
		AffineTransform at = new AffineTransform();
		//at.translate(-start_pt.x, -start_pt.y);
		at.translate(pt.x, pt.y);
		
		return at;
	}
	
	/**
	 * void calculLen():
	 * -------------------------------------
	 * calcul la longueur en pixel du chemin
	 * 
	 */
	private void calculLen(){
		len = 0;
		int i = 0;
		while(this.nextPoint()!=null){
			System.out.println(" i ="+i++ +"  pt_courant = "+this.cur_point);
			len++;
		}
		restartParcour();
	}
	
	/**
	 * void restartParcour():
	 * ------------------------------------
	 * pour recommencer le parcours du debut
	 * tout est remis à zéro
	 */
	private void restartParcour(){
		cur_seg = -1;//remise à zero du segment courant
		cur_point_num = -1;//ramu (remise a "moins un")
		nb_seg = 0;//raz
		cur_point = null;
		cur_dir = null;
		cur_seg_gp = null;
		nb_points_paire = false;
		cur_start_pt = null;
		cur_list_points = null;//permet de faire de la place en mémoire
	}
	
	
	private void generateListToutPoint(){
		this.listToutPoint = new ArrayList<Point2D.Double>();
		Point2D.Double nxt_pt;

		while((nxt_pt = this.nextPoint())!=null){
			
			this.listToutPoint.add(nxt_pt);
		}
		restartParcour();
	}
	
	/**
	 * GeneralPath generatePath() ://TODO : revoir commentaires
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
	 * 
	 * @param listPoint
	 * @return
	 */
	public void generatePath(){
		
		this.chemin = new GeneralPath();
		int limite = listPoint.size();
		boolean paire = true;
		//savoir si nous sommes paire ou impaire :
		if(listPoint.size()%2 == 1){
			paire=false;
		}
		//DESSIN CHEMIN :=====================
		//on place le premier point :
		chemin.moveTo(listPoint.get(0).x,listPoint.get(0).y);
		//on fait des quadCurve pour les points (sauf le dernier si c'est paire):
		for(int i=1 ; limite!=2 && i < limite-1;i+=2){
			chemin.quadTo(listPoint.get(i).x, listPoint.get(i).y, listPoint.get(i+1).x, listPoint.get(i+1).y);
		}
		//si nous sommes paire le dernier point est une ligne
		if (paire){
			chemin.lineTo(listPoint.get(limite-1).x, listPoint.get(limite-1).y);
		}
		//chemin.closePath();//ne pas fermer le chemin
	}
	
	/**
	 * cette version va nous permettre de dessiner le chemin à partir d'un liste de points
	 * retourne null si LP ne comporte pas au moins 2 points
	 * @param LP
	 * @return
	 */
	public static GeneralPath generatePath(ArrayList<PointAndShape> LP){
		int limite = LP.size();
		if (limite < 2){
			return null;
		}
		GeneralPath trajectoire = new GeneralPath();
		boolean paire = true;
		//savoir si nous sommes paire ou impaire :
		if(LP.size()%2 == 1){
			paire=false;
		}
		//DESSIN CHEMIN :=====================
		//on place le premier point :
		trajectoire.moveTo(LP.get(0).getPoint().x,LP.get(0).getPoint().y);
		//on fait des quadCurve pour les points (sauf le dernier si c'est paire):
		for(int i=1 ; limite!=2 && i < limite-1;i+=2){
			trajectoire.quadTo(LP.get(i).getPoint().x, LP.get(i).getPoint().y, LP.get(i+1).getPoint().x, LP.get(i+1).getPoint().y);
		}
		//si nous sommes paire le dernier point est une ligne
		if (paire){
			trajectoire.lineTo(LP.get(limite-1).getPoint().x, LP.get(limite-1).getPoint().y);
		}
		//chemin.closePath();//ne pas fermer le chemin
		
		return trajectoire;
	}
	
	/**
	 * GeneralPath generateSegmentPath(ArrayList<Point2D.Double> LP):
	 * ------------------------------------------------------------------------------
	 *  Prend en paramètre une liste de points retourne le GeneralPath correspondant.
	 *  Intéret on peux sélectionner des segments de chemins (3 points) par exemples
	 *  TODO : renommer generateSegmentPath
	 * @param LP
	 * @return
	 */
	private GeneralPath generateSegmentPath(ArrayList<Point2D.Double> LP){
		//erreur :
		if((LP.size()<2)&&(LP.size()>3)){
			System.err.println("ERREUR - il y n'y a pas le nombre de point demandé");
			return null;
		}
		GeneralPath gp_seg = new GeneralPath();
		//DESSIN CHEMIN(allé+retour) :=====================
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
	 * initCurDir(GeneralPath seg_gp,Point2D.Double pt):
	 * ------------------------------------------------------------------------------------
	 * initialise la direction courante grace au point passé en paramètre.
	 * 
	 * retourne faux si le point n'a pas de voisin et donc il n'y "cur_dir" n'est pas initialisé
	 * @param pt
	 * @return
	 */
	private boolean initCurDir(GeneralPath seg_gp,Point2D.Double pt){
		Point2D.Double nextPts ;
		Direction dir = Direction.S;//on attribut arbitrairement une direction

		//on explore tout les pixels environants sauf celui d'où l'on vien:
		for(int i=0;i<8;i++){
			dir = dir.nextDir();//direction suivante
			nextPts = dir.CorespondPoint(pt);
			//System.out.println("DEBUG -nextPts ="+nextPts+" dir ="+dir+" cas ="+i); //info debug
			if(seg_gp.intersects(nextPts.getX(),nextPts.getY(),1,1)){
				cur_dir = dir;
				return true;
			}
		}
		return false;//on a rien trouvé
	}
	
	
	
	/**
	 * Point2D.Double nextPointSegment(GeneralPath seg_gp) :
	 * ----------------------------------------------------------------
	 * Parcours le GeneralPath "gp". 
	 * Cette méthode utilise "cur_point" comme point de départ et retourne le point voisin.
	 * Si elle trouve, "cur_point" est remplacé par le nouveau point.
	 * Si elle ne trouve pas de point suivant retourne null. Nous somme probablement au bout
	 * du chemin.
	 * Initialise "cur_point" et "cur_dir".
	 * @return
	 */
	//TODO : faire détection de croisement de courbe

	private Point2D.Double nextPointSegment(GeneralPath seg_gp){
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
			case 6: dir = cur_dir.nextDir().nextDir().nextDir();break;	//dir sens anti-horaire +3
			}
			System.out.println("DEBUG - cur_dir ="+cur_dir);
			nextPts = dir.CorespondPoint(cur_point);
			System.out.println("DEBUG -nextPts ="+nextPts+"  cas ="+i);
			if(seg_gp.intersects(nextPts.getX(),nextPts.getY(),1,1)){
				this.cur_dir = dir;
				cur_point = nextPts;
				return nextPts;
			}
		}
		return null;//on a rien trouvé 
	}
	

	
	/**
	 * Point2D.Double nextPoint() :
	 * ------------------------------------------------
	 * retourne le point suivant du chemin
	 * null si nous avons atteint le bout du chemin
	 * Initialise "cur_point" et "cur_dir".
	 * @return
	 */
	private Point2D.Double nextPoint(){
		cur_point_num++;//on incremente le numero du point courant
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
			cur_point = listPoint.get(0);//le point courrant est mis sur le point de départ
		}
		
		//initialisation de cur_seg_gp ,et des point de départ et de fin:
		if (this.cur_seg_gp == null){
			System.out.println("DEBUG -------nouveau seg gp--------");
			ArrayList<Point2D.Double> LP = new ArrayList<Point2D.Double>();//va contenir les points du segment
			cur_list_points = new HashSet<Point2D.Double>();
			//cas pour le dernier segment si c'est une ligne :
			if((cur_seg == this.nb_seg-1) && nb_points_paire){
				cur_start_pt = listPoint.get(listPoint.size()-2);
				cur_end_pt = listPoint.get(listPoint.size()-1);
				cur_point = cur_start_pt;
				LP.add(listPoint.get(listPoint.size()-2));//avant dernier point
				LP.add(listPoint.get(listPoint.size()-1));//denier point
				cur_seg_gp = generateSegmentPath(LP);//généré le chemin
				boolean init_cur_dir = initCurDir(cur_seg_gp, cur_start_pt);//initiliser le "cur_dir"
				System.out.println("DEBUG init_cur_dir ="+init_cur_dir);
			}else{//quadCurve
				
				cur_start_pt = listPoint.get((cur_seg*2)  );
				cur_end_pt = listPoint.get((cur_seg*2)+2);
				cur_point = cur_start_pt;
				LP.add(  listPoint.get((cur_seg*2)  )  );
				LP.add(  listPoint.get((cur_seg*2)+1)  );
				LP.add(  listPoint.get((cur_seg*2)+2)  );
				cur_seg_gp = generateSegmentPath(LP);
				boolean init_cur_dir = initCurDir(cur_seg_gp, cur_start_pt);
				System.out.println("DEBUG init_cur_dir ="+init_cur_dir);
			}
		}
		//============================================
		System.out.println("DEBUG : cur_point = "+cur_point);

		//on recupère le point suivant dans le segment:
		Point2D.Double pre_point = cur_point;//sauvegarde du point précédent
		cur_point = nextPointSegment(cur_seg_gp);
		//on regarde si on est à la fin du segment :
		//si cur_point est à null c'est que le segment est fini
		//Astuce (moche) mais qui marche :
		//ou si on tente d'insérer un point doublon c'est que l'on retourne en arrière et c'est pas bon
		if( (cur_point == null) || (cur_list_points.add(cur_point)== false)){
			System.out.println("DEBUG - fin du segment ");
			//si nous somme au dernier segment retourne null
			if(cur_seg == nb_seg-1){
				System.out.println("DEBUG - dernier point!!!!!!!!");
				return null;
			}
			//pour passer au segment suivant on va mettre cur_seg_gp à null et incrementer
			//cur_seg pour que à la prochaine utilisation on initialise une nouveau segment
			cur_seg_gp = null;
			cur_seg ++;
			return pre_point;
		}
		
		return cur_point;
	}

	/**
	 * Point2D.Double prevPoint()://TODO jamais testé
	 * --------------------------------------------------------
	 * pour avoir le point precedent.
	 * version "sale" qui refait tout le parcours.
	 * utiliser listToutPoint si on veux quelque chose de rapide
	 * 
	 * @return
	 */
	private Point2D.Double prevPoint(){
		int point_num = this.cur_point_num;//copie du numeros de point
		//si nous somme au debut :
		if (point_num < 0)
			return null;
		restartParcour();
		Point2D.Double nxt_pt = null;
		for (int i=0;i<point_num-1;i++){
			nxt_pt = nextPoint();
		}
		return nxt_pt;
	}

	public Element toXml(Document domDocument) {
		Element elem = domDocument.createElement("Translation");
		
		elem.setAttribute("debut", this.getT_debut().toString());
		elem.setAttribute("fin", this.getT_fin().toString());
		elem.setAttribute("easing", String.valueOf(this.getEasing()));
		
		//On va maintenant a partir de la liste de point creer l'arborescence xml
		for(Point2D.Double p : this.listPoint) {
			Element elemFils = domDocument.createElement("Point");
			elemFils.setAttribute("x", String.valueOf(p.getX()));
			elemFils.setAttribute("y", String.valueOf(p.getY()));
			elem.appendChild(elemFils);
		}
		return elem;
	}
	
}













//==========================================================================================================
//==========================================================================================================
//===================EN DESSOUS PLEIN DE TESTES=============================================================
//==========================================================================================================
//==========================================================================================================





class testeTranslation{

	public static void main(String[] args) {

		/*
		 //autre teste :
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
		*/
		JFrame frame = new JFrame("Visionneuse");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toile t = new Toile(new Dimension(500,500));
		frame.getContentPane().add(t);
		frame.pack();
		frame.setVisible(true);
		GestionAnimation gest = new GestionAnimation(t);
		
		
		Rectangle rect = new Rectangle("monrectangle", new Point2D.Double(30,30), 70, 40);
		rect.setStrokeWidth(2);
		rect.setStrokeColor(Color.red);
		rect.setFillColor(Color.yellow);
		
		
		//création des points
		Point2D.Double p1 = new Point2D.Double(30,30);
		Point2D.Double p2 = new Point2D.Double(260,160);
		Point2D.Double p3 = new Point2D.Double(30,180);
		Point2D.Double p4 = new Point2D.Double(260,350);
		Point2D.Double p5 = new Point2D.Double(100,300);
		Point2D.Double p6 = new Point2D.Double(260,250);
		ArrayList<Point2D.Double> LP = new ArrayList<Point2D.Double>();
		LP.add(p1);
		LP.add(p2);
		LP.add(p3);
		LP.add(p4);
		LP.add(p5);
		LP.add(p6);
		
		//création de l'animation translation :
		Translation tr1 = new Translation(0., 500., 0, LP);
		//création d'une rotation :
		Rotation r1 = new Rotation(0., 500., 0, Math.toRadians(-720),rect.getCentre());
		
		CompositeAnimation ca = new CompositeAnimation(0., 500., 0);
		
		
		ca.add(tr1);
		ca.add(r1);
		
		gest.ajouterComportement(rect, ca);
		
		for(int j=0;j<10;j++)
		{
			for(double i=0.;i<500.;i+=3.){
				gest.dessinerToile(i);
				//System.out.println("main  i ="+i);
				try {
					//Thread.sleep((long) (1000));
					Thread.sleep((long) (50));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}



























//====pour le DEBUG 
//TODO : à retirer pour la version final
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
		//dessiner le "chemin"
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
		g2.draw(tr.chemin);

		

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
		/*
		Point2D.Double p_nieme = null;
		for (int i = 0 ; i<compteur; i++){//629 = maximum du chemin
			//System.out.println("boucle next ="+i);
			p_nieme = tr.nextPoint();
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
		
		
		*/
		//System.out.println("chemin.contains(p) = "+tr.chemin.intersects(p.getX(),p.getY(),1,1)+"  p =("+(int)p.getX()+","+(int)p.getY()+")");
		
		
	}
}

