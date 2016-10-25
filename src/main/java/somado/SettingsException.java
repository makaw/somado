/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
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

