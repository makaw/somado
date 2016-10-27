/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package gui;

import java.awt.Image;

import javax.swing.ImageIcon;


/**
 *
 * "Statyczna" klasa udostępniająca obrazki
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public final class ImageRes  {


  private ImageRes() {} 
  
  
  /**
   * Statyczna metoda pobierająca ikonę ze wskazanego pliku z /resources/img
   * @param fileName Nazwa pliku z /resources/img/
   * @return Ikona wczytana ze wskazanego pliku
   */
  public static ImageIcon getIcon(String fileName) {
      
    ImageIcon icon = null; 
      
    try {    	
      icon = (new ImageIcon(ImageRes.class.getClassLoader().getResource("img/"+fileName)));
    }
    catch (NullPointerException e) {
       System.err.println("Brak pliku /resources/img/"+fileName);      
    }
        
    return icon;  
    
  }
  
 
   
  /**
   * Statyczna metoda pobierająca obrazek ze wskazanego pliku z /resources/img
   * @param fileName Nazwa pliku z /resources/img/
   * @return Obrazek wczytany ze wskazanego pliku
   */ 
  public static Image getImage(String fileName) {
      
    ImageIcon ico = getIcon(fileName);  
    return (ico != null) ? getIcon(fileName).getImage() : null;
      
  }  
  
  
  

}
