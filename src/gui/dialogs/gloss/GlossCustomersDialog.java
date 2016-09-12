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
import gui.dialogs.GlossDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * Szablon obiektu okienka dialogowego ze słownikiem odbiorców towaru
 * 
 * @author Maciej Kawecki 
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class GlossCustomersDialog extends GlossDialog<Customer> {
    
   /** Przycisk wyboru do zainicjowania w getButtonsPanel() */
   private JButton chooseButton;
   /** Przycisk edycji do zainicjowania w getButtonsPanel() */
   private JButton editButton;    
   /** Referencja do slownika odbiorców towaru */
   private GlossCustomers glossCustomers;
   
    
    
    /**
     * Konstruktor, wywołuje konstruktor nadklasy z odpowiednim tytułem
     * @param frame Referencja do GUI
     */
    public GlossCustomersDialog(GUI frame) {
        
        this(frame, "S\u0142ownik odbiorc\u00f3w towaru");
        
    }
    
    /**
     * Konstruktor, wywołuje konstruktor nadklasy
     * @param frame Referencja do GUI
     * @param title Tytuł słownika
     */
    public GlossCustomersDialog(GUI frame, String title) {
        
        super(frame, title);
        
    }    
    
    
    /**
     * Konstruktor, wywołuje konstruktor nadklasy
     * @param frame Referencja do GUI
     * @param title Tytuł słownika
     * @param chooseable true jezeli mozna wybrac wartosc ze slownika
     */
    public GlossCustomersDialog(GUI frame, String title, boolean chooseable) {
        
        super(frame, title, chooseable);
        
    }        
    
    
    public GlossCustomersDialog(GUI frame, int customerId) {
        
        super(frame, "S\u0142ownik odbiorc\u00f3w towaru", customerId);
        
    }            
    
    
    /**
     * Zwraca model listy słownika 
     * @return Model listy  
     */
    @Override
    public DefaultListModel<Customer> getListModel() {

      glossCustomers = new GlossCustomers(frame.getDatabaseShared());
        
      return glossCustomers.getListModel(); 

    }
    
    /**
     * Zwraca model listy słownika po filtrowaniu
     * @param params Mapa parametrów filtrowania
     * @return Model listy firm po filtrowaniu
     */
    @Override
    public DefaultListModel<Customer> getListModel(Map<String, String> params) {

      glossCustomers = new GlossCustomers(frame.getDatabaseShared()); 
        
      return glossCustomers.getListModel(params);
        
    }    
    
    
    /**
    * Metoda zwraca referencje do obiektu odpowiedniego slownika
    * @return Ref. do obiektu slownika
    */
    @Override
    public GlossCustomers getGlossary() {
        
       return glossCustomers;
        
    }
  
    
    /**
     * Metoda zwraca panel z przyciskami dostępnych akcji 
     * @return Panel z przyciskami dostępnych akcji 
     */    
    @Override
    protected JPanel getButtonsPanel() {
        
      JPanel p = new JPanel(new GridLayout(1,4));
      
      chooseButton = new JButton("Wybierz");
      chooseButton.setEnabled(false);
      chooseButton.setFocusPainted(false);
            
      chooseButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
           
         selectedElement = glossCustomers.getItem(getObjList().getSelectedIndex());  
         dispose();
          
       }
     });              
      
      p.add(chooseButton);
      
      editButton = new JButton("Edytuj");
      editButton.setEnabled(false);
      editButton.setFocusPainted(false);
      
      editButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
           
         new GlossCustomerEditModDialog(frame, GlossCustomersDialog.this, getObjList().getSelectedIndex());
          
       }
     });               
      
      p.add(editButton);
      
      JButton addButton = new JButton("Dodaj nowy");
      addButton.setFocusPainted(false);
      
      addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
           
         new GlossCustomerEditNewDialog(frame, GlossCustomersDialog.this);
          
       }
     });         
      
      p.add(addButton);
      
      p.add(new CloseButton());        
        
      return p;    
        
        
        
    }
    
    

    /**
     * Metoda konstruuje zestaw pól do filtrowania na danym panelu
     * @param p Panel do umieszczenia pól
     */    
    @Override
    protected void setFiltersFormPanel(JPanel p) {
      
      p.add(new JLabel("Nazwa:"));
      filters.addField("name", new JTextField(12));
      p.add(new JLabel("Miejscowo\u015b\u0107:"));
      filters.addField("city", new JTextField(12));
        
    }
    
    
    @Override
    protected JButton getChooseButton() {
        
       return chooseButton;  
        
    }
    
    
   @Override
   protected JButton getEditButton() {
        
       return editButton; 
        
    }
    
    
}
