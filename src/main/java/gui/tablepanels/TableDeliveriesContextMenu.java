/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package gui.tablepanels;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gui.GUI;
import gui.TableContextMenu;
import gui.dialogs.DeliveryOpenDialog;
import gui.dialogs.DeliveryViewDialog;
import somado.Lang;



/**
 * Szablon obiektu menu kontekstowego dla wierszy tabeli kierowców
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class TableDeliveriesContextMenu extends TableContextMenu {
      
    
  /**
   * Konstruktor
   * @param frame Ref. do GUI 
   */  
  public TableDeliveriesContextMenu(final GUI frame) {

      super(frame);
                
      ContextMenuItem item = new ContextMenuItem(Lang.get("Tables.Delivery.Preview"), "icons/map.png");
      item.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {                        
              
             new DeliveryViewDialog(frame);
              
          }
      
      });
      add(item);
      
      add(new Separator());
      
      
      item = new ContextMenuItem(Lang.get("Menu.NewDelivery"), "icons/delivery.png");
      item.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
           
        	  new DeliveryOpenDialog(frame);
              
          }
      
      });
      
      add(item);
      
       
   }
  
  
   /**
    * (Niezaimplementowane) Zmiana pozycji gotowego menu przed pokazaniem
    */   
   @Override
   protected void updateMenuItems() {}
  
  
    
}
