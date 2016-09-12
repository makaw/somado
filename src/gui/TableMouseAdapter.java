/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.SwingUtilities;



/**
 * Zmodyfikowany adapter listenera myszy dla wierszy tabel
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public abstract class TableMouseAdapter extends MouseAdapter {
    
    
      @Override
       public void mousePressed(MouseEvent e) {
          
         JTable table = (JTable)e.getComponent();
           
         if (SwingUtilities.isRightMouseButton(e)) {
             
             int index = table.rowAtPoint(e.getPoint());
             if (index != table.getSelectedRow()) {
               table.addRowSelectionInterval(index, index);
               table.setRowSelectionInterval(index, index);
             }

         }            
          
       }  
        
       @Override
       public void mouseClicked(MouseEvent e) {
   
          if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) 
             doubleClickAction();

       }
      
       /**
        * Metoda ma wywoływać akcje dla podwójnego kliknięcia 
        */
       protected abstract void doubleClickAction();    
    
    
}
