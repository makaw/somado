/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs.gloss;

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
public class GlossVehicleModelEditModDialog extends GlossVehicleModelEditDialog {
  
  /** Zmienione dane */
  private VehicleModel updatedItem;  
    
  
  /**
   * Konstruktor: istniejacy element slownika
   * @param frame Referencja do GUI
   * @param parentDialog Obiekt nadrzednego okienka
   * @param vmIndex Indeks slownikowy elementu
   */  
  public GlossVehicleModelEditModDialog(GUI frame, GlossDialog<VehicleModel> parentDialog, int vmIndex) {
        
    super(frame, parentDialog, "S\u0142ownik modeli pojazd\u00f3w - edycja modelu pojazdu", vmIndex);
         
  }
  
  
  /**
   * Metoda zapisujaca do BD
   * @param vehicleModel Dane do zapisania
   * @return true jezeli OK
   */  
  @Override
  protected boolean saveItem(VehicleModel vehicleModel) {
      
     updatedItem = vehicleModel;
     boolean saved = glossVehicleModels.updateItem(vehicleModel, frame.getUser());
     
     
     if (saved) 
       try {
        ((TablePanel)(frame.getDataPanel(GUI.TAB_VEHICLES))).refreshTable();
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
