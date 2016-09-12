/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs.gloss;


import datamodel.Customer;
import gui.GUI;
import gui.TablePanel;
import gui.dialogs.GlossDialog;


/**
 *
 * Szablon okienka do modyfikacji elementow slownika
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class GlossCustomerEditModDialog extends GlossCustomerEditDialog {
  
  /** Zmienione dane */
  private Customer updatedItem;  
    
  
  /**
   * Konstruktor: istniejacy element slownika
   * @param frame Referencja do GUI
   * @param parentDialog Obiekt nadrzednego okienka
   * @param cIndex Indeks slownikowy elementu
   */  
  public GlossCustomerEditModDialog(GUI frame, GlossDialog<Customer> parentDialog, int cIndex) {
        
    super(frame, parentDialog, "S\u0142ownik odbiorc\u00f3w towaru - edycja odbiorcy", cIndex);
         
  }
  
  
  public GlossCustomerEditModDialog(GUI frame) {
      
    super(frame);          
      
  }
  
  
  /**
   * Metoda zapisujaca do BD
   * @param customer Dane do zapisania
   * @return true jezeli OK
   */  
  @Override
  protected boolean saveItem(Customer customer) {
      
     updatedItem = customer;
     boolean saved = glossCustomers.updateItem(customer, frame.getUser());
     
     if (saved) 
       try {
        ((TablePanel)(frame.getDataPanel(GUI.TAB_ORDERS))).refreshTable();
       }
       catch (ClassCastException e) {}
     
     return saved;
     
  }
  
  
  /**
   * Metoda odswieza liste po zapisie do BD
   */  
  @Override
  protected void refreshItemsList() {
      
    if (getParentDialog() != null) 
      getParentDialog().getFilters().doUpdate(updatedItem.getId());
      
  }
  
    
}
