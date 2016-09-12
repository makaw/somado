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
import datamodel.glossaries.GlossUsers;
import gui.GUI;
import gui.dialogs.GlossDialog;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * Szablon obiektu okienka dialogowego ze słownikiem użytkowników systemowych
 * 
 * @author Maciej Kawecki 
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class GlossUsersDialog extends GlossDialog<UserData> {
    
   /** Przycisk edycji do zainicjowania w getButtonsPanel() */
   private JButton editButton;    
   /** Referencja do slownika użytkowników */
   private GlossUsers glossUsers;
   
    
    
    /**
     * Konstruktor, wywołuje konstruktor nadklasy z odpowiednim tytułem
     * @param frame Referencja do GUI
     */
    public GlossUsersDialog(GUI frame) {
        
        this(frame, "Lista u\u017cytkownik\u00f3w systemowych");
        
    }
    
    /**
     * Konstruktor, wywołuje konstruktor nadklasy
     * @param frame Referencja do GUI
     * @param title Tytuł słownika
     */
    public GlossUsersDialog(GUI frame, String title) {
        
        super(frame, title);
        
    }    
    
    
    /**
     * Zwraca model listy słownika 
     * @return Model listy  
     */
    @Override
    public DefaultListModel<UserData> getListModel() {

      glossUsers = new GlossUsers(frame.getDatabaseShared());
        
      return glossUsers.getListModel(); 

    }
    
    /**
     * Zwraca model listy słownika po filtrowaniu
     * @param params Mapa parametrów filtrowania
     * @return Model listy firm po filtrowaniu
     */
    @Override
    public DefaultListModel<UserData> getListModel(Map<String, String> params) {

      glossUsers = new GlossUsers(frame.getDatabaseShared());
        
      return glossUsers.getListModel(params);
        
    }    
    
    
    /**
    * Metoda zwraca referencje do obiektu odpowiedniego slownika
    * @return Ref. do obiektu slownika
    */
    @Override
    public GlossUsers getGlossary() {
        
       return glossUsers;
        
    }
  
    
    /**
     * Metoda zwraca panel z przyciskami dostępnych akcji 
     * @return Panel z przyciskami dostępnych akcji 
     */    
    @Override
    protected JPanel getButtonsPanel() {
        
      JPanel p = new JPanel(new GridLayout(1,3));
            
      
      editButton = new JButton("Edytuj");
      editButton.setEnabled(false);
      editButton.setFocusPainted(false);
      
      editButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
           
        new GlossUserEditModDialog(frame, GlossUsersDialog.this, getObjList().getSelectedIndex());
          
       }
     });               
      
      p.add(editButton);
      
      JButton addButton = new JButton("Dodaj nowy");
      addButton.setFocusPainted(false);
      
      addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
           
         new GlossUserEditNewDialog(frame, GlossUsersDialog.this);
          
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
      
      p.add(new JLabel("Login:"));
      filters.addField("login", new JTextField(12));  
      p.add(new JLabel("Nazwisko:"));
      filters.addField("surname", new JTextField(12));
      
    }
    
    
    @Override
    protected JButton getChooseButton() {
        
       return new JButton();
        
    }
    
    
   @Override
   protected JButton getEditButton() {
        
       return editButton; 
        
    }
   
   
  
   @Override
   @SuppressWarnings("serial")
   protected DefaultListCellRenderer getCustomListRenderer() {
       
       
     return new DefaultListCellRenderer() {
       
       @Override
       public Component getListCellRendererComponent(JList<?> list, Object value,
         int index, boolean isSelected, boolean cellHasFocus) {
           
          Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
          if (value!=null && ((UserData)value).isBlocked()) { comp.setForeground(Color.RED); }
          if (value!=null && ((UserData)value).getId().equals(frame.getUser().getId())) 
              { comp.setForeground(Color.BLUE);}
          return comp;
          
       }         
                  
     };
       
       
   }
       
    
    
}
