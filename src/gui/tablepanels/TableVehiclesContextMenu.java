/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.tablepanels;


import datamodel.Vehicle;
import datamodel.glossaries.GlossVehicles;
import datamodel.tablemodels.VehiclesTableModel;
import gui.GUI;
import gui.TableContextMenu;
import gui.TablePanel;
import gui.dialogs.ConfirmDialog;
import gui.dialogs.tableforms.VehicleEditModDialog;
import gui.dialogs.tableforms.VehicleEditNewDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JSeparator;
import javax.swing.JTable;



/**
 * Szablon obiektu menu kontekstowego dla wierszy tabeli pojazdów
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class TableVehiclesContextMenu extends TableContextMenu {
    
    
  /**
   * Konstruktor
   * @param frame Ref. do GUI 
   */  
  public TableVehiclesContextMenu(final GUI frame) {

      super(frame);
      
      
    
      ContextMenuItem item = new ContextMenuItem("Edycja pojazdu", "icons/form_edit.png");
      item.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
           
             new VehicleEditModDialog(frame); 
              
          }
      
      });
      add(item);
      
      item = new ContextMenuItem("Usu\u0144 pojazd", "icons/form_del.png");
      item.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
           
             JTable parentTable = ((TablePanel) frame.getActiveDataPanel()).getTable();
             VehiclesTableModel dModel = (VehiclesTableModel) (parentTable.getModel());     
             Vehicle vTmp = 
                new Vehicle(dModel.getElement(parentTable.convertRowIndexToModel(parentTable.getSelectedRow()))); 
              
             if ((new ConfirmDialog(frame, "Czy na pewno usun\u0105\u0107 pojazd:\n"
                     + vTmp.toString() + " ?", 170)).isConfirmed()) {
                            
                if (new GlossVehicles(frame.getDatabaseShared()).deleteItem(vTmp, frame.getUser())) {
                    ((TablePanel) frame.getActiveDataPanel()).refreshTable();
                    frame.getDataPanel(GUI.TAB_VEHICLES).setChanged(true);   
                    frame.getDataPanel(GUI.TAB_DRIVERS).setChanged(true); 
                }
   
                 
             }              
              
          }
      
      });
      add(item);      
      
      add(new JSeparator());
     
      item = new ContextMenuItem("Dodaj nowy pojazd", "icons/form_add.png");
      item.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
           
            new VehicleEditNewDialog(frame); 
              
          }
      
      });
      add(item);
      
       
   }

    @Override
    protected void updateMenuItems() {}
  
  
     
    
}
