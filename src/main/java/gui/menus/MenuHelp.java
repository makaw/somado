/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package gui.menus;


import gui.GUI;
import gui.dialogs.AboutDialog;
import somado.Lang;

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
       
    super(Lang.get("Menu.Help") + " ");
    setMnemonic(super.getText().contains("P") ? KeyEvent.VK_P : KeyEvent.VK_H);    
    
    JMenuItem menuItem = new JMenuItem(Lang.get("Menu.About"));
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
