/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs;

import datamodel.UserData;
import gui.GUI;
import gui.SimpleDialog;
import gui.formfields.FormRowPad;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import somado.IConf;


/**
 *
 * Szablon obiektu wywołującego okienko dialogowe do zmiany hasła użytkownika
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class ChangePasswordDialog extends SimpleDialog {
    
  /** Dane użytkownika */  
  private UserData user;
  
  /** Czy to zmiana własnego hasła */
  private final boolean ownPass;
  
   /**
    * Konstruktor 
    * @param frame Referencja do interfejsu GUI
    * @param user Dane użytkownika
    */ 
   public ChangePasswordDialog(GUI frame, UserData user) {
        
     super(frame, IConf.APP_NAME + "- zmiana has\u0142a");
     ownPass = user.getId().equals(frame.getUser().getId());
     if (!ownPass) setTitle(getTitle()+" u\u017cytkownika " + user.getLogin());
     this.user = user;
     if (!ownPass && !frame.getUser().isAdmin()) return;
     super.showDialog(450, 200); 
            
   } 
   
   
   /**
    * Konstruktor do zmiany hasła zalogowanego użytkownika
    * @param frame Referencja do interfejsu GUI
    */ 
   public ChangePasswordDialog(GUI frame) {
        
     this(frame, new UserData(frame.getUser()));
            
   }    
   
   
    
   /**
    * Metoda wyświetlająca zawartość okienka
    */
   @Override
   protected void getContent()  {   

      
      JPanel p = new JPanel();
      p.setOpaque(false);
      p.setBorder(new EmptyBorder(5, 5, 5, 5));
      p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
      
      final JPasswordField currPassField = new JPasswordField(8);
      currPassField.setEchoChar('*');
      
      if (ownPass) {
          
        p.add(new FormRowPad("Obecne has\u0142o:", currPassField, 180));
                         
      }
      
      else {
      
        JLabel txt = new JLabel(user.getLogin());       
        txt.setFont(GUI.BASE_FONT);
        p.add(new FormRowPad("Login u\u017cytkownika:", txt, 180));     

      }
      
      final JPasswordField newPassField = new JPasswordField(8);
      newPassField.setEchoChar('*');
      p.add(new FormRowPad("Nowe has\u0142o:", newPassField, 180));
      
      final JPasswordField newPass2Field = new JPasswordField(8);
      newPass2Field.setEchoChar('*');
      p.add(new FormRowPad("Potwierd\u017a nowe has\u0142o:", newPass2Field, 180));
            
      
      JButton submitButton = new JButton("Zmie\u0144 has\u0142o");
      submitButton.setFocusPainted(false);
      submitButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
                     
           String tmp = user.getPassword();
           UserData usr = new UserData(user);
           usr.setPassword(String.valueOf(newPassField.getPassword()));
           
           try {
               
             if (ownPass && !tmp.equals(String.valueOf(currPassField.getPassword())))
               throw new Exception("Podano nieprawid\u0142owe aktualne has\u0142o.");
             usr.verifyPass(String.valueOf(newPass2Field.getPassword()));
             usr.changePassword(usr.getPassword(), frame.getDatabaseShared());
             if (ownPass) frame.getUser().setPassword(usr.getPassword());
             
             new InfoDialog(frame, "Hasło zosta\u0142o zmienione.", 140);
             dispose();
             
           }
           catch (Exception ex) {
               
             new ErrorDialog(frame, ex.getMessage() != null ? ex.getMessage() : "null");  
               
           }
           
             
         }
      });
      
      
      JButton quitButton = new SimpleDialog.CloseButton("Anuluj");
      
      JPanel p2 = new JPanel(new FlowLayout());
      p2.setOpaque(false);
      p2.setBorder(new EmptyBorder(0, 0, 5, 0)); 
      quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
      p2.add(submitButton);
      p2.add(new JLabel(" "));
      p2.add(quitButton);
      p.add(new JLabel(" "));
      p.add(p2);
      add(p);
      
      
   }
   
   
   
  
    
    
    
}
