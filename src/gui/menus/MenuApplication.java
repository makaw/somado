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
import gui.dialogs.ChangePasswordDialog;
import gui.dialogs.ConfirmDialog;
import gui.dialogs.DeliveryOpenDialog;
import gui.dialogs.tableforms.OrderEditNewDialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.KeyEvent;
import javax.swing.JSeparator;
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
       
    super("Aplikacja");
    setMnemonic(KeyEvent.VK_A);    
    
    JMenuItem menuItem = new JMenuItem("Nowe zam\u00f3wienie");
    menuItem.setPreferredSize(new Dimension(190, 20));
    menuItem.setMnemonic(KeyEvent.VK_Z);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
    add(menuItem);
    menuItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(final ActionEvent e) {
           
        frame.setSelectedDataPanel(GUI.TAB_ORDERS);
        new OrderEditNewDialog(frame);
          
       }
    });             
    
    
    menuItem = new JMenuItem("Nowa dostawa");
    menuItem.setPreferredSize(new Dimension(160, 20));
    menuItem.setMnemonic(KeyEvent.VK_D);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
    add(menuItem);
    menuItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(final ActionEvent e) {
           
        new DeliveryOpenDialog(frame);
          
       }
    });              
    
    add(new JSeparator());
    
    menuItem = new JMenuItem("Zmiana has\u0142a");
    menuItem.setPreferredSize(new Dimension(160, 20));
    menuItem.setMnemonic(KeyEvent.VK_H);
    add(menuItem);
    menuItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(final ActionEvent e) {
                   
        new ChangePasswordDialog(frame);
        
       }
    });            
    
    
    menuItem = new JMenuItem("Koniec");
    menuItem.setMnemonic(KeyEvent.VK_K);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.CTRL_MASK));
    add(menuItem);
    menuItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(final ActionEvent e) {
           
        boolean res = new ConfirmDialog(frame, "Czy na pewno zako\u0144czy\u0107 ?").isConfirmed();
        if (res) frame.quitApp();
          
       }
    });   
    
  } 
    
    
}
