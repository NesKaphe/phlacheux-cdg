package affichage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import formes.ObjetGeometrique;
import formes.Rectangle;
import Animations.Animation;
import Animations.Comportement;
import Animations.CompositeAnimation;
import Animations.GestionAnimation;
import Animations.Rotation;

/**
 * Visionneuse d'animation (il faut l'ajouter au sud) f.getContentPane().add("VisionneuseAnimation",BorderLayout.SOUTH);
 * @author clement
 *
 */
public class VisionneuseAnimation extends JScrollPane{

	private JFrame f;
	private JPanel parentPan;
	private JPanel childPan;
	private Tempo tempo;
	private GestionAnimation GA;
	private int maxTime;
	private ArrayList<BlockAnimation> listBlockA;
	
	VisionneuseAnimation(JFrame f,GestionAnimation GA,double maxTime) {
		this.f = f;
		this.GA = GA;
		this.maxTime = (int)maxTime;
		this.tempo = new Tempo(this.maxTime);
		this.listBlockA = new ArrayList<BlockAnimation>();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		parentPan = new JPanel(new BorderLayout());//le panel qui va contenir la représentation de l'animation
		parentPan.setMinimumSize(new Dimension(this.maxTime,screenSize.height/10));//TODO : réfléchir au maxTime
		parentPan.setPreferredSize(new Dimension(this.maxTime,screenSize.height/5));
		//parentPan.setBackground(Color.gray);
		
		childPan = new JPanel();
		childPan.setBackground(new Color(213,246,213));
		childPan.setLayout(new BoxLayout(childPan, BoxLayout.Y_AXIS));
		
		/*
		//ajoute pour tester :=====================================
		//1
		JPanel litelChild1 = new JPanel();
		litelChild1.setSize(new Dimension(200,50));
		litelChild1.setBackground(Color.cyan);
		childPan.add(litelChild1);
		//2
		JPanel litelChild2 = new JPanel();
		litelChild2.setSize(new Dimension(200,50));
		litelChild2.setBackground(Color.blue);
		childPan.add(litelChild2);
		//3
		JPanel litelChild3 = new JPanel();
		litelChild3.setSize(new Dimension(200,50));
		litelChild3.setBackground(Color.green);
		childPan.add(litelChild3);
		//===========================================================
		*/

		
		parentPan.add(childPan,BorderLayout.CENTER);
		parentPan.add(tempo,BorderLayout.NORTH);
		
		this.setPreferredSize(new Dimension(screenSize.width,screenSize.height/4));//va prendre 1/4  de la hauteur de l'écran
		this.setViewportView(parentPan);
		
	}

	
	public void dessineAnimation(){
		HashMap<Integer, Comportement> listComp = GA.getListComportements();
		//pour faire ce foreach voir :
		//http://stackoverflow.com/questions/4234985/how-to-for-each-the-hashmap
		System.out.println("listComp = "+GA.getListComportements().size());
		for(Entry<Integer, Comportement> entry : listComp.entrySet()){
			Comportement c = entry.getValue();
			//listBlockA.add(new BlockAnimation(c, this));//finalement pas besoin de ça
			childPan.add(new BlockAnimation(c, this));
		}
		
		
	}
	
	public void paintComponent(Graphics g){
		//Graphics2D g2 = (Graphics2D)g;
		//dessineAnimation();
	}
	
}




/**
 * pour afficher les chiffres temporelles
 * @author clement
 *
 */
class Tempo extends JPanel{
	private int maxTime ;
	Tempo(int maxTime){
		this.maxTime = maxTime;
		this.setSize(new Dimension(maxTime,20));
		this.setBackground(Color.red);
		
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.drawString("0", 0, 10);//dessiner le zero
		int m = this.maxTime;
		while (m > 0){
			g2.drawString(Integer.toString(m), m, 10);
			m-=50;
		}
	}
	
	
}




class BlockAnimation extends JPanel{
	private ObjetGeometrique objGeo;
	private CompositeAnimation CA;
	private int id;
	private ArrayList<Animation> la;
	private ArrayList<ArrayList<Animation>> lla;//chaque sous-liste sera un niveau pour les animations
	private VisionneuseAnimation parent;//pour connaitre son parent
	private int nbNiveaux;//nombres de niveau que blockAnimation va contenir sur chaque ces niveaux sera représenté les listes.
	private int levelSize;
	private int firstSize;
	private boolean dessinOK = false;//pour savoir si la création de la list "lla" est fini
	
	BlockAnimation(Comportement C,VisionneuseAnimation parent){
		this.objGeo = C.getObjGeo();
		this.CA = (CompositeAnimation) C.getAnimation();
		this.id = C.getId();
		this.parent = parent;
		this.la = CA.getAllChilds();
		this.lla = new ArrayList<ArrayList<Animation>>();

		creationLLA();
		
		this.nbNiveaux = lla.size();
		this.levelSize = 30;
		this.firstSize = 20;
		this.setSize(parent.getSize().width, (nbNiveaux * levelSize) + firstSize);
		
		//attribuer une couleur par type d'animations
		//dessiner dans le panel
		this.dessinOK = true;
	}
	
	/**
	 * creationLLA() :
	 * --------------------------------
	 * permet de remplir la liste "lla"
	 */
	public void creationLLA(){
		insertionListe(la,0);
	}
	
	/**
	 * void insertionListe(ArrayList<Animation> list_a,int niveau):
	 * ------------------------------------------------------------------------
	 * prend en paramètre la liste de ce qu'il faut insérer
	 * dans la liste "lla", le paramètre niveau correspond à la sous-liste dans laquelle
	 * on va insérer les éléments.
	 * L'objectif étant de n'avoir aucunes "colisions temporelles" par niveaux. 
	 * Autrement dit il n'y a pas de d'intervalle temps qui se super-posent dans le 
	 * même niveau.
	 * Cette méthode est récurssive et crée automatiquement les niveaux 
	 * sous-liste de "lla" si il y a besoin.
	 * 
	 * @param list_a
	 * @return
	 */
	public void insertionListe(ArrayList<Animation> list_a,int niveau){
		System.out.println("DEBUG ni = "+niveau);
		ArrayList<Animation> notInsertList = new ArrayList<Animation>();//liste des animations qui n'ont pas pus être inséré
		
		for (Animation a : list_a){
			
			//création du niveau si il n'existe pas :
			try{
				lla.get(niveau);
			}catch (IndexOutOfBoundsException e){
				lla.add(new ArrayList<Animation>());
			}
			//si il n'y a pas de colision on insert dans le niveau actuel :
			if(detecteColision(lla.get(niveau), a)){
				
				System.out.println("DEBUG - detecteColision = true");

				lla.get(niveau).add(a);
			}
			else{
				System.out.println("DEBUG - detecteColision = false");
				notInsertList.add(a);
			}
				
		}
		
		if (notInsertList.size() > 0){
			System.out.println("DEBUG notInsertList = "+notInsertList);
			insertionListe(notInsertList, niveau++);
		}
	}
	
	
	/**
	 * boolean detecteColision(ArrayList<Animation> list_a,Animation a):
	 * -----------------------------------------------------------------
	 * retourne vrai si il est possible d'ajouter "a" dans "list_a" sans 
	 * collision temporelle avec celle déjà présentent dans la "list_a".
	 * retourne faux si impossible.
	 * @param list_a
	 * @param a
	 * @return
	 */
	public boolean detecteColision(ArrayList<Animation> list_a,Animation a){
		if(list_a.size() == 0) return true;
		
		for (Animation sa : list_a){
			if((a.getT_debut()<sa.getT_fin()) || (a.getT_fin()>sa.getT_debut())){
				return false;
			}
		}
		return true;
	}
	
	
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		if(this.dessinOK){			
			g2.drawString(objGeo.getNom()+" "+id, 10, 15);//écrit le nom + id de l'objGeo
			//boucle psur le nombre de ligne de lla
			int i = 1;
			for(ArrayList<Animation> list_a : lla){
				for (Animation a : list_a){
					//dessine rectangle à la bonne position:
					g2.setColor(Color.blue);//donner une couleur au rectangle
					g2.fill(new Rectangle2D.Double(a.getT_debut(), i*(levelSize-10), a.getT_fin()-a.getT_debut(), 15));

				}
				i++;
			}
		}
	}
	
}





class testeVisionAnim{
	public static void main(String[] args) {
		JFrame f= new JFrame("teste de visonneuse Animation");
		f.setPreferredSize(new Dimension(450,450));
		Toile t = new Toile(new Dimension(400,400));
		
		
		GestionAnimation GA = new GestionAnimation(t);
		VisionneuseAnimation vi = new VisionneuseAnimation(f,GA,2000.0);
		

		
		
		f.getContentPane().add(t,BorderLayout.CENTER);
		f.getContentPane().add(vi,BorderLayout.SOUTH);

		f.pack();//ATTENTION à faire avant de faire GA.dessineToile() sinon bug (on sais pas trop pourquoi)!!!

		
		//dessiner un rectangle dans le GA :
		Rectangle rect = new Rectangle("rectangle", new Point2D.Double(100,200), 70, 40);
		rect.setStrokeWidth(2);
		rect.setStrokeColor(Color.green);
		rect.setFillColor(Color.cyan);
		Rotation rr1 = new Rotation(200., 500., 0, Math.toRadians(-120),rect.getCentre());
		GA.ajouterComportement(rect, rr1);
		GA.dessinerToile(0.);
		System.out.println("GA.list ="+GA.getListComportements().size());
		vi.dessineAnimation();
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);

	}
}





