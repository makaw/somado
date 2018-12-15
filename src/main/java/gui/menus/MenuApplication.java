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
import gui.dialogs.ConfirmDialog;
import gui.dialogs.SettingsDialog;
import somado.Lang;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;


/**
 *
 * Szablon obiektu budującego menu "Aplikacja" 
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class MenuApplication extends JMenu {
    
  /**
   * Konstruktor budujący menu "Aplikacja" aplikacji
   * @param frame Referencja do interfejsu GUI
   */    
  public MenuApplication(final GUI frame) {
       
    super(Lang.get("Menu.Application"));
    setMnemonic(KeyEvent.VK_A);    
      
    
    JMenuItem menuItem = new JMenuItem(Lang.get("Menu.Settings"));
    menuItem.setPreferredSize(new Dimension(160, 20));    
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
    add(menuItem);
    menuItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(final ActionEvent e) {
           
        new SettingsDialog(frame);
          
       }
    });              
        
    
    
    menuItem = new JMenuItem(Lang.get("Menu.Quit"));
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
    add(menuItem);
    menuItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(final ActionEvent e) {
           
        boolean res = new ConfirmDialog(frame, Lang.get("Menu.Quit.AreYouSure")).isConfirmed();
        if (res) frame.quitApp();
          
       }
    });   
    
  } 
    
    
}
