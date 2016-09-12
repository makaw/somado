/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */

package datamodel.tablemodels;

import datamodel.Order;
import datamodel.OrderItem;
import datamodel.OrderState;
import datamodel.Product;
import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import somado.Database;
import somado.Settings;

/**
 *
 * Szablon obiektu modelu tabeli z listą zamówień
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class OrdersTableModel extends TableModel<Order> {
       
  /** Nazwy (nagłówki) kolumn tabeli */
  private String[] columnNames = { "Data i godzina", "Numer", "Odbiorca", "Stan", "Produkty",
                                   "Waga [kg]", "Uwagi" };
  /** Nazwy pól filtrów */
  private String[] filterNames = { "number", "customer_name", "customer_city", "state_id" };

  /** Indeks kolumny stanu */
  final private static int STATE_COL_INDEX = 3;
  /** Indeks kolumny wymagającej łamania tekstu */
  final private static int WRAP_COL_INDEX = 4;
  /** Liczba wszystkich elementów */
  final private int allElementsNum;
  
  private boolean highlight[];
  
          
  /**
   * Konstruktor modelu
   * @param database Ref. do obiektu bazy danych
   * @param params Mapa parametrow (filtry)
   */  
  public OrdersTableModel(Database database, Map<String, String> params) {
      
    if (params == null) {
        
       params = new HashMap<>();
       for (String filterName : filterNames)  params.put(filterName, "");

    }  
    
    
    items = new ArrayList<>();
      
    int perPage = Integer.parseInt(Settings.getValue("items_per_page"));   
    int num = 0;
    
    // filtr stanu zamówień
    int state = OrderState.NEW.getId();
    try {
      state = Integer.parseInt(params.get("state_id"));
    }
    catch (NumberFormatException e) {}
    
    
    String query = "SELECT COUNT(DISTINCT(o.id)) FROM dat_orders AS o "
          + "INNER JOIN glo_customers AS c ON c.id=o.customer_id "
          + "WHERE o.state_id = ? AND o.number LIKE ? AND c.name LIKE ? AND c.city LIKE ? ";
    
    try {

       PreparedStatement ps = database.prepareQuery(query);
       
       ps.setInt(1, state);
       ps.setString(2, "%"+params.get("number")+"%");
       ps.setString(3, "%"+params.get("customer_name")+"%");
       ps.setString(4, "%"+params.get("customer_city")+"%");
       
       ResultSet rs = ps.executeQuery();
       
       if (rs.first()) num = rs.getInt(1);
       
    
    } catch (SQLException e) {
       
        System.err.println("B\u0142\u0105d SQL: "+e);
       
    }  
    
    allElementsNum = num;
    
    if (num>perPage) num = perPage;
    
    data = new Object[num][columnNames.length];  
    highlight = new boolean[num];
    
    query = "SELECT o.id, o.number, o.state_id, o.date_add, o.comment, "
            + "c.id AS customer_id, c.name AS customer_name, c.street AS customer_street, "
            + "c.city AS customer_city, c.postcode AS customer_postcode, c.longitude AS customer_longitude, "
            + "c.latitude AS customer_latitude "
            + "FROM dat_orders AS o "
            + "INNER JOIN glo_customers AS c ON c.id=o.customer_id "
            + "WHERE o.state_id = ? AND o.number LIKE ? AND c.name LIKE ? AND c.city LIKE ? "
            + "LIMIT ?;";
    
    try {
                
      PreparedStatement ps = database.prepareQuery(query);
      
      ps.setInt(1, state);
      ps.setString(2, "%"+params.get("number")+"%");
      ps.setString(3, "%"+params.get("customer_name")+"%");
      ps.setString(4, "%"+params.get("customer_city")+"%");
      ps.setInt(5, perPage);
              
      ResultSet rs = ps.executeQuery();
            
      
      int i = 0;
      
      while(rs.next()) {          
         
         Order order = new Order(rs);
         String products = "";
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
           if (!highlight[i]) highlight[i] = !tmp.getProduct().isAvailable();
           products += tmp.toString() + System.getProperty("line.separator");
           weight += tmp.getTotalWeight();
         }
         
         
         data[i][0] = Settings.DATETIME_NS_FORMAT.format(order.getDateAdd());
         data[i][1] = rs.getString("number");
         data[i][2] = order.getCustomer().toString();
         data[i][3] = order.getState().toString();
         data[i][4] = products;        
         data[i][5] = weight;
         data[i][6] = rs.getString("comment");
         
         items.add(i, order);
         
         i++;         
           
       }
           
    } catch (SQLException e) {
       
        System.err.println("B\u0142\u0105d SQL: "+e);
       
    }     

    
  }  
  
  
  /**
   * Konstruktor modelu
   * @param database Ref. do obiektu bazy danych
   */
  public OrdersTableModel(Database database) {
      
     this(database, null);  
 
  }
  
      
  
  /**
   * Metoda zwraca domyslne sortowanie ASC
   * @return indeks kolumny do domyslnego sortowania
   */
  @Override
  public int getDefaultSortOrder() {
      
     return  0;
      
  }

  /**
   * Metoda zwraca porzadek domyslnego sortowania
   * @return true jezeli sortowanie narastajaco
   */
  @Override
  public boolean getDefaultSortOrderAsc() {
      
    return false;  
      
  }
  
  

  /**
   * Metoda zwraca tytul naglowka menu kontekstowego
   * @param rowIndex Indeks wybranego wiersza tabeli
   * @return Tytul naglowka menu kontekstowego
   */  
  @Override
  public String getPopupMenuTitle(int rowIndex) {
      
     return (!((String)getValueAt(rowIndex,1)).isEmpty()) ? (String)getValueAt(rowIndex, 1) : "";
      
  }
    
  
  
  

  @Override
  public int getColumnCount() {
      
    return columnNames.length;
    
  }  
  

  @Override
  public String getColumnName(int col) {
      
    return columnNames[col];
    
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
      
    return (col == STATE_COL_INDEX);
      
  }
  
  
  /**
   * Metoda ma zwrocic kolor tekstu w polu stanu w danym wierszu
   * @param row Indeks wiersza
   * @return Kolor tekstu w polu stanu
   */    
  @Override
  public Color getStateCellColor(int row) {
      
     return items.get(row).getState().getColor();
      
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
  public boolean isWrapColumn(int col) {
      
    return col == WRAP_COL_INDEX;
      
  }  
  
  
  @Override
  public boolean isHighlightedCell(int col, int row) {
      
     return isWrapColumn(col) && highlight[row];
      
  }
  
  
}    
    


