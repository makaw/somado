/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.formfields;

import datamodel.Product;
import gui.GUI;
import gui.IconButton;
import gui.ImageRes;
import gui.dialogs.gloss.GlossProductsDialog;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 *
 * Szablon obiektu panelu z polem wyboru produktu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class ProductField extends JPanel implements IFormField {
    
  /** Obiekt wybranego produktu */  
  private Product selectedItem;
  /** Obiekt zmiany pola wybranego produktu - dla listenera */  
  private PropertyChangeSupport changes = new PropertyChangeSupport(this);  
  /** Pole tekstowe */
  private final JTextField textField;
  /** Przycisk zmiany */
  private final JButton changeButton;
  
   
    
  /**
   * Konstruktor
   * @param frame Ref. do GUI
   * @param product Wybrany produkt
   */
  public ProductField(final GUI frame, Product product) {
      
    super(new FlowLayout(FlowLayout.LEFT));
        
    setOpaque(false);
    
    textField = new JTextField(22);
    textField.setBackground(new Color(0xeaeaea));
    textField.setEditable(false);
   
    if (product.getId()==0)  selectedItem = new Product();
    else selectedItem = product; 
    
    textField.setText(product.toString());
    textField.setPreferredSize(new Dimension(400, 30));
    add(textField);
        
    changeButton = new IconButton(ImageRes.getIcon("icons/form_edit.png"), "Zmie\u0144 produkt");
    
    changeButton.addActionListener(new ActionListener() {

       @Override
       public void actionPerformed(ActionEvent e) {
            
          Product tmp = selectedItem; 
          GlossProductsDialog glossProductsDialog = new GlossProductsDialog(frame,  selectedItem.getId());
          selectedItem = glossProductsDialog.getSelectedElement();
   
          if (selectedItem != null) {
             textField.setText(selectedItem.toString());
             changes.firePropertyChange("selectedItem", tmp, selectedItem);
          }
               
        }

    });        
        
    add(changeButton);      

  }
  
  
  /**
   * Metoda zwraca wybrany ze slownika obiekt
   * @return Wybrany ze slownika obiekt
   */
  @Override
  public Object getSelectedElement() {
      
    return selectedItem;
      
  }
  
  
  /**
   * Metoda ustawia dana wartosc
   * @param element Wartosc do ustawienia
   */
  @Override
  public void setSelectedElement(Object element) {
      
     selectedItem = (Product) element;
     textField.setText(selectedItem.toString());
      
  }
  
  
  
  @Override
  public void setEnabled(boolean enabled) {

     changeButton.setEnabled(enabled);
     textField.setEnabled(enabled);
      
  }
  
  
  public void setEditable(boolean editable) {
      
    changeButton.setEnabled(editable);  
      
  }

  
  @Override
  public void addPropertyChangeListener(PropertyChangeListener l) {
     changes.addPropertyChangeListener(l);
  }

  
  @Override
  public void removePropertyChangeListener(PropertyChangeListener l) {
     changes.removePropertyChangeListener(l);
  }
    
  
    
}
