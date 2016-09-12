/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package somado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * Ogólny szablon obiektu służącego do komunikacji z bazą danych
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public abstract class Database {
    
    /** Połączenie z bazą danych */
    protected Connection connection;
    /** Interfejs do wykonywania zapytań */
    protected Statement statement;  
    
    /**
     * Metoda tworzy prekompilowane zapytanie
     * @param query Tresc zapytania SQL
     * @param genKeys jezeli true, to BD ma zwracac utworzone klucze
     * @return Uchwyt do prekompilowanego zapytania
     * @throws SQLException Blad SQL
     */
    public PreparedStatement prepareQuery(String query, boolean genKeys) throws SQLException {
        
       if (genKeys)
         return connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
       else 
         return connection.prepareStatement(query);
        
    }
       
       
    /**
     * Metoda tworzy prekompilowane zapytanie, nie zwraca kluczy
     * @param query Tresc zapytania SQL
     * @return Uchwyt do prekompilowanego zapytania
     * @throws SQLException Blad SQL
     */    
    public PreparedStatement prepareQuery(String query) throws SQLException {
    
      return prepareQuery(query, false);
      
    }
        
    
    /**
     * Metoda wykonuje zapytanie (SELECT)
     * @param query Zapytanie SQL
     * @return Zestaw wyników
     * @throws SQLException Błąd SQL
     */
    public ResultSet doQuery(String query) throws SQLException {
        
       return statement.executeQuery(query);
   
    }
    
    /**
     * Metoda wykonuje zapytanie (INSERT/UPDATE/DELETE)
     * @param query Zapytanie SQL
     * @throws SQLException Błąd SQL
     */
    public void doUpdate(String query) throws SQLException {
        
       statement.executeUpdate(query);
   
    }    
    
    
    /**
     * Metoda zamyka połączenie z BD
     * @throws SQLException Błąd SQL
     */
    public void close() throws SQLException {
        
      statement.close();  
      connection.close();
   
    }
    
   
    
}
