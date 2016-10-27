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
import gui.dialogs.DeliveryOpenDialog;
import gui.dialogs.gloss.GlossCustomersDialog;
import gui.dialogs.gloss.GlossProductsDialog;
import gui.dialogs.gloss.GlossVehicleModelsDialog;
import gui.dialogs.tableforms.OrderEditNewDialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;


/**
 *
 * Szablon obiektu budującego menu "Dostawy" aplikacji
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class MenuDeliveries extends JMenu {
    
   /**
    * Konstruktor budujący menu "Dostawy" aplikacji
    * @param frame Referencja do GUI 
    */    
   public MenuDeliveries(final GUI frame) {
       
    super("Dostawy ");
    setMnemonic(KeyEvent.VK_D);
    
    
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
    
    menuItem = new JMenuItem("Modele pojazd\u00f3w");
    menuItem.setPreferredSize(new Dimension(160, 20));
    menuItem.setMnemonic(KeyEvent.VK_M);
    add(menuItem);    
    menuItem.addActionListener(new ActionListener() {
     @Override
     public void actionPerformed(final ActionEvent e) {
           
         new GlossVehicleModelsDialog(frame);
          
       }
     });          
     
     
    menuItem = new JMenuItem("Produkty");
    menuItem.setMnemonic(KeyEvent.VK_P);
    add(menuItem);    
    menuItem.addActionListener(new ActionListener() {
     @Override
     public void actionPerformed(final ActionEvent e) {
           
         new GlossProductsDialog(frame);
          
       }
     });    
     
    menuItem = new JMenuItem("Odbiorcy towaru");
    menuItem.setMnemonic(KeyEvent.VK_O);
    add(menuItem);    
    menuItem.addActionListener(new ActionListener() {
     @Override
     public void actionPerformed(final ActionEvent e) {
           
         new GlossCustomersDialog(frame);
          
       }
     });           
    
   }
  
    
    
}
