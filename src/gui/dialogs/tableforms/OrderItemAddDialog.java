/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs.tableforms;

import datamodel.OrderItem;
import datamodel.Product;
import gui.GUI;
import gui.SimpleDialog;
import gui.dialogs.ErrorDialog;
import gui.formfields.FormRowPad;
import gui.formfields.ProductField;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import somado.IConf;


/**
 *
 * Szablon obiektu okienka dodawania pozycji do zamówienia
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class OrderItemAddDialog extends SimpleDialog implements IDialogForm {
    
  /** Pozycja zamówienia */
  private OrderItem orderItem;
  /** True jezeli konieczne jest odświeżenie powiązanego pola */
  private boolean needRefresh = false;
    
  
  /**
   * Konstruktor 
   * @param frame Referencja do GUI
   */
  public OrderItemAddDialog(GUI frame) {
        
    super(frame, IConf.APP_NAME + " - dodanie pozycji zam\u00f3wienia");
    orderItem = new OrderItem();
    super.showDialog(480, 145);
         
  }    
  
  
  /**
   * Klasa wewnętrzna - szablon całego formularza
   */
  @SuppressWarnings("serial")
  private class FormPanel extends JPanel {
      
 
     FormPanel(JDialog dialog) {
         
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        final ProductField productField = new ProductField(frame, orderItem.getProduct());        
        add(new FormRowPad("Produkt:", productField));   
        
        final JTextField numberField = new JTextField(10);
        numberField.setText(String.valueOf(orderItem.getItemsNumber()));
        add(new FormRowPad("Liczba szt.:", numberField));                   
     
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(12, 0, 2, 0));

        JButton saveButton = new JButton(" Dodaj ");
        saveButton.setFocusPainted(false);
        
        saveButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
            
          int itemsNumber = 0;
          try {
            itemsNumber = Integer.parseInt(numberField.getText());  
            if (itemsNumber<1) throw new NumberFormatException();
          }
          catch (NumberFormatException ex) {
            new ErrorDialog(frame, "Nieprawid\u0142owa liczba sztuk produktu.");  
            return;                
          }
                  
          Product product = (Product)productField.getSelectedElement();
          
          if (product == null || product.getId()==0) {
            new ErrorDialog(frame, "Nie wybrano produktu.");
            return;              
          }
           
          orderItem = new OrderItem(product, itemsNumber);
          needRefresh = true;
          dispose();
        
          
          }
        });            
        
        p.add(saveButton);
        p.add(new JLabel(" "));
        p.add(new JLabel(" "));
        p.add(new CloseButton(" Anuluj "));        
        add(p);        
         
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
       
    p.add(new FormPanel(this));
    add(p);

     
  }
      
  
  /**
   * Metoda okresla czy konieczne jest odswiezenie tabeli-rodzica
   * @return True jezeli konieczne jest odswiezenie
   */  
  @Override
  public boolean isNeedRefresh() {
      
    return needRefresh;  
      
  }
  

  public OrderItem getOrderItem() {
    return orderItem;
  }
  
  
  
  
 /** 
   * (Niezaimplementowana) Metoda zwraca indeks BD ostatnio dodanego elementu
   * @return Indeks BD ostatnio dodanego elementu
   */
  @Override
  public int getAddedIndex() {
      
    return -1; 
      
  }
  
  
  
    
}
