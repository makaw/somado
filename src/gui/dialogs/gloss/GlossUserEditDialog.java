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
import datamodel.UserData;
import datamodel.glossaries.GlossUsers;
import gui.GUI;
import gui.dialogs.ChangePasswordDialog;
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
import gui.dialogs.ErrorDialog;
import gui.formfields.CustomerField;
import gui.formfields.FormRowPad;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import somado.IConf;
import somado.UserRole;


/**
 *
 * Szablon okienka do edycji/dodawania użytkowników systemowych
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public abstract class GlossUserEditDialog extends EditLockableDataDialog {
    
  /** Referencja do obiektu powiazanego slownika */  
  final protected GlossUsers glossUsers;
  /** Referencja do nadrzednego okienka */
  final private GlossDialog<UserData> parentDialog;
  /** Modyfikowany/wprowadzany obiekt(dane) */
  final protected UserData user;
  /** Czy modyfikowany użytkownik to zalogowany użytkownik */
  final private boolean ownData;
    
  /**
   * Konstruktor: istniejacy element slownika
   * @param frame Referencja do GUI
   * @param parentDialog Obiekt nadrzednego okienka
   * @param title Naglowek okienka
   * @param usrIndex Indeks slownikowy elementu
   */
  public GlossUserEditDialog(GUI frame, GlossDialog<UserData> parentDialog, String title, int usrIndex) {
        
    super(frame, IConf.APP_NAME + " - " + title);
    glossUsers = (GlossUsers)(parentDialog.getGlossary());
    this.parentDialog = parentDialog;
    this.user = (usrIndex == -1) ? new UserData() : glossUsers.getItem(usrIndex);
    ownData = user.getId().equals(frame.getUser().getId());
    checkLock(this.user);
    super.showDialog(480, usrIndex == -1 ? 420 : 385);
         
  }
  
  
  /**
   * Konstruktor: nowy element slownika
   * @param frame Referencja do GUI
   * @param parentDialog Obiekt nadrzednego okienka
   * @param title Naglowek okienka
   */
  public GlossUserEditDialog(GUI frame, GlossDialog<UserData> parentDialog, String title) {
        
    this(frame, parentDialog, title, -1);
         
  }  
   
   
  
  /**
   * Metoda zapisujaca do BD
   * @param user Dane do zapisania
   * @return true jezeli OK
   */
  protected abstract boolean saveItem(UserData user);
  
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
         
        super(frame, "Dane u\u017cytkownika", user);

        final JTextField loginField = new JTextField(22);
        
        JLabel txt = new JLabel(user.getLogin());
        txt.setFont(GUI.BASE_FONT);
        
        dataTabPane.add(new FormRowPad("Login: ", user.getId()>0 ? txt : loginField));
        
        final JPasswordField passField = new JPasswordField(8);
        passField.setEchoChar('*');
      
      
        final JPasswordField pass2Field = new JPasswordField(8);
        pass2Field.setEchoChar('*');
        
        if (user.getId() == 0) {
          dataTabPane.add(new FormRowPad("Has\u0142o:", passField));
          dataTabPane.add(new FormRowPad("Potwierd\u017a has\u0142o:", pass2Field));
        }
        
        final JTextField surnameField = new JTextField(22);
        surnameField.setText(user.getSurname());
        surnameField.setEditable(!locked);
        dataTabPane.add(new FormRowPad("Nazwisko:", surnameField));
        
        final JTextField firstnameField = new JTextField(22);
        firstnameField.setText(user.getFirstname());
        firstnameField.setEditable(!locked);
        dataTabPane.add(new FormRowPad("Imi\u0119:", firstnameField));        

        final JComboBox<UserRole> roleField  = new JComboBox<>();      
        roleField.setEnabled(!locked && !ownData);
        roleField.setModel(new DefaultComboBoxModel<>(UserRole.values()));
        roleField.setFont(GUI.BASE_FONT);               
        roleField.setSelectedItem(user.getRole());
        dataTabPane.add(new FormRowPad("Rola w systemie:", roleField));
        
        
        final CustomerField customerField = new CustomerField(frame, new Customer(), 24);
        customerField.setEditable(user.getRole() == UserRole.CUSTOMER && !locked);
        if (user.getRole() == UserRole.CUSTOMER && user.getCustomer() != null)
           customerField.setSelectedElement(user.getCustomer());
        dataTabPane.add(new FormRow("Odbiorca towaru:", customerField));      
        
        roleField.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
              if (e.getStateChange() == ItemEvent.SELECTED) {
               
                if (roleField.getSelectedItem() != UserRole.CUSTOMER) {
                  customerField.setSelectedElement(new Customer());
                  customerField.setEditable(false);
                }
                else {
                  customerField.setEditable(true);
                }
                  
              }                  
            }
            
        });
        
        final JCheckBox blockedField = new JCheckBox(" konto zablokowane");
        blockedField.setSelected(user.isBlocked());
        blockedField.setOpaque(false);
        blockedField.setFocusPainted(false);
        blockedField.setEnabled(!locked && !ownData);
        blockedField.setFont(GUI.BASE_FONT);  
        dataTabPane.add(new FormRowPad("Zablokowany:", blockedField));
        
               
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
  
        
        saveButton.addActionListener(new ActionListener() {
            
          @Override
          public void actionPerformed(final ActionEvent e) {
           
            UserData userData = new UserData(user.getId(), (user.getId()==0 ? loginField.getText() : user.getLogin()), 
                 firstnameField.getText(), surnameField.getText(), 
                 ownData ? user.getRole() : (UserRole)roleField.getSelectedItem(), 
                 ownData ? false : blockedField.isSelected());
                       
            if (userData.getRole() == UserRole.CUSTOMER && customerField.getSelectedElement() != null)
               userData.setCustomer((Customer) customerField.getSelectedElement());
                        
            if (user.getId()==0) {
                
               userData.setPassword(String.valueOf(passField.getPassword()));
                
               try {
                 userData.verifyPass(String.valueOf(pass2Field.getPassword()));
               }  
               catch (Exception ex) {
                 new ErrorDialog(frame, ex.getMessage());
                 return;
               }                
                
            }             
            
            userData.setUserAttr();
            
            
            if (!saveItem(userData)) {
              
              new ErrorDialog(frame, glossUsers.getLastError());
                
            }
             
            else {
                
              refreshItemsList(); 
              dispose();
              
            }        
          
          }

        });            
        
        saveButton.setEnabled(!locked);
        
        
        JButton passButton = new JButton(" Zmie\u0144 has\u0142o ");
        passButton.setFocusPainted(false);
        passButton.setEnabled(!locked && !ownData);
  
        
        passButton.addActionListener(new ActionListener() {
            
          @Override
          public void actionPerformed(final ActionEvent e) {
           
            new ChangePasswordDialog(frame, user);
                           
          }

        });            
        
        
        p.add(saveButton);
        
        if (user.getId()>0) {
          p.add(new JLabel(" "));
          p.add(new JLabel(" "));
          p.add(passButton);
        }
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
  protected GlossDialog<UserData> getParentDialog() {
      
     return parentDialog; 
      
  }
  
    
    
}
