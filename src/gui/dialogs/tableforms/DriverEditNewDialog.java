/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs.tableforms;

import datamodel.Driver;
import datamodel.glossaries.GlossDrivers;
import gui.GUI;
import java.awt.Color;
import javax.swing.JPanel;


/**
 *
 * Szablon obiektu okienka dodawania nowego pojazdu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class DriverEditNewDialog extends DriverEditDialog {
    

   /**
    * Konstruktor
    * @param frame Ref. do GUI
    */ 
   public DriverEditNewDialog(GUI frame) {
       
     super(frame, "Nowy kierowca", false);  
       
   }    
   

    @Override
    protected boolean saveItem(Driver driver) {

       GlossDrivers glossDrivers =  new GlossDrivers(frame.getDatabaseShared());                             
       boolean tmp = glossDrivers.addItem(driver, frame.getUser());
       lastError = glossDrivers.getLastError();
       return tmp;       
    
    }
    
    
    
   /**
    * (Niezaimplementowana) Metoda zwraca panel z lista historii zmian
    * @param bgColor Kolor tla
    * @return Panel z lista szkolen
    */    
    @Override
    protected JPanel getAuditPanel(Color bgColor) {   
        
      return null;
        
    }     
    
    
}
