/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs.gloss;


import datamodel.Product;
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
public class GlossProductEditModDialog extends GlossProductEditDialog {
  
  /** Zmienione dane */
  private Product updatedItem;  
    
  
  /**
   * Konstruktor: istniejacy element slownika
   * @param frame Referencja do GUI
   * @param parentDialog Obiekt nadrzednego okienka
   * @param pIndex Indeks slownikowy elementu
   */  
  public GlossProductEditModDialog(GUI frame, GlossDialog<Product> parentDialog, int pIndex) {
        
    super(frame, parentDialog, "S\u0142ownik produkt\u00f3w - edycja produktu", pIndex);
         
  }
  
  
  /**
   * Metoda zapisujaca do BD
   * @param product Dane do zapisania
   * @return true jezeli OK
   */  
  @Override
  protected boolean saveItem(Product product) {
      
     updatedItem = product;
     boolean saved = glossProducts.updateItem(product, frame.getUser());     
     
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
      
    getParentDialog().getFilters().doUpdate(updatedItem.getId());
      
  }
  
    
}
