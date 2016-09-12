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
import gui.dialogs.GlossDialog;


/**
 *
 * Szablon okienka do dodawania nowego elementu slownika
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class GlossCustomerEditNewDialog extends GlossCustomerEditDialog {
    
  /** Zmienione dane */  
  private Customer addedItem;
    
    
  /**
   * Konstruktor: nowy element slownika
   * @param frame Referencja do GUI
   * @param parentDialog Obiekt nadrzednego okienka
   */  
  public GlossCustomerEditNewDialog(GUI frame, GlossDialog<Customer> parentDialog) {
        
    super(frame, parentDialog, "S\u0142ownik odbiorc\u00f3w towaru - nowy odbiorca");
         
  }
  
  
  /**
   * Metoda zapisujaca do BD
   * @param item Dane do zapisania
   * @return true jezeli OK
   */  
  @Override
  protected boolean saveItem(Customer item) {
      
     addedItem = item;
     return glossCustomers.addItem(item, frame.getUser());
            
  }
  
  
  /**
   * Metoda odswieza liste po zapisie do BD
   */  
  @Override
  protected void refreshItemsList() {
      
    getParentDialog().getFilters().doUpdate(addedItem.getId());
      
  }
  
    
}

