/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.tablepanels;


import datamodel.Driver;
import datamodel.glossaries.GlossDrivers;
import datamodel.tablemodels.DriversTableModel;
import gui.GUI;
import gui.TableContextMenu;
import gui.TablePanel;
import gui.dialogs.ConfirmDialog;
import gui.dialogs.tableforms.DriverEditModDialog;
import gui.dialogs.tableforms.DriverEditNewDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JSeparator;
import javax.swing.JTable;



/**
 * Szablon obiektu menu kontekstowego dla wierszy tabeli kierowców
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class TableDriversContextMenu extends TableContextMenu {
  
    
    
  /**
   * Konstruktor
   * @param frame Ref. do GUI 
   */  
  public TableDriversContextMenu(final GUI frame) {

      super(frame);
                
      ContextMenuItem item = new ContextMenuItem("Edycja kierowcy", "icons/form_edit.png");
      item.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
           
             new DriverEditModDialog(frame); 
              
          }
      
      });
      add(item);
      
      item = new ContextMenuItem("Usu\u0144 kierowc\u0119", "icons/form_del.png");
      item.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
           
             JTable parentTable = ((TablePanel) frame.getActiveDataPanel()).getTable();
             DriversTableModel dModel = (DriversTableModel) (parentTable.getModel());     
             Driver dTmp = 
                new Driver(dModel.getElement(parentTable.convertRowIndexToModel(parentTable.getSelectedRow()))); 
              
             if ((new ConfirmDialog(frame, "Czy na pewno usun\u0105\u0107 kierowc\u0119:\n"
                     + dTmp.toString() + " ?", 170)).isConfirmed()) {
                            
                if (new GlossDrivers(frame.getDatabaseShared()).deleteItem(dTmp, frame.getUser())) {
                    ((TablePanel) frame.getActiveDataPanel()).refreshTable();
                    frame.getDataPanel(GUI.TAB_DRIVERS).setChanged(true);              
                }
   
                 
             }  
              
          }
      
      });
      add(item);      
      
      add(new JSeparator());
     
      item = new ContextMenuItem("Dodaj nowego kierowc\u0119", "icons/form_add.png");
      item.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
           
            new DriverEditNewDialog(frame); 
              
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
