package affichage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import formes.ObjetGeometrique;
import formes.Rectangle;
import Animations.Animation;
import Animations.Comportement;
import Animations.CompositeAnimation;
import Animations.GestionAnimation;
import Animations.Rotation;
import Animations.Translation;

/**
 * Visionneuse d'animation (il faut l'ajouter au sud) f.getContentPane().add("VisionneuseAnimation",BorderLayout.SOUTH);
 * @author clement
 *
 */
public class VisionneuseAnimation extends JScrollPane{

	private JFrame f;
	private JPanel parentPan;//c'est le "viewport" de VisioneuseA. (contien tempo au nord et childPan au centre)
	private JPanel childPan;//va contenir tout les blocks correspondant au objgeo
	private Tempo tempo;//va afficher la tempo 0 10 20 30 ...
	private GestionAnimation GA;
	private int maxTime;//temps maximum que l'on va dessiner
	private ArrayList<BlockAnimation> listBlockA;
	private int cursorPosition;
	
	VisionneuseAnimation(JFrame f,GestionAnimation GA,double maxTime) {
		this.f = f;
		this.GA = GA;
		this.maxTime = (int)maxTime;
		this.tempo = new Tempo(this.maxTime);
		this.listBlockA = new ArrayList<BlockAnimation>();
		this.cursorPosition = 0;
		this.setOpaque(true);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		parentPan = new JPanel(new BorderLayout());//le panel qui va contenir la représentation de l'animation
		parentPan.setMinimumSize(new Dimension(this.maxTime,screenSize.height/10));
		parentPan.setPreferredSize(new Dimension(this.maxTime,screenSize.height/5));//donne la taille
		parentPan.setOpaque(true);
		
		childPan = new JPanel();
		childPan.setBackground(new Color(213,246,213));
		childPan.setLayout(new BoxLayout(childPan, BoxLayout.Y_AXIS));
		childPan.setOpaque(true);
		
		parentPan.add(childPan,BorderLayout.CENTER);
		parentPan.add(tempo,BorderLayout.NORTH);
		
		this.setPreferredSize(new Dimension(screenSize.width,screenSize.height/4));//va prendre 1/4  de la hauteur de l'écran
		this.setViewportView(parentPan);
		this.getVerticalScrollBar().addAdjustmentListener(new myAdjustmentListener(this));//ajout du ajustment listener
	}

	
	    
    /**
     * class myAdjustmentListener implements AdjustmentListener :
     * ---------------------------------------------------------
     * inner class 
     * inspiré de : http://www.coderanch.com/t/531943/java/java/trap-JScrollPane-event-scolled-perform
     * pour avoir initialiser "this.adjustmentListener"
     * @author clement
     *
     */
    class myAdjustmentListener implements AdjustmentListener{

    	private JScrollPane scrollPane;
    	private int oldVPos;
    	private int oldHPos;
    	
    	myAdjustmentListener(JScrollPane scrollPane){
    		this.scrollPane = scrollPane;
    		this.oldVPos = 0;
    		this.oldHPos = 0;
    	}
    	
		@Override
		/*
		 * pour repeindre le JscrollPane
		 */
		public void adjustmentValueChanged(AdjustmentEvent e) {
            int vPos = scrollPane.getVerticalScrollBar().getValue();
            int hPos = scrollPane.getHorizontalScrollBar().getValue();
            
	            if (e.getSource().equals(scrollPane.getVerticalScrollBar())   
	                    && vPos != oldVPos) {  
	                System.out.println("Vertical Scroll Bar changed position to "  
	                        + scrollPane.getVerticalScrollBar().getValue());  
	                oldVPos = vPos;
	                scrollPane.repaint();
	            }
	            if (e.getSource().equals(scrollPane.getHorizontalScrollBar())  
	                    && hPos != oldHPos) {  
	                System.out.println("Horizontal Scroll Bar changed position to "  
	                        + scrollPane.getHorizontalScrollBar().getValue());  
	                oldHPos = hPos;
	                scrollPane.repaint();
	            }
		}
    	
    }
	
	
	
	
	public void dessineAnimation(){
		HashMap<Integer, Comportement> listComp = GA.getListComportements();
		//pour faire ce foreach voir :
		// http://stackoverflow.com/questions/4234985/how-to-for-each-the-hashmap
		System.out.println("listComp = "+GA.getListComportements().size());
		childPan.removeAll();//on vide le child pan
		//on va retailler le childPan en fonction du nombres d'élément qu'il va contenir : 
		int hauteurChildPan = 0;
		for(Entry<Integer, Comportement> entry : listComp.entrySet()){
			Comportement c = entry.getValue();
			BlockAnimation ba = new BlockAnimation(c, this,cursorPosition, this.f);
			hauteurChildPan += ba.getTotalHeight();
			childPan.add(ba);
		}
		childPan.add(Box.createVerticalGlue());
		parentPan.setPreferredSize(new Dimension(maxTime,hauteurChildPan));
		this.revalidate();
		this.repaint();
	}
	
	
	/**
	 * pour changer la position du curseur
	 * @param position
	 */
	public void changeCursorPosition(int position){
		this.cursorPosition = position;
		this.dessineAnimation();
	}

	
	
	public int getMaxTime() {
		return maxTime;
	}
}










/**
 * class Tempo extends JPanel :
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
		int secondes = m / 30;
		while (m >= 0){
			m-=30;
			g2.drawString(Integer.toString(secondes--), m, 10);
		}
	}
}



















//TODO : tout les commentaires sont modifier pour la nouvelle version avec AnimAndShape

class BlockAnimation extends JPanel {
	private Comportement Comp;
	private CompositeAnimation CA;
	private int id;
	private ArrayList<Animation> la;//liste de toutes les animations
	private ArrayList<ArrayList<AnimAndShape>> lla;//chaque sous-liste sera un niveau pour les animations
	private VisionneuseAnimation parent;//pour connaitre son parent
	private int nbNiveaux;//nombres de niveau que blockAnimation va contenir sur chaque ces niveaux sera représenté les listes.
	private int levelSize;//hauteur d'un niveau en px
	private int firstSize;//hauteur du premier niveau en px
	private int totalHeight;//va contenir la hauteur totale 
	private boolean dessinOK = false;//pour savoir si la création de la list "lla" est fini
	private Color backgroundColor;
	private int cursorPos;//position du curseur
	private Rectangle2D.Double rectSelection;//ça va être le rectangle qui nous permet de représenté une animation sélectionné si null (aucune sélection)
	private boolean rectSelecOK;
	
	private BlockMouseListener bml;
	private JPopupMenu popupMenu; //menu pour le clic droit
	
	private JFrame f;
	
	BlockAnimation(Comportement C,VisionneuseAnimation parent,int cursorPos, JFrame f){
		this.Comp = C;
		this.CA = (CompositeAnimation) C.getAnimation();
		this.id = C.getId();
		this.parent = parent;
		this.la = CA.getAllChilds();
		this.lla = new ArrayList<ArrayList<AnimAndShape>>();
		this.cursorPos = cursorPos;
		creationLLA();

		bml = new BlockMouseListener(lla,this);
		this.addMouseListener(bml);
		this.addMouseMotionListener(bml);
		this.nbNiveaux = lla.size();
		this.levelSize = 30;
		this.firstSize = 20;
		this.rectSelection = new Rectangle2D.Double();
		this.rectSelecOK = false;
		
		this.f = f;
		
		//dessiner dans le panel
		this.totalHeight=((nbNiveaux+1)*(levelSize));//calcul de la hauteur du block (15 = hauteur d'un rectangle)
		
		this.setSize(parent.getMaxTime(), this.totalHeight);
		this.setPreferredSize(new Dimension(parent.getMaxTime(), this.totalHeight));
		
		//attribuer une couleur de font au block :
		if(Comp.getId() % 2 == 0)
			backgroundColor = Color.white;
		else
			backgroundColor = Color.gray;
		this.dessinOK = true;
		
		//Menu textuel du clic droit
		popupMenuListener popupListener = new popupMenuListener(this);
		popupMenu = new JPopupMenu();
		
		JMenuItem menuItem_modification = new JMenuItem("Modifier");
		menuItem_modification.setActionCommand("modification");
		menuItem_modification.addActionListener(popupListener);
		
		JMenuItem menuItem_Suppression = new JMenuItem("Suppression");
		menuItem_Suppression.setActionCommand("suppression");
		menuItem_Suppression.addActionListener(popupListener);
		popupMenu.add(menuItem_modification);
		popupMenu.add(menuItem_Suppression);
	}
	
	public JPopupMenu getPopupMenu() {
		return this.popupMenu;
	}
	
	/**
	 * creationLLA() :
	 * --------------------------------
	 * permet de remplir la liste "lla"
	 */
	public void creationLLA(){
		this.la = CA.getAllChilds();
		lla.removeAll(lla);
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
			System.out.println("DEBUG - insertionListe list_a = "+list_a.size());
			//création du niveau si il n'existe pas :
			try{
				lla.get(niveau);
			}catch (IndexOutOfBoundsException e){
				lla.add(new ArrayList<AnimAndShape>());
			}
			//si il n'y a pas de colision on insert dans le niveau actuel :
			if(detecteColision(lla.get(niveau), a)){
				notInsertList.add(a);
			}
			else{
				lla.get(niveau).add(new AnimAndShape(a,null));
			}
		}
		
		if (notInsertList.size() > 0){
			insertionListe(notInsertList, ++niveau);
		}
	}
	
	
	/**
	 * boolean detecteColision(ArrayList<Animation> list_a,Animation a):
	 * -----------------------------------------------------------------
	 * retourne vrai si il est possible d'ajouter "a" dans "list_a" sans 
	 * collision temporelle avec celle déjà présentent dans la "list_a".
	 * retourne vrai si il y a une colision 
	 * @param list_a
	 * @param a
	 * @return
	 */
	public boolean detecteColision(ArrayList<AnimAndShape> list_a,Animation a){
		if(list_a.size() == 0) return false;
		
		for (AnimAndShape aas : list_a){
			Animation sa = aas.getAnimation();
			if( !((a.getT_fin()<sa.getT_debut()) || (a.getT_debut()>sa.getT_fin()))  ){
				return true;
			}
		}
		return false;
	}
	
	
	
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		Graphics2D g2 = (Graphics2D)g;
		if(this.dessinOK){
			//dessiner un rectangle pour le font (setBackgroud avec opaque ne marche pas):
			g2.setColor(backgroundColor);
			g2.fillRect(0, 0, parent.getMaxTime(), parent.getHeight());
			
			//écrit le nom + id de l'objGeo:
			g2.setColor(Color.black);
			g2.drawString(Comp.toString(), 10, 15);
			
			int i = 1;
			//boucle sur le nombre de ligne de lla :
			for(ArrayList<AnimAndShape> list_a : lla){
				for (AnimAndShape aas : list_a){
					Animation a = aas.getAnimation();
					//donner une couleur au rectangle :
					Color couleurRect = this.getAnimationColor(a);
					/*
					if(a.getType().equals("rotation"))
						g2.setColor(Color.blue);
					if(a.getType().equals("translation"))
						g2.setColor(Color.red);
					*/
					g2.setColor(couleurRect);
					//dessine rectangle à la bonne position:
					aas.setShape(new Rectangle2D.Double(a.getT_debut(), i*(levelSize), a.getT_fin()-a.getT_debut(), 15));
					g2.fill(aas.getShape());//dessiner le rectangle
					//écrire le message de l'aniamtion : TODO : problème si l'animation est trop court (solution : écrire la chaine et faire une sous chaine si c'est trop long)
					g2.setColor(Color.yellow);
					g2.drawString(a.getType(),Math.round(a.getT_debut()), (i*levelSize)+13);//TODO : ajouter des informations (ex :rotaion + angle)
				}
				i++;
			}
			//afficher le rectangle selection :
			if(rectSelecOK){
				g2.setColor(Color.green);
				g2.draw(rectSelection);
			}
			//solution nule pour dessiner le curseur :(utiliser JpanelLayers)
			g2.setColor(Color.red);
			g2.fillRect(cursorPos, 0, 3, this.totalHeight*2);
		}
	}

	/**
	 * pour afficher un rectangle vert correspondant à une animation selectionner
	 * si r != null c'est ce rectangle qui sera dessiné
	 * si r est à null alors le rectangle disparait
	 * @param r
	 */
	public void setSelectionRect(Rectangle2D.Double r){
		if (r != null){
			this.rectSelection.setRect(r);
			rectSelecOK =true;
		}else{
			this.rectSelecOK = false;
		}
		this.revalidate();
		this.repaint();
	}
	
	
	public int getTotalHeight() {
		return totalHeight;
	}
	
	/**
	 * retourne la couleur correpsondante à l'animation
	 * passé en paramètre
	 * @param Animation a
	 * @return
	 */
	public Color getAnimationColor(Animation a){
		String type = a.getType();
		Color couleur = null;
		switch (type) {
		case "Rotation":
			couleur = Color.blue;
			break;
		case "Translation":
			couleur = Color.red;			
			break;
		case "StrokeWidth":
			couleur = Color.black;
			break;
		case "StrokeColor":
			couleur = Color.green;
			break;
		case "FillColor":
			couleur = Color.magenta;
			break;
		default:
			couleur = Color.pink;
		}
		
		return couleur;
	}
	
	public JFrame getFrame() {
		return this.f;
	}
	
	public int getCursorPos() {
		return this.cursorPos;
	}
	
	public Comportement getComp() {
		return this.Comp;
	}
	
	//inner class pour le popup listener
	class popupMenuListener implements ActionListener {

		BlockAnimation parent;
		
		public popupMenuListener(BlockAnimation parent) {
			this.parent = parent;
		}
		
		public void actionPerformed(ActionEvent e) {
			AnimAndShape anim = bml.getSelectedAnimAndShape();
			System.out.println(anim);
			switch(e.getActionCommand()) {
			case "suppression":
				if(anim != null) {
					//On va supprimer l'animation apres avoir demandé confirmation
					int reponse = JOptionPane.showConfirmDialog(null, "Voulez vous vraiment supprimer l'animation ?", "Suppression", JOptionPane.OK_CANCEL_OPTION);
					if(reponse == JOptionPane.OK_OPTION) {
						CA.remove(anim.getAnimation());
						parent.la = parent.CA.getAllChilds();
						parent.creationLLA();
						parent.repaint();	
					}
				}
				break;
			case "modification":
				if(anim != null) {
					//On va montrer l'alertbox pour modifier l'objet
					Animation changement = ModifAnimBox.createBoxAndModify(anim.getAnimation(), parent.getComp(), (double)parent.getCursorPos(), parent.getFrame());
					
					if(changement != null) {
						((CompositeAnimation)Comp.getAnimation()).remove(anim.getAnimation());
						((CompositeAnimation)Comp.getAnimation()).add(changement);
					}
					
					((CompositeAnimation)Comp.getAnimation()).refreshTime();
					parent.creationLLA();
					parent.repaint();
				}
				break;
			}
		}
		
	}
}






/**
 * mouse Listener du blockAnimation
 * @author clement
 *
 */
class  BlockMouseListener implements MouseListener,MouseMotionListener {
	
	private ArrayList<ArrayList<AnimAndShape>> lla;//liste pour savoir qui à été cliqué
	private AnimAndShape a;
	private BlockAnimation parent;
	private int oldPosX;
	private boolean startDrag;
	private Rectangle2D.Double rectSelec;
	private int changePeriode;//quand on fait un drag on veux connaitre ou on à déplacé notre animation
	
	public BlockMouseListener(ArrayList<ArrayList<AnimAndShape>> lla,BlockAnimation parent) {
		this.lla = lla;
		this.parent = parent;
		this.a = null;
		this.oldPosX = 0;
		this.rectSelec = new Rectangle2D.Double();
		this.startDrag = false;
		this.changePeriode = 0;
		
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		//on recherche l'animation qui a été cliquée :
		for(ArrayList<AnimAndShape> list_a : lla){
			for (AnimAndShape aas : list_a){
				if(aas.contains(e.getPoint())){
					//System.out.println("vous avez cliqué sur : id="+aas.getAnimation().getId()+" "+aas.getAnimation().getType()+" e.getClickCount()"+e.getClickCount());
					this.a = aas;
					if(e.isPopupTrigger()) {
						parent.getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
					}
					else {
						if(e.getClickCount() > 1){
							Animation anim = a.getAnimation();
							Animation changement = ModifAnimBox.createBoxAndModify(anim, parent.getComp(), (double)parent.getCursorPos(), parent.getFrame());
							
							if(changement != null) {
								((CompositeAnimation)parent.getComp().getAnimation()).remove(a.getAnimation());
								((CompositeAnimation)parent.getComp().getAnimation()).add(changement);
							}
							
							((CompositeAnimation)parent.getComp().getAnimation()).refreshTime();
							parent.creationLLA();
							parent.repaint();
						}
					}
				}
			}
		}
		this.startDrag = true;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("mouseReleased");
		if(e.isPopupTrigger()) {
			parent.getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
		}
		else {
			if(!parent.getPopupMenu().isShowing()) {
				System.out.println("Pas de menu");
				//on applique le changement de période de temps :
				if(changePeriode != 0){
					a.getAnimation().changePeriode(changePeriode);
					((CompositeAnimation)parent.getComp().getAnimation()).refreshTime();
					changePeriode = 0;//on remet à zero le période
					parent.creationLLA();
					parent.repaint();
				}
				this.a = null;
				this.startDrag =false;
				parent.setSelectionRect(null);//on n'affiche plus le rectangle selectionné
			}
		}
	}
	
	@Override
	/**
	 * pour déplacer une selection
	 */
	public void mouseDragged(MouseEvent e) {
		//Rectangle2D r = (Rectangle2D)a.getShape();
		//Mouse Dragged - dedans :
		
		if(this.a != null){
			//inialisation du point de départ du drag :
			if(startDrag){
				this.oldPosX = e.getX();
				startDrag = false;
			}
			Rectangle2D.Double r =(Rectangle2D.Double)a.getShape();
			rectSelec.setRect(r.x+(e.getX()-oldPosX),r.y,r.width,r.height);
			parent.setSelectionRect(this.rectSelec);
			System.out.println("e.getX()="+e.getX()+"  oldPosX="+oldPosX+"  r.x+(oldPosX-e.getX())="+(r.x+(e.getX()-oldPosX)));
			//modification de la période de notre animation:
			changePeriode = (e.getX()-oldPosX);
		}
	}

	public AnimAndShape getSelectedAnimAndShape() {
		return this.a;
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {}
	@Override
	
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

}


















/**
 * classe qui sera utilisé par BlockAnimation
 * L'intéret c'est d'unire une animation et le shape associer
 * @author clement
 *
 */
class AnimAndShape{
	private Animation a;
	private Shape sh;//se sera un rectangle
	
	public AnimAndShape(Animation a, Shape sh) {
		this.a = a;
		this.sh = sh;
	}

	public void setAnimation(Animation changement) {
		this.a = changement;
		
	}

	public Animation getAnimation() {
		return a;
	}

	public Shape getShape() {
		return sh;
	}
	
	public void setShape(Shape sh) {
		this.sh = sh;
	}

	/*
	 * pour savoir si le shape contien le point passé en paramètre
	 */
	public boolean contains(Point point){
		return sh.contains(point);
	}
}

class testeVisionAnim{
	public static void main(String[] args) {
		JFrame f= new JFrame("teste de visonneuse Animation");
		f.setPreferredSize(new Dimension(450,450));
		Toile t = new Toile(new Dimension(400,400));
		
		VisionneuseAnimation vi = null;
		GestionAnimation GA = new GestionAnimation(t);
		vi = new VisionneuseAnimation(f,GA,2000.0);
		

		
		
		f.getContentPane().add(t,BorderLayout.CENTER);
		f.getContentPane().add(vi,BorderLayout.SOUTH);

		f.pack();//ATTENTION à faire avant de faire GA.dessineToile() sinon bug (on sais pas trop pourquoi)!!!

		
		//dessiner un rectangle dans le GA :
		Rectangle rect = new Rectangle("rectangle", new Point2D.Double(100,200), 70, 40);
		rect.setStrokeWidth(2);
		rect.setStrokeColor(Color.green);
		rect.setFillColor(Color.cyan);
		Rotation rr1 = new Rotation(200., 500., 0, Math.toRadians(-120),rect.getCentre());
		Rotation rr2 = new Rotation(100., 300., 0, Math.toRadians(+20),rect.getCentre());
		
		
		//création des points
		Point2D.Double p1 = new Point2D.Double(30,30);
		Point2D.Double p2 = new Point2D.Double(260,160);
		ArrayList<Point2D.Double> LP = new ArrayList<Point2D.Double>();
		LP.add(p1);
		LP.add(p2);

		Translation tr = new Translation(20., 150., 0, LP);
		
		CompositeAnimation ca = new CompositeAnimation(0., 0., 0);
		ca.add(rr1);
		ca.add(rr2);
		ca.add(tr);
		GA.ajouterComportement(rect, ca);

		GA.dessinerToile(0.);
		System.out.println("GA.list ="+GA.getListComportements().size());
		System.out.println("GA.getListComportements() =\n\n"+GA.getListComportements()+"\n");
		vi.dessineAnimation();
		

		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
		
		int i= 0;
		while(i++<1000){
			vi.dessineAnimation();
			try {
				Thread.sleep((long) (100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			vi.changeCursorPosition(i);
		}
	}
}





