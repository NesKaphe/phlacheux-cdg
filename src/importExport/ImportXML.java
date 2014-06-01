package importExport;

import java.awt.Color;
import java.awt.geom.Point2D;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import formes.Carre;
import formes.Cercle;
import formes.Croix;
import formes.Etoile;
import formes.Hexagone;
import formes.ObjetGeometrique;
import formes.Rectangle;
import formes.SegmentDroite;
import formes.Triangle;

import Animations.Animation;
import Animations.CompositeAnimation;
import Animations.FillColor;
import Animations.GestionAnimation;
import Animations.Rotation;
import Animations.StrokeColor;
import Animations.StrokeWidth;
import Animations.Translation;

public class ImportXML {

	private GestionAnimation gestionnaire;
	
	public ImportXML(GestionAnimation gestionnaire){
		this.gestionnaire = gestionnaire;
	}
	
	public void doImport() {
		JFileChooser chooser = new JFileChooser(new File("./"));
		int reponse = chooser.showOpenDialog(null);
		if(reponse == JFileChooser.APPROVE_OPTION) {
			File fileName = chooser.getSelectedFile();
			this.parse(fileName);
		}
	}
	
	private void parse(File f) {
		try {
		// création d'une fabrique de documents
		DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
		
		// création d'un constructeur de documents
		DocumentBuilder constructeur = fabrique.newDocumentBuilder();
		
		// lecture du contenu d'un fichier XML avec DOM
		Document document = constructeur.parse(f);
		
		//On recupère maintenant notre racine
		Element racine = document.getDocumentElement();
		
		NodeList liste =  racine.getElementsByTagName("Comportement");
		
		//On va parcourir tous nos comportements
		for(int i = 0; i < liste.getLength(); i++) {
			Element e = (Element) liste.item(i);
			
			NodeList listeFilsTotal = e.getChildNodes();
			Element obj = null;
			Element anim = null;
			int nbFils = 0;
			for (int j = 0; j < listeFilsTotal.getLength(); j++) {
				Node filsNode = listeFilsTotal.item(j);
				if(filsNode.getNodeType() == Node.ELEMENT_NODE) {
					Element filsElement = (Element) filsNode;
					if (!filsElement.getParentNode().getNodeName().equals("Comportement")) {
						continue;
					}
					else {
						if(filsElement.getTagName().equals("objGeo")) {
							nbFils++;
							NodeList nodesobj = filsElement.getChildNodes();
							int k = 0;
							while(k < nodesobj.getLength()) {
								if(nodesobj.item(k).getNodeType() == Node.ELEMENT_NODE) {
									obj = (Element) nodesobj.item(k);
									break;
								}
								k++;
							}
						}
						else if (filsElement.getTagName().equals("CompositeAnimation")) {
							nbFils++;
							anim = filsElement;
						}
					}
				}
			}
			//Chaque comportement n'a que deux fils
			System.out.println("nbfils  =   "+nbFils);
			if(nbFils  != 2) {
				throw new Exception("xml mal formé");
			}
			
			
			ObjetGeometrique geo = this.getObj(obj);
			Animation animation = this.getAnim(anim);
						
			
			this.gestionnaire.ajouterComportement(geo, animation);
		}
		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private ObjetGeometrique getObj(Element elem) {
		ObjetGeometrique geo = null;
		System.out.println(elem.getTagName());
		switch(elem.getTagName()) {
		case "Carre":
			Point2D.Double centreCarre = new Point2D.Double(Double.parseDouble(elem.getAttribute("centreX")),Double.parseDouble(elem.getAttribute("centreY")));
			double coteCarre = Double.parseDouble(elem.getAttribute("cote"));
			geo = new Carre(centreCarre, coteCarre);
			
			break;
		case "Cercle":
			Point2D.Double centreCercle = new Point2D.Double(Double.parseDouble(elem.getAttribute("centreX")),Double.parseDouble(elem.getAttribute("centreY")));
			double rayon = Double.parseDouble(elem.getAttribute("Rayon"));
			geo = new Cercle(centreCercle, rayon);
			
			break;
		case "Croix":
			Point2D.Double centreCroix = new Point2D.Double(Double.parseDouble(elem.getAttribute("centreX")),Double.parseDouble(elem.getAttribute("centreY")));
			int tailleCroix = Integer.parseInt(elem.getAttribute("taille"));
			geo = new Croix(centreCroix, tailleCroix);
			
			break;
		case "Etoile":
			Point2D.Double centreEtoile = new Point2D.Double(Double.parseDouble(elem.getAttribute("centreX")),Double.parseDouble(elem.getAttribute("centreY")));
			int tailleEtoile = Integer.parseInt(elem.getAttribute("taille"));
			geo = new Etoile(centreEtoile, tailleEtoile);
			
			break;
		case "Hexagone":
			Point2D.Double centreHexagone = new Point2D.Double(Double.parseDouble(elem.getAttribute("centreX")),Double.parseDouble(elem.getAttribute("centreY")));
			int tailleHexagone = Integer.parseInt(elem.getAttribute("taille"));
			geo = new Hexagone(centreHexagone, tailleHexagone);
			
			break;
		case "Rectangle":
			Point2D.Double centreRectangle = new Point2D.Double(Double.parseDouble(elem.getAttribute("centreX")),Double.parseDouble(elem.getAttribute("centreY")));
			double largeurRectangle = Double.parseDouble(elem.getAttribute("Width"));
			double longueurRectangle = Double.parseDouble(elem.getAttribute("Height"));
			geo = new Rectangle("Rectangle", centreRectangle, largeurRectangle, longueurRectangle);
			
			break;
		case "Segment":
			Point2D.Double p1Segment = new Point2D.Double(Double.parseDouble(elem.getAttribute("x1")),Double.parseDouble(elem.getAttribute("y1")));
			Point2D.Double p2Segment = new Point2D.Double(Double.parseDouble(elem.getAttribute("x2")),Double.parseDouble(elem.getAttribute("y2")));
			geo = new SegmentDroite(p1Segment, p2Segment);
			
			break;
		case "Triangle":
			Point2D.Double centreTriangle = new Point2D.Double(Double.parseDouble(elem.getAttribute("centreX")),Double.parseDouble(elem.getAttribute("centreY")));
			int tailleTriangle = Integer.parseInt(elem.getAttribute("taille"));
			geo = new Triangle(centreTriangle, tailleTriangle);
			
			break;
		default:
			return null;
		}
		
		NodeList l1 = elem.getElementsByTagName("Trait");
		NodeList l2 = elem.getElementsByTagName("Fond");
		
		//On va chercher la definition du trait
		Element trait = (Element) l1.item(0);
		int r = Integer.parseInt(trait.getAttribute("red"));
		int g = Integer.parseInt(trait.getAttribute("green"));
		int b = Integer.parseInt(trait.getAttribute("blue"));
		Color strokeColor = new Color(r,g,b);
		
		geo.setStrokeColor(strokeColor);
		geo.setStrokeWidth(Float.parseFloat(trait.getAttribute("epaisseur")));
		
		//On verifie qu'une couleur de fond est definie (cas particulier du segment)
		if(l2.getLength() > 0) {
			Element fond = (Element) l2.item(0);
			int fR = Integer.parseInt(fond.getAttribute("red"));
			int fG = Integer.parseInt(fond.getAttribute("green"));
			int fB = Integer.parseInt(fond.getAttribute("blue"));
			Color fillColor = new Color(fR,fG,fB);
			
			geo.setFillColor(fillColor);
		}

		return geo;
	}
	
	private Animation getAnim(Element elem) {
		Animation anim = null;
				
		switch(elem.getTagName()) {
		case "CompositeAnimation":
			CompositeAnimation composite = new CompositeAnimation(0., 0., 0);
			NodeList listeAnims = elem.getChildNodes();
			for(int i = 0; i < listeAnims.getLength(); i++) {
				if(listeAnims.item(i).getNodeType() == Node.ELEMENT_NODE)
					composite.add(this.getAnim((Element) listeAnims.item(i)));
			}
			composite.refreshTime();
			anim = composite;
			break;
		case "Rotation":
			double debutRotation = Double.parseDouble(elem.getAttribute("debut"));
			double finRotation = Double.parseDouble(elem.getAttribute("fin"));
			int easingRotation = Integer.parseInt(elem.getAttribute("easing"));
			double angleRotation = Math.toRadians(Double.parseDouble(elem.getAttribute("angle")));
			Point2D.Double centreRotation = new Point2D.Double(Double.parseDouble(elem.getAttribute("centreX")), Double.parseDouble(elem.getAttribute("centreY")));
			
			anim = new Rotation(debutRotation, finRotation, easingRotation, angleRotation, centreRotation);
			
			break;
		case "Translation":
			double debutTranslation = Double.parseDouble(elem.getAttribute("debut"));
			double finTranslation = Double.parseDouble(elem.getAttribute("fin"));
			int easingTranslation = Integer.parseInt(elem.getAttribute("easing"));
			
			//On va maintenant recuperer tous les points de la translation
			ArrayList<Point2D.Double> listePoints = new ArrayList<Point2D.Double>();
			NodeList liste = elem.getChildNodes();
			
			for(int i = 0; i < liste.getLength(); i++) {
				if(liste.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element p = (Element) liste.item(i);
					Point2D.Double point = new Point2D.Double(Double.parseDouble(p.getAttribute("x")), Double.parseDouble(p.getAttribute("y")));
					listePoints.add(point);
				}
			}
			
			anim = new Translation(debutTranslation, finTranslation, easingTranslation, listePoints);
			break;
		case "StrokeWidth":
			double debutStrokeWidth = Double.parseDouble(elem.getAttribute("debut"));
			double finStrokeWidth = Double.parseDouble(elem.getAttribute("fin"));
			int easingStrokeWidth = Integer.parseInt(elem.getAttribute("easing"));
			float incrWidth = Float.parseFloat(elem.getAttribute("incrWidth"));
			
			anim = new StrokeWidth(debutStrokeWidth, finStrokeWidth, easingStrokeWidth, incrWidth);
			break;
		case "StrokeColor":
			double debutStrokeColor = Double.parseDouble(elem.getAttribute("debut"));
			double finStrokeColor = Double.parseDouble(elem.getAttribute("fin"));
			int easingStrokeColor = Integer.parseInt(elem.getAttribute("easing"));
			
			int sR = Integer.parseInt(elem.getAttribute("incrR"));
			int sG = Integer.parseInt(elem.getAttribute("incrG"));
			int sB = Integer.parseInt(elem.getAttribute("incrB"));
			
			anim = new StrokeColor(debutStrokeColor, finStrokeColor, easingStrokeColor, sR, sG, sB);
			break;
		case "FillColor":
			double debutFillColor = Double.parseDouble(elem.getAttribute("debut"));
			double finFillColor = Double.parseDouble(elem.getAttribute("fin"));
			int easingFillColor = Integer.parseInt(elem.getAttribute("easing"));
			
			int fR = Integer.parseInt(elem.getAttribute("incrR"));
			int fG = Integer.parseInt(elem.getAttribute("incrG"));
			int fB = Integer.parseInt(elem.getAttribute("incrB"));
			
			anim = new FillColor(debutFillColor, finFillColor, easingFillColor, fR, fG, fB);
			break;
		default: 
			return null;
		}
		
		return anim;
	}
}
