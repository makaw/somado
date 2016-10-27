/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package gui.dialogs.tableforms;
 

import datamodel.Order;
import datamodel.glossaries.GlossOrders;
import gui.GUI;


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
   
       GlossOrders glossOrders =  new GlossOrders(frame.getDatabase());                             
       boolean tmp = glossOrders.updateItem(order, frame.getUser());
       lastError = glossOrders.getLastError();
       return tmp;

    }


    
}
