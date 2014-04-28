package importExport;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import Animations.Comportement;
import Animations.GestionAnimation;

public class ExportXML {

	private GestionAnimation gestionnaire;
	
	private Document xml;
	
	public ExportXML(GestionAnimation gestionnaire) {
		this.gestionnaire = gestionnaire;
		this.xml = null;
	}
	
	public void doExport() {
		JFileChooser chooser = new JFileChooser(new File("./"));
		int reponse = chooser.showSaveDialog(null);
		if(reponse == JFileChooser.APPROVE_OPTION) {
			File fileName = new File(chooser.getSelectedFile()+".xml");
			if(fileName.exists())
		    {
		        reponse = JOptionPane.showConfirmDialog(null, "Remplacer ce fichier?");
		        // may need to check for cancel option as well
		        if (reponse == JOptionPane.NO_OPTION)
		            return;
		    }
		    //Ici, on peut finalement lancer notre export
		    this.doXml();
		    
		    try {
			    TransformerFactory tFactory = TransformerFactory.newInstance();
			    Transformer transformer = tFactory.newTransformer();
			    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			    
			    DOMSource source = new DOMSource(this.xml);
			    StreamResult result = new StreamResult(fileName);
			    transformer.transform(source, result);
		    }
		    catch(Exception e) {
		    	e.printStackTrace();
		    }
		    
		    this.xml = null; //Enregistrement terminé, on ne se souvient plus du xml en mémoire
		}
	}
	
	private void doXml() {
		try {
		DocumentBuilderFactory XML_Fabrique_Constructeur = DocumentBuilderFactory.newInstance();
		DocumentBuilder XML_Constructeur = XML_Fabrique_Constructeur.newDocumentBuilder();
 		
		Document XML_Document = XML_Constructeur.newDocument();
		HashMap<Integer, Comportement> comportements = this.gestionnaire.getListComportements();
		
		Element racine = XML_Document.createElement("Comportements");
		
		for(Entry<Integer, Comportement> entry : comportements.entrySet()) {
			Element fils = entry.getValue().toXml(XML_Document);
			
			racine.appendChild(fils);
		}
		
		XML_Document.appendChild(racine);
		
		this.xml = XML_Document;
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
