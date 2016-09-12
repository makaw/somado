/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package somado;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.sqlite.SQLiteConfig;


/**
 *
 * Lokalna baza danych SQLite+Spatialite
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class DatabaseLocal extends Database {
    
    
   private static DatabaseLocal lastInstance; 
    
    /**
     * Konstruktor obiektu bazodanowego, ustanawia połączenie, nie może być wywołany bezpośrednio
     * @throws SQLException Problem z połączeniem z BD
     * @throws java.lang.ClassNotFoundException Brak pliku sterownika
     * @throws NullPointerException Problem ze sterownikiem
     * @throws somado.SettingsException Brak pliku bazy danych SQLite
     */
    private DatabaseLocal() throws SQLException, ClassNotFoundException, NullPointerException, SettingsException {
        
       File f = new File(Settings.getValue("db_local_name"));
       if(!f.exists() || f.isDirectory()) throw new SettingsException("Brak pliku lokalnej bazy danych "
               + "przestrzennych: " + f.getPath());
      
        
       Class.forName("org.sqlite.JDBC");         
       SQLiteConfig config = new SQLiteConfig();
       config.enableLoadExtension(true);
       connection = DriverManager.getConnection("jdbc:sqlite:"+Settings.getValue("db_local_name"), config.toProperties());
       
       statement = connection.createStatement();
       statement.setQueryTimeout(30);
       
       // załadowanie Spatialite
       statement.execute("SELECT load_extension('mod_spatialite')" );
       
       // sprawdzenie czy istnieje baza danych zawierająca cache geokodowania (jeśli nie, to zostanie utworzona)    
       // i dołączenie jej do bazy danych przestrzennych
       f = new File(Settings.getValue("db_localcache_name"));
       boolean cacheExists = (f.exists() && !f.isDirectory());
       
       statement.execute("ATTACH DATABASE '" + Settings.getValue("db_localcache_name") + "' AS ncache;");
       
       if (!cacheExists) {
          
           statement.execute("CREATE TABLE ncache.nominatim_cache (\"id\" INTEGER PRIMARY KEY, "
                   + "\"request\" TEXT, \"display_name\" TEXT, \"longitude\" REAL, \"latitude\" REAL,"
                   + "\"date_add\" TEXT);");
           
       }                  
       
              
    }    

    /**
     * Statyczna metoda zwracająca nową instancję z otwartym połączeniem
     * @return Nowa instancja
     * @throws SQLException Problem z połączeniem z BD
     * @throws java.lang.ClassNotFoundException Brak pliku sterownika
     * @throws NullPointerException Problem ze sterownikiem
     * @throws somado.SettingsException Brak pliku bazy danych SQLite
     */
    public static DatabaseLocal getInstance() throws SQLException, ClassNotFoundException,
                                                        NullPointerException, SettingsException {
      lastInstance = new DatabaseLocal();
      return lastInstance;
        
    }    
    
    /**
     * Metoda próbuje zamknąć połączenie dla ostatniej użytej instancji
     */
    public static void closeLastInstance() {
        
      try {
        lastInstance.close();  
      } 
      catch (Exception e) {}        
        
    }
    
    
}
