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
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
* Klasa szablonu #2 wiersza formularza
* 
* @author Maciej Kawecki
* @version 1.0
*/
@SuppressWarnings("serial")
public class FormRowPad extends FormRow {
      
      
   /**
    * Konstruktor
    * @param labelTitle Treść etykiety opisującej pole
    * @param field Pole (komponent)
    * @param labelWidth Szerokość etykiety
    */    
   public FormRowPad(String labelTitle, Component field, int labelWidth) {
         
      super(labelTitle, field); 
        
      int lw = ((field instanceof IFormField) ? labelWidth : labelWidth + 5);
      super.getLabel().setPreferredSize(new Dimension(lw, 40));
      super.getLabel().setMaximumSize(new Dimension(lw, 40));
      
      if (!(field instanceof DateField)) {
        if (field instanceof JPanel)
          field.setMaximumSize(new Dimension(600, 150));
        else if (field instanceof JScrollPane)
          field.setMaximumSize(new Dimension(600, field.getPreferredSize().height));    
        else field.setMaximumSize(new Dimension(600, 26));     
      }
        
   }
  

   /**
    * Konstruktor
    * @param labelTitle Treść etykiety opisującej pole
    * @param field Pole (komponent)
    */
   public FormRowPad(String labelTitle, Component field) {
     
     this(labelTitle, field, 135);  
     
   }
     
        
}

   
