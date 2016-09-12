/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import javax.swing.JPanel;


/**
 *
 * Szablon obiektu panelu z obrazkowym tlem
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class BgPanel extends JPanel {
    
  /** Nazwa pliku z obrazem tła */  
  private final String imgName; 

  /**
   * Konstruktor
   * @param imgName Nazwa pliku z obrazem tła
   * @param layoutManager Obiekt zarządcy rozkładu
   */
  public BgPanel(String imgName, LayoutManager layoutManager) {
    
    super(layoutManager);  
    this.imgName = imgName;
      
  }
  
  
  /**
   * Konstruktor
   * @param imgName Nazwa pliku z obrazem tła
   */
  public BgPanel(String imgName) {
      
     this(imgName, new FlowLayout());  
      
  }
  
    
  /**
   * Przeciążona metoda klasy nadrzędnej komponentu JPanel. 
   * wykonywana w momencie wołania konstruktora klasy nadrzędnej.  
   * Wykorzystywana do narysowania planszy na komponencie.
   * @param g Referencja do urządzenia wyjściowego (ekran)
   */
  @Override
  public void paintComponent(Graphics g)  {
    
    Graphics2D g2D = (Graphics2D)g;   
    // rysowanie tla
    Image bg = ImageRes.getImage(imgName);
    if (bg != null)
      g2D.drawImage(ImageRes.getImage(imgName), 0, 0, this);
   
  }
     
    
    
}
