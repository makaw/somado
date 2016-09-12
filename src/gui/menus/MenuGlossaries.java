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
import gui.dialogs.gloss.GlossCustomersDialog;
import gui.dialogs.gloss.GlossProductsDialog;
import gui.dialogs.gloss.GlossVehicleModelsDialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;


/**
 *
 * Szablon obiektu budującego menu "Słowniki" aplikacji
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class MenuGlossaries extends JMenu {
    
   /**
    * Konstruktor budujący menu "Słowniki" aplikacji
    * @param frame Referencja do GUI 
    */    
   public MenuGlossaries(final GUI frame) {
       
    super("S\u0142owniki ");
    setMnemonic(KeyEvent.VK_S);
    
    JMenuItem menuItem = new JMenuItem("Modele pojazd\u00f3w");
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
