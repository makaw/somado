/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.formfields;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;


/**
 *
 * Szablon obiektu zakładek w okienku z formularzem
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class FormTabbedPane extends JTabbedPane {
    
   /** Kolor tla okna */ 
   private final Color bgColor; 
   
    
   /**
    * Konstruktor
    * @param bgColor Kolor tła
    */
   public FormTabbedPane(Color bgColor) {
              
     super(JTabbedPane.TOP);  
     setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
     
     this.bgColor = bgColor;
     UIManager.put("TabbedPane.selected", bgColor);
     UIManager.put("TabbedPane.contentBorderInsets", new Insets(1, 1, 1, 1));
              
     setFocusable(false);    
    
   }
   
   
   @Override
   public Component add(String title, Component p) {
       
     if (p instanceof JPanel) {          
       ((JPanel)p).setBorder(new EmptyBorder(5, 5, 5, 5));
       ((JPanel)p).setLayout(new BoxLayout((JPanel)p, BoxLayout.Y_AXIS)); 
     }
     p.setBackground(bgColor);
      
     return super.add(title, p);         
       
   }
   
    
}
