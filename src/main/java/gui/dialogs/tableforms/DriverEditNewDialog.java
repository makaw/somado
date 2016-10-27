/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package gui.dialogs.tableforms;

import datamodel.Driver;
import datamodel.glossaries.GlossDrivers;
import gui.GUI;


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

       GlossDrivers glossDrivers =  new GlossDrivers(frame.getDatabase());                             
       boolean tmp = glossDrivers.addItem(driver, frame.getUser());
       lastError = glossDrivers.getLastError();
       return tmp;       
    
    }
    
    
    
    
}
