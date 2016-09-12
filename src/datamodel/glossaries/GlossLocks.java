/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package datamodel.glossaries;

import datamodel.IData;
import datamodel.Lock;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import somado.Database;
import somado.Somado;
import somado.User;

/**
 *
 * Szablon obiektu "słownika" blokad obiektu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class GlossLocks extends GlossaryAdapter<Lock> {
   

  /** Blokowany obiekt */
  private final IData object;
  
  
  /**
   * Konstruktor
   * @param database Ref. do DB
   * @param object Blokowany obiekt
   */
  public GlossLocks(Database database, IData object) {
      
    super(database);
    this.object = object;
           
  }
      
  
  /**
   * Metoda dodaje nowy obiekt do słownika
   * @param lock Nowy element
   * @param user Ref. do obiektu zalogowanego użytkownika
   * @return true jezeli OK
   */    
  @Override
  public boolean addItem(Lock lock, User user) {
      
    
    try {
        
      PreparedStatement ps = database.prepareQuery("REPLACE INTO sys_rec_locks (parent_type, parent_id, "
                + "pid, user_login, date_add) VALUES (?, ?, ?, ?, NOW());");
      ps.setString(1, lock.getParentType());
      ps.setInt(2, lock.getParentId());
      ps.setString(3, lock.getPid());
      ps.setString(4, lock.getUserLogin());
      ps.executeUpdate();
        
    }
      
    catch (SQLException e) {
        
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        return false;        
        
    }
    
    return true;
      
  }
  
  
  /**
   * Metoda dodaje nowy obiekt do słownika
   * @param element Nowy element
   * @return true jezeli OK
   */  
  public boolean addItem(Lock element) {
      
    return addItem(element, null);  
      
  }
  
  
  /**
   * Metoda usuwa element ze słownika
   * @param lock Element do usunięcia
   * @param user Ref. do obiektu zalogowanego użytkownika
   * @return true jezeli OK
   */  
  @Override
  public boolean deleteItem(Lock lock, User user) {
      
    if (lock == null) return false;
    
    try {
        
      PreparedStatement ps = database.prepareQuery("DELETE FROM sys_rec_locks WHERE "
              + "parent_type = ? AND parent_id = ?;");
      ps.setString(1, lock.getParentType());
      ps.setInt(2, lock.getParentId());
      ps.executeUpdate();
        
    }
      
    catch (SQLException e) {
        
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        return false;        
        
    }
    
    return true;       
      
      
  }
  
  
  /**
   * Metoda usuwa element ze słownika
   * @param element Element do usunięcia
   * @return true jezeli OK
   */    
  public boolean deleteItem(Lock element) {
      
    return deleteItem(element, null);  
      
  }
  
  
 
  
  /**
   * Metoda zwraca obiekt blokady dla podanego obiektu
   * (lub null jezeli obiekt niezablokowany)
   * @return Blokada sprawdzanego obiektu
   */
  @Override
  public Lock getDefaultItem() {
      
    Lock lock = null;
     
    try {
        
      PreparedStatement ps = database.prepareQuery("SELECT * FROM sys_rec_locks WHERE "
              + "parent_type = ? AND parent_id = ?;");
      ps.setString(1, object.getClass().getSimpleName());
      ps.setInt(2, object.getId());
      ResultSet rs = ps.executeQuery();
      if (rs.next()) lock = new Lock(rs);
        
    }
      
    catch (SQLException e) {
        
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();

    }
     
    return lock;
      
      
  }
  
  
  /**
   * Metoda statyczna, usuwa wszystkie blokady ustawione przez zalogowanego uzytkownika 
   * (w obecnej instancji aplikacji oraz z innych instancji starsze niż 3 minuty)
   * @param database Referencja do bazy danych
   * @param user Referencja do obiektu zalogowanego użytkownika
   */
  public static void clearItems(Database database, User user) {
     
    try {  
      PreparedStatement ps = database.prepareQuery("DELETE FROM sys_rec_locks WHERE user_login = ? "
              + "AND (pid = ? OR date_add < ADDDATE(NOW(), INTERVAL -3 MINUTE))");
      ps.setString(1, user.getLogin());
      ps.setString(2, Somado.APP_PID);
      ps.executeUpdate();
    }
     
    catch (SQLException e) {        
      System.err.println("B\u0142\u0105d SQL: "+e);      
    }
    
  }  
  
    
}
