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
import datamodel.glossaries.GlossCustomers;
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
import gui.formfields.FormRow;
import gui.dialogs.EditLockableDataDialog;
import gui.dialogs.GeocodeDialog;
import gui.mapview.MapDialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import somado.IConf;
import somado.Settings;


/**
 *
 * Szablon okienka do edycji/dodawania elementow slownika
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public abstract class GlossCustomerEditDialog extends EditLockableDataDialog {
    
  /** Referencja do obiektu powiazanego slownika */  
  final protected GlossCustomers glossCustomers;
  /** Referencja do nadrzednego okienka */
  final private GlossDialog<Customer> parentDialog;
  /** Modyfikowany/wprowadzany obiekt(dane) */
  final protected Customer customer;
  
    
  /**
   * Konstruktor: istniejacy element slownika
   * @param frame Referencja do GUI
   * @param parentDialog Obiekt nadrzednego okienka
   * @param title Naglowek okienka
   * @param customerIndex Indeks slownikowy elementu
   */
  public GlossCustomerEditDialog(GUI frame, GlossDialog<Customer> parentDialog, String title, int customerIndex) {
        
    super(frame, IConf.APP_NAME + " - " + title);
    glossCustomers = parentDialog == null ? new GlossCustomers(frame.getDatabaseShared())
            : (GlossCustomers)(parentDialog.getGlossary());
    this.parentDialog = parentDialog;
    
    // edycja magazynu, nowy element, lub edycja elementu słownika
    this.customer = (customerIndex == -2) ? Settings.getDepot() : 
           ((customerIndex == -1) ? new Customer() : glossCustomers.getItem(customerIndex));
    
    checkLock(this.customer);
    super.showDialog(530, 315);
         
  }
  
  
  /**
   * Konstruktor: nowy element slownika
   * @param frame Referencja do GUI
   * @param parentDialog Obiekt nadrzednego okienka
   * @param title Naglowek okienka
   */
  public GlossCustomerEditDialog(GUI frame, GlossDialog<Customer> parentDialog, String title) {
        
    this(frame, parentDialog, title, -1);
         
  }  
  
  /**
   * Konstruktor: edycja danych magazynu
   * @param frame Referencja do GUI
   */
  public GlossCustomerEditDialog(GUI frame) {
      
    this(frame, null, "Edycja danych magazynu", -2);  
      
  }
   
   
  
  /**
   * Metoda zapisujaca do BD
   * @param customer Dane do zapisania
   * @return true jezeli OK
   */
  protected abstract boolean saveItem(Customer customer);
  
  /**
   * Metoda odswieza liste po zapisie do BD
   */
  protected abstract void refreshItemsList();
  
  
  /**
   * Klasa wewnetrzna - szablon calego formularza
   */
  @SuppressWarnings("serial")
  private class FormPanel extends GlossFormPanel {
      
     /** Pole tekstowe - długość geograficzna */ 
     private final JTextField longitudeField;
     /** Pole tekstowe - szerokość geograficzna */
     private final JTextField latitudeField;
     
     FormPanel() {
         
        super(frame, (customer.getId().equals(Settings.getDepot().getId()) ? "Dane magazynu" : 
                "Dane odbiorcy towaru"), customer);        

        final JTextField nameField = new JTextField(22);
        nameField.setText(customer.getName());
        nameField.setEditable(!locked);
        dataTabPane.add(new FormRow("Nazwa:", nameField));
        
        final JTextField streetField = new JTextField(22);
        streetField.setText(customer.getStreet());
        streetField.setEditable(!locked);
        dataTabPane.add(new FormRow("Ulica i nr:", streetField));        
        
        final JTextField cityField = new JTextField(22);
        cityField.setText(customer.getCity());
        cityField.setEditable(!locked);
        dataTabPane.add(new FormRow("Miejscowo\u015b\u0107:", cityField)); 
        
        final JTextField postcodeField = new JTextField(12);
        postcodeField.setText(customer.getPostcode());
        postcodeField.setEditable(!locked);
        dataTabPane.add(new FormRow("Kod pocztowy:", postcodeField));        
        
        longitudeField = new JTextField(12);
        longitudeField.setText(String.format("%.8f", customer.getLongitude()));
        longitudeField.setEditable(!locked);
        dataTabPane.add(new FormRow("D\u0142ugo\u015b\u0107 geogr.:", longitudeField));   
        
        latitudeField = new JTextField(12);
        latitudeField.setText(String.format("%.8f", customer.getLatitude()));
        latitudeField.setEditable(!locked);
        dataTabPane.add(new FormRow("Szeroko\u015b\u0107 geogr.:", latitudeField));          

        JPanel p = new JPanel(new FlowLayout());
        p.setPreferredSize(new Dimension(600, 35));
        p.setOpaque(false);
        dataTabPane.add(p);
        
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(12, 0, 2, 0));
        
  
        JButton mapButton = new JButton("Na mapie");
        mapButton.setFocusPainted(false);
        mapButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
             
            double[] lonlat = new double[] { 0.0, 0.0 };
            // weryfikacja wprowadzonych wsp. geogr.
            try {
              lonlat = testCoords();
            } 
            catch (Exception ex) {
                
              new ErrorDialog(frame, "Nieprawid\u0142owa d\u0142ugo\u015b\u0107 lub szeroko\u015b\u0107 geograficzna.\n"
                      + Settings.infoBoxCoords());
              return;
                
            }
            
            new MapDialog(frame, lonlat[0], lonlat[1]);
              
          }
        });
      
        
        JButton geocodeButton = new JButton("Wyznacz wsp.geogr.");
        geocodeButton.setFocusPainted(false);
        
        geocodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
              String request = cityField.getText() + " " + streetField.getText(); 
              String simpleRequest = !cityField.getText().isEmpty() ? cityField.getText() : streetField.getText();
              if (request.isEmpty() || request.length()<3) {
                 new ErrorDialog(frame, "Nie wype\u0142niono \u017cadnego z p\u00f3l: ulica, miasto.");
                 return; 
              }
             
              GeocodeDialog g = new GeocodeDialog(frame, request, simpleRequest);
              if (g.isSelected()) {
                longitudeField.setText(String.format("%.8f", g.getSelectedAddress().getLongitude()));
                latitudeField.setText(String.format("%.8f", g.getSelectedAddress().getLatitude()));  
              }  
              
            }
        });
        
        

        JButton saveButton = new JButton(" Zapisz ");
        saveButton.setFocusPainted(false);
  
        final Integer id = customer.getId();
        
        saveButton.addActionListener(new ActionListener() {
            
          @Override
          public void actionPerformed(final ActionEvent e) {
              
            double[] lonlat = new double[] { 0.0, 0.0 };
            // weryfikacja wprowadzonych wsp. geogr.
            try {
              lonlat = testCoords();
            }
            catch (Exception ex) {
                
              new ErrorDialog(frame, "Nieprawid\u0142owa d\u0142ugo\u015b\u0107 lub szeroko\u015b\u0107 geograficzna.\n"
                      + Settings.infoBoxCoords());
              return;
                
            }
              
            Customer customer = new Customer(id, nameField.getText(), cityField.getText(), streetField.getText(), 
                    postcodeField.getText(), lonlat[0], lonlat[1]);
            
            if (!saveItem(customer)) {
              
              new ErrorDialog(frame, glossCustomers.getLastError());
                
            }
             
            else {
                
              refreshItemsList(); 
              dispose();
              
            }     
          
          }

        });            
        
        geocodeButton.setEnabled(!locked);
        saveButton.setEnabled(!locked);
        
        p.add(mapButton);
        p.add(new JLabel(" "));
        p.add(geocodeButton);
        p.add(new JLabel(" "));
        p.add(saveButton);
        p.add(new JLabel(" "));
        p.add(new CloseButton(" Anuluj "));        
        
        dataTabPane.add(p);        

         
     }
     
     
     /**
      * Sprawdzenie poprawności współrzędnych geograficznych
      * @return Tablica lon-lat (długość, szerokość geograficzna)
      * @throws Exception Niepoprawne współrzędne
      */
     private double[] testCoords() throws Exception {
            
       double lon = Double.valueOf(longitudeField.getText().replace(",", "."));
       double lat = Double.valueOf(latitudeField.getText().replace(",", "."));
       if (!Settings.checkBoxCoords(lon, lat)) throw new Exception();
       return new double[] {lon, lat};
      
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
  protected GlossDialog<Customer> getParentDialog() {
      
     return parentDialog; 
      
  }
  
    
    
}
