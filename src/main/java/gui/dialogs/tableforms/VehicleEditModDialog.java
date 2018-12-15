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
import somado.Lang;


/**
 *
 * Szablon obiektu okienka modyfikacji pojazdu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class VehicleEditModDialog extends VehicleEditDialog {
   
   /**
    * Konstruktor
    * @param frame Ref. do GUI
    */ 
   public VehicleEditModDialog(GUI frame) {
       
     super(frame, Lang.get("Gloss.EditVehicle"), true);  
       
   }    
   

    @Override
    protected boolean saveItem(Vehicle vehicle) {
   
       GlossVehicles glossVehicles =  new GlossVehicles(frame.getDatabase());                             
       boolean tmp = glossVehicles.updateItem(vehicle, frame.getUser());
       lastError = glossVehicles.getLastError();
       return tmp;

    }
       

    
}
