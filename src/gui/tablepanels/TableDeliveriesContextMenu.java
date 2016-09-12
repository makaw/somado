/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.tablepanels;


import gui.GUI;
import gui.TableContextMenu;
import gui.dialogs.DeliveryViewDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



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
                
      ContextMenuItem item = new ContextMenuItem("Podgl\u0105d dostawy", "icons/map.png");
      item.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {                        
              
             new DeliveryViewDialog(frame);
              
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
