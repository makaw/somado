/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs.tableforms;
 

import datamodel.Order;
import gui.dialogs.docpanels.DialogDocPanel;
import datamodel.docs.DocAudit;
import datamodel.glossaries.GlossOrders;
import gui.GUI;
import java.awt.Color;
import javax.swing.JPanel;


/**
 *
 * Szablon obiektu okienka modyfikacji zamówienia
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class OrderEditModDialog extends OrderEditDialog {
   
   
   /**
    * Konstruktor
    * @param frame Ref. do GUI 
    */
   public OrderEditModDialog(GUI frame) {
       
     super(frame, "Edycja zam\u00f3wienia", true);  
       
   }    
   

    @Override
    protected boolean saveItem(Order order) {
   
       GlossOrders glossOrders =  new GlossOrders(frame.getDatabaseShared());                             
       boolean tmp = glossOrders.updateItem(order, frame.getUser());
       lastError = glossOrders.getLastError();
       return tmp;

    }
    
    
    
   /**
    * Metoda zwraca panel z lista historii zmian
    * @param bgColor Kolor tla
    * @return Panel z lista historii zmian
    */    
    @Override
    protected JPanel getAuditPanel(Color bgColor) {
        
      DocAudit docAudit = new DocAudit(frame.getDatabaseShared(), order);

      return new DialogDocPanel<>(docAudit, bgColor);
        
        
    }        


    
}
