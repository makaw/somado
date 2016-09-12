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
import datamodel.UserData;
import datamodel.docs.DocAudit;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.DefaultListModel;
import somado.Database;
import somado.User;
import somado.UserRole;


/**
 *
 * Szablon obiektu reprezentującego słownik użytkowników systemowych
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class GlossUsers extends Glossary<UserData> implements IGlossaryEditable<UserData> {

  /** komunikat ostatniego błędu (operacje DB) */
  private String lastError = ""; 
  
    
  /**
   * Konstruktor
   * @param database Ref. do BD
   */
  public GlossUsers(Database database) {
      
    super(database);
    
  }    

      
    
  /**
   * Metoda zwraca domyslny (pierwszy na liscie) element slownika
   * @return Domyslny element slownika
   */
  @Override
  public UserData getDefaultItem() {
      
      return null;
      
  }
  

  

  /**
   * Metoda pobiera model dla komponentu JComboBox
   * @param params Lista parametrów
   * @return Model dla komponentu JComboBox
   */  
  @Override
  public DefaultListModel<UserData> getListModel(Map<String, String> params) {
      

    DefaultListModel<UserData> listModel = new DefaultListModel<>();
      
    items.clear();

    try {
             
      PreparedStatement ps = database.prepareQuery("SELECT u.*, IF (c.id IS NULL, 0, 1) AS cid, "
              + "c.name AS customer_name, c.street AS customer_street, "
              + "c.city AS customer_city, c.postcode AS customer_postcode, c.longitude AS customer_longitude, "
              + "c.latitude AS customer_latitude "
              + "FROM sys_users AS u LEFT OUTER JOIN glo_customers AS c ON c.id = u.customer_id "
              + "WHERE u.login LIKE ? AND u.surname LIKE ?;");
      ps.setString(1, "%"+params.get("login")+"%");
      ps.setString(2, "%"+params.get("surname")+"%");
      
      ResultSet rs = ps.executeQuery();      
      
      while (rs.next()) {
             
         UserData tmp = new UserData(rs);
         if (tmp.getRole() == UserRole.CUSTOMER && rs.getBoolean("cid")) 
           tmp.setCustomer(new Customer(rs, "customer_"));
           
         items.add(tmp);
             
      }
         
    } catch (SQLException e) {
       
        System.err.println("B\u0142\u0105d SQL: "+e);
       
    }         
    
    Iterator<UserData> iterator = items.iterator();
    while (iterator.hasNext())  listModel.addElement(iterator.next());
      
    return listModel; 
    
      
  }  
  
  
  @Override
  public DefaultListModel<UserData> getListModel() {

     Map<String, String> dumb = new HashMap<>();
     dumb.put("login", "");          
     dumb.put("surname", "");
     return getListModel(dumb);
      
  }
  
  
  /**
   * Sprawdzenie czy podany login już istnieje
   * @param login Login
   * @throws Exception Wyjątek rzucany jeżeli login zajęty
   */
  private void checkLogin(UserData user) throws Exception {
      
    PreparedStatement ps = database.prepareQuery("SELECT COUNT(*) AS c FROM sys_users WHERE login = ? AND id <> ?");
    ps.setString(1, user.getLogin());
    ps.setInt(2, user.getId());
    ResultSet rs = ps.executeQuery();
    rs.next();
    if (rs.getInt("c")>0) 
     throw new Exception("Ju\u017c istnieje u\u017cytkownik o loginie: " + user.getLogin());
      
  }


  /**
   * Metoda dodaje nowego użytkownika systemowego
   * @param userData Nowy element
   * @param user Ref. do obiektu zalogowanego uzytkownika
   * @return true jezeli OK
   */
  @Override
  public boolean addItem(UserData userData, User user) {
      
      try {
          
         userData.verify();
         checkLogin(userData);        
         
      } catch (Exception e) {
         lastError = e.getMessage();
         return false;
      }

      
      try {
          
         PreparedStatement ps = database.prepareQuery("INSERT INTO sys_users (id, login, passwd, "
                 + "firstname, surname, role, blocked, customer_id, date_add, user_add_id) VALUES " 
                 + "(NULL, ?, PASSWORD(?), ?, ?, ?, ?, ?, NOW(), ? );", true);
         
         ps.setString(1, userData.getLogin());
         ps.setString(2, userData.getPassword());
         ps.setString(3, userData.getFirstname());
         ps.setString(4, userData.getSurname());
         ps.setInt(5, userData.getRole().getId());
         ps.setBoolean(6, userData.isBlocked());  
         ps.setInt(7, (userData.getRole()==UserRole.CUSTOMER && userData.getCustomer() != null) ? 
                 userData.getCustomer().getId() : 0);
         ps.setInt(8, user.getId());

         ps.executeUpdate();
         
         ResultSet rs = ps.getGeneratedKeys();
         if (rs.next()) userData.setId(rs.getInt(1));         
         
         Audit audit = new Audit(userData, userData, AuditDiff.AM_ADD, "Dodano u\u017cytkownika");
         (new DocAudit(database, userData)).addElement(audit, user); 
         
      } catch (SQLException e) {
      
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        return false;
       
      }    
      
      
      return true;
      
  }  
  
  
  
  
  /**
   * Metoda modyfikuje użytkownika systemowego
   * @param userData Dane po modyfikacji
   * @param user Ref. do obiektu zalogowanego uzytkownika
   * @return true jezeli OK
   */
  @Override
  public boolean updateItem(UserData userData, User user) {
      
      
      UserData uOld = getItem(getItemsIndex(userData.getId()));
      uOld.setUserAttr();
      
      try {

         userData.verify();
         checkLogin(userData);                       
          
      } catch (Exception e) {
          
         lastError = e.getMessage();
         return false;
      }
      
      
      try {
          
          PreparedStatement ps = database.prepareQuery("UPDATE sys_users SET firstname = ?, surname = ?, "
                  + "role = ?, blocked = ?, customer_id = ?, date_mod=NOW(), user_mod_id=? WHERE id=? LIMIT 1;");
          ps.setString(1, userData.getFirstname());
          ps.setString(2, userData.getSurname());
          ps.setInt(3, userData.getRole().getId());
          ps.setBoolean(4, userData.isBlocked());
          ps.setInt(5, (userData.getRole()==UserRole.CUSTOMER && userData.getCustomer() != null) ? 
                 userData.getCustomer().getId() : 0);
          ps.setInt(6, user.getId());
          ps.setInt(7, userData.getId());
                    
          ps.executeUpdate();        
          
          // usunięcie z listy kierowców w razie zmiany roli
          if (uOld.getRole() == UserRole.DRIVER && userData.getRole() != UserRole.DRIVER) {
              
             ps = database.prepareQuery("DELETE FROM dat_drivers WHERE user_id = ? LIMIT 1"); 
             ps.setInt(1, userData.getId());
             ps.executeUpdate();
             
          }
          
          Audit audit = new Audit(uOld, userData, userData, AuditDiff.AM_MOD, "Zmodyfikowano dane u\u017cytkownika");
          (new DocAudit(database, userData)).addElement(audit, user);           
          
          
      } catch (SQLException e) {
      
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        return false;
       
      }    
      
      return true;
      
  }  
    
  
  

  @Override
  public String getLastError() {
     return lastError;
  }


  @Override
  public boolean deleteItem(UserData element, User user) {
    return false;
  }
  
 
  
    
}
