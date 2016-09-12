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
import datamodel.Product;
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


/**
 *
 * Szablon obiektu reprezentującego słownik produktów
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class GlossProducts extends Glossary<Product> implements IGlossaryEditable<Product> {
    

  /** komunikat ostatniego błędu (operacje DB) */
  private String lastError;
  
  
  public GlossProducts(Database database) {
    
     super(database);
           
  }
  
  
  /**
   * Metoda zwraca domyślny element słownika
   * @return Domyślny element słownika
   */
  @Override
  public Product getDefaultItem() {
      
    try {
          
      ResultSet rs = database.doQuery("SELECT * FROM glo_products LIMIT 1;");
      if (rs.next()) return new Product(rs);
          
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
  public DefaultListModel<Product> getListModel(Map<String, String> params) {
      

    DefaultListModel<Product> listModel = new DefaultListModel<>();
      
    items.clear();

    try {

      PreparedStatement ps = database.prepareQuery("SELECT * FROM glo_products "
              + "WHERE name LIKE ? ORDER BY name LIMIT ?;");
      ps.setString(1, "%"+params.get("name")+"%");
      ps.setInt(2, dbLimit);
        
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
             
         items.add(new Product(rs));             
             
       }
         
    } catch (SQLException e) {
       
        System.err.println("B\u0142\u0105d SQL: "+e);
       
    }         
    
    Iterator<Product> iterator = items.iterator();
    while (iterator.hasNext()) listModel.addElement(iterator.next());      
      
    return listModel; 
    
      
  }  
  
  
  @Override
  public DefaultListModel<Product> getListModel() {
      
     Map<String, String> dumb = new HashMap<>();
     dumb.put("name", "");          
     return getListModel(dumb);
      
  }
  
  
  
  /**
   * Metoda dodaje nowy element do słownika
   * @param product Nowy element
   * @param user Ref. do obiektu zalogowanego uzytkownika
   * @return true jezeli OK
   */
  @Override
  public boolean addItem(Product product, User user) {

      try {
         product.verify();
      } catch (Exception e) {
         lastError = e.getMessage();
         return false;
      }

      
      try {
          
         PreparedStatement ps = database.prepareQuery("INSERT INTO glo_products (id, name, "
                 + "weight, available, date_add, user_add_id) VALUES " 
                 + "(NULL, ?, ?, ?, NOW(), ? );", true);
         ps.setString(1, product.getName());
         ps.setDouble(2, product.getWeight());
         ps.setBoolean(3, product.isAvailable());
         ps.setInt(4, user.getId());

         ps.executeUpdate();
         
         ResultSet rs = ps.getGeneratedKeys();
         if (rs.next()) product.setId(rs.getInt(1));
         
         Audit audit = new Audit(product, product, AuditDiff.AM_ADD, "Dodano produkt");
         (new DocAudit(database, product)).addElement(audit, user); 
         
      } catch (SQLException e) {
      
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        return false;
       
      }    
      
      
      return true;
      
  }
  
  
  /**
   * Metoda modyfikuje pozycję w słowniku
   * @param product Dane po modyfikacji
   * @param user Ref. do obiektu zalogowanego uzytkownika
   * @return true jezeli OK
   */
  @Override
  public boolean updateItem(Product product, User user) {
      
      Product pOld = getItem(getItemsIndex(product.getId()));
      
      try {
         product.verify();
      } catch (Exception e) {
         lastError = e.getMessage();
         return false;
      }
      
      
      try {
          
          PreparedStatement ps = database.prepareQuery("UPDATE glo_products SET name=?, "
                  + "weight=?, available=?, date_mod=NOW(), user_mod_id=? WHERE id=? LIMIT 1;");
          ps.setString(1, product.getName());
          ps.setDouble(2, product.getWeight());
          ps.setBoolean(3, product.isAvailable());
          ps.setInt(4, user.getId());
          ps.setInt(5, product.getId());
                    
          ps.executeUpdate();        
          
          Audit audit = new Audit(pOld, product, product, AuditDiff.AM_MOD, "Zmodyfikowano produkt");
          (new DocAudit(database, product)).addElement(audit, user);           
          
          
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
   * @param user Ref. do obiektu zalogowanego uzytkownika
   * @return true jezeli OK   
   */
  @Override
  public boolean deleteItem(Product element, User user) {
  
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
