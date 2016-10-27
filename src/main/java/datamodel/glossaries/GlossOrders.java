/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package datamodel.glossaries;

import datamodel.Order;
import datamodel.OrderItem;
import datamodel.OrderState;
import datamodel.Product;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import javax.swing.DefaultListModel;
import somado.Database;
import somado.User;

/**
 *
 * Szablon obiektu reprezentującego słownik zamówień
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class GlossOrders extends Glossary<Order> implements IGlossaryEditable<Order>  {
    
  /** komunikat ostatniego błędu (operacje DB) */
  private String lastError;    
  
  /**
   * Konstruktor  
   * @param database Ref. do BD
   */  
  public GlossOrders(Database database) {
      
    super(database);
           
  }

  
  /**
   * Metoda dodaje nowe zamówienie
   * @param order Nowy element
   * @param user Ref. do obiektu zalogowanego uzytkownika
   * @return true jezeli OK
   */
  @Override
  public boolean addItem(Order order, User user) {
      
      
      try {
          order.verify();
      } catch (Exception e) {
          lastError = e.getMessage();
          return false;
      }
      
      
      try {
          
         database.begin();
          
         // numer zamówienia jest tworzony w bazie przez trigger
         PreparedStatement ps = database.prepareQuery("INSERT INTO dat_orders "
                 + "(id, state_id, customer_id, comment, "
                 + "date_add, user_add_id) VALUES "
                 + "(NULL, ?, ?, ?, DATETIME('now'), ?);", true);
        
         ps.setInt(1, order.getState().getId());
         ps.setInt(2, order.getCustomer().getId());
         ps.setString(3, order.getComment());
         ps.setInt(4, user.getId());
         
         ps.executeUpdate();
         
         ResultSet rs = ps.getGeneratedKeys();
         if (!rs.next()) {         
           database.rollback();
           throw new SQLException("b\u0142ad zapisu"); 
         }             
         order.setId(rs.getInt(1));  
         rs.close();
         
         insertProducts(order);
    
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
   * Zapisanie listy produktów do BD
   * @param order Obiekt zamówienia
   * @throws SQLException Błąd SQL
   */
  private void insertProducts(Order order) throws SQLException {
      
     Iterator<OrderItem> iterator = order.getProducts().iterator();
     while (iterator.hasNext()) {
       OrderItem item = iterator.next();
       PreparedStatement ps = database.prepareQuery("INSERT INTO dat_products_to_orders "
               + "(product_id, order_id, items) VALUES (?, ?, ?);");
       ps.setInt(1, item.getProduct().getId());
       ps.setInt(2, order.getId());
       ps.setInt(3, item.getItemsNumber());
       ps.executeUpdate();
     }        
      
  }
  
  
  /**
   * Metoda zwraca obiekt zamówienia dla zadanego klucza
   * @param elementId Klucz zamówienia
   * @param fromDb True jezeli klucz z BD, false jezeli slownika
   * @return Obiekt zamówienia
   */  
  public Order getItem(int elementId, boolean fromDb) {
      
    return (fromDb) ? getItem(elementId) : super.getItem(elementId);
      
  }
  
  
  /**
   * Metoda zwraca obiekt zamówienia dla zadanego klucza z BD
   * @param elementId Klucz zamówienia w BD
   * @return Obiekt zamówienia
   */
  @Override
  public Order getItem(int elementId) {
       
    Order order = null;  
      
    try {        
        
    
      String query = "SELECT o.id, o.number, o.state_id, o.comment, o.date_add, "
            + "c.id AS customer_id, c.name AS customer_name, c.street AS customer_street, "
            + "c.city AS customer_city, c.postcode AS customer_postcode, c.longitude AS customer_longitude, "
            + "c.latitude AS customer_latitude "
            + "FROM dat_orders AS o "
            + "INNER JOIN glo_customers AS c ON c.id=o.customer_id "
            + "WHERE o.id = ? ;";

            
      PreparedStatement ps = database.prepareQuery(query);
      ps.setInt(1, elementId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
                    
         order = new Order(rs);      
      
         query = "SELECT p.*, po.items FROM glo_products AS p "
              + "INNER JOIN dat_products_to_orders AS po ON po.product_id = p.id "
              + "WHERE po.order_id = ? ORDER BY p.name;";
         ps = database.prepareQuery(query);
         ps.setInt(1, order.getId());
         ResultSet rs2 = ps.executeQuery(); 
         while (rs2.next()) 
             order.getProducts().add(new OrderItem(new Product(rs2), rs2.getInt("items")));
         
         rs2.close();
         
      }
      
      rs.close();
     
    }
    
    catch (SQLException e) {
        
      System.err.println("B\u0142\u0105d SQL: "+e);
      lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        
    }
    
    return order;
       
  }
      
  
  /**
   * Metoda modyfikuje pozycję w słowniku
   * @param order Dane po modyfikacji
   * @param state Nowy stan zamówienia
   * @param user Ref. do obiektu zalogowanego użytkownika
   * @return true jezeli OK
   */  
  public boolean changeState(Order order, OrderState state, User user) {
      
    order.setState(state);
      
    try {  
      
      PreparedStatement ps = database.prepareQuery("UPDATE dat_orders SET state_id = ?, "
              + "date_state_mod = DATETIME('now'), date_mod = DATETIME('now'),"
              + " user_mod_id = ? WHERE id = ?;");
        
      ps.setInt(1, state.getId());
      ps.setInt(2, user.getId());
      ps.setInt(3, order.getId());
      ps.executeUpdate();
      
    } catch (SQLException e) {
      
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        return false;
       
    }                
          
    
    return true;      
         
      
  }  
  
  
  /**
   * Metoda modyfikuje pozycję w słowniku
   * @param order Dane po modyfikacji
   * @param user Ref. do obiektu zalogowanego użytkownika
   * @return true jezeli OK
   */  
  @Override
  public boolean updateItem(Order order, User user) {
      
      
      try {
        order.verify();
      } catch (Exception e) {
        lastError = e.getMessage();
        return false;
      }
      
      try {
          
         database.begin();
          
         PreparedStatement ps = database.prepareQuery("UPDATE dat_orders SET customer_id = ?, "
                 + "comment = ?, date_mod = DATETIME('now'), user_mod_id = ? WHERE id = ?;");
        
         ps.setInt(1, order.getCustomer().getId());
         ps.setString(2, order.getComment());
         ps.setInt(3, user.getId());
         ps.setInt(4, order.getId());
         
         ps.executeUpdate();  
         
         ps = database.prepareQuery("DELETE FROM dat_products_to_orders WHERE order_id = ? ");
         ps.setInt(1, order.getId());
         ps.executeUpdate();
         
         insertProducts(order);
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
   * Metoda usuwa element ze słownika
   * @param order Element do usunięcia
   * @param user Obiekt zalogowanego użytkownika
   * @return true jezeli OK
   */
  @Override
  public boolean deleteItem(Order order, User user) {      
      
      try {
          
        database.begin();
          
        PreparedStatement ps = database.prepareQuery("DELETE FROM dat_orders WHERE id = ?;");
        ps.setInt(1, order.getId());
        ps.executeUpdate();      
        
        ps = database.prepareQuery("DELETE FROM dat_products_to_orders WHERE order_id = ?;");
        ps.setInt(1, order.getId());
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
  
  
  @Override
  public Order getDefaultItem() {
        
    return null;
       
  }
       

  /**
   * Metoda pobiera model dla komponentu JList (tylko nowe, do otwarcia dostawy)
   * @return Model dla komponentu JList
   */  
  @Override
  public DefaultListModel<Order> getListModel() {
      
      return null;    
      
  }  
  
  
  @Override
  public DefaultListModel<Order> getListModel(Map<String, String> params) {

      return getListModel();
      
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

