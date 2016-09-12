/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;


/**
 *
 * Szablon obiektu przycisku z ikona dla formularzy
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class IconButton extends JButton {
    
     /**
      * Konstruktor
      * @param image Obrazek na przycisku
      * @param toolTipText Tekst podpowiedzi
      */ 
     public IconButton(ImageIcon image, String toolTipText) {
         
         super("", image);
         setPreferredSize(new Dimension(30, 30));
         setMaximumSize(new Dimension(30, 30));
         setFocusPainted(false);
         setToolTipText(toolTipText); 
         
     }  
          
    
}
