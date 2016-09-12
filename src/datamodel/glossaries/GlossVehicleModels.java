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
import datamodel.VehicleModel;
import datamodel.docs.DocAudit;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.DefaultListModel;
import somado.Database;
import somado.IConf;
import somado.User;


/**
 *
 * Szablon obiektu reprezentującego słownik modeli pojazdów
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class GlossVehicleModels extends Glossary<VehicleModel> implements IGlossaryEditable<VehicleModel> {
    

  /** komunikat ostatniego błędu (operacje DB) */
  private String lastError;
  
  
  public GlossVehicleModels(Database database) {
    
     super(database);
           
  }
  
  
  /**
   * Metoda zwraca domyślny element słownika
   * @return Domyślny element słownika
   */
  @Override
  public VehicleModel getDefaultItem() {
      
    try {
          
      ResultSet rs = database.doQuery("SELECT * FROM glo_vehicle_models LIMIT 1;");
      if (rs.next()) return new VehicleModel(rs);
          
    } catch (SQLException e) {
       System.err.println("B\u0142\u0105d SQL: "+e);
    }
     
    return null;
      
  }
  
  
  
  /**
   * Metoda pobiera model dla komponentu JList
   * @param params Zestaw (mapa) parametrów - filtrów
   * @return Model dla komponentu JList
   */  
  @Override
  public DefaultListModel<VehicleModel> getListModel(Map<String, String> params) {
      

    DefaultListModel<VehicleModel> listModel = new DefaultListModel<>();
      
    items.clear();

    try {

      PreparedStatement ps = database.prepareQuery("SELECT * FROM glo_vehicle_models "
              + "WHERE name LIKE ? ORDER BY name LIMIT ?;");
      ps.setString(1, "%"+params.get("name")+"%");
      ps.setInt(2, dbLimit);
        
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
             
         items.add(new VehicleModel(rs));             
             
       }
         
    } catch (SQLException e) {
       
        System.err.println("B\u0142\u0105d SQL: "+e);
       
    }         
    
    Iterator<VehicleModel> iterator = items.iterator();
    while (iterator.hasNext()) listModel.addElement(iterator.next());      
      
    return listModel; 
    
      
  }  
  
  
  @Override
  public DefaultListModel<VehicleModel> getListModel() {
      
     Map<String, String> dumb = new HashMap<>();
     dumb.put("name", "");          
     return getListModel(dumb);
      
  }
  
  
  
  /**
   * Metoda dodaje nowy element do slownika
   * @param vehicleModel Nowy element
   * @param user Ref. do obiektu zalogowanego uzytkownika
   * @return true jezeli OK
   */
  @Override
  public boolean addItem(VehicleModel vehicleModel, User user) {

      try {
         vehicleModel.verify();
      } catch (Exception e) {
         lastError = e.getMessage();
         return false;
      }

      
      try {
          
         PreparedStatement ps = database.prepareQuery("INSERT INTO glo_vehicle_models (id, name, "
                 + "maximum_load, avg_fuel_consumption, date_add, user_add_id) VALUES " 
                 + "(NULL, ?, ?, ?, NOW(), ? );", true);
         ps.setString(1, vehicleModel.getName());
         ps.setDouble(2, vehicleModel.getMaximumLoad());
         ps.setDouble(3, vehicleModel.getAvgFuelConsumption());
         ps.setInt(4, user.getId());

         ps.executeUpdate();
         
         ResultSet rs = ps.getGeneratedKeys();
         if (rs.next()) vehicleModel.setId(rs.getInt(1));
         
         Audit audit = new Audit(vehicleModel, vehicleModel, AuditDiff.AM_ADD, "Dodano model pojazdu");
         (new DocAudit(database, vehicleModel)).addElement(audit, user); 
         
      } catch (SQLException e) {
      
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        return false;
       
      }    
      
      
      return true;
      
  }
  
  
  /**
   * Metoda modyfikuje pozycje w slowniku
   * @param vehicleModel Dane po modyfikacji
   * @param user Ref. do obiektu zalogowanego uzytkownika
   * @return true jezeli OK
   */
  @Override
  public boolean updateItem(VehicleModel vehicleModel, User user) {
      
      VehicleModel vmOld = getItem(getItemsIndex(vehicleModel.getId()));
      
      try {
         vehicleModel.verify();
      } catch (Exception e) {
         lastError = e.getMessage();
         return false;
      }
      
      
      try {
          
          PreparedStatement ps = database.prepareQuery("UPDATE glo_vehicle_models SET name=?, "
                  + "maximum_load=?, avg_fuel_consumption=?, date_mod=NOW(), user_mod_id=? WHERE id=? LIMIT 1;");
          ps.setString(1, vehicleModel.getName());
          ps.setDouble(2, vehicleModel.getMaximumLoad());
          ps.setDouble(3, vehicleModel.getAvgFuelConsumption());
          ps.setInt(4, user.getId());
          ps.setInt(5, vehicleModel.getId());
                    
          ps.executeUpdate();        
          
          Audit audit = new Audit(vmOld, vehicleModel, vehicleModel, AuditDiff.AM_MOD, "Zmodyfikowano model pojazdu");
          (new DocAudit(database, vehicleModel)).addElement(audit, user);           
          
          
      } catch (SQLException e) {
      
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        return false;
       
      }    
      
      return true;
      
  }  
  
  
  /**
   * (Niezaimplementowana) Metoda usuwa element ze slownika
   * @param element Element do usuniecia
   * @param user Ref. do obiektu zalogowanego uzytkownika
   * @return true jezeli OK   
   */
  @Override
  public boolean deleteItem(VehicleModel element, User user) {
  
    return false;
      
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
