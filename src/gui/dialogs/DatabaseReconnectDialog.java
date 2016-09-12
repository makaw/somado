/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs;

import gui.GUI;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


/**
 *
 * Szablon obiektu wywołującego okienko dialogowe do ponownego łączenia z BD
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class DatabaseReconnectDialog extends InfoDialog {
    
    
   private static final String ERR = "Utracono po\u0142\u0105czenie ze zdaln\u0105 baz\u0105 danych. "
                          + "Spr\u00f3buj odnowi\u0107 po\u0142\u0105czenie lub uruchom program ponownie.\n\n"
                          + " Je\u017celi problem si\u0119 powt\u00f3rzy, "
                          + "skontaktuj si\u0119 z administratorem.";
    
    
   /**
    * Konstruktor, wywołanie konstruktora klasy nadrzędnej i wyświetlenie okienka
    * @param frame Referencja do interfejsu GUI
    */ 
   public DatabaseReconnectDialog(GUI frame) {
       
      super(frame, DialogType.WARNING, ERR, 210, true);
            
   } 
   
   
   /**
    * Dolny panel z przyciskami
    * @return Dolny panel
    */
   @Override
   protected JPanel buttonPanel() {
       
      frame.setDbLabel(false);
       
      // przycisk wznawiajacy polaczenie
      JButton conBut = new JButton(" Po\u0142\u0105cz ponownie ");
      conBut.setFocusPainted(false);
      conBut.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
      
            boolean reconnect = true;
            frame.getAppObserver().sendObject("db_connect", null);
            
            try {
             
              Thread.sleep(100);
              frame.getDatabaseShared().doQuery("SELECT 1;");
             
            } 
            
            catch (SQLException ex) {  reconnect = false; }
            
            catch (InterruptedException ex) {}
         
            if (reconnect) { 
                
                frame.refreshTabPanels();
                frame.setDbLabel(true);
                new InfoDialog(frame, "Po\u0142\u0105czenie wznowione poprawnie.", 180);
                dispose();
            }
              
          }
      });
      
      
      
      JPanel p = new JPanel();
      p.setLayout(new FlowLayout(FlowLayout.CENTER));
      p.setOpaque(false);
      p.add(conBut);
      p.add(new QuitButton(" Zako\u0144cz "));
      p.setBorder(new EmptyBorder(0, 0, 10, 0));
      
      return p;

   }
      
   

}

