package affichage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class MyColorChooser extends JColorChooser{

	public MyColorChooser() {
		//modifier l'aperçu :
		this.setPreviewPanel(new MyPreviewPanel(this));
		//modifier le selecteur
		for(final AbstractColorChooserPanel accp : this.getChooserPanels()) {
			if(!accp.getDisplayName().equals("RVB")) {
				this.removeChooserPanel(accp);
			}
		}
	}

	public void setColor(int[] strokeColor) {
		this.setColor(strokeColor[0], strokeColor[1], strokeColor[2]);
		
	}

}



@SuppressWarnings("serial")
//code trouvé sur ce site : http://exampledepot.8waytrips.com/egs/javax.swing.colorchooser/CustPreview.html
//This preview panel simply displays the currently selected color.
class MyPreviewPanel extends JComponent {
// The currently selected color
Color curColor;

public MyPreviewPanel(JColorChooser chooser) {
   // Initialize the currently selected color
   curColor = chooser.getColor();

   // Add listener on model to detect changes to selected color
   ColorSelectionModel model = chooser.getSelectionModel();
   model.addChangeListener(new ChangeListener() {
       public void stateChanged(ChangeEvent evt) {
           ColorSelectionModel model = (ColorSelectionModel)evt.getSource();

           // Get the new color value
           curColor = model.getSelectedColor();
       }
   }) ;

   // Set a preferred size
   setPreferredSize(new Dimension(50, 50));
}

// Paint current color
public void paint(Graphics g) {
   g.setColor(curColor);
   g.fillRect(0, 0, getWidth()-1, getHeight()-1);
}
}