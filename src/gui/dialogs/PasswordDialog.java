/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs;

import gui.BgPanel;
import gui.SimpleDialog;
import gui.GUI;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import somado.IConf;


/**
 *
 * Szablon obiektu wywołującego okienko dialogowe z zapytaniem o hasło 
 * i wyborem bazy danych
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class PasswordDialog extends SimpleDialog {
    
   /** Pole do wprowadzenia loginu usera */
   private final JTextField userField;        
   /** Pole do wprowadzenia hasła */
   private final JPasswordField passField;    
   
   
   /**
    * Konstruktor, wywołanie konstruktora klasy nadrzędnej i wyświetlenie okienka
    * @param frame Referencja do interfejsu GUI
    */ 
   public PasswordDialog(GUI frame) {
        
     super(frame);
     
     userField = new JTextField();
     passField = new JPasswordField(); 
     passField.setEchoChar('*');
     
     setTitle(IConf.APP_NAME +" - podaj login i has\u0142o");
     super.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
     
     super.showDialog(370, 300); 
            
   } 
   
   
    
   /**
    * Metoda wyświetlająca zawartość okienka
    */
   @Override
   protected void getContent()  {   

      JPanel mainPanel = new BgPanel("splash_nc.png"); 
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
      Color bg = new Color(255, 255, 255, 160);
      
      JLabel tmp = new JLabel();
      tmp.setBorder(new EmptyBorder(115, 0, 0, 0));
      mainPanel.add(tmp);
      JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
      sep.setBackground(bg);
      sep.setOpaque(true);
      mainPanel.add(sep);
      // panel z loginem i hasłem
      JPanel passPanel = new JPanel(new GridLayout(4, 2));
      passPanel.setBackground(bg);
      passPanel.setBorder(new EmptyBorder(25, 10, 0, 10));
      passPanel.setOpaque(true);
      JLabel txt = new JLabel("Login u\u017cytkownika:    ");
      txt.setFont(GUI.BASE_FONT);
      passPanel.add(txt);
      userField.setText(IConf.DEMO_USER);
      passPanel.add(userField);
      
      passPanel.add(new JLabel());
      passPanel.add(new JLabel());
      
      txt = new JLabel("Has\u0142o do aplikacji: ");
      txt.setFont(GUI.BASE_FONT);
      passPanel.add(txt);

      passField.setFocusable(true);
      passField.setText(IConf.DEMO_PASS);
      passPanel.add(passField);      
      
            
      userField.setBorder(new LineBorder((passField.getPassword().length>0) ? 
                    new Color(0x7a8a99) : Color.red));  
            
      passField.setBorder(new LineBorder((passField.getPassword().length>0) ? 
                    new Color(0x7a8a99) : Color.red));  


      ActionListener aList = new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {              
          submitPassword();             
        }
      };
              
      
      userField.addActionListener(aList);
      passField.addActionListener(aList);
           
      mainPanel.add(passPanel);
      

      JButton loginButton = new JButton("Zaloguj");
      loginButton.setFocusPainted(false);
      loginButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
           
            submitPassword();
             
         }
      });
      
      
      JButton quitButton = new QuitButton("Anuluj");
      
      JPanel p = new JPanel(new FlowLayout());
      p.setBackground(bg);
      p.setOpaque(true);
      p.setBorder(new EmptyBorder(5, 0, 5, 0)); 
      quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
      p.add(loginButton);
      p.add(new JLabel(" "));
      p.add(quitButton);
      
      
      mainPanel.add(p);
      
      add(mainPanel);
      
      
   }
   
   /**
    * Metoda przesyła do wątku głównego haslo aplikacyjne i nr bazy danych i zamyka okienko
    */
   private void submitPassword() {
     
     String userName = userField.getText();
     String appPass = String.valueOf(passField.getPassword());
     
     if (userName.isEmpty() || appPass.isEmpty()) return;
     
     frame.getAppObserver().sendObject("app_pass", new Object[] { userName, appPass });
     dispose();  
       
   }
    
   
   /**
    * Nadpisana metoda nadklasy JDialog, dodatkowo ustawia focus na pole do wpisania hasła
    * @param value 
    */
   @Override
   public void setVisible(boolean value) {
       
     passField.requestFocusInWindow();  
     super.setVisible(value);
     
   }   
   
   
   
}

