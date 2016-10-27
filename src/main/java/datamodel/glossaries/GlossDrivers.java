/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package datamodel.glossaries;

import datamodel.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.swing.DefaultListModel;
import somado.Database;
import somado.User;

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
                 + "(id, vehicle_id, firstname, surname, comment, available, "
                 + "date_add, user_add_id) VALUES "
                 + "(NULL, ?, ?, ?, ?, ?, DATETIME('now'), ?);", true);
        
         ps.setInt(1, driver.getVehicle().getId());
         ps.setString(2, driver.getFirstname());
         ps.setString(3, driver.getSurname());
         ps.setString(4, driver.getComment());
         ps.setBoolean(5, driver.isAvailable());
         ps.setInt(6, user.getId());
         
         ps.executeUpdate();
         
         ResultSet rs = ps.getGeneratedKeys();
         if (rs.next()) driver.setId(rs.getInt(1));         
         rs.close();               

         
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
            + "vehicle_id = ? AND id <> ? AND vehicle_id > 0");
    ps.setInt(1, driver.getVehicle().getId());
    ps.setInt(2, driver.getId());
    
    ResultSet rs = ps.executeQuery();
    int num = rs.next() ? rs.getInt("num") : 1;
    rs.close();
    if (num > 0) 
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
        
    
      String query = "SELECT d.firstname, d.surname, d.id, d.comment, d.available, "
          + "vm.id AS vehicle_model_id, vm.name AS vehicle_model_name, "
          + "vm.maximum_load AS vehicle_model_maximum_load, "
          + "vm.avg_fuel_consumption AS vehicle_model_avg_fuel_consumption, "
          + "v.id AS vehicle_id, v.year AS vehicle_year, v.registration_no AS vehicle_registration_no, "
          + "v.comment AS vehicle_comment, v.capable AS vehicle_capable FROM dat_drivers AS d "
          + "LEFT OUTER JOIN dat_vehicles AS v ON v.id=d.vehicle_id "
          + "LEFT OUTER JOIN glo_vehicle_models AS vm ON vm.id=v.vehicle_model_id "
          + " WHERE " + (isVehicleId ? "v" : "d") + ".id = ?";

            
      PreparedStatement ps = database.prepareQuery(query);
      ps.setInt(1, elementId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) driver = new Driver(rs);
      rs.close();
      
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
      
      try {
        driver.verify();
        checkVehicle(driver);
      } catch (Exception e) {
        lastError = e.getMessage();
        return false;
      }
      
      try {
          
         database.begin();
          
         PreparedStatement ps;
         
         if (driver.getVehicle().getId()>0) {
           ps = database.prepareQuery("UPDATE dat_drivers SET vehicle_id=0 WHERE vehicle_id=?");
           ps.setInt(1, driver.getVehicle().getId());
           ps.executeUpdate();
         }
         
         ps = database.prepareQuery("UPDATE dat_drivers SET firstname = ?, surname = ?, "
                 + "vehicle_id = ?, comment = ?, available = ?,"
                 + " date_mod = DATETIME('now'), user_mod_id = ? WHERE id = ?;");
        
         ps.setString(1,  driver.getFirstname());
         ps.setString(2,  driver.getSurname());
         ps.setInt(3, driver.getVehicle().getId());
         ps.setString(4, driver.getComment());
         ps.setBoolean(5, driver.isAvailable());
         ps.setInt(6, user.getId());
         ps.setInt(7, driver.getId());
         
         ps.executeUpdate();  
         
         database.commit();
         
         
      } catch (SQLException e) {
      
        try {  
          database.rollback();
        }
        catch (SQLException ex2) {  System.err.println(ex2); }
        
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        return false;
       
      }                
          
            
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
          
        PreparedStatement ps = database.prepareQuery("DELETE FROM dat_drivers WHERE id = ?;");
        ps.setInt(1, driver.getId());
        ps.executeUpdate();                
        
      } catch (SQLException e) {
          
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        return false;
          
      }
       
      
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

