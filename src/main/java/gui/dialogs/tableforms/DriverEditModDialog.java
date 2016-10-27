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
 * Szablon obiektu okienka modyfikacji kierowcy
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class DriverEditModDialog extends DriverEditDialog {
   
   /**
    * Konstruktor
    * @param frame Ref. do GUI
    */
   public DriverEditModDialog(GUI frame) {
       
     super(frame, "Edycja kierowcy", true);  
       
   }    
   

    @Override
    protected boolean saveItem(Driver driver) {
   
       GlossDrivers glossDrivers =  new GlossDrivers(frame.getDatabase());                             
       boolean tmp = glossDrivers.updateItem(driver, frame.getUser());
       lastError = glossDrivers.getLastError();
       return tmp;

    }
    
  


    
}
