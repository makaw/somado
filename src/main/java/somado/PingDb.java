/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package somado;

import gui.GUI;
import gui.dialogs.DatabaseReconnectDialog;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;


/**
 *
 * Szablon obiektu timera wysyłającego pingi do serwera BD
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class PingDb extends Timer {
    
  /** Co ile sekund wysyłać ping do serwera */  
  private final static double PING_SEC = 3.0;    
  
  
  public PingDb(final GUI frame, final Database database) {
  
    // uruchomienie cyklicznego zadania
    scheduleAtFixedRate(new TimerTask() {
    @Override
    public void run() {
        
         // ping do BD
         try {
             
           database.doQuery("SELECT 1;");
             
         } catch (SQLException e) {
             
           new DatabaseReconnectDialog(frame);

         }
         
     }
    }, Math.round(PING_SEC*1000.0), Math.round(PING_SEC*1000.0));   
  
  }
  
  
  /**
   * Metoda zatrzymuje zadanie
   */
  public void stop() {
      
     cancel();
     purge();
      
  }
  

}
