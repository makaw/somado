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
public class GlossUserEditNewDialog extends GlossUserEditDialog {
    
  /** Zmienione dane */  
  private UserData addedItem;
    
    
  /**
   * Konstruktor: nowy element slownika
   * @param frame Referencja do GUI
   * @param parentDialog Obiekt nadrzednego okienka
   */  
  public GlossUserEditNewDialog(GUI frame, GlossDialog<UserData> parentDialog) {
        
    super(frame, parentDialog, "Nowy u\u017cytkownik systemowy");
         
  }
  
  
  /**
   * Metoda zapisujaca do BD
   * @param user Dane do zapisania
   * @return true jezeli OK
   */  
  @Override
  protected boolean saveItem(UserData user) {
      
     addedItem = user;
     return glossUsers.addItem(user, frame.getUser());
            
  }
  
  
  /**
   * Metoda odswieza liste po zapisie do BD
   */  
  @Override
  protected void refreshItemsList() {
      
    getParentDialog().getFilters().doUpdate(addedItem.getId());
      
  }
  
    
}

