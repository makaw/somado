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
 * Wyjątek rzucany w razie problemów z autoryzacją 
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class UserException extends Exception {

  /** Kod błędu */
  private final int code;  
  /** Kod dla błędu autoryzacji */
  public final static int AUTH = 1;
      
  /**
   * Konstruktor
   * @param code Kod błędu 
   */
  public UserException(int code) {
        
      super();
      this.code = code;
                
  }
        
  @Override
  public String getMessage() {
        
    switch (code) {
          
        default: 
        case AUTH:
            return "B\u0142\u0105d uwierzytelnienia: brak uprawnie\u0144.";
                    
    }          
        
  }
    
    
  public int getCode() {     
    return code;        
  }

}    
    
    