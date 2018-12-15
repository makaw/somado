/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package gui.dialogs;

import gui.ImageRes;
import somado.Lang;
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
           case INFO: return Lang.get("Dialogs.Info");
           case WARNING: return Lang.get("Dialogs.Warning");
           case ERROR: return Lang.get("Dialogs.Error");
           
       }  

     }     
     
     protected String getHeader() {
         
       switch (this) {
           
           default:
           case INFO: return "   ";
           case WARNING: return Lang.get("Dialogs.Info.Header") + ":          ";
           case ERROR: return Lang.get("Dialogs.Error.Header") + ":";
           
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

