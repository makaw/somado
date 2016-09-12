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
import datamodel.Vehicle;
import datamodel.VehicleModel;
import datamodel.docs.DocAudit;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import somado.Database;
import somado.User;

/**
 *
 * Szablon obiektu reprezentującego słownik pojazdów
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class GlossVehicles extends Glossary<Vehicle> implements IGlossaryEditable<Vehicle>  {
    
  /** komunikat ostatniego bledu (operacje DB) */
  private String lastError;    
  
  /**
   * Konstruktor  
   * @param database Ref. do BD
   */  
  public GlossVehicles(Database database) {
      
    super(database);
           
  }

  
  /**
   * Metoda dodaje nowy pojazd
   * @param vehicle Nowy element
   * @param user Ref. do obiektu zalogowanego uzytkownika
   * @return true jezeli OK
   */
  @Override
  public boolean addItem(Vehicle vehicle, User user) {
      
      
      try {
          vehicle.verify();
      } catch (Exception e) {
          lastError = e.getMessage();
          return false;
      }
      
      
      try {
          
         PreparedStatement ps = database.prepareQuery("INSERT INTO dat_vehicles "
                 + "(id, vehicle_model_id, year, registration_no, comment, capable, "
                 + "date_add, user_add_id) VALUES "
                 + "(NULL, ?, ?, ?, ?, ?, NOW(), ?);", true);
        
         ps.setInt(1, vehicle.getVehicleModel().getId());
         ps.setInt(2, vehicle.getYear());
         ps.setString(3, vehicle.getRegistrationNo());
         ps.setString(4, vehicle.getComment());
         ps.setBoolean(5, vehicle.isCapable());
         ps.setInt(6, user.getId());
         
         ps.executeUpdate();
         
         ResultSet rs = ps.getGeneratedKeys();
         if (rs.next()) vehicle.setId(rs.getInt(1));         
         
         Audit audit = new Audit(vehicle, vehicle, AuditDiff.AM_ADD, "Dodano pojazd");
         (new DocAudit(database, vehicle)).addElement(audit, user);          

         
      } catch (SQLException e) {
      
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        return false;
       
      }    
      
      
      return true;
      
  }
  
  
  /**
   * Metoda zwraca obiekt pojazdu dla zadanego klucza
   * @param elementId Klucz pojazdu
   * @param fromDb True jezeli klucz z BD, false jezeli slownika
   * @return Obiekt pojazdu
   */  
  public Vehicle getItem(int elementId, boolean fromDb) {
      
    return (fromDb) ? getItem(elementId) : super.getItem(elementId);
      
  }
  
  
  /**
   * Metoda zwraca obiekt pojazdu dla zadanego klucza z BD
   * @param elementId Klucz pojazdu w BD
   * @return Obiekt pojazdu
   */
  @Override
  public Vehicle getItem(int elementId) {
       
    Vehicle v = null;  
      
    try {        
        
    
     String query = "SELECT v.*, "
          + "vm.id AS vm_id, vm.name AS vm_name, vm.maximum_load AS vm_maximum_load, "
          + "vm.avg_fuel_consumption AS vm_avg_fuel_consumption "
          + "FROM dat_vehicles AS v "
          + "INNER JOIN glo_vehicle_models AS vm ON vm.id=v.vehicle_model_id "
          + " WHERE v.id = ? ";
            
      PreparedStatement ps = database.prepareQuery(query);
      ps.setInt(1, elementId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) v = new Vehicle(rs, new VehicleModel(rs, "vm_"));
     
    }
    
    catch (SQLException e) {
        
      System.err.println("B\u0142\u0105d SQL: "+e);
      lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        
    }
    
    return v;
       
  }
    
  
  
  
  /**
   * Metoda modyfikuje pozycje w slowniku
   * @param vehicle Dane po modyfikacji
   * @param user Ref. do obiektu zalogowanego uzytkownika
   * @return true jezeli OK
   */
  
  @Override
  public boolean updateItem(Vehicle vehicle, User user) {
      
      Vehicle vehicleOld = getItem(vehicle.getId());
      
      try {
        vehicle.verify();
      } catch (Exception e) {
        lastError = e.getMessage();
        return false;
      }
      
      try {
          
         PreparedStatement ps = database.prepareQuery("UPDATE dat_vehicles SET "
                 + "vehicle_model_id = ?, year = ?, registration_no = ?, comment = ?, capable = ?,"
                 + " date_mod = NOW(), user_mod_id = ? "
                 + "WHERE id = ? LIMIT 1;");
        
         ps.setInt(1, vehicle.getVehicleModel().getId());
         ps.setInt(2, vehicle.getYear());
         ps.setString(3, vehicle.getRegistrationNo());
         ps.setString(4, vehicle.getComment());
         ps.setBoolean(5, vehicle.isCapable());
         ps.setInt(6, user.getId());
         ps.setInt(7, vehicle.getId());
         
         ps.executeUpdate();                  
         
      } catch (SQLException e) {
      
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        return false;
       
      }                
          
      Audit audit = new Audit(vehicleOld, vehicle, vehicle, AuditDiff.AM_MOD, "Zmodyfikowano pojazd");
      (new DocAudit(database, vehicle)).addElement(audit, user); 
            
      return true;
      
  }
  
  
  /**
   * Metoda usuwa element ze slownika
   * @param vehicle Element do usuniecia
   * @param user Obiekt zalogowanego uzytkownika
   * @return true jezeli OK
   */
  @Override
  public boolean deleteItem(Vehicle vehicle, User user) {      
      
      try {
          
        database.doUpdate("START TRANSACTION;");
          
        PreparedStatement ps = database.prepareQuery("DELETE FROM dat_vehicles WHERE id = ? LIMIT 1;");
        ps.setInt(1, vehicle.getId());
        ps.executeUpdate();
        
        ps = database.prepareQuery("UPDATE dat_drivers SET vehicle_id=0 WHERE vehicle_id = ?;");
        ps.setInt(1, vehicle.getId());
        ps.executeUpdate();  
                
        
        database.doUpdate("COMMIT;");
        
        
      } catch (SQLException e) {
          
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        return false;
          
      }
      
      Audit audit = new Audit(vehicle, vehicle, AuditDiff.AM_DEL, "Usuni\u0119to pojazd");
      (new DocAudit(database, vehicle)).addElement(audit, user); 
      
      return true;
      
  }
  
  
  @Override
  public Vehicle getDefaultItem() {
        
    return null;
       
  }
       

  /**
   * Metoda pobiera model dla komponentu JComboBox
   * @return Model dla komponentu JComboBox
   */  
  @Override
  public DefaultComboBoxModel<Vehicle> getListModel() {
      

    DefaultComboBoxModel<Vehicle> listModel = new DefaultComboBoxModel<>();
      
    items.clear();
    // brak pojazdu
    items.add(new Vehicle());
    
    try {
        
      String query = "SELECT v.*, "
          + "vm.id AS vm_id, vm.name AS vm_name, vm.maximum_load AS vm_maximum_load, "
          + "vm.avg_fuel_consumption AS vm_avg_fuel_consumption "
          + "FROM dat_vehicles AS v "
          + "INNER JOIN glo_vehicle_models AS vm ON vm.id=v.vehicle_model_id;";      
      ResultSet rs = database.doQuery(query);
      while (rs.next()) {
             
         items.add(new Vehicle(rs, new VehicleModel(rs, "vm_")));
             
      }
         
    } catch (SQLException e) {
       
        System.err.println("B\u0142\u0105d SQL: "+e);
       
    }         
    
    Iterator<Vehicle> iterator = items.iterator();
    while (iterator.hasNext())  listModel.addElement(iterator.next());
      
    return listModel; 
    
      
  }  
  
  
  @Override
  public DefaultComboBoxModel<Vehicle> getListModel(Map<String, String> params) {

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

