/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package datamodel.glossaries;

import datamodel.Audit;
import datamodel.AuditDiff;
import datamodel.Driver;
import datamodel.docs.DocAudit;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.swing.DefaultListModel;
import somado.Database;
import somado.User;
import somado.UserRole;

/**
 *
 * Szablon obiektu reprezentującego słownik kierowców
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class GlossDrivers extends Glossary<Driver> implements IGlossaryEditable<Driver>  {
    
  /** komunikat ostatniego bledu (operacje DB) */
  private String lastError;    
  
  /**
   * Konstruktor  
   * @param database Ref. do BD
   */  
  public GlossDrivers(Database database) {
      
    super(database);
           
  }

  
  /**
   * Metoda dodaje nowego kierowcę
   * @param driver Nowy element
   * @param user Ref. do obiektu zalogowanego uzytkownika
   * @return true jezeli OK
   */
  @Override
  public boolean addItem(Driver driver, User user) {
      
      
      try {
          driver.verify();
          checkVehicle(driver);
      } catch (Exception e) {
          lastError = e.getMessage();
          return false;
      }
      
      
      try {
          
         PreparedStatement ps = database.prepareQuery("INSERT INTO dat_drivers "
                 + "(id, vehicle_id, user_id, comment, available, "
                 + "date_add, user_add_id) VALUES "
                 + "(NULL, ?, ?, ?, ?, NOW(), ?);", true);
        
         ps.setInt(1, driver.getVehicle().getId());
         ps.setInt(2, driver.getUserData().getId());
         ps.setString(3, driver.getComment());
         ps.setBoolean(4, driver.isAvailable());
         ps.setInt(5, user.getId());
         
         ps.executeUpdate();
         
         ResultSet rs = ps.getGeneratedKeys();
         if (rs.next()) driver.setId(rs.getInt(1));         
         
         Audit audit = new Audit(driver, driver, AuditDiff.AM_ADD, "Dodano kierowc\u0119");
         (new DocAudit(database, driver)).addElement(audit, user);          

         
      } catch (SQLException e) {
      
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        return false;
       
      }    
      
      
      return true;
      
  }
  
  
  /**
   * Metoda zwraca obiekt kierowcy dla zadanego klucza
   * @param elementId Klucz kierowcy
   * @param fromDb True jezeli klucz z BD, false jezeli slownika
   * @return Obiekt kierowcy
   */  
  public Driver getItem(int elementId, boolean fromDb) {
      
    return (fromDb) ? getItem(elementId) : super.getItem(elementId);
      
  }
  
  
  /**
   * Sprawdzenie czy pojazd jest wolny
   * @param driver Obiekt kierowcy
   * @throws Exception Wyjątek rzucany jeżeli pojazd jest zajęty
   */
  private void checkVehicle(Driver driver) throws Exception {
      
    PreparedStatement ps = database.prepareQuery("SELECT COUNT(*) AS num FROM dat_drivers WHERE "
            + "vehicle_id = ? AND user_id <> ? AND vehicle_id > 0");
    ps.setInt(1, driver.getVehicle().getId());
    ps.setInt(2, driver.getUserData().getId());
    
    ResultSet rs = ps.executeQuery();
    if (!rs.next() || rs.getInt("num")>0) 
      throw new Exception("Pojazd " + driver.getVehicle().getRegistrationNo() + " ju\u017c przyporz\u0105dkowano "
              + "do innego kierowcy.");          
      
  }
  
  
  /**
   * Metoda zwraca obiekt kierowcy dla zadanego klucza z BD lub dla zadanego klucza pojazdu z BD
   * @param elementId Klucz obiektu w BD
   * @param isVehicleId True jeżeli klucz pojazdu, false jeżeli klucz kierowcy
   * @return Obiekt kierowcy
   */
  public Driver getItemByVehicleOrDriverKey(int elementId, boolean isVehicleId) {
       
    Driver driver = null;  
      
    try {        
        
    
      String query = "SELECT u.id AS user_id, u.login AS user_login, u.firstname AS user_firstname, "
          + "u.surname AS user_surname, u.role AS user_role, u.blocked AS user_blocked, d.id, d.comment, d.available, "
          + "vm.id AS vehicle_model_id, vm.name AS vehicle_model_name, "
          + "vm.maximum_load AS vehicle_model_maximum_load, "
          + "vm.avg_fuel_consumption AS vehicle_model_avg_fuel_consumption, "
          + "v.id AS vehicle_id, v.year AS vehicle_year, v.registration_no AS vehicle_registration_no, "
          + "v.comment AS vehicle_comment, v.capable AS vehicle_capable FROM sys_users AS u "
          + "INNER JOIN dat_drivers AS d ON d.user_id=u.id "
          + "LEFT OUTER JOIN dat_vehicles AS v ON v.id=d.vehicle_id "
          + "LEFT OUTER JOIN glo_vehicle_models AS vm ON vm.id=v.vehicle_model_id "
          + " WHERE " + (isVehicleId ? "v" : "d") + ".id = ? AND u.role = ?";

            
      PreparedStatement ps = database.prepareQuery(query);
      ps.setInt(1, elementId);
      ps.setInt(2, UserRole.DRIVER.getId());
      ResultSet rs = ps.executeQuery();
      if (rs.next()) driver = new Driver(rs);
     
    }
    
    catch (SQLException e) {
        
      System.err.println("B\u0142\u0105d SQL: "+e);
      lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        
    }
    
    return driver;
       
  }
    
  
  
  
  /**
   * Metoda zwraca obiekt kierowcy dla zadanego klucza z BD
   * @param elementId Klucz kierowcy w BD
   * @return Obiekt kierowcy
   */
  @Override
  public Driver getItem(int elementId) {
      
    return getItemByVehicleOrDriverKey(elementId, false);
      
  }  
  
  
  
  
  /**
   * Metoda modyfikuje pozycje w slowniku
   * @param driver Dane po modyfikacji
   * @param user Ref. do obiektu zalogowanego uzytkownika
   * @return true jezeli OK
   */
  
  @Override
  public boolean updateItem(Driver driver, User user) {
      
      Driver driverOld = getItem(driver.getId());
      driver.setUserData(driverOld.getUserData());
      
      try {
        driver.verify();
        checkVehicle(driver);
      } catch (Exception e) {
        lastError = e.getMessage();
        return false;
      }
      
      try {
          
         database.doUpdate("START TRANSACTION;");
          
         PreparedStatement ps;
         
         if (driver.getVehicle().getId()>0) {
           ps = database.prepareQuery("UPDATE dat_drivers SET vehicle_id=0 WHERE vehicle_id=?");
           ps.setInt(1, driver.getVehicle().getId());
           ps.executeUpdate();
         }
         
         ps = database.prepareQuery("UPDATE dat_drivers SET "
                 + "vehicle_id = ?, comment = ?, available = ?,"
                 + " date_mod = NOW(), user_mod_id = ? WHERE id = ? LIMIT 1;");
        
         ps.setInt(1, driver.getVehicle().getId());
         ps.setString(2, driver.getComment());
         ps.setBoolean(3, driver.isAvailable());
         ps.setInt(4, user.getId());
         ps.setInt(5, driver.getId());
         
         ps.executeUpdate();  
         
         database.doUpdate("COMMIT;");
         
         
      } catch (SQLException e) {
      
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        return false;
       
      }                
          
      Audit audit = new Audit(driverOld, driver, driver, AuditDiff.AM_MOD, "Zmodyfikowano kierowc\u0119");
      (new DocAudit(database, driver)).addElement(audit, user); 
            
      return true;
      
  }
  
  
  /**
   * Metoda usuwa element ze slownika
   * @param driver Element do usuniecia
   * @param user Obiekt zalogowanego uzytkownika
   * @return true jezeli OK
   */
  @Override
  public boolean deleteItem(Driver driver, User user) {      
      
      try {
          
        PreparedStatement ps = database.prepareQuery("DELETE FROM dat_drivers WHERE id = ? LIMIT 1;");
        ps.setInt(1, driver.getId());
        ps.executeUpdate();                
        
      } catch (SQLException e) {
          
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        return false;
          
      }
      
      Audit audit = new Audit(driver, driver, AuditDiff.AM_DEL, "Usuni\u0119to kierowc\u0119");
      (new DocAudit(database, driver)).addElement(audit, user); 
      
      return true;
      
  }
  
  
  @Override
  public Driver getDefaultItem() {
        
    return null;
       
  }
       

  /**
   * Metoda pobiera model dla komponentu JList
   * @return Model dla komponentu JList
   */  
  @Override
  public DefaultListModel<Driver> getListModel() {
      
      return null;    
      
  }  
  
  
  @Override
  public DefaultListModel<Driver> getListModel(Map<String, String> params) {

      return getListModel();
      
  }
  
  
  
  /**
   * Metoda zwraca ostatni blad (logika lub BD)
   * @return Komunikat ostatniego bledu
   */
  @Override
  public String getLastError() {
      
    return lastError + "             ";  
      
  }
    
}

