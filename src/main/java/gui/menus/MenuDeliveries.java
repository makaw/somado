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
import somado.Lang;

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
       
    super(Lang.get("Menu.Deliveries") + " ");
    setMnemonic(KeyEvent.VK_D);
    
    
    JMenuItem menuItem = new JMenuItem(Lang.get("Menu.NewOrder"));
    menuItem.setPreferredSize(new Dimension(190, 20));
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
    add(menuItem);
    menuItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(final ActionEvent e) {
           
        frame.setSelectedDataPanel(GUI.TAB_ORDERS);
        new OrderEditNewDialog(frame);
          
       }
    });             
    
    
    menuItem = new JMenuItem(Lang.get("Menu.NewDelivery"));
    menuItem.setPreferredSize(new Dimension(160, 20));
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
    add(menuItem);
    menuItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(final ActionEvent e) {
           
        new DeliveryOpenDialog(frame);
          
       }
    });           
     
    
    add(new JSeparator());
    
    menuItem = new JMenuItem(Lang.get("Menu.CarModels"));
    menuItem.setPreferredSize(new Dimension(160, 20));
    add(menuItem);    
    menuItem.addActionListener(new ActionListener() {
     @Override
     public void actionPerformed(final ActionEvent e) {
           
         new GlossVehicleModelsDialog(frame);
          
       }
     });          
     
     
    menuItem = new JMenuItem(Lang.get("Menu.Products"));
    add(menuItem);    
    menuItem.addActionListener(new ActionListener() {
     @Override
     public void actionPerformed(final ActionEvent e) {
           
         new GlossProductsDialog(frame);
          
       }
     });    
     
    menuItem = new JMenuItem(Lang.get("Menu.Receivers"));
    add(menuItem);    
    menuItem.addActionListener(new ActionListener() {
     @Override
     public void actionPerformed(final ActionEvent e) {
           
         new GlossCustomersDialog(frame);
          
       }
     });           
    
   }
  
    
    
}
