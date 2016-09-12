/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.tablepanels;


import datamodel.Order;
import datamodel.OrderState;
import datamodel.glossaries.GlossOrders;
import datamodel.tablemodels.OrdersTableModel;
import gui.GUI;
import gui.TableContextMenu;
import gui.TablePanel;
import gui.dialogs.ConfirmDialog;
import gui.dialogs.tableforms.OrderEditModDialog;
import gui.dialogs.tableforms.OrderEditNewDialog;
import gui.mapview.MapDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JSeparator;
import javax.swing.JTable;



/**
 * Szablon obiektu menu kontekstowego dla wierszy tabeli zamówień
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class TableOrdersContextMenu extends TableContextMenu {
  
  /** Opcja edycji */  
  private final ContextMenuItem editItem;
  /** Opcja anulowania */
  private final ContextMenuItem cancelItem;
  /** Opcja usunięcia */
  private final ContextMenuItem deleteItem; 
  
    
    
  /**
   * Konstruktor
   * @param frame Ref. do GUI 
   */  
  public TableOrdersContextMenu(final GUI frame) {

      super(frame);
      
      final Order selectedOrder = getSelectedItem(); 
                
      editItem = new ContextMenuItem("Edycja zam\u00f3wienia", "icons/form_edit.png");
      editItem.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
           
             if (selectedOrder.getState() != OrderState.NEW) return; 
             new OrderEditModDialog(frame); 
              
          }
      
      });
      add(editItem);
      
      deleteItem = new ContextMenuItem("Usu\u0144 zam\u00f3wienie", "icons/form_del.png");
      deleteItem.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
                           
             if (selectedOrder.getState() != OrderState.NEW) return;  
             if ((new ConfirmDialog(frame, "Czy na pewno usun\u0105\u0107 zam\u00f3wienie:\n"
                     + selectedOrder.toString() + " ?", 170)).isConfirmed()) {
                            
                if (new GlossOrders(frame.getDatabaseShared()).deleteItem(selectedOrder, frame.getUser())) {
                    ((TablePanel) frame.getActiveDataPanel()).refreshTable();
                    frame.getDataPanel(GUI.TAB_ORDERS).setChanged(true);              
                }
   
                 
             }  
              
          }
      
      });
      add(deleteItem);      
      
      
      cancelItem = new ContextMenuItem("Anuluj zam\u00f3wienie", "icons/form_cancel.png");
      cancelItem.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
              
             if (selectedOrder.getState() != OrderState.NEW) return; 
              
             if ((new ConfirmDialog(frame, "Czy na pewno anulowa\u0107 zam\u00f3wienie:\n"
                     + selectedOrder.toString() + " ?", 170)).isConfirmed()) {
                                                                             
                if (new GlossOrders(frame.getDatabaseShared()).changeState(selectedOrder, OrderState.CANCELLED,
                        frame.getUser())) {
                    ((TablePanel) frame.getActiveDataPanel()).refreshTable();
                    frame.getDataPanel(GUI.TAB_ORDERS).setChanged(true);              
                }
   
                 
             }  
              
          }
      
      });
      add(cancelItem);      
            
      
      
      ContextMenuItem item = new ContextMenuItem("Podgl\u0105d na mapie", "icons/map.png");
      item.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
           
             new MapDialog(frame, getSelectedItem().getCustomer());
              
          }
      
      });
      add(item);      
      
      add(new JSeparator());
     
      item = new ContextMenuItem("Dodaj nowe zam\u00f3wienie", "icons/form_add.png");
      item.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
           
            new OrderEditNewDialog(frame); 
              
          }
      
      });
      
      add(item);
      
       
       
   }
  
  
   /**
    * Zmiana pozycji gotowego menu przed pokazaniem
    */   
   @Override
   protected void updateMenuItems() {   
     
      Order selectedOrder = getSelectedItem(); 
       
      editItem.setEnabled(selectedOrder.getState() == OrderState.NEW);
      deleteItem.setEnabled(selectedOrder.getState() == OrderState.NEW);
      cancelItem.setEnabled(selectedOrder.getState() == OrderState.NEW);
   
   }
  
   
   /**
    * Zwraca zaznaczony obiekt
    * @return Zaznaczony obiekt
    */
   private Order getSelectedItem() {
       
      JTable parentTable = ((TablePanel) frame.getActiveDataPanel()).getTable();
      OrdersTableModel oModel = (OrdersTableModel) (parentTable.getModel());     
      return new Order(oModel.getElement(parentTable.convertRowIndexToModel(parentTable.getSelectedRow()))); 
       
   }
  
    
}
