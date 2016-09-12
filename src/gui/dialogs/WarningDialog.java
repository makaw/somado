/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs;

import gui.GUI;


/**
 *
 * Szablon obiektu wywołującego okienko dialogowe z ostrzezeniem
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class WarningDialog extends InfoDialog {
    
    
   /**
    * Konstruktor 
    * @param frame Referencja do GUI
    * @param error Treść komunikatu o błędzie
    * @param height Wysokość okienka 
    */ 
   public WarningDialog(GUI frame, String error, int height) {
       
      super(frame, DialogType.WARNING, error, height, true);
            
   } 
   

}

