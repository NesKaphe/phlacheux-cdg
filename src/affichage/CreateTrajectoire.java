package affichage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import Animations.Comportement;
import Animations.CompositeAnimation;
import Animations.EasingFunction;
import Animations.Translation;

@SuppressWarnings("serial")
public class CreateTrajectoire extends JPanel {
	
	private Edition edition;
	private Comportement comp;
	private double tdeb,tfin;
	private int easing;
	private BufferedImage buff;
	private Toile toile;//pour sauvegarde la toile
	private ArrayList<PointAndShape> listPoint;
	private Point pointOrg;
	private MouseListener mouseListener;
	private ActionListener actionListener;
	private GeneralPath dessinTraj;
	private JToolBar menu;
	private JButton Terminer;
	private JButton Annuler;

	public CreateTrajectoire(Edition edition,Comportement comportement,double tempsDebut,double tempsFin, int easing) {
		super(new BorderLayout());
		this.edition = edition;
		this.comp = comportement;
		this.tdeb = tempsDebut;
		this.tfin = tempsFin;
		this.easing = easing;
		this.buff = edition.getGestionAnimation().getCopyBackBuffer();
		this.setPreferredSize(edition.getToile().getPreferredSize());
		this.listPoint = new ArrayList<PointAndShape>();
		//positionner le premier point ou le point d'origine :
		pointOrg = new Point();
		pointOrg.setLocation(comp.getObjGeo().getCentre());
		listPoint.add(new PointAndShape(pointOrg));
		//this.fin = new WaitUntilDeath(this);
		//fin.start();
		this.mouseListener = new CTMouseListener(listPoint,this);
		this.actionListener = new TrajListener(this); 
		this.dessinTraj = new GeneralPath();
		this.addMouseListener(mouseListener);

		initMenuBouton();
		
		//on échange la toile par notre JPanel :
		this.toile = edition.getToile();
		edition.remove(toile);
		edition.add(this, BorderLayout.CENTER);
		edition.validate();
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
		System.out.println("DEBUG -repaint trajectoire");
		Graphics2D g2 = (Graphics2D) g;
		//dessin de l'arrière plan :
		g2.drawImage(buff, null, 0, 0);
		
		//dessin des points :
		for (PointAndShape pas : listPoint){
			g2.setColor(Color.orange);
			g2.draw(pas.getRect());
			g2.setColor(Color.green);
			g2.fill(pas.getRect());
		}
		
		//dessin de du chemin :
		g2.setColor(Color.blue);
		g2.draw(dessinTraj);
		
	}
	
	
	
	/*
	 * retourne la liste de points au bon format pour générer
	 * la trajectoire
	 */
	public ArrayList<Point2D.Double> generateListPoint(){
		ArrayList<Point2D.Double> l = new ArrayList<Point2D.Double>();
		for (PointAndShape pas : listPoint){
			l.add(new Point2D.Double(pas.getPoint().x-pointOrg.x,pas.getPoint().y-pointOrg.y) );
		}
		return l;
	}
	
	/**
	 * pour générer le dessin de la trajectoire
	 */
	public void GenerateDessinTraj(){
		if (listPoint.size() > 1)
			this.dessinTraj = Translation.generatePath(listPoint);
	}

	
	/**
	 * quand l'utilisateur à terminer on ajoute l'animation au comportement passé en paramètre
	 */
	public void AjouterAnimation(){
		Translation tr = new Translation(tdeb, tfin, easing, generateListPoint());
		((CompositeAnimation)comp.getAnimation()).add(tr);
		edition.getVisionneuse().dessineAnimation();
		putBackToile();
	}
	
	/**
	 * pour remettre en place la toile
	 */
	public void putBackToile(){
		edition.remove(this);
		edition.add(toile,BorderLayout.CENTER);
		edition.validate();
		edition.repaint();
	}
	
	public void initMenuBouton(){
		Terminer = new JButton("Terminer");
		Terminer.setActionCommand("terminer");
		Terminer.addActionListener(actionListener);
		Annuler = new JButton("Annuler");
		Annuler.setActionCommand("annuler");
		Annuler.addActionListener(actionListener);
		menu = new JToolBar(SwingConstants.HORIZONTAL);
		menu.setFloatable(true);
		menu.setRollover(true);//??
		menu.add(Terminer);
		menu.add(Annuler);
		this.add(menu, BorderLayout.NORTH);// à voir 
	}
}









/**
 * listener de Create Trajectoire
 * @author clement
 *
 */
class CTMouseListener implements MouseListener,MouseMotionListener{
	
	private ArrayList<PointAndShape> list_pas;
	private CreateTrajectoire parent;
	private Point oldPos;
	private boolean startDrag;
	private boolean generatePoint;
	private Rectangle2D.Double rectSelec;
	
	public CTMouseListener(ArrayList<PointAndShape> list_pas,CreateTrajectoire parent) {
		this.list_pas = list_pas;
		this.parent = parent;
		this.startDrag = false;
		this.generatePoint =true;
	}
	
	@Override
	/**
	 * génère un point et l'ajoute dans la liste
	 * ou sinon c'est que nous voulons déplacer un point (drag)
	 * @param e
	 */
	public void mouseClicked(MouseEvent e) {
		System.out.println("mouseClicked");
		for(PointAndShape pas : list_pas){
			//si nous cliquons sur un point c'est que nous voulons le déplacer
			if(pas.contains(e.getPoint())){
				generatePoint = false;//TODO : nous voulons déplacer un point existant
			}
		}
		//si la recherche de point n'a pas aboutie nous ajoutons un nouveau point dans la liste
		if(generatePoint){
			System.out.println("generatePoint");
			list_pas.add(new PointAndShape(new Point(e.getPoint())));
			parent.GenerateDessinTraj();//pour regénérer le chemin
			parent.repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.generatePoint = true;//raz
		//TODO : à finir
		//this.startDrag = ??
	}

	@Override
	public void mouseDragged(MouseEvent e) {}
	@Override
	public void mouseMoved(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
}





/*
 * pour écouter les boutons Annuler et terminer
 * 
 */
class TrajListener implements ActionListener{
	
	private CreateTrajectoire parent;
	
	TrajListener(CreateTrajectoire parent){
		this.parent = parent;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "annuler":	
			parent.putBackToile();
			break;
			
		case "terminer":
			parent.AjouterAnimation();
			break;
		}
	}
}



