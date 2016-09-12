/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package somado;

  
/** 
 * 
 * Wyjątek rzucany w razie braku istotnych ustawień 
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class SettingsException extends Exception {

  /**
   * Konstruktor
   * @param message Komunikat błędu 
   */
  public SettingsException(String message) {
        
      super(message);
                
  }
  
  

}

