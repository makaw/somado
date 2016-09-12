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
import gui.dialogs.docpanels.DialogDocPanel;
import datamodel.docs.DocAudit;
import datamodel.glossaries.GlossDrivers;
import gui.GUI;
import java.awt.Color;
import javax.swing.JPanel;


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
   
       GlossDrivers glossDrivers =  new GlossDrivers(frame.getDatabaseShared());                             
       boolean tmp = glossDrivers.updateItem(driver, frame.getUser());
       lastError = glossDrivers.getLastError();
       return tmp;

    }
    
    
    
   /**
    * Metoda zwraca panel z lista historii zmian
    * @param bgColor Kolor tla
    * @return Panel z lista historii zmian
    */    
    @Override
    protected JPanel getAuditPanel(Color bgColor) {
        
      DocAudit docAudit = new DocAudit(frame.getDatabaseShared(), driver);

      return new DialogDocPanel<>(docAudit, bgColor);
        
        
    }        


    
}
