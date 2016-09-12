/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package somado;

import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * Współdzielona (zdalna) baza danych MySQL
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class DatabaseShared extends Database {
   
    /**
     * Konstruktor obiektu bazodanowego, ustanawia połączenie
     * @throws SQLException Problem z połączeniem z BD
     * @throws java.lang.ClassNotFoundException Brak pliku sterownika
     */
    public DatabaseShared() throws SQLException, ClassNotFoundException, NullPointerException {
        
       Class.forName("com.mysql.jdbc.Driver");
       
       connection = DriverManager.getConnection("jdbc:" + Settings.getValue("db_url") + 
                 "?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=true", 
                 Settings.getValue("db_username"), Settings.getValue("db_password"));
       
       statement = connection.createStatement();
              
    }    
    
    
}
