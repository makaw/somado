/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */

package gui.formfields;

import datamodel.Order;
import datamodel.OrderItem;
import gui.GUI;
import gui.IconButton;
import gui.ImageRes;
import gui.dialogs.ConfirmDialog;
import gui.dialogs.ErrorDialog;
import gui.dialogs.tableforms.OrderItemAddDialog;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



/**
 *
 * Szablon obiektu panelu z polem produktów zamówienia
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class OrderProductsField extends JPanel implements IFormField {
    
   /** Przycisk usuwający pozycję zamówienia */ 
   private final IconButton deleteButton;
   /** Przycisk dodający pozycję zamówienia */
   private final IconButton addButton;   
   /** Czy pole dostępne */
   private boolean enabled = true;
   /** Obiekt zamówienia */
   private final Order order;
    
    
   /** Obiekt zmiany pola pozycji zamówienia - dla listenera */  
   private PropertyChangeSupport changes = new PropertyChangeSupport(this);      

    @Override
    public Object getSelectedElement() {
       return order.getProducts();
    }

    @SuppressWarnings("unchecked")  
    @Override    
    public void setSelectedElement(Object element) {
       try {          
         order.setProducts((List<OrderItem>)element);
       }
       catch (ClassCastException e) {
         System.err.println(e);  
       }
    }
    
   
   /** Klasa wewn. - renderer do komponentu JList z uprawnieniami */
   @SuppressWarnings("serial")
   private class OrderItemsListCellRenderer extends DefaultListCellRenderer {
       
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
            boolean isSelected, boolean cellHasFocus) {
        
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        Color bg = value.toString().equals("(brak)") ? Color.gray : Color.black;     
        setForeground(bg);
        setOpaque(true); 
        return this; 
      }
    
   }
           
   
   /**
    * Konstruktor
    * @param frame Ref. do GUI
    * @param order Obiekt zamówienia
    */          
   public OrderProductsField(final GUI frame, final Order order) {
         
     super(new FlowLayout(FlowLayout.LEFT));
     setOpaque(false);
     setPreferredSize(new Dimension(600, 120));     
     setBorder(new EmptyBorder(4, 0, 4, 0));  
     
     this.order = order;
     
     final JList<OrderItem> itemsList = new JList<OrderItem>(getListModel()) {
          
          @Override
          public boolean isSelectionEmpty() {          
            return (super.isSelectionEmpty() || super.getSelectedValue().getProduct() == null);
          }  
          
     };
        
     itemsList.setCellRenderer(new OrderItemsListCellRenderer());
     itemsList.setFont(GUI.BASE_FONT);
     itemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
     itemsList.setVisibleRowCount(5);
        
     JScrollPane sp = new JScrollPane(itemsList);
     sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
     sp.setPreferredSize(new Dimension(390, 100));
     add(sp);
         
     add(new JLabel(" "));
     addButton = new IconButton(ImageRes.getIcon("icons/form_add.png"), "Dodaj produkt");
     deleteButton = new IconButton(ImageRes.getIcon("icons/form_del.png"), "Usu\u0144 produkt");
     deleteButton.setEnabled(false);
            
     add(addButton);
     add(deleteButton);
       
        
     // blokowanie przyciskow edycji i usuwania
     itemsList.addListSelectionListener(new ListSelectionListener() {

       @Override
       public void valueChanged(ListSelectionEvent e) {
               
          try {  
            if (itemsList.getSelectedValue().getProduct() == null)
                itemsList.removeSelectionInterval(itemsList.getSelectedIndex(), itemsList.getSelectedIndex());               
          }
          catch (NullPointerException ex) {}
                
          boolean b = (!itemsList.isSelectionEmpty());
          if (b) b = b && (itemsList.getSelectedValue().getProduct() != null);          

          deleteButton.setEnabled(b && enabled);
                
       }            
            
     });
        
        
     addButton.addActionListener(new ActionListener() {

       @Override
       public void actionPerformed(ActionEvent e) {

         OrderItemAddDialog addDialog = new OrderItemAddDialog(frame);
         // odswiezenie listy 
         if (addDialog.isNeedRefresh()) {
                   
            OrderItem tmp = addDialog.getOrderItem();
            boolean err = false;
            Iterator<OrderItem> iterator = order.getProducts().iterator();
            while (iterator.hasNext() && !err) {
               if (iterator.next().getProduct().getId().equals(tmp.getProduct().getId())) err = true;
            }
            
            if (err) new ErrorDialog(frame, "Produkt:\n" + tmp.getProduct().toString() + "\nju\u017c "
                       + "znajduje si\u0119 na li\u015bcie.");
            
            else {            
              order.getProducts().add(tmp);
              itemsList.setModel(getListModel());
            }
                                            
          }
        }

     });    
        
        
     deleteButton.addActionListener(new ActionListener() {

       @Override
       public void actionPerformed(ActionEvent e) {
                
          // usuniecie, odswiezenie listy w formularzu i tabeli 
          if ((new ConfirmDialog(frame,"Czy na pewno usun\u0105\u0107 ?")).isConfirmed()) {
                   
              int index = -1; 
              Iterator<OrderItem> iterator = order.getProducts().iterator();
              while (iterator.hasNext()) {
                index++;
                if (iterator.next().getProduct().getId().equals(itemsList.getSelectedValue().getProduct().getId()))
                  break;
              }
              
              if (index != -1) {
                order.getProducts().remove(index);
                itemsList.setModel(getListModel());
              }
                   
          }
               
       }

     });           
                             
       
   }
   
   /**
    * Metoda konstruuje model dla komponentu JList
    * @return Model dla komponentu JList
    */
   private DefaultListModel<OrderItem> getListModel() {
       
     DefaultListModel<OrderItem> model = new DefaultListModel<>();
     
     Iterator<OrderItem> iterator = order.getProducts().iterator();
     int i = 0;
     while (iterator.hasNext()) {
        OrderItem tmp = iterator.next();
        model.add(i++, tmp);
     }       
     
     return model;       
       
   }
   
   
   @Override
   public void setEnabled(boolean enabled) {
       
      addButton.setEnabled(enabled);
      if (!enabled) deleteButton.setEnabled(enabled);
      this.enabled = enabled;
       
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
