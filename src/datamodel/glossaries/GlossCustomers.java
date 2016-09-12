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
import datamodel.Customer;
import datamodel.docs.DocAudit;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.DefaultListModel;
import somado.Database;
import somado.Settings;
import somado.User;


/**
 *
 * Szablon obiektu reprezentującego słownik odbiorców towaru
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class GlossCustomers extends Glossary<Customer> implements IGlossaryEditable<Customer> {
    

  /** komunikat ostatniego bledu (operacje DB) */
  private String lastError;
  
  /**
   * Konstruktor
   * @param database Ref. do globalnej bazy danych
   */
  public GlossCustomers(Database database) {
    
     super(database);
           
  }
  
  
  /**
   * Metoda zwraca domyslny element slownika
   * @return Domyslny element slownika
   */
  @Override
  public Customer getDefaultItem() {
      
    try {
          
      PreparedStatement ps = database.prepareQuery("SELECT * FROM glo_customers WHERE id<>? ORDER BY name LIMIT 1;");
      ps.setInt(1, Settings.getDepot().getId());
      ResultSet rs = ps.executeQuery();
      if (rs.next()) return new Customer(rs);
          
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
  public DefaultListModel<Customer> getListModel(Map<String, String> params) {
      

    DefaultListModel<Customer> listModel = new DefaultListModel<>();
      
    items.clear();

    try {

      PreparedStatement ps = database.prepareQuery("SELECT * FROM glo_customers "
              + "WHERE id<>? AND name LIKE ? AND city LIKE ? ORDER BY name LIMIT ?;");
      ps.setInt(1, Settings.getDepot().getId());
      ps.setString(2, "%"+params.get("name")+"%");
      ps.setString(3, "%"+params.get("city")+"%");
      ps.setInt(4, dbLimit);
        
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
             
         items.add(new Customer(rs));             
             
       }
         
    } catch (SQLException e) {
       
        System.err.println("B\u0142\u0105d SQL: "+e);
       
    }         
    
    Iterator<Customer> iterator = items.iterator();
    while (iterator.hasNext()) listModel.addElement(iterator.next());      
      
    return listModel; 
    
      
  }  
  
  
  @Override
  public DefaultListModel<Customer> getListModel() {
      
     Map<String, String> dumb = new HashMap<>();
     dumb.put("name", "");      
     dumb.put("city", "");     
     return getListModel(dumb);
      
  }
  
  
  
  /**
   * Metoda dodaje nowy element do słownika
   * @param customer Nowy element
   * @param user Ref. do obiektu zalogowanego uzytkownika
   * @return true jezeli OK
   */
  @Override
  public boolean addItem(Customer customer, User user) {

      try {
         customer.verify();
      } catch (Exception e) {
         lastError = e.getMessage();
         return false;
      }

      
      try {
          
         PreparedStatement ps = database.prepareQuery("INSERT INTO glo_customers (id, name, "
                 + "street, city, postcode, longitude, latitude, date_add, user_add_id) VALUES " 
                 + "(NULL, ?, ?, ?, ?, ?, ?, NOW(), ? );", true);
         ps.setString(1, customer.getName());
         ps.setString(2, customer.getStreet());
         ps.setString(3, customer.getCity());
         ps.setString(4, customer.getPostcode());
         ps.setDouble(5, customer.getLongitude());         
         ps.setDouble(6, customer.getLatitude());
         ps.setInt(7, user.getId());

         ps.executeUpdate();
         
         ResultSet rs = ps.getGeneratedKeys();
         if (rs.next()) customer.setId(rs.getInt(1));
         
         Audit audit = new Audit(customer, customer, AuditDiff.AM_ADD, "Dodano odbiorc\u0119 towaru");
         (new DocAudit(database, customer)).addElement(audit, user); 
         
      } catch (SQLException e) {
      
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        return false;
       
      }    
      
      
      return true;
      
  }
  
  
  /**
   * Metoda modyfikuje pozycję w słowniku
   * @param customer Dane po modyfikacji
   * @param user Ref. do obiektu zalogowanego użytkownika
   * @return true jezeli OK
   */
  @Override
  public boolean updateItem(Customer customer, User user) {
      
      Customer cOld = getItem(getItemsIndex(customer.getId()));
      
      try {
         customer.verify();
      } catch (Exception e) {
         lastError = e.getMessage();
         return false;
      }
      
      
      try {
          
          PreparedStatement ps = database.prepareQuery("UPDATE glo_customers SET name = ?, street = ?, "
                  + "city = ?, postcode = ?, longitude = ?, latitude = ?, date_mod=NOW(), user_mod_id=? WHERE id=? LIMIT 1;");
          ps.setString(1, customer.getName());
          ps.setString(2, customer.getStreet());
          ps.setString(3, customer.getCity());
          ps.setString(4, customer.getPostcode());          
          ps.setDouble(5, customer.getLongitude());
          ps.setDouble(6, customer.getLatitude());
          ps.setInt(7, user.getId());
          ps.setInt(8, customer.getId());
                    
          ps.executeUpdate();        
          
          Audit audit = new Audit(cOld, customer, customer, AuditDiff.AM_MOD, "Zmodyfikowano " 
              + (customer.getId().equals(Settings.getDepot().getId()) ? " dane magazynu" : "odbiorc\u0119 towaru"));
          (new DocAudit(database, customer)).addElement(audit, user);           
          
          
      } catch (SQLException e) {
      
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        return false;
       
      }    
      
      return true;
      
  }  
  
  
  /**
   * (Niezaimplementowana) Metoda usuwa element ze słownika
   * @param element Element do usunięcia
   * @param user Ref. do obiektu zalogowanego użytkownika
   * @return true jezeli OK   
   */
  @Override
  public boolean deleteItem(Customer element, User user) {
  
    return false;
      
  }
  
  
   
  /**
   * Metoda zwraca ostatni błąd (logika lub BD)
   * @return Komunikat ostatniego błędu
   */
  @Override
  public String getLastError() {
      
    return lastError + "             ";  
      
  }
  
  
    
    
}
