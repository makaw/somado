/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs.gloss;

import datamodel.UserData;
import datamodel.VehicleModel;
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
public class GlossUserEditModDialog extends GlossUserEditDialog {
  
  /** Zmienione dane */
  private UserData updatedItem;  
    
  
  /**
   * Konstruktor: istniejacy element slownika
   * @param frame Referencja do GUI
   * @param parentDialog Obiekt nadrzednego okienka
   * @param usrIndex Indeks slownikowy elementu
   */  
  public GlossUserEditModDialog(GUI frame, GlossDialog<UserData> parentDialog, int usrIndex) {
        
    super(frame, parentDialog, "Edycja danych u\u017cytkownika systemowego", usrIndex);
         
  }
  
  
  /**
   * Metoda zapisujaca do BD
   * @param user Dane do zapisania
   * @return true jezeli OK
   */  
  @Override
  protected boolean saveItem(UserData user) {
      
     updatedItem = user;
     boolean saved = glossUsers.updateItem(user, frame.getUser());
          
     if (saved) 
       try {
        ((TablePanel)(frame.getDataPanel(GUI.TAB_DRIVERS))).refreshTable();
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
