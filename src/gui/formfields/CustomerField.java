/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.formfields;

import datamodel.Customer;
import gui.GUI;
import gui.IconButton;
import gui.ImageRes;
import gui.dialogs.gloss.GlossCustomersDialog;
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
 * Szablon obiektu panelu z polem wyboru odbiorcy towaru
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class CustomerField extends JPanel implements IFormField {
    
  /** Obiekt wybranego odbiorcy towaru */  
  private Customer selectedItem;
  /** Obiekt zmiany pola wybranego odbiorcy - dla listenera */  
  private PropertyChangeSupport changes = new PropertyChangeSupport(this);  
  /** Pole tekstowe */
  private final JTextField textField;
  /** Przycisk zmiany */
  private final JButton changeButton;
  
   
    
  /**
   * Konstruktor
   * @param frame Ref. do GUI
   * @param customer Wybrany odbiorca towaru
   * @param fieldSize Rozmiar pola tekstowego
   */
  public CustomerField(final GUI frame, Customer customer, int fieldSize) {
      
    super(new FlowLayout(FlowLayout.LEFT));       
    setOpaque(false);
    
    textField = new JTextField(fieldSize);
    textField.setBackground(new Color(0xeaeaea));
    textField.setEditable(false);
   
    if (customer.getId()==0)  selectedItem = new Customer();
    else selectedItem = customer;
    
    textField.setText(customer.toString());
    textField.setPreferredSize(new Dimension(400, 30));
    textField.setCaretPosition(0);
    add(textField);
        
    changeButton = new IconButton(ImageRes.getIcon("icons/form_edit.png"), "Zmie\u0144 odbiorc\u0119 towaru");
    
    changeButton.addActionListener(new ActionListener() {

       @Override
       public void actionPerformed(ActionEvent e) {
            
          Customer tmp = selectedItem; 
          GlossCustomersDialog glossCustomersDialog = new GlossCustomersDialog(frame, selectedItem.getId());
          selectedItem = glossCustomersDialog.getSelectedElement();
   
          if (selectedItem != null) {
             textField.setText(selectedItem.toString());
             textField.setCaretPosition(0);
             changes.firePropertyChange("selectedItem", tmp, selectedItem);
          }
               
        }

    });        
        
    add(changeButton);      

  }
  
  
  /**
   * Konstruktor
   * @param frame Ref. do GUI
   * @param customer Wybrany odbiorca towaru
   */
  public CustomerField(final GUI frame, Customer customer) {
      
    this(frame, customer, 39);  
      
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
      
     selectedItem = (Customer) element;
     textField.setText(selectedItem.toString());
     textField.setCaretPosition(0);
      
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
