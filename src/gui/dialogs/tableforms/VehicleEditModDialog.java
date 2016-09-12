/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs.tableforms;
 


import gui.dialogs.docpanels.DialogDocPanel;
import datamodel.Vehicle;
import datamodel.docs.DocAudit;
import datamodel.glossaries.GlossVehicles;
import gui.GUI;
import java.awt.Color;
import javax.swing.JPanel;


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
       
     super(frame, "Edycja pojazdu", true);  
       
   }    
   

    @Override
    protected boolean saveItem(Vehicle vehicle) {
   
       GlossVehicles glossVehicles =  new GlossVehicles(frame.getDatabaseShared());                             
       boolean tmp = glossVehicles.updateItem(vehicle, frame.getUser());
       lastError = glossVehicles.getLastError();
       return tmp;

    }
    
    
    
   /**
    * Metoda zwraca panel z lista historii zmian
    * @param bgColor Kolor tla
    * @return Panel z lista historii zmian
    */    
    @Override
    protected JPanel getAuditPanel(Color bgColor) {
        
      DocAudit docAudit = new DocAudit(frame.getDatabaseShared(), vehicle);

      return new DialogDocPanel<>(docAudit, bgColor);
        
        
    }        


    
}
