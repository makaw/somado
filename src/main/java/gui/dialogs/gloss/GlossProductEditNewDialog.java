/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package gui.dialogs.gloss;



import datamodel.Product;
import gui.GUI;
import gui.dialogs.GlossDialog;
import somado.Lang;


/**
 *
 * Szablon okienka do dodawania nowego elementu slownika
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class GlossProductEditNewDialog extends GlossProductEditDialog {
    
  /** Zmienione dane */  
  private Product addedItem;
    
    
  /**
   * Konstruktor: nowy element slownika
   * @param frame Referencja do GUI
   * @param parentDialog Obiekt nadrzednego okienka
   */  
  public GlossProductEditNewDialog(GUI frame, GlossDialog<Product> parentDialog) {
        
    super(frame, parentDialog, Lang.get("Gloss.GlossItems") + " - " + Lang.get("Gloss.AddItem"));
         
  }
  
  
  /**
   * Metoda zapisujaca do BD
   * @param item Dane do zapisania
   * @return true jezeli OK
   */  
  @Override
  protected boolean saveItem(Product item) {
      
     addedItem = item;
     return glossProducts.addItem(item, frame.getUser());
            
  }
  
  
  /**
   * Metoda odswieza liste po zapisie do BD
   */  
  @Override
  protected void refreshItemsList() {
      
    getParentDialog().getFilters().doUpdate(addedItem.getId());
      
  }
  
    
}

