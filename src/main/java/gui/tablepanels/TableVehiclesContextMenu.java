/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
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
import somado.Lang;

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
      
      
    
      ContextMenuItem item = new ContextMenuItem(Lang.get("Tables.VehicleModels.Edit"), "icons/form_edit.png");
      item.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
           
             new VehicleEditModDialog(frame); 
              
          }
      
      });
      add(item);
      
      item = new ContextMenuItem(Lang.get("Tables.VehicleModels.Delete"), "icons/form_del.png");
      item.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
           
             JTable parentTable = ((TablePanel) frame.getActiveDataPanel()).getTable();
             VehiclesTableModel dModel = (VehiclesTableModel) (parentTable.getModel());     
             Vehicle vTmp = 
                new Vehicle(dModel.getElement(parentTable.convertRowIndexToModel(parentTable.getSelectedRow()))); 
              
             if ((new ConfirmDialog(frame, Lang.get("Tables.VehicleModels.Delete.AreYouSure", vTmp.toString()), 170)).isConfirmed()) {
                            
                if (new GlossVehicles(frame.getDatabase()).deleteItem(vTmp, frame.getUser())) {
                    ((TablePanel) frame.getActiveDataPanel()).refreshTable();
                    frame.getDataPanel(GUI.TAB_VEHICLES).setChanged(true);   
                    frame.getDataPanel(GUI.TAB_DRIVERS).setChanged(true); 
                }
   
                 
             }              
              
          }
      
      });
      add(item);      
      
      add(new JSeparator());
     
      item = new ContextMenuItem(Lang.get("Tables.VehicleModels.Add"), "icons/form_add.png");
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
