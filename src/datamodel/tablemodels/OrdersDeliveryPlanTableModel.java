/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */

package datamodel.tablemodels;

import datamodel.Customer;
import datamodel.Order;
import datamodel.OrderItem;
import datamodel.OrderState;
import datamodel.Pack;
import datamodel.Product;
import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import somado.Database;
import somado.Settings;

/**
 *
 * Szablon obiektu modelu tabeli z listą zamówień o stanie "Nowy" dla otwarcia nowej dostawy
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class OrdersDeliveryPlanTableModel extends TableModel<Order> {
       
  /** Nazwy (nagłówki) kolumn tabeli */
  private String[] columnNames = { "", "Data i godzina", "Numer", "Odbiorca", "Waga [kg]" };
  
  /** Nazwy (nagłówki) kolumn tabeli dla modelu tworzonego z listy */
  private String[] columnNamesFromList = { "Data i godzina", "Numer", "Odbiorca", "Waga [kg]" };  
  
  /** Liczba wszystkich elementów */
  final private int allElementsNum;                 
  
  /** Czy model jest utworzony z gotowej listy */
  final boolean modelFromList;
  
  /**
   * Konstruktor modelu
   * @param database Ref. do obiektu bazy danych
   */  
  public OrdersDeliveryPlanTableModel(Database database) {
           
    items = new ArrayList<>();
    int num = 0;    
    modelFromList = false;
        
    String query = "SELECT COUNT(DISTINCT(o.id)) FROM dat_orders AS o "
          + "INNER JOIN glo_customers AS c ON c.id=o.customer_id "
          + "WHERE o.state_id = ? "
          + "AND (SELECT COUNT(p.id) FROM glo_products AS p "
          + "INNER JOIN dat_products_to_orders AS po ON po.product_id=p.id "
          + "WHERE po.order_id=o.id AND available<>'1')=0";
    
    try {

       PreparedStatement ps = database.prepareQuery(query);
       
       ps.setInt(1, OrderState.NEW.getId());
       
       ResultSet rs = ps.executeQuery();
       
       if (rs.first()) num = rs.getInt(1);
       
    
    } catch (SQLException e) {
       
        System.err.println("B\u0142\u0105d SQL: "+e);
       
    }  
    
    allElementsNum = num;    
    
    data = new Object[num][columnNames.length];
    
    query = "SELECT o.id, o.number, o.state_id, o.date_add, o.comment, "
            + "c.id AS customer_id, c.name AS customer_name, c.street AS customer_street, "
            + "c.city AS customer_city, c.postcode AS customer_postcode, c.longitude AS customer_longitude, "
            + "c.latitude AS customer_latitude "
            + "FROM dat_orders AS o "
            + "INNER JOIN glo_customers AS c ON c.id=o.customer_id "
            + "WHERE o.state_id = ? "
            + "AND (SELECT COUNT(p.id) FROM glo_products AS p "
            + "INNER JOIN dat_products_to_orders AS po ON po.product_id=p.id  "
            + "WHERE po.order_id=o.id AND available<>'1')=0";            
    
    try {
                
      PreparedStatement ps = database.prepareQuery(query);
      
      ps.setInt(1, OrderState.NEW.getId());
              
      ResultSet rs = ps.executeQuery();
            
      
      int i = 0;
      
      while(rs.next()) {          
         
         Order order = new Order(rs);
         double weight = 0.0;         
         
         query = "SELECT p.*, po.items FROM glo_products AS p "
              + "INNER JOIN dat_products_to_orders AS po ON po.product_id = p.id "
              + "WHERE po.order_id = ? ORDER BY p.name;";
         ps = database.prepareQuery(query);
         ps.setInt(1, order.getId());
         ResultSet rs2 = ps.executeQuery();   
         
         while (rs2.next()) {
           OrderItem tmp = new OrderItem(new Product(rs2), rs2.getInt("items"));           
           order.getProducts().add(tmp);
           weight += tmp.getTotalWeight();
         }
   
         data[i][0] = true;
         data[i][1] = Settings.DATETIME_NS_FORMAT.format(order.getDateAdd());
         data[i][2] = rs.getString("number");
         data[i][3] = order.getCustomer().toString();
         data[i][4] = String.format("%.2f", weight);
         
         items.add(i, order);
         
         i++;         
           
       }
           
    } catch (SQLException e) {
       
        System.err.println("B\u0142\u0105d SQL: "+e);
       
    }     

    
  }  
  
  
  /**
   * Konstruktor modelu dla planu dostawy (z gotowej listy)
   * @param items Lista zamówień
   */  
  public OrdersDeliveryPlanTableModel(List<Order> items) {
                 
    this.items = items;
    allElementsNum = items.size();  
    data = new Object[allElementsNum][columnNames.length];
    modelFromList = true;
 
    for (int i=0; i<allElementsNum; i++) {
       
       Order order = new Order(items.get(i));
       data[i][0] = Settings.DATETIME_NS_FORMAT.format(order.getDateAdd());
       data[i][1] = order.getNumber();
       data[i][2] = order.getCustomer().toString();
       data[i][3] = order.getTotalWeight();
         
     }   
    
  }  
  
      
  
  /**
   * Utworzenie połączonych przesyłek z  zaznaczonych zamówień
   * @return Lista utworzonych przesyłek
   */
  public List<Pack> getSelectedItems() {
      
    ArrayList<Order> orders = new ArrayList<>();
    for (int i=0; i<items.size(); i++)         
      if ((boolean)data[i][0]) orders.add(items.get(i));                            
       
    
    // posortowanie listy zamówień wg ID klienta
    Collections.sort(orders, new Comparator<Order>() {
      @Override
      public int compare(Order o1, Order o2) {
        return o1.getCustomer().getId().compareTo(o2.getCustomer().getId());
      }
    });
    
        
    // utworzenie listy paczek dla odbiorców towaru
    ArrayList<Pack> packList = new ArrayList<>();
    if (orders.isEmpty()) return packList;
    
    Order tmp = orders.get(0);
    if (orders.size()>1) orders.remove(0);     
   
    Customer custOld;
    
    Iterator<Order> iterator = orders.iterator();
    while (iterator.hasNext()) {       
      
      Pack pack = new Pack(tmp.getCustomer());
      
      do {
                  
        custOld = tmp.getCustomer();  
        pack.addOrder(tmp);
        tmp = iterator.next();
          
      } while (iterator.hasNext() && tmp.getCustomer().getId().equals(custOld.getId()));
        
      packList.add(pack);
      
      if (!iterator.hasNext() && !tmp.getCustomer().getId().equals(custOld.getId())) {
          
        pack = new Pack(tmp.getCustomer());
        pack.addOrder(tmp);
        packList.add(pack);
          
      }
              
      
    }
        
    return packList;
      
  }
  
  
  /**
   * Metoda zwraca domyslne sortowanie ASC
   * @return indeks kolumny do domyslnego sortowania
   */
  @Override
  public int getDefaultSortOrder() {
      
     return  modelFromList ? 0 : 1;
      
  }

  /**
   * Metoda zwraca porzadek domyslnego sortowania
   * @return true jezeli sortowanie narastajaco
   */
  @Override
  public boolean getDefaultSortOrderAsc() {
      
    return false;  
      
  }
  
  


  @Override
  public String getPopupMenuTitle(int rowIndex) {
      
     return "";
      
  }
    
  
  
  

  @Override
  public int getColumnCount() {
      
    return modelFromList ? columnNamesFromList.length : columnNames.length;
    
  }  
  

  @Override
  public String getColumnName(int col) {
      
    return modelFromList ? columnNamesFromList[col] : columnNames[col];
    
  }  

    
  @Override
  public void setValueAt(Object value, int row, int col) {

    data[row][col] = value;
    fireTableCellUpdated(row, col);

  }    
  
  
 /** 
   * Metoda ma odpowiedziec czy dana kolumna zawiera pole stanu
   * @param col Indeks kolumny
   * @return True jezeli zawiera pole stanu
   */    
  @Override
  public boolean isStateColumn(int col) {
      
    return false;
    
  }
  
  
  /**
   * Metoda ma zwrocic kolor tekstu w polu stanu w danym wierszu
   * @param row Indeks wiersza
   * @return Kolor tekstu w polu stanu
   */    
  @Override
  public Color getStateCellColor(int row) {
      
     return null;
      
  }  
  
  
  /**
   * Metoda ma zwrocic maksymalna dostepna ilosc elementow
   * @return maks. ilosc elementow modelu
   */
  @Override
  public int getAllElementsCount() {
      
     return allElementsNum; 
      
  }
  
  
  
  @Override
  public boolean isCellEditable(int row, int col) {
  
    // w pierwszej kolumnie checkbox do zaznaczenia zamówienia
    return !modelFromList && col == 0;
    
  }
  
  
  @Override
  public boolean isWrapColumn(int col) {
      
    return false;  
      
  }
  
  
}    
    


