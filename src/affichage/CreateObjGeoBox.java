package affichage;

import java.awt.BorderLayout;
import java.awt.geom.Point2D;

import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import Animations.GestionAnimation;

import formes.Carre;
import formes.Cercle;
import formes.Croix;
import formes.Etoile;
import formes.Hexagone;
import formes.ObjetGeometrique;
import formes.Rectangle;
import formes.SegmentDroite;
import formes.Triangle;

public class CreateObjGeoBox{
	
	
	private GestionAnimation gestionnaire;
	private JColorChooser StrokeChooser;
	private JColorChooser FillChooser;
	private float Epaisseur = -1;
	private String type ;
	private ObjetGeometrique objGeo;//si l'objetGeometrique est à null nous somme en mode création
									//si l'objetGeo est non null nous sommes en mode modification
	private double default_taille ;
	private String nomBox;
	//consytructeur pour le mode création
	public CreateObjGeoBox(GestionAnimation gestionnaire,String type){
		System.out.println("INFO - constructeur création");
		this.gestionnaire = gestionnaire;
		this.type = type;
		this.objGeo = null;
		this.default_taille = 100;
		nomBox = "creation :"+type;
	}
	
	//constructeur pour le mode modification
	public CreateObjGeoBox(GestionAnimation gestionnaire,ObjetGeometrique objGeo){
		System.out.println("INFO - constructeur modif");
		this.gestionnaire = gestionnaire;
		this.type = objGeo.getNom();
		this.objGeo = objGeo;
		this.default_taille = 100;
		nomBox = "modification :"+type;
		//TODO : celui qui appel celle métode se charge de supprimer l'objet
		//si GenerateAndConfigureBox() retourne null on supprime l'ancien objGeo
	}

	private void initColorChooser(){
		StrokeChooser = new MyColorChooser();
		FillChooser = new MyColorChooser();
		JTextField Epaisseur = new JTextField();
		Epaisseur.setText("1");
	}
	
	/**
	 * premet de créer le colorChooser du trait et du font de la forme
	 * @return
	 */
	private JPanel CreateColorChooserPanel(){
		initColorChooser();
		JPanel panel_Colorchoosers = new JPanel(new BorderLayout());
		JPanel panel_stroke = new JPanel(new BorderLayout());
		JPanel panel_fill = new JPanel(new BorderLayout());
		JLabel stroke = new JLabel("Couleur de trait :");
		JLabel fill = new JLabel("Couleur de fond :");
		panel_stroke.add(stroke, BorderLayout.NORTH);
		panel_stroke.add(StrokeChooser, BorderLayout.CENTER);
		panel_fill.add(fill, BorderLayout.NORTH);
		panel_fill.add(FillChooser, BorderLayout.CENTER);
		panel_Colorchoosers.add(panel_stroke, BorderLayout.WEST);
		panel_Colorchoosers.add(panel_fill, BorderLayout.EAST);
		//si nous sommes en mode modification :
		if (objGeo != null){
			//préremplir les couleurs selectionnés :
			StrokeChooser.setColor(objGeo.getStrokeColor());
			FillChooser.setColor(objGeo.getFillColor());
		}
			
			
		return panel_Colorchoosers;
	}
	
	
	private boolean createBox(JPanel configuration){
	
		Object[] contenu = {
			    "", 
			    configuration,
			    "Choix des couleur",
			    CreateColorChooserPanel()
			};
			
			int option = JOptionPane.showConfirmDialog(null, contenu, nomBox, JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION) {
				try {
					objGeo.setStrokeColor(StrokeChooser.getColor());
					objGeo.setFillColor(FillChooser.getColor());
					objGeo.setStrokeWidth(Epaisseur);
					gestionnaire.setObjGeoEnCreation(objGeo);
				}
				catch(Exception e) {
					System.err.println("Erreur de saisie des champs :");
					return false;
				}
				
				return true;
			} 
			else {
			    System.out.println("Annulation");
			    return false;
			}
		
	}
	

	/**
	 * class createChamp extends JPanel :
	 * -----------------------------------------
	 * permet de créer un champ
	 * Composé d'un nom JLabel et d'un champs de saisie JTextField
	 * @author clement
	 *
	 */
	class CreateChamp extends JPanel{
		JTextField txtF = new JTextField();
		public CreateChamp( String nom_champ, double default_value) {
			super(new BorderLayout());
			txtF.setText(Double.toString(default_value));
			JLabel label_forme = new JLabel(nom_champ);
			this.add(label_forme, BorderLayout.NORTH);
			this.add(txtF, BorderLayout.CENTER);
		}
		public String getTextFieldText() {
			return txtF.getText();
		}
		public double Value() {
			return Double.parseDouble(getTextFieldText());
		}
	}
	
	/**
	 * si nous somme en mode création cette méthode retourne un objet géométrique
	 * partiellement construit. La position est mise à null;
	 * Si elle est en mode modification et retourne l'objet modifier.
	 * La position est la même qu'avant.
	 * @return
	 */
	public ObjetGeometrique GenerateAndConfigureBox(){
		JPanel config_forme = new JPanel(new BorderLayout());//va contenir toutes les conficguration
		CreateChamp champ_strokeW = new CreateChamp("Taille du trait :", 1);
		config_forme.add(champ_strokeW, BorderLayout.SOUTH);
		
		//TODO : il y a beaucoup de pseudo-redondance mais bon la flème de tout changer
		switch(type) {
			case "Cercle":
				CreateChamp champ_rayon;
				if(objGeo == null){//mode création
					champ_rayon = new CreateChamp("Diamètre :", default_taille);
					objGeo = new Cercle(new Point2D.Double(0,0),default_taille);
				}
				else//mode modification
					champ_rayon = new CreateChamp("Diamètre :", ((Cercle)objGeo).getRayon()*2);
				//ajout du champ dans le panel d'affichage
				config_forme.add(champ_rayon, BorderLayout.CENTER);
				if(createBox(config_forme)){
					((Cercle)objGeo).setRayon(champ_rayon.Value()/2);//modifrayon
					objGeo.setStrokeWidth((float)champ_strokeW.Value());//modif strokeW
				}else{
					return null;
				}
				break;
				
			case "Rectangle":
				CreateChamp champ_h,champ_l;
				if(this.objGeo == null){//mode création
					champ_h = new CreateChamp("Hauteur :", default_taille);
					champ_l = new CreateChamp("Largeur :", default_taille);
					objGeo = new Rectangle("rectangle", new Point2D.Double(0,0), default_taille, default_taille);//TODO : ce constructeur ne sera plus accèssible
				}
				else{//mode modification
					champ_h = new CreateChamp("Hauteur :", ((Rectangle)objGeo).getHeight());
					champ_l = new CreateChamp("Largeur :", ((Rectangle)objGeo).getWidth());
				}
				//ajout du champ dans le panel d'affichage
				config_forme.add(champ_h, BorderLayout.NORTH);
				config_forme.add(champ_l, BorderLayout.CENTER);
				if(createBox(config_forme)){
					((Rectangle)objGeo).setHeight(champ_h.Value());//modif hauteur
					((Rectangle)objGeo).setWidth(champ_l.Value());//modif largeur
					objGeo.setStrokeWidth((float)champ_strokeW.Value());//modif strokeW
				}else{
					return null;
				}
				break;
				
			case "Carre":
				CreateChamp champ_cote;
				if(this.objGeo == null){//mode création
					champ_cote = new CreateChamp("Taille coté :", default_taille);
					this.objGeo = new Carre( new Point2D.Double(0,0), default_taille);
				}
				else{//mode modification
					champ_cote = new CreateChamp("Taille coté :", ((Carre)objGeo).getcote());
				}
				//ajout du champ dans le panel d'affichage
				config_forme.add(champ_cote, BorderLayout.CENTER);
			champ_strokeW.Value();
				if(createBox(config_forme)){
					champ_cote.Value();
					((Carre)this.objGeo).setcote(champ_cote.Value());//modif cote
					objGeo.setStrokeWidth((float)champ_strokeW.Value());//modif strokeW
				}else{
					return null;
				}
				break;
				
			case "Triangle":
				CreateChamp champ_tt;
				if(this.objGeo == null){//mode création
					champ_tt = new CreateChamp("Taille :", default_taille);
					objGeo = new Triangle(new Point2D.Double(0,0), (int)default_taille);
				}else{//mode modification
					champ_tt = new CreateChamp("Taille :", ((Triangle)objGeo).getTaille());
				}
				//ajout du champ dans le panel d'affichage
				config_forme.add(champ_tt, BorderLayout.CENTER);
			champ_strokeW.Value();
				if(createBox(config_forme)){
					((Triangle)this.objGeo).setTaille(champ_tt.Value());
					objGeo.setStrokeWidth((float)champ_strokeW.Value());//modif strokeW
				}else{
					return null;
				}
				break;
				
			case "Segment":
				if(this.objGeo == null)//mode création
					this.objGeo = new SegmentDroite( new Point2D.Double(0,0), new Point2D.Double(0,0));
				if(createBox(config_forme)){
					objGeo.setStrokeWidth((float)champ_strokeW.Value());//modif strokeW
				}else{
					return null;
				}
				break;
				
			case "Etoile":
				CreateChamp champ_te;
				if(this.objGeo == null){//mode création
					champ_te = new CreateChamp("Taille :", default_taille);
					this.objGeo = new Etoile(new Point2D.Double(0,0), (int)default_taille);
					objGeo.setStrokeWidth((float)champ_strokeW.Value());//modif strokeW
				}else{//mode modification
					champ_te = new CreateChamp("Taille :", ((Etoile)objGeo).getTaille());
				}
				//ajout du champ dans le panel d'affichage
				config_forme.add(champ_te, BorderLayout.CENTER);
			champ_strokeW.Value();
				if(createBox(config_forme)){
					((Etoile)this.objGeo).setTaille(champ_te.Value()/2);
					objGeo.setStrokeWidth((float)champ_strokeW.Value());//modif strokeW
				}else{
					return null;
				}
				break;
				
			case "Croix":
				CreateChamp champ_cr;
				if(this.objGeo == null) { //mode création
					champ_cr = new CreateChamp("Taille :", default_taille);
					this.objGeo = new Croix(new Point2D.Double(0,0), (int)default_taille);
					objGeo.setStrokeWidth((float)champ_strokeW.Value());
				}
				else { //mode modification
					champ_cr = new CreateChamp("Taille :", ((Croix)objGeo).getTaille()*2);
				}
				//ajout du champ dans le panel d'affichage
				config_forme.add(champ_cr, BorderLayout.CENTER);
			champ_strokeW.Value();
				if(createBox(config_forme)){
					((Croix)this.objGeo).setTaille(champ_cr.Value()/2);
					objGeo.setStrokeWidth((float)champ_strokeW.Value());
				}
				else { //mode modification
					return null;
				}
				
			case "Hexagone":
				CreateChamp champ_he;
				if(this.objGeo == null) {
					champ_he = new CreateChamp("Taille :", default_taille);
					this.objGeo = new Hexagone(new Point2D.Double(0,0), (int)default_taille);
					objGeo.setStrokeWidth((float)champ_strokeW.Value());
				}
				else {
					champ_he = new CreateChamp("Taille :", ((Hexagone)objGeo).getTaille()*2);
				}
				//ajout du champ dans le panel d'affichage
				config_forme.add(champ_he, BorderLayout.CENTER);
			champ_strokeW.Value();
			if(createBox(config_forme)) {
				((Hexagone)this.objGeo).setTaille(champ_he.Value()/2);
				objGeo.setStrokeWidth((float)champ_strokeW.Value());
			}
			else { //mode modification
				return null;
			}
			default :
				System.out.println("Le type :\""+type+"\" n'a pas encore été implémenté CreateObjBox");
		}
		return this.objGeo;
	}
}