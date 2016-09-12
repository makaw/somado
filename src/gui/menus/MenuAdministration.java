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
import gui.dialogs.AuditDialog;
import gui.dialogs.SettingsDialog;
import gui.dialogs.gloss.GlossUsersDialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;


/**
 *
 * Szablon obiektu budującego menu "System" 
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class MenuAdministration extends JMenu {
    
  /**
   * Konstruktor budujący menu "System" aplikacji
   * @param frame Referencja do interfejsu GUI
   */    
  public MenuAdministration(final GUI frame) {
       
    super("Administracja");
    setMnemonic(KeyEvent.VK_D);    
    
    JMenuItem menuItem = new JMenuItem("Historia zmian");
    menuItem.setPreferredSize(new Dimension(160, 20));
    menuItem.setMnemonic(KeyEvent.VK_H);
    add(menuItem);    
    menuItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(final ActionEvent e) {
           
        new AuditDialog(frame);
          
       }
    });       
   
    
    menuItem = new JMenuItem("Ustawienia");
    menuItem.setPreferredSize(new Dimension(160, 20));
    menuItem.setMnemonic(KeyEvent.VK_T);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
    add(menuItem);
    menuItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(final ActionEvent e) {
           
        new SettingsDialog(frame);
          
       }
    });              
    
    
    menuItem = new JMenuItem("U\u017cytkownicy");
    menuItem.setMnemonic(KeyEvent.VK_U);
    add(menuItem);
    menuItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(final ActionEvent e) {
                  
        new GlossUsersDialog(frame);
        
        
       }
    });   
    
  } 
    
    
}
