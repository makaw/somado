/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package gui.dialogs.tableforms;

import datamodel.Vehicle;
import datamodel.glossaries.GlossVehicles;
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
public class VehicleEditNewDialog extends VehicleEditDialog {
    
   /**
    * Konstruktor
    * @param frame Ref. do GUI
    */ 
   public VehicleEditNewDialog(GUI frame) {
       
     super(frame, "Nowy pojazd", false);  
       
   }    
   

    @Override
    protected boolean saveItem(Vehicle vehicle) {

       GlossVehicles glossVehicles =  new GlossVehicles(frame.getDatabase());                             
       boolean tmp = glossVehicles.addItem(vehicle, frame.getUser());
       lastError = glossVehicles.getLastError();
       return tmp;       
    
    }
    
    
    
    
}
