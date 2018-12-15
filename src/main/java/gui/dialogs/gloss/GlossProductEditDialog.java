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
import datamodel.glossaries.GlossProducts;
import gui.GUI;
import gui.SimpleDialog;
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
import gui.formfields.FormRowPad;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JCheckBox;
import somado.IConf;
import somado.Lang;


/**
 *
 * Szablon okienka do edycji/dodawania elementow slownika
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public abstract class GlossProductEditDialog extends SimpleDialog {
    
  /** Referencja do obiektu powiazanego slownika */  
  final protected GlossProducts glossProducts;
  /** Referencja do nadrzednego okienka */
  final private GlossDialog<Product> parentDialog;
  /** Modyfikowany/wprowadzany obiekt(dane) */
  final protected Product product;
  
    
  /**
   * Konstruktor: istniejacy element slownika
   * @param frame Referencja do GUI
   * @param parentDialog Obiekt nadrzednego okienka
   * @param title Naglowek okienka
   * @param productIndex Indeks slownikowy elementu
   */
  public GlossProductEditDialog(GUI frame, GlossDialog<Product> parentDialog, String title, int productIndex) {
        
    super(frame, IConf.APP_NAME + " - " + title);
    glossProducts = (GlossProducts)(parentDialog.getGlossary());
    this.parentDialog = parentDialog;
    this.product = (productIndex == -1) ? new Product() : glossProducts.getItem(productIndex);
    
    super.showDialog(480, 235);
         
  }
  
  
  /**
   * Konstruktor: nowy element slownika
   * @param frame Referencja do GUI
   * @param parentDialog Obiekt nadrzednego okienka
   * @param title Naglowek okienka
   */
  public GlossProductEditDialog(GUI frame, GlossDialog<Product> parentDialog, String title) {
        
    this(frame, parentDialog, title, -1);
         
  }  
   
   
  
  /**
   * Metoda zapisujaca do BD
   * @param product Dane do zapisania
   * @return true jezeli OK
   */
  protected abstract boolean saveItem(Product product);
  
  /**
   * Metoda odswieza liste po zapisie do BD
   */
  protected abstract void refreshItemsList();
  
  
  /**
   * Klasa wewnetrzna - szablon calego formularza
   */
  private class FormPanel extends GlossFormPanel {
      
 
     FormPanel() {
         
        super(frame, Lang.get("Gloss.ItemData"), product);

        final JTextField nameField = new JTextField(22);
        nameField.setText(product.getName());
        dataTabPane.add(new FormRow(Lang.get("Gloss.Name") + ":", nameField));
        
        final JTextField weightField = new JTextField(22);
        weightField.setText(String.valueOf(product.getWeight()));
        dataTabPane.add(new FormRow(Lang.get("Gloss.Weight") + " [kg]:", weightField));
        
        final JCheckBox availableField = new JCheckBox(Lang.get("Gloss.ItemAvailable"));
        availableField.setOpaque(false);
        availableField.setFocusPainted(false);
        availableField.setSelected(product.isAvailable());
        availableField.setFont(GUI.BASE_FONT);
        dataTabPane.add(new FormRowPad(Lang.get("Gloss.Availability") + ":", availableField));        
               
        JPanel p = new JPanel(new FlowLayout());
        p.setPreferredSize(new Dimension(600, 35));
        p.setOpaque(false);
        dataTabPane.add(p);
        
        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(12, 0, 2, 0));

        JButton saveButton = new JButton(Lang.get("Save"));
        saveButton.setFocusPainted(false);
  
        final Integer id = product.getId();
        
        saveButton.addActionListener(new ActionListener() {
            
          @Override
          public void actionPerformed(final ActionEvent e) {
           
            double weight = 0.0;
            try {
              weight = Double.valueOf(weightField.getText().replace(",", "."));
            }
            // ew.wyjatek bedzie rzucony w klasie Product
            catch (Exception ex) {}
              
            Product product = new Product(id, nameField.getText(), weight, availableField.isSelected());
            
            if (!saveItem(product)) {
              
              new ErrorDialog(frame, glossProducts.getLastError());
                
            }
             
            else {
                
              refreshItemsList(); 
              dispose();
              
            }     
          
          }

        });            
        
        
        p.add(saveButton);
        p.add(new JLabel(" "));
        p.add(new JLabel(" "));
        p.add(new CloseButton(Lang.get("Cancel")));        
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
  protected GlossDialog<Product> getParentDialog() {
      
     return parentDialog; 
      
  }
  
    
    
}
