/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.formfields;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

 
  
/**
* Klasa szablonu wiersza formularza
* 
* @author Maciej Kawecki
* @version 1.0
* 
*/
@SuppressWarnings("serial")
public class FormRow extends JPanel {
        
   /** Etykieta opisująca pole */ 
   private final JLabel label;  
    
   /**
    * Konstruktor
    * @param labelTitle Treść etykiety opisującej pole
    * @param field Pole (komponent)
    */
   public FormRow(String labelTitle, Component field) {
           
      super();
      setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
      setOpaque(false);
        
      setBorder(new EmptyBorder(2, 0, 2, 0));
 
      if (labelTitle.isEmpty()) labelTitle = "        ";
      label = new JLabel(labelTitle);
      int lw = ((field instanceof IFormField) ? 135 : 140);
      label.setPreferredSize(new Dimension(lw, 20));
        
      add(label);
      add(field);
        
               

   } 
     
     
   public JLabel getLabel() {
         
     return label;  
         
   }
     
        
}  