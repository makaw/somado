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
import somado.Somado;


/**
 *
 * Szablon obiektu wywołującego okienko dialogowe z informacją o błędzie
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class ErrorDialog extends InfoDialog {  
   
   
   /**
    * Konstruktor 
    * @param frame Referencja do GUI
    * @param error Treść komunikatu o błędzie
    * @param height Wysokość okienka 
    * @param logError Czy zapisywać błąd do logów
    */ 
   public ErrorDialog(GUI frame, String error, int height, boolean logError) {
       
      super(frame, DialogType.ERROR, error, height, true);
      
      // zapis logów
      if (logError)
      try {
        Somado.addLog(frame.getUser(), error);
      }
      catch (NullPointerException e) { System.err.println(error); }
                 
   }    
   
   /**
    * Konstruktor 
    * @param frame Referencja do GUI
    * @param error Treść komunikatu o błędzie
    * @param logError Czy zapisywać błąd do logów
    */ 
   public ErrorDialog(GUI frame, String error, boolean logError) {
       
     this(frame, error, 200, logError); 
            
   }    
   
   /**
    * Konstruktor
    * @param frame Referencja do GUI
    * @param error Treść komunikatu o błędzie
    */ 
   public ErrorDialog(GUI frame, String error) {
       
     this(frame, error, 200, false); 
            
   }    
      
   
   
}

