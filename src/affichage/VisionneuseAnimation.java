package affichage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
	//private ArrayList<JPanel> 
	
	VisionneuseAnimation(JFrame f,GestionAnimation GA,double maxTime) {
		this.f = f;
		this.GA = GA;
		this.maxTime = (int)maxTime;
		this.tempo = new Tempo(this.maxTime);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		parentPan = new JPanel(new BorderLayout());//le panel qui va contenir la représentation de l'animation
		parentPan.setMinimumSize(new Dimension(this.maxTime,screenSize.height/10));//TODO : réfléchir au maxTime
		parentPan.setPreferredSize(new Dimension(this.maxTime,screenSize.height/5));
		parentPan.setBackground(Color.gray);
		
		childPan = new JPanel();										
		childPan.setLayout(new BoxLayout(childPan, BoxLayout.Y_AXIS));
		
		//ajoute pour tester :
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
		//2
		JPanel litelChild3 = new JPanel();
		litelChild3.setSize(new Dimension(200,50));
		litelChild3.setBackground(Color.green);
		childPan.add(litelChild3);
		
		
		//f.getContentPane().add(childPan,BorderLayout.CENTER);
		parentPan.add(childPan,BorderLayout.CENTER);
		parentPan.add(tempo,BorderLayout.NORTH);
		
		this.setPreferredSize(new Dimension(screenSize.width,screenSize.height/4));//va prendre 1/4  de la hauteur de l'écran
		this.setViewportView(parentPan);
	}

	public void dessineAnimation(){
		HashMap<Integer, Comportement> listComp = GA.getListComportements();
		//pour faire ce foreach voir :
		//http://stackoverflow.com/questions/4234985/how-to-for-each-the-hashmap
		for(Entry<Integer, Comportement> entry : listComp.entrySet()){
			Comportement c = entry.getValue();
			CompositeAnimation a = (CompositeAnimation) c.getAnimation();
			a.getT_debut();
			a.getT_fin();
		}
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
	
	
	
}


class blockAnimation extends JPanel{

	private CompositeAnimation CA;
	private ArrayList<Animation> la;
	private ArrayList<ArrayList<Animation>> lla;//chaque sous-liste sera un niveau pour les animations
	private VisionneuseAnimation parent;
	private int nbNiveaux;//nombres de niveau que blockAnimation va contenir sur chaque ces niveaux sera représenté les listes.
	
	blockAnimation(CompositeAnimation CA,VisionneuseAnimation parent){
		
		this.CA = CA;
		this.parent = parent;
		la = CA.getAllChilds();
		lla = new ArrayList<ArrayList<Animation>>();
		creationLLA();
		
		//attribuer une couleur par type d'animations
		//dessiner dans le panel
		
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
		
		ArrayList<Animation> notInsertList = new ArrayList<Animation>();//liste des animations qui n'ont pas pus être inséré
		
		for (Animation a : list_a){
			//si il n'y a pas de colision on insert dans le niveau actuel :
			if(detecteColision(list_a, a)){
				//création du niveau si il n'existe pas :
				try{
					lla.get(niveau);
				}catch (IndexOutOfBoundsException e){
					lla.add(new ArrayList<Animation>());
				}
				lla.get(niveau).add(a);
			}
			else{
				notInsertList.add(a);
			}
				
		}
		
		if (notInsertList.size() > 0)
			insertionListe(notInsertList, niveau++);
		
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
		//if(list_a == null) return true;
		
		for (Animation sa : list_a){
			if((a.getT_debut()<sa.getT_fin()) || (a.getT_fin()>sa.getT_debut())){
				return false;
			}
		}
		return true;
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
		Rectangle rect = new Rectangle("monrectangle", new Point2D.Double(100,200), 70, 40);
		rect.setStrokeWidth(2);
		rect.setStrokeColor(Color.green);
		rect.setFillColor(Color.cyan);
		Rotation rr1 = new Rotation(0., 125., 0, Math.toRadians(-120),rect.getCentre());
		GA.ajouterComportement(rect, rr1);
		GA.dessinerToile(0.);
		
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);

	}
}





