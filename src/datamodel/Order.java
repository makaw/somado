/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package datamodel;



import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import somado.IConf;
import somado.Settings;


/**
 *
 * Szablon obiektu reprezentującego zamówienie
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class Order implements IDataEditable {    
    
   /** Klucz w tabeli BD */ 
   private Integer id;
   /** Numer zamówienia */
   private String number;
   /** Lista produktów */
   private List<OrderItem> products;
   /** Odbiorca zamówienia */
   private Customer customer;   
   /** Stan zamówienia */
   private OrderState state;
   /** Data zamówienia */
   private Date dateAdd;
   /** Komentarz */
   private String comment;
   
   
   /**
    * Konstruktor
    * @param id Id rekordu w bazie
    * @param number Numer zamówienia
    * @param products Lista produktów
    * @param customer Obiekt odbiorcy towaru
    * @param state Stan zamówienia
    * @param comment Komentarz
    * @param dateAdd Data i godzina zamówienia
    */
   public Order(int id, String number, List<OrderItem> products, Customer customer,
           OrderState state, String comment, Date dateAdd) {
       
     this.id = id;
     this.number = number;
     this.products = products;
     this.customer = customer;
     this.state = state;
     this.comment = comment;
     this.dateAdd = dateAdd;
     
   }
   
   /**
    * Konstruktor obiektu zamówienia (z BD)
    * @param rs Zestaw wyników zapytania do BD
    * @param prefix Prefix pol dot. zamówienia w zapytaniu
    * @throws SQLException Błąd SQL
    */
   public Order(ResultSet rs, String prefix) throws SQLException {
       
     this.id = rs.getInt(prefix + "id");
     this.number = rs.getString(prefix + "number");
     this.customer = new Customer(rs, prefix + "customer_");
     this.state = OrderState.get(rs.getInt("state_id"));
     this.products = new ArrayList<>();
     this.comment = rs.getString("comment");
     try {
       this.dateAdd = Settings.DATETIME_FORMAT.parse(rs.getString("date_add"));
     } catch (ParseException e) {
       this.dateAdd = null;
       System.err.println(e.getMessage());
     }    
       
   }
   
   /**
    * Konstruktor obiektu zamówienia (z BD)
    * @param rs Zestaw wyników zapytania do BD
    * @throws SQLException Błąd SQL
    */    
    public Order(ResultSet rs) throws SQLException {
        
      this(rs, "");  
        
    }
    
    
    /**
     * Konstruktor tworzący kopię obiektu
     * @param order Kopiowany obiekt
     */
    public Order(Order order) {
       
      this(order.id, order.number, order.products, order.customer, order.state, order.comment, order.dateAdd);
       
    }   
    
    
    /**
     * Konstruktor pustego obiektu dla wskazanego odbiorcy (lub magazynu)
     * @param customer Odbiorca towaru 
     */
    public Order(Customer customer) {
        
      this(0, "", new ArrayList<OrderItem>(), customer, OrderState.NEW, "", new Date());
        
    }
    
   
    /**
     * Konstruktor pustego obiektu (nowego)
     */
    public Order() {
        
      this(0, "", new ArrayList<OrderItem>(), new Customer(), OrderState.NEW, "", new Date());
        
    }       
   

   @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<OrderItem> getProducts() {
        return products;
    }

    public void setProducts(List<OrderItem> products) {
        this.products = products;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    

   
   @Override
   public String toString() {
       
     return number;
       
   }
   
   
   public double getTotalWeight() {
       
      double totalWeight = 0.0;      
      Iterator<OrderItem> iterator = products.iterator();
      while (iterator.hasNext()) {
        totalWeight += iterator.next().getTotalWeight();
      }
      
      return totalWeight;
       
   }

   
   @Override
   public void verify() throws Exception {
        
      if (customer.getId()==0)  throw new Exception("Nie wybrano odbiorcy towaru.");    
      if (products.isEmpty()) throw new Exception("Nie wybrano produkt\u00f3w.");  

      double totalWeight = getTotalWeight();
      
      if (totalWeight > IConf.MAX_VEHICLE_MAXLOAD*1000.0) 
        throw new Exception("\u0141\u0105czna waga produkt\u00f3w to:\n" + String.format("%.2f", totalWeight)
                  + " kg.\n\u0141\u0105czna waga produkt\u00f3w nie mo\u017ce przekracza\u0107 "
                  + "maksymalnej \u0142adowno\u015bci pojazdu "
                  + "(" + String.format("%.2f", IConf.MAX_VEHICLE_MAXLOAD) + " t).");
        
    }
    
}
