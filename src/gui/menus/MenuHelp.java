/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.menus;


import gui.GUI;
import gui.dialogs.AboutDialog; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.KeyEvent;


/**
 *
 * Szablon obiektu budującego menu "Pomoc" aplikacji
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class MenuHelp extends JMenu {
    
   /**
    * Konstruktor budujący menu "Pomoc" aplikacji
    * @param frame Referencja do interfejsu GUI 
    */    
   public MenuHelp(final GUI frame) {
       
    super("Pomoc ");
    setMnemonic(KeyEvent.VK_P);    
    
    JMenuItem menuItem = new JMenuItem("O programie");
    menuItem.setMnemonic(KeyEvent.VK_O);
    add(menuItem);
    menuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        // wywołanie okienka z informacjami o aplikacji  
        new AboutDialog(frame);
      }
    });        
    
   } 
    
    
}
