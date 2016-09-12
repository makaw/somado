/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JLabel;

import javax.swing.JPanel;


/**
 *
 * Szablon obiektu pustego centralnego panelu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class EmptyPanel extends JPanel {
    
  /** Szerokosc calego ekranu */  
  final static public int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
  
  /** Czy wyswietlane dane musza byc odswiezone ? */
  private boolean changed;  
  
  
  /** Wewnetrzna klasa - etykieta bez pogrubienia, opcjonalnie kolor */
  public class NormLabel extends JLabel {
      
    NormLabel(String text, boolean redCol) {
        
       super(text);
       if (redCol) setForeground(new Color(0x990000));
       Font f = getFont();
       setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));

    }  

    NormLabel(String text) {
        
      this(text, false);  
        
    }
      
  }  
  
  
  
  public EmptyPanel() {
    
    super();
    changed = false;
    setLayout(new FlowLayout(FlowLayout.LEFT));
    setBackground(new Color(0xe3, 0xf1, 0xff));
    setPreferredSize(new Dimension(GUI.FRAME_WIDTH, GUI.FRAME_HEIGHT));
    
  }    
  
  
  
  public boolean isChanged() {
      return changed;
  }

  public void setChanged(boolean changed) {
      this.changed = changed;
  }  
    
    
    
    
}
