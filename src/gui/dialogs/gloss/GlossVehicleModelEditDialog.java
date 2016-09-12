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
import gui.dialogs.ErrorDialog;
import gui.dialogs.GlossDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import datamodel.glossaries.GlossVehicleModels;
import gui.formfields.SliderField;
import gui.formfields.FormRow;
import gui.dialogs.EditLockableDataDialog;
import gui.formfields.FormRowPad;
import java.awt.Dimension;
import java.awt.FlowLayout;
import somado.IConf;


/**
 *
 * Szablon okienka do edycji/dodawania elementow slownika
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public abstract class GlossVehicleModelEditDialog extends EditLockableDataDialog {
    
  /** Referencja do obiektu powiazanego slownika */  
  final protected GlossVehicleModels glossVehicleModels;
  /** Referencja do nadrzednego okienka */
  final private GlossDialog<VehicleModel> parentDialog;
  /** Modyfikowany/wprowadzany obiekt(dane) */
  final protected VehicleModel vehicleModel;
  
    
  /**
   * Konstruktor: istniejacy element slownika
   * @param frame Referencja do GUI
   * @param parentDialog Obiekt nadrzednego okienka
   * @param title Naglowek okienka
   * @param vmIndex Indeks slownikowy elementu
   */
  public GlossVehicleModelEditDialog(GUI frame, GlossDialog<VehicleModel> parentDialog, String title, int vmIndex) {
        
    super(frame, IConf.APP_NAME + " - " + title);
    glossVehicleModels = (GlossVehicleModels)(parentDialog.getGlossary());
    this.parentDialog = parentDialog;
    this.vehicleModel = (vmIndex == -1) ? new VehicleModel() : glossVehicleModels.getItem(vmIndex);
    checkLock(this.vehicleModel);
    super.showDialog(480, 285);
         
  }
  
  
  /**
   * Konstruktor: nowy element slownika
   * @param frame Referencja do GUI
   * @param parentDialog Obiekt nadrzednego okienka
   * @param title Naglowek okienka
   */
  public GlossVehicleModelEditDialog(GUI frame, GlossDialog<VehicleModel> parentDialog, String title) {
        
    this(frame, parentDialog, title, -1);
         
  }  
   
   
  
  /**
   * Metoda zapisujaca do BD
   * @param vehicleModel Dane do zapisania
   * @return true jezeli OK
   */
  protected abstract boolean saveItem(VehicleModel vehicleModel);
  
  /**
   * Metoda odswieza liste po zapisie do BD
   */
  protected abstract void refreshItemsList();
  
  
  /**
   * Klasa wewnetrzna - szablon calego formularza
   */
  @SuppressWarnings("serial")
  private class FormPanel extends GlossFormPanel {
      
 
     FormPanel() {
         
        super(frame, "Dane modelu pojazdu", vehicleModel);

        final JTextField nameField = new JTextField(22);
        nameField.setText(vehicleModel.getName());
        nameField.setEditable(!locked);
        dataTabPane.add(new FormRow("Nazwa:", nameField));

        final SliderField maximumLoadField = new SliderField("", 2, IConf.MIN_VEHICLE_MAXLOAD, 
                IConf.MAX_VEHICLE_MAXLOAD, "t   ");
        maximumLoadField.setValue(vehicleModel.getMaximumLoad());
        maximumLoadField.setEditable(!locked);
        dataTabPane.add(new FormRow("\u0141adowno\u015b\u0107:", maximumLoadField));
        
        final SliderField avgFuelConsumptionField = new SliderField("", 2, IConf.MIN_VEHICLE_FUEL_CONSUMPTION,
                IConf.MAX_VEHICLE_FUEL_CONSUMPTION, "l   ");
        avgFuelConsumptionField.setValue(vehicleModel.getAvgFuelConsumption());
        avgFuelConsumptionField.setEditable(!locked);
        dataTabPane.add(new FormRowPad("<html>\u015ar. zu\u017cycie<br />paliwa /100km:</html>:",
                avgFuelConsumptionField));        
               
        JPanel p = new JPanel(new FlowLayout());
        p.setPreferredSize(new Dimension(600, 35));
        p.setOpaque(false);
        dataTabPane.add(p);
        
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(12, 0, 2, 0));

        JButton saveButton = new JButton(" Zapisz ");
        saveButton.setFocusPainted(false);
  
        final Integer id = vehicleModel.getId();
        
        saveButton.addActionListener(new ActionListener() {
            
          @Override
          public void actionPerformed(final ActionEvent e) {
           
            VehicleModel vehicleModel = new VehicleModel(id, nameField.getText(), maximumLoadField.getValue(), 
                avgFuelConsumptionField.getValue());
            
            if (!saveItem(vehicleModel)) {
              
              new ErrorDialog(frame, glossVehicleModels.getLastError());
                
            }
             
            else {
                
              refreshItemsList(); 
              dispose();
              
            }     
          
          }

        });            
        
        saveButton.setEnabled(!locked);
        
        p.add(saveButton);
        p.add(new JLabel(" "));
        p.add(new JLabel(" "));
        p.add(new CloseButton(" Anuluj "));        
        dataTabPane.add(p);        

         
     }
      
  }


  /**
   * Zawartosc okienka
   */
  @Override
  protected void getContent() {

    JPanel p = new JPanel(new GridLayout(1,1));
    p.setOpaque(false);
    p.setBorder(new EmptyBorder(5, 5, 5, 5));
    p.add(new FormPanel());
    add(p);

     
  }
  
  
  /**
   * Metoda zwraca referencje do nadrzednego okienka
   * @return Ref. do obiektu nadrzednego okna
   */
  protected GlossDialog<VehicleModel> getParentDialog() {
      
     return parentDialog; 
      
  }
  
    
    
}
