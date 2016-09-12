/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs;

import gui.ImageRes;
import gui.GUI;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


/**
 *
 * Szablon obiektu wywołującego informacyjne okienko dialogowe
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class InfoDialog extends SimpleInfoDialog {
        
    
   /** Klasa wewn. : typ okienka */ 
   protected enum DialogType { 
       
     INFO, WARNING, ERROR; 
   
     protected String getTitle() {
         
       switch (this) {
           
           default:
           case INFO: return "informacja";
           case WARNING: return "ostrze\u017cenie";
           case ERROR: return "b\u0142\u0105d";
           
       }  

     }     
     
     protected String getHeader() {
         
       switch (this) {
           
           default:
           case INFO: return "   ";
           case WARNING: return "Uwaga:          ";
           case ERROR: return "Wyst\u0105pi\u0142 b\u0142\u0105d:";
           
       }  

     }     
     
     
     protected Icon getIcon() {
         
       switch (this) {
           
           default:
           case INFO: return ImageRes.getIcon("info.png");
           case WARNING: return ImageRes.getIcon("warning.png");
           case ERROR: return  ImageRes.getIcon("error.png");
           
       }           
         
     }
        
   } 
    
   
   /**
    * Konstruktor 
    * @param frame Referencja do GUI
    * @param type Typ okna
    * @param info Tresc komunikatu
    * @param height Wysokość okienka 
    * @param scroll Czy ma byc przewijanie info
    */ 
   protected InfoDialog(GUI frame, DialogType type, String info, int height, boolean scroll) {
       
     super(frame, type.getTitle(), type.getHeader(), info, height, type.getIcon(), scroll);     
     super.showDialog(); 
            
   } 
   
   
   /**
    * Konstruktor 
    * @param frame Referencja do GUI
    * @param info Tresc komunikatu
    * @param height Wysokość okienka 
    */ 
   public InfoDialog(GUI frame, String info, int height) {
       
     this(frame, DialogType.INFO, info, height, false);
            
   }    
   
   
   /**
    * Konstruktor 
    * @param frame Referencja GUI
    * @param type Typ okna
    * @param info Tresc komunikatu
    */ 
   public InfoDialog(GUI frame, DialogType type, String info) {
       
     this(frame, type, info, 200, false); 
            
   }    

   
   /**
    * Dolny panel z przyciskami
    * @return Dolny panel
    */
   @Override
   protected JPanel buttonPanel() {
       
     // przycisk zamykający okienko
      JButton okButton = new CloseButton(" OK ");
      
      JPanel p = new JPanel();
      p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
      p.setOpaque(false);
      p.add(okButton);
      p.setBorder(new EmptyBorder(0, 0, 10, 0));
      
      return p;

   }
   
   
}

