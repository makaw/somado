/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.mapview;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;

  
/**
 *
 * Panel z informacją o licencji OSM
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */    
@SuppressWarnings("serial")
public class OSMCreditPanel extends JPanel {
        
   /** Użyta czcionka */
   private final static Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 11); 
    
   /**
    * Konstruktor
    */
   public OSMCreditPanel() {
            
      super(new FlowLayout(FlowLayout.CENTER));
      JLabel lab = new JLabel("Map data \u00a9 ");
      lab.setFont(font);
      lab.setForeground(new Color(0x505050));
      add(lab);
                
      lab = new JLabel("OpenStreetMap");
      lab.setCursor(new Cursor(Cursor.HAND_CURSOR));
      Map<TextAttribute, Integer> fontAttr = new HashMap<>();
      fontAttr.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
      lab.setFont(font.deriveFont(fontAttr));
      lab.setForeground(new Color(0x330066));
      add(lab);
                
      addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          try {
             new BrowserLauncher().openURLinBrowser("http://www.openstreetmap.org/copyright");
          }  catch (BrowserLaunchingInitializingException | UnsupportedOperatingSystemException ex) {
             System.err.println(ex);
          }
        }
      });                
                
      lab = new JLabel(" contributors");
      lab.setFont(font);
      lab.setForeground(new Color(0x505050));
      add(lab);                
                
      setBackground(new Color(210, 210, 210, 170));
      setOpaque(true);
      setPreferredSize(new Dimension(270, 24));            

            
   }                            
    
    
 }
    